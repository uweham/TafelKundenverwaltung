package kundenverwaltung.dao;

import kundenverwaltung.controller.StatistiktoolController;
import kundenverwaltung.model.statistiktool.Statistiktool;
import kundenverwaltung.service.Constants;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Implementierung der StatistiktoolDAO-Schnittstelle.
 * Diese Klasse enthält die Methoden zur Interaktion mit der Datenbank für das Statistiktool.
 */
public class StatistiktoolDAOimpl implements kundenverwaltung.dao.StatistiktoolDAO
{
  record SQLStringPar(int pos,String value) {};
  record SQLIntPar(int pos,int value) {};

  List<SQLStringPar>sqlstringpar=new ArrayList<>();
  List<SQLIntPar>sqlintpar=new ArrayList<>();
  private int cntpar=0;

   public void clearSQLPar()
   {
     sqlstringpar.clear();
     sqlintpar.clear();
     cntpar=0;
   }
   
   public void addSqlPar(int pos,String value)
   {
     sqlstringpar.add(new SQLStringPar(pos, value));
     cntpar++;
   }
   
   public void addSqlPar(int pos,int value)
   {
     sqlintpar.add(new SQLIntPar(pos, value));
     cntpar++;
   }
    /**
     * Lädt die Altersstatistik aus der Datenbank basierend auf dem Jahr und den Altersgruppen.
     * @param year Das Jahr, für das die Statistik geladen werden soll.
     * @param altersgruppen Eine Liste von Altersgruppen, die in der Statistik berücksichtigt werden sollen.
     * @return Ein Optional, das das Statistiktool enthält, falls die Abfrage erfolgreich war.
     */
    public String buildSqlQueryAlterstatistik(int verteilstelleId, int year, List<int[]> altersgruppen)
    {
      /* sample query
       * select b.grp_age_from,b.grp_age_to,count(*) as age_count from 
         (select haushaltId,TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) as age from familienmitglied) a  
          join 
           (select 0 as grp_age_from, 6 as grp_age_to from dual union
            select 7 as grp_age_from, 99 as grp_age_to from dual ) b
            on a.age between b.grp_age_from and b.grp_age_to
            group by b.grp_age_from,b.grp_age_to

       */
      String sqlsubquery= (verteilstelleId==Constants.ALL_DISTRIBUTION_POINTS)?
                          " true ":
                          " haushaltId in (select kundennummer from haushalt where verteilstellenId = "+verteilstelleId+" )";
      String sqlquery="";
      if (year == 0)
      {
         sqlquery = "select b.grp_age_from,b.grp_age_to,count(*) as age_count from "
                        + "(select haushaltId,TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) as age from familienmitglied "
                        + "where "
                        + sqlsubquery
                        + ") a " 
                        + "join ( ";
              for (int i = 0; i < altersgruppen.size(); i++)
                  {
                    sqlquery += (i>0)?" union select ":" select ";
                    sqlquery += altersgruppen.get(i)[0]+" as grp_age_from, "+ altersgruppen.get(i)[1] + " as grp_age_to from dual";
                  }
              sqlquery += ") b"
                          +" on a.age between b.grp_age_from and b.grp_age_to "
                          +" group by b.grp_age_from,b.grp_age_to ";
        }
      else
      {
        sqlquery="select "+year+" as year_of_birth, "
                +" count(CASE WHEN YEAR(gDatum) = "+year
                +" Then 1 END) as year_count from familienmitglied"
                + " where "
                + sqlsubquery;
      }
      return sqlquery;      

    }
    
    public String buildSqlQueryGuthabenstatistik(int verteilstelleId,  int statistictypId)
    {
      
      String sqlquery="";
      String sqlhaving=switch (statistictypId) {
        case Constants.STATISTIK_AMOUNTS_ALL -> "";
        case Constants.STATISTIK_AMOUNTS_CREDITS -> " having berechnetersaldo>0 " ;
        case Constants.STATISTIK_AMOUNTS_OUTSTANDING -> " having berechnetersaldo < 0";
        default -> " ";
      };
      if (statistictypId != Constants.STATISTIK_AMOUNTS_ERROR)
      {
        sqlquery= "SELECT  e.kunde, f.vName,f.nName, "
            + "    sum(e.summeEinkauf) as summeEinkauf, sum(e.summeZahlung) as summeZahlung, "
            + "    sum(case when e.warentyp <> "+Constants.WARENTYPID_GUTSCHRIFT+" then e.anzahlKinder+ e.anzahlErwachsene else 0 end) as anzahlPortionen ,"
            + "    sum(e.summeZahlung-e.summeEinkauf) as berechnetersaldo"
            + "    FROM einkauf e  "
            + "    JOIN verteilstelle v ON e.beiVerteilstelle  = v.verteilstellenId "
            + "    JOIN familienmitglied f ON e.kunde = f.haushaltId and f.haushaltsVorstand = 1"
            + "    WHERE " 
            +    ((verteilstelleId==Constants.ALL_DISTRIBUTION_POINTS) ? " true  ":"e.beiVerteilstelle = ? ")
            + "  AND e.storniertAm IS NULL "
            + "    group by e.kunde "
            +  sqlhaving + " ;";
      }
      else 
      {
        sqlquery="SELECT  e.kunde, f.vName,f.nName,sum(e.summeZahlung-e.summeEinkauf) as berechnetersaldo, h.saldo as haushaltsaldo "
            +    " FROM einkauf e  "
            +    " JOIN haushalt h ON e.kunde = h.kundennummer "
            +    " JOIN familienmitglied f ON e.kunde = f.haushaltId and f.haushaltsVorstand = 1 "
            +    " WHERE "
            +   ((verteilstelleId==Constants.ALL_DISTRIBUTION_POINTS) ? " true  ":"e.beiVerteilstelle = ? ")
            +     " AND e.storniertAm IS NULL "
            +    " group by e.kunde "
            +    " having berechnetersaldo != haushaltsaldo"
            +    " ;";
            
            
      }
      return sqlquery;
    }
    
