package setupprogram;

import static kundenverwaltung.toolsandworkarounds.PropertiesFileController.PROPERTIES_LOCATION_DATABASE_INFO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kundenverwaltung.toolsandworkarounds.BlowfishEncryption;
import kundenverwaltung.toolsandworkarounds.CustomPropertiesStore;

public class DatabaseMigrationController
{
    private static final Logger LOGGER = Logger.getLogger(DatabaseMigrationController.class.getName());

    @FXML private PasswordField txtDbPasswordNew;
    @FXML private Button btnCheckConnect;
    @FXML private Button btnStartMigration;
    @FXML private Button btnCreateDatabase;
    @FXML private Button btnBack;
    @FXML private TextField txtDbPortNew;
    @FXML private TextField txtDbUserOld;
    @FXML private TextField txtDbAddressNew;
    @FXML private PasswordField txtDbPasswordOld;
    @FXML private TextField txtDbUserNew;
    @FXML private TextField txtDbPortOld;
    @FXML private CheckBox ckbxOldDb;
    @FXML private CheckBox ckbxNewDb;
    @FXML private CheckBox ckbxMigrate;
    @FXML private CheckBox ckbxDbCreate;
    @FXML private TextField txtDbNameOld;
    @FXML private TextField txtDbNameNew;
    @FXML private TextField txtDbAddressOld;

    private String dbAddressOld;
    private String dbAddressNew;
    private String dbPortOld;
    private String dbPortNew;
    private String dbUsernameOld;
    private String dbUsernameNew;
    private String dbPasswordOld;
    private String dbPasswordNew;
    private String dbNameOld;
    private String dbNameNew;

    @FXML
    void checkDatabaseConnection(ActionEvent event)
    {
        dbAddressOld = txtDbAddressOld.getText();
        dbAddressNew = txtDbAddressNew.getText();
        dbPortOld = txtDbPortOld.getText();
        dbPortNew = txtDbPortNew.getText();
        dbUsernameOld = txtDbUserOld.getText();
        dbUsernameNew = txtDbUserNew.getText();
        dbPasswordOld = txtDbPasswordOld.getText();
        dbPasswordNew = txtDbPasswordNew.getText();
        dbNameOld = txtDbNameOld.getText();

        Connection checkConOld = null;
        Connection checkConNew = null;
        try
        {
            checkConOld = DriverManager.getConnection(
                "jdbc:mariadb://" + dbAddressOld + ":" + dbPortOld + "/" + dbNameOld + "?useUnicode=true&characterEncoding=UTF-8",
                    dbUsernameOld, dbPasswordOld);

            if (checkConOld != null)
            {
                ckbxOldDb.setVisible(true);
                ckbxOldDb.setSelected(true);
                ckbxOldDb.setStyle("-fx-color: green");
                ckbxOldDb.setText("Alte Datenbank verbunden");
                txtDbAddressOld.setEditable(false);
                txtDbPortOld.setEditable(false);
                txtDbUserOld.setEditable(false);
                txtDbPasswordOld.setEditable(false);
            }
            checkConOld.close();
        } catch (SQLException e)
        {
            LOGGER.log(Level.SEVERE, "Fehler beim Verbinden zur alten Datenbank", e);
            ckbxOldDb.setVisible(true);
            ckbxOldDb.setSelected(false);
            ckbxOldDb.setStyle("-fx-color: red");
            ckbxOldDb.setText("Alte Datenbank nicht verbunden");
        }

        try
        {
            checkConNew = DriverManager.getConnection(
                "jdbc:mariadb://" + dbAddressNew + ":" + dbPortNew + "?useUnicode=true&characterEncoding=UTF-8",
                    dbUsernameNew, dbPasswordNew);

            if (checkConNew != null)
            {
                ckbxNewDb.setVisible(true);
                ckbxNewDb.setSelected(true);
                ckbxNewDb.setStyle("-fx-color: green");
                ckbxNewDb.setText("Neuer DB-Server verbunden");
                txtDbAddressNew.setEditable(false);
                txtDbPortNew.setEditable(false);
                txtDbUserNew.setEditable(false);
                txtDbPasswordNew.setEditable(false);
            }
            checkConNew.close();
        } catch (SQLException e)
        {
            LOGGER.log(Level.SEVERE, "Fehler beim Verbinden zum neuen DB-Server", e);
            ckbxNewDb.setVisible(true);
            ckbxNewDb.setSelected(false);
            ckbxNewDb.setStyle("-fx-color: red");
            ckbxNewDb.setText("Neuer DB-Server nicht verbunden");
        }

        if (checkConOld != null && checkConNew != null)
        {
            txtDbNameNew.setDisable(false);
            btnCreateDatabase.setDisable(false);
        }
    }

