package kundenverwaltung.dao;

import kundenverwaltung.controller.StatistiktoolController;
import kundenverwaltung.model.statistiktool.Statistiktool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StatistiktoolDAO
{
   void addSqlPar(int pos,String value);
   void addSqlPar(int pos,int value);
        
    Optional<ResultSet> loadStatistik(String query);
    
 
    
    boolean saveAltersgruppen(int year, List<int[]> altersgruppen, int jahresergebnis);

    boolean saveAltersgruppen(int year, List<int[]> altersgruppen, int jahresergebnis, List<Integer> ergebnisse);

    Optional<Statistiktool> loadAltersstatistik(int year, List<int[]> altersgruppen, String verteilstelle);

    List<Statistiktool> loadAltersstatistik(LocalDate selectedDate, List<int[]> altersgruppen);

    List<Statistiktool> loadAltersstatistik(StatistiktoolController controller);

    List<Statistiktool> loadNationalitaeten();
    List<Map<String, String>> executeSQLQuery(String sqlQuery) throws SQLException;

    void saveStatistik(List<String[]> statistikData, String fileName);
//    List<String[]> loadStatistik(String fileName);

    void executeCustomQuery(String selectedQuery);
    

    Connection getConnection();

    boolean saveAltersgruppen(int year, List<int[]> altersgruppen);

    public String buildSqlQueryAlterstatistik(int verteilstelleId, int year, List<int[]> altersgruppen);
    public String buildSqlQueryGuthabenstatistik(int verteilstelleId,  int statistictypId);
}


