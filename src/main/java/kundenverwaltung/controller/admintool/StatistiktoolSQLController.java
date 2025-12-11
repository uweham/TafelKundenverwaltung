package kundenverwaltung.controller.admintool;

import kundenverwaltung.dao.SQLConnection;
import kundenverwaltung.dao.StatistiktoolDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import kundenverwaltung.service.TablePreferenceServiceImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class StatistiktoolSQLController
{

	// FXML-Elemente
	@FXML
	private TextArea sqlQueryArea;

	@FXML
	private Button executeQueryButton;

	@FXML
	private Button loadRelationsButton;

	@FXML
	private Button loadTablesAndColumnsButton;

	@FXML
	private Button addCustomSQLQueryButton;

	@FXML
	private Button deleteQueryButton;

	@FXML
	private TableView<ObservableList<String>> queryResultTable;

	@FXML
	private ComboBox<String> sqlDropdown;

	@FXML
	private ComboBox<String> xAxisDropdown;

	@FXML
	private ComboBox<String> yAxisDropdown;

	@FXML
	private Button generatePieChartButton;

	@FXML
	private Button resetButton;

	@FXML
	private Button savePieChartButton;

	@FXML
	private PieChart pieChart;

	@FXML
	private VBox mainVBox;

	@SuppressWarnings("unused")
  private StatistiktoolDAO statistikDAO;
	/**
    *
    */
	// Initialisierungsmethode, die automatisch aufgerufen wird, wenn die FXML-Datei geladen wird
	@FXML
	public void initialize()
	{
		executeQueryButton.setOnAction(event -> executeSQLQuery());
		loadRelationsButton.setOnAction(event -> loadRelations());
		loadTablesAndColumnsButton.setOnAction(event -> loadTablesAndColumns());
		sqlDropdown.setItems(FXCollections.observableArrayList(getCommonSQLQueries()));
		sqlDropdown.setOnAction(event ->
		{
			if (!sqlDropdown.getItems().contains(sqlDropdown.getValue()))
			{
				sqlDropdown.getItems().add(sqlDropdown.getValue());
			}
			sqlQueryArea.setText(sqlDropdown.getValue());
		});
		generatePieChartButton.setOnAction(event -> generatePieChart());
		savePieChartButton.setOnAction(event -> savePieChartAsPDF());
		resetButton.setOnAction(event -> resetFields());
		deleteQueryButton.setOnAction(event -> handleDeleteQuery());
		addCustomSQLQueryButton.setOnAction(event -> addCustomSQLQuery());  // Hinzufügen des Event-Handlers
		loadSavedQueries(); // Lade gespeicherte SQL-Abfragen
	}

	/** Methode, um den Tabellennamen aus der SQL-Abfrage zu extrahieren.
	private String getTableNameFromQuery(String query) {
		query = query.toLowerCase();
		if (query.contains(" from ")) {
			return query.split(" from ")[1].trim().split("\\s+")[0];
		} else if (query.contains(" into ")) {
			return query.split(" into ")[1].trim().split("\\s+")[0];
		} else if (query.contains(" table ")) {
			return query.split(" table ")[1].trim().split("\\s+")[0];
		}
		return "";
	}
	   **/

	// Methode, um eine SQL-Abfrage auszuführen
	private void executeSQLQuery()
	{
		String query = sqlQueryArea.getText().trim();
		if (query.isEmpty())
		{
			showAlert(Alert.AlertType.ERROR, "Fehler", "SQL-Abfrage darf nicht leer sein.");
			return;
		}
		saveSQLQuery(query); // Speichert die Abfrage

		// Überprüfen und Hinzufügen der Abfrage zum Dropdown-Menü
		if (!sqlDropdown.getItems().contains(query))
		{
			sqlDropdown.getItems().add(query);
		}

		if (query.toLowerCase().startsWith("select") || query.toLowerCase().startsWith("show"))
		{
			executeSelectQuery(query);
		} else
		{
			boolean success = executeUpdateQuery(query);
			if (success)
			{
				showAlert(Alert.AlertType.INFORMATION, "Erfolg", "SQL-Abfrage erfolgreich ausgeführt.");
			} else
			{
				showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler bei der Ausführung der SQL-Abfrage.");
			}
		}
	}

	// Methode zum Löschen einer gespeicherten SQL-Abfrage
	@FXML
	private void handleDeleteQuery()
	{
		String selectedQuery = sqlDropdown.getValue();
		if (selectedQuery == null)
		{
			showAlert(Alert.AlertType.ERROR, "Fehler", "Bitte wählen Sie eine Abfrage zum Löschen aus.");
			return;
		}

		sqlDropdown.getItems().remove(selectedQuery);
		saveQueriesToFile();
		showAlert(Alert.AlertType.INFORMATION, "Erfolg", "Abfrage erfolgreich gelöscht.");
	}

	private void saveQueriesToFile()
	{
		// Versuch, eine neue Datei "sql_queries.txt" zu erstellen und einen BufferedWriter dafür zu öffnen
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("sql_queries.txt")))
		{
			// Durchläuft alle SQL-Abfragen, die im Dropdown-Menü gespeichert sind
			for (String query : sqlDropdown.getItems())
			{
				// Schreibt jede SQL-Abfrage in die Datei
				writer.write(query);
				// Fügt einen Zeilenumbruch nach jeder Abfrage hinzu
				writer.newLine();
			}
		} catch (IOException e)
		{
			// Zeigt eine Fehlermeldung an, wenn ein Fehler beim Speichern der Abfragen auftritt
			showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler beim Speichern der SQL-Abfragen: " + e.getMessage());
			// Gibt den vollständigen Stack-Trace des Fehlers aus, um das Debugging zu erleichtern
			e.printStackTrace();
		}
	}

	// Methode zum Hinzufügen einer individuellen SQL-Abfrage
	@FXML
	private void addCustomSQLQuery()
	{
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Neue SQL-Abfrage");
		dialog.setHeaderText("Individuelle SQL-Abfrage hinzufügen");
		dialog.setContentText("Bitte geben Sie Ihre SQL-Abfrage ein:");

		DialogPane dialogPane = dialog.getDialogPane();

		// Anpassen des TextInputDialog-Fensters
		TextArea textArea = new TextArea();
		textArea.setPrefHeight(100); // Höhe anpassen
		textArea.setPrefWidth(400);  // Breite anpassen
		dialogPane.setContent(textArea);

		Optional<String> result = dialog.showAndWait();
		result.ifPresent(query ->
		{
			if (!sqlDropdown.getItems().contains(query))
			{
				sqlDropdown.getItems().add(query);
				saveSQLQuery(query);
			}
		});
	}

	// Methode, um eine SQL-Abfrage zu speichern
	private void saveSQLQuery(String query)
	{
		String insertQuery = "INSERT INTO saved_queries (query) VALUES (?)";

		try (Connection conn = SQLConnection.getCon();
			 PreparedStatement pstmt = conn.prepareStatement(insertQuery))
		{

			pstmt.setString(1, query);
			pstmt.executeUpdate();
		} catch (SQLException e)
		{
			showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler beim Speichern der SQL-Abfrage: " + e.getMessage());
			e.printStackTrace();
		}
	}
	/**
     *
     */
	// Methode zum Setzen der SQL-Abfrage
	public void setSqlQuery(String sqlQuery)
	{
		if (sqlQueryArea != null)
		{
			sqlQueryArea.setText(sqlQuery); // Setzt die übergebene SQL-Abfrage in das Textfeld
		}
	}

	// Methode, um eine Update-SQL-Abfrage auszuführen
	private boolean executeUpdateQuery(String query)
	{
		try (Connection conn = SQLConnection.getCon();
			 Statement stmt = conn.createStatement())
		{
			stmt.execute(query);
			return true;
		} catch (SQLException e)
		{
			showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler bei der Ausführung der SQL-Abfrage: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Methode, um eine SELECT-SQL-Abfrage auszuführen
	private void executeSelectQuery(String query)
	{
		try (Connection conn = SQLConnection.getCon();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(query))
		{

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			queryResultTable.getColumns().clear();

			for (int i = 1; i <= columnCount; i++)
			{
				final int j = i;
				TableColumn<ObservableList<String>, String> column = new TableColumn<>(rsmd.getColumnName(i));
				column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j - 1)));
				queryResultTable.getColumns().add(column);
			}

			ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
			while (rs.next())
			{
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= columnCount; i++)
				{
					row.add(rs.getString(i));
				}
				data.add(row);
			}
			queryResultTable.setItems(data);
			populateAxisDropdowns(rsmd);
		} catch (SQLException e)
		{
			showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler bei der Ausführung der SQL-Abfrage: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Methode, um Dropdown-Menüs mit Spaltennamen zu füllen
	private void populateAxisDropdowns(ResultSetMetaData rsmd) throws SQLException
	{
		ObservableList<String> columns = FXCollections.observableArrayList();
		for (int i = 1; i <= rsmd.getColumnCount(); i++)
		{
			columns.add(rsmd.getColumnName(i));
		}
		xAxisDropdown.setItems(columns);
		yAxisDropdown.setItems(columns);
	}

	// Methode, um ein PieChart für jede Tabelle zu generieren
	private void generatePieChart()
	{
		String xAxisValue = xAxisDropdown.getValue();
		String yAxisValue = yAxisDropdown.getValue();
		if (xAxisValue == null || yAxisValue == null)
		{
			showAlert(Alert.AlertType.ERROR, "Fehler", "Bitte wählen Sie sowohl die X-Achse als auch die Y-Achse.");
			return;
		}

		String query = sqlQueryArea.getText().trim();
		if (!query.toLowerCase().startsWith("select"))
		{
			showAlert(Alert.AlertType.ERROR, "Fehler", "Bitte führen Sie eine SELECT-Abfrage aus.");
			return;
		}

		try (Connection conn = SQLConnection.getCon();
			 Statement stmt = conn.createStatement())
		{

			// Prüfe, ob die Y-Achse numerische Daten enthält
			ResultSet rs = stmt.executeQuery("SELECT " + yAxisValue + " FROM (" + query + ") AS tempTable LIMIT 1");
			ResultSetMetaData rsmd = rs.getMetaData();
			boolean isNumericY = rsmd.getColumnType(1) == java.sql.Types.NUMERIC || rsmd.getColumnType(1) == java.sql.Types.DECIMAL;

			// Wenn die Y-Achse numerisch ist, wähle mit GROUP BY für die X-Achse und SUM für die Y-Achse
			if (isNumericY)
			{
				query = "SELECT " + xAxisValue + ", SUM(" + yAxisValue + ") AS " + yAxisValue + " FROM (" + query + ") AS tempTable GROUP BY " + xAxisValue;
			}

			// Führe die angepasste oder unveränderte Abfrage aus
			rs = stmt.executeQuery(query);

			ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
			double total = 0;

			// Verarbeite die Abfrageergebnisse und erstelle das PieChart
			while (rs.next())
			{
				String xValue = rs.getString(xAxisValue);
				double yValue = rs.getDouble(yAxisValue);
				total += yValue;
				pieChartData.add(new PieChart.Data(xValue, yValue));
			}

			// Berechne die Prozentsätze
			for (PieChart.Data data : pieChartData)
			{
				double percentage = (data.getPieValue() / total) * 100;
				data.setName(data.getName() + " " + String.format("%.2f%%", percentage));
			}

			// Setze das PieChart-Diagramm
			pieChart.setData(pieChartData);

		} catch (SQLException e)
		{
			showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler bei der Generierung des PieCharts: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Methode, um das PieChart als PDF zu speichern
	private void savePieChartAsPDF()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Dateien", "*.pdf"));
		fileChooser.setTitle("PieChart als PDF speichern");
		File file = fileChooser.showSaveDialog(null);

		if (file != null)
		{
			WritableImage snapshot = pieChart.snapshot(new SnapshotParameters(), null);
			File tempFile = new File("tempPieChart.png");

			try
			{
				ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", tempFile);

				PDDocument document = new PDDocument();
				PDPage page = new PDPage(PDRectangle.A4);
				document.addPage(page);

				PDImageXObject pdImage = PDImageXObject.createFromFile("tempPieChart.png", document);
				PDPageContentStream contentStream = new PDPageContentStream(document, page);
				contentStream.drawImage(pdImage, 20, 300, pdImage.getWidth() / 2, pdImage.getHeight() / 2);

				// Beschreibung zum PDF hinzufügen
				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
				contentStream.newLineAtOffset(20, 750);
				contentStream.showText("PieChart: " + xAxisDropdown.getValue() + " vs. " + yAxisDropdown.getValue());
				contentStream.endText();

				contentStream.close();
				document.save(file);
				document.close();
				tempFile.delete();

				showAlert(Alert.AlertType.INFORMATION, "Erfolg", "PieChart wurde erfolgreich als PDF gespeichert.");
			} catch (IOException e)
			{
				showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler beim Speichern des PieCharts als PDF: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	// Methode, um alle Eingabefelder zurückzusetzen
	private void resetFields()
	{
		sqlQueryArea.clear();
		xAxisDropdown.getSelectionModel().clearSelection();
		yAxisDropdown.getSelectionModel().clearSelection();
		pieChart.getData().clear();
		queryResultTable.getItems().clear();
		queryResultTable.getColumns().clear();
	}

	// Methode, um die Relationen der Datenbank zu laden
	private void loadRelations()
	{
		String query = "SELECT TABLE_NAME, COLUMN_NAME, CONSTRAINT_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME "
	+
				"FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE "
		    +
				"WHERE TABLE_SCHEMA = DATABASE() AND REFERENCED_TABLE_NAME IS NOT NULL;";

		try (Connection conn = SQLConnection.getCon();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(query))
		{

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			queryResultTable.getColumns().clear();

			for (int i = 1; i <= columnCount; i++)
			{
				final int j = i;
				TableColumn<ObservableList<String>, String> column = new TableColumn<>(rsmd.getColumnName(i));
				column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j - 1)));
				column.setId(rsmd.getColumnName(i));
				queryResultTable.getColumns().add(column);
			}

			ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
			while (rs.next())
			{
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= columnCount; i++)
				{
					row.add(rs.getString(i));
				}
				data.add(row);
			}
			queryResultTable.setItems(data);

			TablePreferenceServiceImpl.getInstance().setupPersistence(queryResultTable, "QueryResultTableStatistikTool");
		} catch (SQLException e)
		{
			showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler bei der Abfrage der Relationen: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Methode, um Tabellen und Spalten der Datenbank zu laden
	private void loadTablesAndColumns()
	{
		String query = "SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE "
	+
				"FROM INFORMATION_SCHEMA.COLUMNS "
	+
				"WHERE TABLE_SCHEMA = DATABASE();";

		try (Connection conn = SQLConnection.getCon();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(query))
		{

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			queryResultTable.getColumns().clear();

			for (int i = 1; i <= columnCount; i++)
			{
				final int j = i;
				TableColumn<ObservableList<String>, String> column = new TableColumn<>(rsmd.getColumnName(i));
				column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j - 1)));
				queryResultTable.getColumns().add(column);
			}

			ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
			while (rs.next())
			{
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= columnCount; i++)
				{
					row.add(rs.getString(i));
				}
				data.add(row);
			}
			queryResultTable.setItems(data);
		} catch (SQLException e)
		{
			showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler bei der Abfrage der Tabellen und Spalten: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Methode, um gespeicherte SQL-Abfragen zu laden
	private void loadSavedQueries()
	{
		sqlDropdown.getItems().clear();
		String query = "SELECT query FROM saved_queries";

		try (Connection conn = SQLConnection.getCon();
			 PreparedStatement pstmt = conn.prepareStatement(query);
			 ResultSet rs = pstmt.executeQuery())
		{

			while (rs.next())
			{
				String savedQuery = rs.getString("query");
				if (!sqlDropdown.getItems().contains(savedQuery))
				{
					sqlDropdown.getItems().add(savedQuery);
				}
			}
		} catch (SQLException e)
		{
			showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler beim Laden der gespeicherten SQL-Abfragen: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Methode, um eine Alert-Box anzuzeigen
	private void showAlert(Alert.AlertType alertType, String title, String message)
	{
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// Methode, um häufig verwendete SQL-Abfragen für das Dropdown-Menü bereitzustellen
	private ObservableList<String> getCommonSQLQueries()
	{
		return FXCollections.observableArrayList(
				"SELECT * FROM ausgabegruppe",
				"SELECT * FROM ausgabegruppe_ausgabe",
				"SELECT * FROM ausgabetagzeit",
				"SELECT * FROM berechtigung",
				"SELECT * FROM bescheid",
				"SELECT * FROM bescheidart",
				"SELECT * FROM deleted_memberofthefamily",
				"SELECT * FROM einkauf",
				"SELECT * FROM einstellungen",
				"SELECT * FROM familienmitglied",
				"SELECT * FROM Geburtsdatum",
				"SELECT * FROM haushalt",
				"SELECT * FROM nation",
				"SELECT * FROM neue_tabelle",
				"SELECT * FROM ortsteil",
				"SELECT * FROM PIECHART",
				"SELECT * FROM plz",
				"SELECT * FROM recht",
				"SELECT * FROM statistiktool",
				"SELECT * FROM tools",
				"SELECT * FROM users",
				"SELECT * FROM verteilestelle",
				"SELECT * FROM vollmacht",
				"SELECT * FROM vorlage",
				"SELECT * FROM warentyp",
				"INSERT INTO ausgabegruppe (column1, column2) VALUES (value1, value2)",
				"UPDATE ausgabetagzeit SET column1 = value1 WHERE condition",
				"DELETE FROM berechtigung WHERE condition",
				"CREATE TABLE neue_tabelle (column1 datatype, column2 datatype)",
				"ALTER TABLE einkauf ADD column_name datatype",
				"DROP TABLE deleted_memberofthefamily"
		);
	}
	/*
	/** Methode, um eine Tabelle aus der Datenbank abzufragen und anzuzeigen
	private void executeTableQuery(String tableName) {
		if (tableName == null || tableName.isEmpty()) {
			showAlert(Alert.AlertType.ERROR, "Fehler", "Tabellenname darf nicht leer sein.");
			return;
		}

		String query = "SELECT * FROM " + tableName;

		try (Connection conn = SQLConnection.getCon();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(query)) {

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			// Vorhandene Spalten löschen
			queryResultTable.getColumns().clear();

			// Neue Spalten hinzufügen
			for (int i = 1; i <= columnCount; i++) {
				final int j = i;
				TableColumn<ObservableList<String>, String> column = new TableColumn<>(rsmd.getColumnName(i));
				column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j - 1)));
				queryResultTable.getColumns().add(column);
			}

			// Daten zur Tabelle hinzufügen
			ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
			while (rs.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= columnCount; i++) {
					row.add(rs.getString(i));
				}
				data.add(row);
			}
			queryResultTable.setItems(data);
		} catch (SQLException e) {
			showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler bei der Abfrage der Tabelle: " + e.getMessage());
			e.printStackTrace();
		}
	}         **/
}