    private File chooseSqlScript()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wählen Sie die SQL-Datei");
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("SQL-Skript", "*.sql"));
        return fileChooser.showOpenDialog(new Stage());
    }

    private void runSqlScript(Connection con, File sqlFile) throws SQLException, IOException
    {
        try (Statement stmt = con.createStatement())
        {
            String sql = new String(Files.readAllBytes(sqlFile.toPath()), StandardCharsets.UTF_8);
            String[] sqlStatements = sql.split(";");
            for (String statement : sqlStatements)
            {
                if (!statement.trim().isEmpty())
                {
                    stmt.execute(statement);
                }
            }
        }
    }

    @FXML
    void createDatabase(ActionEvent event)
    {
        dbNameNew = txtDbNameNew.getText();

        try
        {
            CustomPropertiesStore prop = CustomPropertiesStore.loadDatabasePropertiesFileCustomStore();

            prop.setProperty("jdbc.hostname", dbAddressNew);
            prop.setProperty("jdbc.port", dbPortNew);
            prop.setProperty("jdbc.username", dbUsernameNew);
            prop.setProperty("jdbc.password", BlowfishEncryption.encrypt(dbPasswordNew));

            try (Connection con = DriverManager.getConnection(
                "jdbc:mariadb://" + dbAddressNew + ":" + dbPortNew + "?useUnicode=true&characterEncoding=UTF-8",
                    dbUsernameNew, dbPasswordNew))
            {

                try (Statement smt = con.createStatement())
                {
                    smt.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + dbNameNew + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                }

                try (Connection conNewDb = DriverManager.getConnection(
                    "jdbc:mariadb://" + dbAddressNew + ":" + dbPortNew + "/" + dbNameNew + "?useUnicode=true&characterEncoding=UTF-8",
                        dbUsernameNew, dbPasswordNew))
                {

                    try (FileOutputStream output = new FileOutputStream(PROPERTIES_LOCATION_DATABASE_INFO))
                    {
                        prop.setProperty("jdbc.dbName", dbNameNew);
                        prop.store(output, "MySQL database information");
                    }

                    File sqlFile = chooseSqlScript();
                    if (sqlFile != null)
                    {
                        runSqlScript(conNewDb, sqlFile);

                        Alert alertPositiv = new Alert(Alert.AlertType.INFORMATION);
                        alertPositiv.setTitle("Datenbank-Wiederherstellung");
                        alertPositiv.setHeaderText("Die Datenbank \"" + dbNameNew
                                + "\" wurde erfolgreich auf den Server \"" + dbAddressNew
                                + "\" importiert.");
                        alertPositiv.setContentText("Die Datenbank-Struktur wurde erstellt. Sie können nun die "
                                + "Migration der alten Daten starten.");
                        alertPositiv.showAndWait();
                        btnStartMigration.setDisable(false);
                        ckbxDbCreate.setVisible(true);
                        ckbxDbCreate.setSelected(true);
                        ckbxDbCreate.setStyle("-fx-color: green");
                        ckbxDbCreate.setText("Datenbank erstellt");
                    } else
                    {
                        Alert alertNoFile = new Alert(Alert.AlertType.WARNING);
                        alertNoFile.setTitle("Datei auswählen");
                        alertNoFile.setHeaderText("Sie haben keine .sql Datei ausgewählt!");
                        alertNoFile.setContentText(
                                "Bitte wählen Sie eine SQL-Sicherungsdatei (*.sql) aus und versuchen Sie es erneut.");
                        alertNoFile.showAndWait();
                    }
                }
            }
        } catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, "Fehler beim Erstellen der Datenbank", e);
            Alert sqlAlert = new Alert(Alert.AlertType.WARNING);
            sqlAlert.setTitle("Fehler bei der Verbindung!");
            sqlAlert.setHeaderText(
                    "Achtung, die Datenbank-Einstellungen sind möglicherweise fehlerhaft!");
            sqlAlert.setContentText(
                    "Es kann keine Verbindung zum SQL-Server hergestellt werden. Bitte prüfen Sie die Einstellungen oder Ihren Datenbank-Server.");
            sqlAlert.showAndWait();
        }
    }

    @FXML
    void startMigration(ActionEvent event)
    {
        LOGGER.info("Migration gestartet");
        try
        {
            LOGGER.info("Verbindung zur alten Datenbank wird hergestellt...");
            Connection conOldDb = DriverManager.getConnection(
                "jdbc:mariadb://" + dbAddressOld + ":" + dbPortOld + "/" + dbNameOld + "?"
            +
                "zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8",
                dbUsernameOld, dbPasswordOld);
            LOGGER.info("Verbindung zur alten Datenbank hergestellt.");

            LOGGER.info("Verbindung zur neuen Datenbank wird hergestellt...");
            Connection conNewDb = DriverManager.getConnection(
                "jdbc:mariadb://" + dbAddressNew + ":" + dbPortNew + "/" + dbNameNew
                +
                "?useUnicode=true&characterEncoding=UTF-8",
                dbUsernameNew, dbPasswordNew);
            LOGGER.info("Verbindung zur neuen Datenbank hergestellt.");

            int oldDbVersion = detectDbVersion(conOldDb);
            if (oldDbVersion > 0)
            {
                LOGGER.info("Alte Datenbank-Version erkannt: " + oldDbVersion);
            } else
            {
                LOGGER.warning("Alte Datenbank-Version konnte nicht eindeutig erkannt werden. Fortfahren mit Heuristiken.");
            }

            migrateTables(conOldDb, conNewDb);

            LOGGER.info("Migration abgeschlossen. Verbindungen werden geschlossen.");
            conOldDb.close();
            conNewDb.close();

            ckbxMigrate.setVisible(true);
            ckbxMigrate.setSelected(true);
            ckbxMigrate.setStyle("-fx-color: green");
            ckbxMigrate.setText("Migration abgeschlossen");

        } catch (SQLException e)
        {
            LOGGER.log(Level.SEVERE, "Fehler bei der Migration", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Migrationsfehler");
            alert.setHeaderText("Ein Fehler ist während der Migration aufgetreten");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private int detectDbVersion(Connection conOldDb)
    {
        try (PreparedStatement ps = conOldDb.prepareStatement(
                "SELECT value1 FROM lita_einstellungen WHERE app = ? AND key1 = ? LIMIT 1"))
        {
            ps.setString(1, "general");
            ps.setString(2, "DBVersion");
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    String v = rs.getString(1);
                    try
                    {
                        int ver = Integer.parseInt(v.trim());
                        return ver;
                    } catch (NumberFormatException nfe)
                    {
                        LOGGER.log(Level.WARNING, "DBVersion konnte nicht geparst werden: " + v, nfe);
                    }
                }
            }
        } catch (SQLException e)
        {
            LOGGER.log(Level.FINE, "lita_einstellungen DBVersion-Abfrage fehlgeschlagen (vielleicht Tabelle existiert nicht).", e);
        }

        try
        {
            if (columnExists(conOldDb, "lita_familienmitglieder", "hinzugefuegt") ||
                columnExists(conOldDb, "lita_familienmitglieder", "geandert"))
            {
                return 8;
            }
            if (columnExists(conOldDb, "lita_familienmitglieder", "Gebuehrenbefreiung"))
            {
                return 7;
            }
        } catch (SQLException e)
        {
            LOGGER.log(Level.FINE, "Fehler bei Schema-Heuristik zur Erkennung der DB-Version", e);
        }

        return 6;
    }

    private boolean columnExists(Connection con, String tableName, String columnName) throws SQLException
    {
        DatabaseMetaData meta = con.getMetaData();
        try (ResultSet rs = meta.getColumns(null, null, tableName, columnName))
        {
            return rs.next();
        }
    }

    private void migrateTables(Connection conOldDb, Connection conNewDb) throws SQLException
    {
      Map<String, Integer> verteilstelleMapping = createColumnMapping(
          Map.of("verteilstellenId", 1, "bezeichnung", 2, "adresse", 3, "listennummer", 4)
      );
      migrateTable(conOldDb, conNewDb, "lita_verteilstellen", "verteilstelle", verteilstelleMapping, "verteilstellenId", List.of());

      Map<String, Integer> plzMapping = createColumnMapping(
          Map.of("plzId", 1, "plz", 2, "ort", 3)
      );
      migrateTable(conOldDb, conNewDb, "lita_postleitzahlen_zuordnung", "plz", plzMapping, "plzId", List.of());

      Map<String, Integer> haushaltMapping = new HashMap<>();
      haushaltMapping.put("kundennummer", 1);
      haushaltMapping.put("strasse", 3);
      haushaltMapping.put("hausnummer", 4);
      haushaltMapping.put("plz", 5);
      haushaltMapping.put("telefonnummer", 6);
      haushaltMapping.put("mobilnummer", 7);
      haushaltMapping.put("bemerkung", 8);
      haushaltMapping.put("kundeSeit", 9);
      haushaltMapping.put("saldo", 10);
      haushaltMapping.put("verteilstellenId", 11);
      haushaltMapping.put("istArchiviert", 12);
      haushaltMapping.put("istGesperrt", 13);
      haushaltMapping.put("ausgabeGruppeId", null);
      haushaltMapping.put("belieferung", 15);
      haushaltMapping.put("datenschutzerklaerung", 16);
      migrateTable(conOldDb, conNewDb, "lita_kundenstamm", "haushalt", haushaltMapping, "kundennummer", List.of("verteilstellenId", "verteilstelle", "verteilstellenId"));

      migrateFamilienmitglieder(conOldDb, conNewDb);

      Map<String, Integer> bescheidMapping = new HashMap<>();
      bescheidMapping.put("bescheidId", 1);
      bescheidMapping.put("personId", 2);
      bescheidMapping.put("bescheidartId", 3);
      bescheidMapping.put("gueltigAb", 4);
      bescheidMapping.put("gueltigBis", 5);
      migrateTable(conOldDb, conNewDb, "lita_bescheide", "bescheid", bescheidMapping, "bescheidId", List.of("personId", "familienmitglied", "personId", "bescheidartId", "bescheidart", "bescheidArtId"));

      Map<String, Integer> einkaufMapping = new HashMap<>();
      einkaufMapping.put("einkaufId", 1);
      einkaufMapping.put("warentyp", 2);
      einkaufMapping.put("storniertAm", 3);
      einkaufMapping.put("stornoText", 4);
      einkaufMapping.put("buchungstext", 6);
      einkaufMapping.put("kunde", 7);
      einkaufMapping.put("person", 8);
      einkaufMapping.put("erfassungsZeit", 9);
      einkaufMapping.put("summeEinkauf", 10);
      einkaufMapping.put("summeZahlung", 11);
      einkaufMapping.put("beiVerteilstelle", 12);
      einkaufMapping.put("anzahlKinder", 13);
      einkaufMapping.put("anzahlErwachsene", 14);
      migrateTable(conOldDb, conNewDb, "lita_kunden_einkaeufe", "einkauf", einkaufMapping, "einkaufId",
              List.of("kunde", "haushalt", "kundennummer", "person", "familienmitglied", "personId", "beiVerteilstelle", "verteilstelle", "verteilstellenId"));

      Map<String, Integer> rechtMapping = createColumnMapping(
          Map.of("rechtId", 1, "Name", 2, "Beschreibung", 3)
      );
      migrateTable(conOldDb, conNewDb, "lita_rechte", "recht", rechtMapping, "rechtId", List.of());

      Map<String, Integer> vollmachtMapping = createColumnMapping(
          Map.of("vollmachtId", 1, "haushaltId", 2, "bevollmaechtigtePersonId", 3, "ausgestelltAm", 4, "ablaufDatum", 5)
      );
      migrateTable(conOldDb, conNewDb, "lita_vollmacht", "vollmacht", vollmachtMapping, "vollmachtId",
              List.of("haushaltId", "haushalt", "kundennummer", "bevollmaechtigtePersonId", "familienmitglied", "personId"));
    }

    private void migrateFamilienmitglieder(Connection conOldDb, Connection conNewDb) throws SQLException
    {
        LOGGER.info("Migration der Tabelle lita_familienmitglieder nach familienmitglied gestartet.");

        final String selectSQL = "SELECT * FROM lita_familienmitglieder";
        final String insertSQL =
                "INSERT INTO familienmitglied (" +
                        "personId, haushaltId, anredeId, genderId, vName, nName, gDatum, bemerkung, " +
                        "haushaltsVorstand, einkaufsBerechtigt, gebuehrenBefreiung, nation, berechtigungId, " +
                        "aufAusweis, dseSubmitted, hinzugefuegtAm, geaendertAm) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement selectStmt = conOldDb.prepareStatement(selectSQL);
             ResultSet rs = selectStmt.executeQuery();
             PreparedStatement insert = conNewDb.prepareStatement(insertSQL))
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            Map<String,Integer> colIndexMap = buildNormalizedColumnIndexMap(rsmd);

            boolean hasGebuehrenbefreiung = colIndexMap.containsKey(normalizeCol("Gebuehrenbefreiung"));
            boolean hasHinzugefuegt = colIndexMap.containsKey(normalizeCol("hinzugefuegt"));
            boolean hasGeaendert = colIndexMap.containsKey(normalizeCol("geandert"));

            int batchCount = 0;
            while (rs.next())
            {
                int personId = getIntByMap(rs, colIndexMap, "Person_Nr", -1);
                int haushaltId = getIntByMap(rs, colIndexMap, "Kundennummer", -1);

                if (haushaltId <= 0)
                {
                    LOGGER.warning("Überspringe Person_Nr=" + personId + " (ungültiger Haushalt " + haushaltId + ")");
                    continue;
                }

                if (!checkForeignKeyConstraint(conNewDb, "haushalt", "kundennummer", haushaltId))
                {
                    LOGGER.warning("Überspringe Person_Nr=" + personId + " (fehlender Haushalt " + haushaltId + ")");
                    continue;
                }

                if (checkPrimaryKeyExists(conNewDb, "familienmitglied", "personId", personId))
                {
                    LOGGER.warning("Überspringe Person_Nr=" + personId + " (personId existiert bereits).");
                    continue;
                }

                int oldAnrede = getIntByMap(rs, colIndexMap, "Anrede_ID", 1);
                int anredeId = normalizeAnredeId(oldAnrede);
                int genderId = mapGenderFromAnrede(anredeId);

                String vName = truncate(getStringByMap(rs, colIndexMap, "Vorname"), 50);
                String nName = truncate(getStringByMap(rs, colIndexMap, "Nachname"), 50);
                String bemerkung = truncate(getStringByMap(rs, colIndexMap, "Bemerkung"), 255);

                java.sql.Date gDatum = getDateByMap(rs, colIndexMap, "Geburtsdatum");

                boolean haushaltsVorstand = getIntByMap(rs, colIndexMap, "Haushaltsvorstand", 0) != 0;
                boolean einkaufsBerechtigt = getIntByMap(rs, colIndexMap, "Einkaufberechtigt", 0) != 0;
                boolean aufAusweis = getIntByMap(rs, colIndexMap, "AufAusweis", 0) != 0;

                int gebuehrenBefreiungInt = 0;
                if (hasGebuehrenbefreiung)
                {
                    gebuehrenBefreiungInt = getIntByMap(rs, colIndexMap, "Gebuehrenbefreiung", 0);
                } else
                {
                    gebuehrenBefreiungInt = 0;
                }
                boolean gebuehrenBefreiung = gebuehrenBefreiungInt != 0;

                int oldNation = getIntByMap(rs, colIndexMap, "Nation", 1);
                int mappedNation = mapNationId(oldNation);
                if (!checkForeignKeyConstraint(conNewDb, "nation", "nationId", mappedNation))
                {
                    mappedNation = 1;
                }

                int berechtigungId = getIntByMap(rs, colIndexMap, "Berechtigung", 1);
                boolean dseSubmitted = false;

                Timestamp hinzugefuegt = null;
                Timestamp geaendert = null;
                if (hasHinzugefuegt)
                {
                    hinzugefuegt = getTimestampByMap(rs, colIndexMap, "hinzugefuegt");
                }
                if (hasGeaendert)
                {
                    geaendert = getTimestampByMap(rs, colIndexMap, "geandert");
                }
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if (hinzugefuegt == null) hinzugefuegt = now;
                if (geaendert == null) geaendert = now;

                int p = 1;
                insert.setInt(p++, personId);
                insert.setInt(p++, haushaltId);
                insert.setInt(p++, anredeId);
                insert.setInt(p++, genderId);
                insert.setString(p++, vName);
                insert.setString(p++, nName);
                insert.setDate(p++, gDatum);
                insert.setString(p++, bemerkung);
                insert.setBoolean(p++, haushaltsVorstand);
                insert.setBoolean(p++, einkaufsBerechtigt);
                insert.setBoolean(p++, gebuehrenBefreiung);
                insert.setInt(p++, mappedNation);
                insert.setInt(p++, berechtigungId);
                insert.setBoolean(p++, aufAusweis);
                insert.setBoolean(p++, dseSubmitted);
                insert.setTimestamp(p++, hinzugefuegt);
                insert.setTimestamp(p++, geaendert);

                insert.addBatch();
                batchCount++;
                if (batchCount % 500 == 0)
                {
                    insert.executeBatch();
                }
            }

            insert.executeBatch();
        }

        LOGGER.info("Migration der Tabelle lita_familienmitglieder abgeschlossen.");
    }

    // Helper: normalized map builder
    private static final Pattern NON_ALNUM = Pattern.compile("[^a-z0-9]");

    private Map<String,Integer> buildNormalizedColumnIndexMap(ResultSetMetaData rsmd) throws SQLException
    {
        Map<String,Integer> map = new HashMap<>();
        int cols = rsmd.getColumnCount();
        for (int i = 1; i <= cols; i++)
        {
            String label = rsmd.getColumnLabel(i);
            if (label == null) label = rsmd.getColumnName(i);
            String normalized = normalizeCol(label);
            map.put(normalized, i);

            // zusätzlich Varianten ohne Tabellennamen, ohne Präfix und ohne "_"
            // z.B. "lita_familienmitglieder.person_nr" -> "personnr"
            if (label.contains("."))
            {
                String afterDot = label.substring(label.lastIndexOf('.') + 1);
                map.put(normalizeCol(afterDot), i);
            }
            // variant with underscores removed
            map.put(normalized.replace("_", ""), i);
            map.put(normalized.replace("_", "").replaceAll("[^a-z0-9]", ""), i);
        }
        return map;
    }

    // Normalisiert Spaltennamen: alles lower-case, entfernt Tabellenpräfixe, ersetzt trenner durch _
    private String normalizeCol(String s)
    {
        if (s == null) return "";
        String low = s.toLowerCase();
        if (low.contains(".")) low = low.substring(low.lastIndexOf('.') + 1);
        // replace non-alnum by underscore to preserve word separation
        low = NON_ALNUM.matcher(low).replaceAll("_");
        // collapse multiple underscores
        low = low.replaceAll("_+", "_");
        low = low.replaceAll("^_|_$", "");
        return low;
    }

    // Zugriff über Map (verwenden normalizeCol intern)
    private int getIntByMap(ResultSet rs, Map<String,Integer> colIndexMap, String colName, int defaultValue)
    {
        try
        {
            Integer idx = colIndexMap.get(normalizeCol(colName));
            if (idx == null) return defaultValue;
            int v = rs.getInt(idx);
            if (rs.wasNull()) return defaultValue;
            return v;
        } catch (SQLException e)
        {
            LOGGER.log(Level.FINER, "getIntByMap Fehler für Spalte " + colName + ", Default " + defaultValue, e);
            return defaultValue;
        }
    }

    private String getStringByMap(ResultSet rs, Map<String,Integer> colIndexMap, String colName)
    {
        try
        {
            Integer idx = colIndexMap.get(normalizeCol(colName));
            if (idx == null) return null;
            return rs.getString(idx);
        } catch (SQLException e)
        {
            LOGGER.log(Level.FINER, "getStringByMap Fehler für Spalte " + colName, e);
            return null;
        }
    }

    private java.sql.Date getDateByMap(ResultSet rs, Map<String,Integer> colIndexMap, String colName)
    {
        try
        {
            Integer idx = colIndexMap.get(normalizeCol(colName));
            if (idx == null) return null;
            return rs.getDate(idx);
        } catch (SQLException e)
        {
            LOGGER.log(Level.FINER, "getDateByMap Fehler für Spalte " + colName, e);
            return null;
        }
    }

    private Timestamp getTimestampByMap(ResultSet rs, Map<String,Integer> colIndexMap, String colName)
    {
        try
        {
            Integer idx = colIndexMap.get(normalizeCol(colName));
            if (idx == null) return null;
            return rs.getTimestamp(idx);
        } catch (SQLException e)
        {
            LOGGER.log(Level.FINER, "getTimestampByMap Fehler für Spalte " + colName, e);
            return null;
        }
    }

    // --- übrige Hilfsfunktionen (gleich wie vorher) ---
    private int mapGenderFromAnrede(int anredeId)
    {
        return switch (anredeId)
        {
            case 31 -> 71;
            case 32 -> 72;
            case 33 -> 73;
            default -> 74;
        };
    }

    private int normalizeAnredeId(int oldAnrede)
    {
        if (oldAnrede == 31 || oldAnrede == 32 || oldAnrede == 33)
            return oldAnrede;
        if (oldAnrede == 1)
            return 31;
        return 31;
    }

    private int mapNationId(int oldNation)
    {
        return (oldNation >= 51 && oldNation <= 238) ? oldNation : 1;
    }

    private String truncate(String s, int max)
    {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }

    private Map<String, Integer> createColumnMapping(Map<String, Integer> originalMapping)
    {
        for (Map.Entry<String, Integer> entry : originalMapping.entrySet())
        {
            if (entry.getValue() == null)
            {
                LOGGER.warning("Spalte " + entry.getKey() + " enthält einen Nullwert und wird übersprungen.");
            }
        }
        return originalMapping;
    }

    private String buildInsertSQL(String tableName, Map<String, Integer> columnMapping)
    {
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        for (String column : columnMapping.keySet())
        {
            if (columns.length() > 0)
            {
                columns.append(", ");
                placeholders.append(", ");
            }
            columns.append(column);
            placeholders.append("?");
        }

        return "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";
    }

    private boolean validateData(ResultSet rs, Map<String, Integer> columnMapping) throws SQLException
    {
        Integer index = columnMapping.get("verteilstellenId");
        if (index != null && index > 0)
        {
            int value = rs.getInt(index);
            return value > 0;
        }
        return true;
    }

    private boolean checkPrimaryKeyExists(Connection conNewDb, String tableName, String primaryKey, int keyValue) throws SQLException
    {
        String query = "SELECT 1 FROM " + tableName + " WHERE " + primaryKey + " = ?";
        try (PreparedStatement stmt = conNewDb.prepareStatement(query))
        {
            stmt.setInt(1, keyValue);
            try (ResultSet rs = stmt.executeQuery())
            {
                return rs.next();
            }
        }
    }

    private boolean checkForeignKeyConstraint(Connection conNewDb, String refTable, String refColumn, int fkValue) throws SQLException
    {
        String query = "SELECT 1 FROM " + refTable + " WHERE " + refColumn + " = ?";
        try (PreparedStatement stmt = conNewDb.prepareStatement(query))
        {
            stmt.setInt(1, fkValue);
            try (ResultSet rs = stmt.executeQuery())
            {
                return rs.next();
            }
        }
    }

 // ---------------------- REPLACEMENT: migrateTable ----------------------
    private void migrateTable(Connection conOldDb, Connection conNewDb,
                              String oldTableName, String newTableName,
                              Map<String, Integer> columnMapping,
                              String primaryKey, List<String> foreignKeys) throws SQLException
    {
        LOGGER.info("Migration der Tabelle " + oldTableName + " nach " + newTableName + " gestartet.");

        String selectSQL = "SELECT * FROM " + oldTableName;
        try (PreparedStatement selectStmt = conOldDb.prepareStatement(selectSQL);
             ResultSet rs = selectStmt.executeQuery())
        {

            // Build normalized column index map for fallbacks (name-based lookups)
            ResultSetMetaData rsmd = rs.getMetaData();
            Map<String,Integer> colIndexMap = buildNormalizedColumnIndexMap(rsmd);
            int rsCols = rsmd.getColumnCount();

            String insertSQL = buildInsertSQL(newTableName, columnMapping);
            try (PreparedStatement insertStmt = conNewDb.prepareStatement(insertSQL))
            {
                while (rs.next())
                {
                    if (!validateData(rs, columnMapping))
                    {
                        LOGGER.warning("Ungültige Daten gefunden, überspringe Datensatz.");
                        continue;
                    }

                    // Primary key check: only if mapping contains the primary key with a source index
                    if (primaryKey != null && columnMapping.get(primaryKey) != null)
                    {
                        Integer pkSrc = columnMapping.get(primaryKey);
                        Integer pkVal = null;
                        if (pkSrc != null)
                        {
                            // try read safely
                            Object obj = getObjectByMapping(rs, rsmd, colIndexMap, pkSrc, primaryKey);
                            if (obj instanceof Number) pkVal = ((Number) obj).intValue();
                            else if (obj != null) {
                                try { pkVal = Integer.parseInt(obj.toString()); } catch (Exception ignored) {}
                            }
                        }
                        if (pkVal != null && checkPrimaryKeyExists(conNewDb, newTableName, primaryKey, pkVal))
                        {
                            LOGGER.warning("Primärschlüssel existiert bereits, überspringe Datensatz.");
                            continue;
                        }
                    }

                    boolean fkValid = true;
                    for (int i = 0; i < foreignKeys.size(); i += 3)
                    {
                        String fkColumn = foreignKeys.get(i);
                        String refTable = foreignKeys.get(i + 1);
                        String refColumn = foreignKeys.get(i + 2);

                        Integer colIndex = columnMapping.get(fkColumn);
                        if (colIndex == null)
                        {
                            LOGGER.warning("FK Mapping für Spalte " + fkColumn + " ist NULL, überspringe FK-Prüfung für diesen Eintrag.");
                            continue;
                        }

                        Object fkObj = getObjectByMapping(rs, rsmd, colIndexMap, colIndex, fkColumn);
                        int fkValue = 0;
                        if (fkObj instanceof Number) fkValue = ((Number)fkObj).intValue();
                        else if (fkObj != null) {
                            try { fkValue = Integer.parseInt(fkObj.toString()); } catch (Exception ex) { fkValue = 0; }
                        }

                        if (!checkForeignKeyConstraint(conNewDb, refTable, refColumn, fkValue))
                        {
                            LOGGER.warning("Fremdschlüsselverletzung für Spalte " + fkColumn + ", überspringe Datensatz.");
                            fkValid = false;
                            break;
                        }
                    }
                    if (!fkValid) continue;

                    int parameterIndex = 1;
                    for (Map.Entry<String, Integer> entry : columnMapping.entrySet())
                    {
                        Object value = null;
                        Integer src = entry.getValue();
                        if (src != null)
                        {
                            value = getObjectByMapping(rs, rsmd, colIndexMap, src, entry.getKey());
                        } else
                        {
                            value = null;
                        }

                        // special-case: verteilstellenId 0 -> 1
                        if ("verteilstellenId".equals(entry.getKey()) && (value instanceof Number) && ((Number) value).intValue() == 0)
                        {
                            value = 1;
                        }
                        insertStmt.setObject(parameterIndex++, value);
                    }

                    insertStmt.addBatch();
                }

                insertStmt.executeBatch();
            }
        }

        LOGGER.info("Migration der Tabelle " + oldTableName + " abgeschlossen.");
    }
 // ---------------------- HELPER: getObjectByMapping ----------------------
 // srcIndex: numeric position the mapping contained (1-based). If srcIndex is in range, try rs.getObject(srcIndex).
 // Otherwise try to resolve by normalized column name of the destination column (fallback to name-based lookup).
 private Object getObjectByMapping(ResultSet rs, ResultSetMetaData rsmd, Map<String,Integer> colIndexMap, Integer srcIndex, String destColumnName)
 {
     try
     {
         int cols = rsmd.getColumnCount();
         if (srcIndex != null && srcIndex >= 1 && srcIndex <= cols)
         {
             try
             {
                 return rs.getObject(srcIndex);
             } catch (SQLException e)
             {
                 LOGGER.log(Level.FINER, "rs.getObject auf Index " + srcIndex + " für Spalte " + destColumnName + " schlug fehl.", e);
             }
         }

         // Fallback: try to find a column by name (destination column often equals source name)
         Integer idxByName = colIndexMap.get(normalizeCol(destColumnName));
         if (idxByName != null && idxByName >= 1 && idxByName <= cols)
         {
             try
             {
                 return rs.getObject(idxByName);
             } catch (SQLException e)
             {
                 LOGGER.log(Level.FINER, "rs.getObject auf gefundenen Index " + idxByName + " (Name-Fallback) für " + destColumnName + " schlug fehl.", e);
             }
         }

         // No usable source column - return null
         LOGGER.log(Level.FINE, "Quell-Spalte für Ziel '" + destColumnName + "' nicht gefunden (index=" + srcIndex + "). Setze NULL.");
         return null;
     } catch (SQLException e)
     {
         LOGGER.log(Level.FINER, "Fehler beim Lesen von Quellspalte für " + destColumnName, e);
         return null;
     }
 }

    @FXML
    void returnToOptionMenu(ActionEvent event)
    {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        currentStage.close();
        WelcomeScreenController.getChooseDatabaseOptionStg().show();
    }

    @FXML
    void finishSetup(ActionEvent event)
    {
        Platform.exit();
    }
}