    @Override
    public Optional<ResultSet> loadStatistik(String query) {
      System.out.println("SQL-Abfrage: " + query);
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try 
        {
           conn = SQLConnection.getCon();
           pstmt = conn.prepareStatement(query); 
           if (cntpar>0)
           {
             for (int i=0 ;i<sqlstringpar.size() ;i++)
             {
               pstmt.setString(sqlstringpar.get(i).pos  , sqlstringpar.get(i).value);
             }
             for (int i=0 ;i<sqlintpar.size() ;i++)
             {
               pstmt.setInt(sqlintpar.get(i).pos  , sqlintpar.get(i).value);
             }
           }
           sqlstringpar.clear();
           cntpar=0;
           rs = pstmt.executeQuery();
           return Optional.of(rs);
          
      } catch (SQLException e)
      {
          System.out.println("Datenabfrage fehlgeschlagen: " + e.getMessage());
          e.printStackTrace();
      }
      finally {
        // Do not close the ResultSet here, as it is returned to the caller.
        // The caller is responsible for closing it.
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.out.println("Fehler beim Schließen des PreparedStatement: " + e.getMessage());
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Fehler beim Schließen der Connection: " + e.getMessage());
            }
        }
    }
     return Optional.empty();
    }
   
    
    
    public void executeSQLVoidQuery(String sqlQuery) throws SQLException
    {
        try (Connection conn = SQLConnection.getCon();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery))
        {

            // Hier kannst du das ResultSet verarbeiten oder in der View anzeigen
            while (rs.next())
            {
                // Beispielsweise kannst du die Spalten ausgeben
                System.out.println(rs.getString(1));  // Ausgabe der ersten Spalte
            }
        } catch (SQLException e)
        {
            System.out.println("Fehler bei der SQL-Abfrage: " + e.getMessage());
            e.printStackTrace();
            throw e; // Exception weiterwerfen
        }
    }
    /**
     */
    public List<Map<String, String>> executeSQLQuery(String sqlQuery) throws SQLException
    {
        List<Map<String, String>> results = new ArrayList<>();
        try (Connection conn = SQLConnection.getCon();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery))
        {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rs.next())
            {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++)
                {
                    row.put(rsmd.getColumnName(i), rs.getString(i));
                }
                results.add(row);
            }
        }
        return results;
    }
    /**
     */
    @Override
    public boolean saveAltersgruppen(int year, List<int[]> altersgruppen, int jahresergebnis)
    {
        List<Integer> ergebnisse = new ArrayList<>();
        for (int i = 0; i < altersgruppen.size(); i++)
        {
            ergebnisse.add(0); // Standardwert, falls keine Ergebnisse vorhanden
        }
        return saveAltersgruppen(year, altersgruppen, jahresergebnis, ergebnisse);
    }
    /**
     */
    @Override
    public boolean saveAltersgruppen(int year, List<int[]> altersgruppen, int jahresergebnis, List<Integer> ergebnisse)
    {
        String deleteQuery = "DELETE FROM altersgruppen WHERE year = ?";
        String insertQuery = "INSERT INTO altersgruppen (year, startAlter, endAlter, ergebnis, jahresergebnis) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = SQLConnection.getCon();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery))
        {

            // Lösche alte Einträge für das Jahr
            deleteStmt.setInt(1, year);
            deleteStmt.executeUpdate();

            // Füge neue Einträge hinzu
            for (int i = 0; i < altersgruppen.size(); i++)
            {
                int startAlter = altersgruppen.get(i)[0];
                int endAlter = altersgruppen.get(i)[1];
                int ergebnis = ergebnisse.get(i); // Ergebnis aus der Liste der Ergebnisse

                insertStmt.setInt(1, year); // Das Jahr für das Jahresergebnis
                insertStmt.setInt(2, startAlter);
                insertStmt.setInt(3, endAlter);
                insertStmt.setInt(4, ergebnis);
                insertStmt.setInt(5, jahresergebnis);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
            return true;
        } catch (SQLException e)
        {
            System.out.println("Fehler beim Speichern der Altersgruppen: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    /**
     */
    @Override
    public boolean saveAltersgruppen(int year, List<int[]> altersgruppen)
    {
        throw new UnsupportedOperationException("Diese Methode wird nicht verwendet.");
    }

    // Die Methode getErgebnisForGroup bleibt unverändert
    @SuppressWarnings("unused")
    private int getErgebnisForGroup(int year, int startAlter, int endAlter)
    {
        String sql = "SELECT COUNT(*) FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN ? AND ?";
        try (Connection conn = SQLConnection.getCon();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, startAlter);
            pstmt.setInt(2, endAlter);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
            {
                return rs.getInt(1);
            }
        } catch (SQLException e)
        {
            System.out.println("Fehler bei der Berechnung des Ergebnisses: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Berechnet das Jahresergebnis basierend auf dem gegebenen Jahr.
     *
     * @param year Das Jahr, für das das Ergebnis berechnet werden soll.
     * @return Die Anzahl der Einträge für das gegebene Jahr.
     */
    public int getYearResult(int year)
    {
        if (year < 0)
        {
            System.out.println("Bitte geben Sie ein gültiges Jahr ein.");
            return 0;
        }

        String sql = "SELECT COUNT(*) AS Jahresergebnis FROM familienmitglied WHERE YEAR(gDatum) = ?";
        try (Connection conn = SQLConnection.getCon();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
            {
                return rs.getInt("Jahresergebnis");
            }
        } catch (SQLException e)
        {
            System.out.println("Fehler bei der Berechnung des Jahresergebnisses: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;  // Rückgabe 0, wenn keine Ergebnisse gefunden wurden oder ein Fehler aufgetreten ist
    }

    /**
     * Diese Methode wird nicht implementiert.
     */
    @Override
    public Optional<Statistiktool> loadAltersstatistik(int year, List<int[]> altersgruppen, String verteilstelle)
    {
        return Optional.empty();
    }

    /**
     * Diese Methode wird nicht implementiert.
     */
    @Override
    public List<Statistiktool> loadAltersstatistik(LocalDate selectedDate, List<int[]> altersgruppen)
    {
        return null;
    }

    /**
     * Diese Methode wird nicht implementiert.
     */
    @Override
    public List<Statistiktool> loadAltersstatistik(StatistiktoolController controller)
    {
        return null;
    }
    /**
     */
    @Override
    public List<Statistiktool> loadNationalitaeten()
    {
        List<Statistiktool> nationalitaetenList = new ArrayList<>();
        String sql = "SELECT * FROM nation"; // Beispiel SQL, passe den Tabellennamen an

        try (Connection conn = SQLConnection.getCon();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery())
        {

            while (rs.next())
            {
                String name = rs.getString("name"); // Passe den Spaltennamen an
                Statistiktool nationalitaet = new Statistiktool();
                // Verwenden des Standardkonstruktors
                nationalitaet.setName(name); // Setzen des Namens
                nationalitaetenList.add(nationalitaet); // Hinzufügen zur Liste
            }
        } catch (SQLException e)
        {
            System.out.println("Fehler beim Laden der Nationalitäten: " + e.getMessage());
            e.printStackTrace();
        }

        return nationalitaetenList;
    }
    /**
     * Speichert die Statistikdaten in einer CSV-Datei.
     *
     * @param statistikData Die zu speichernden Statistikdaten.
     * @param fileName      Der Name der Datei, in die die Daten gespeichert werden sollen.
     */
    @Override
    public void saveStatistik(List<String[]> statistikData, String fileName)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName)))
        {
            for (String[] data : statistikData)
            {
                writer.write(String.join(",", data));
                writer.newLine();
            }
        } catch (IOException e)
        {
            System.out.println("Fehler beim Speichern der CSV-Datei: " + e.getMessage());
        }
    }

    /**
     * Lädt die Statistikdaten aus einer CSV-Datei.
     *
     * @param fileName Der Name der Datei, aus der die Daten geladen werden sollen.
     * @return Eine Liste von String-Arrays, die die geladenen Daten enthalten.
     */
 /*   @Override
    public List<String[]> loadStatistik(String fileName)
    {
        List<String[]> statistikData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                statistikData.add(line.split(","));
            }
        } catch (IOException e)
        {
            System.out.println("Fehler beim Lesen der CSV-Datei: " + e.getMessage());
        }
        return statistikData;
    }
*/
    
    /**
     * Führt eine benutzerdefinierte SQL-Abfrage aus.
     *
     * @param query Die auszuführende SQL-Abfrage.
     */
    public void executeCustomQuery(String query)
    {
        try (Connection conn = SQLConnection.getCon();
             Statement stmt = conn.createStatement())
        {
            stmt.execute(query);
            System.out.println("SQL-Abfrage erfolgreich ausgeführt: " + query);
        } catch (SQLException e)
        {
            System.out.println("Fehler beim Ausführen der SQL-Abfrage: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     */
    @Override
    public Connection getConnection()
    {
        return null; // Methode implementieren oder entfernen
    }





 

}

