package kundenverwaltung.service;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.csv.CSVFormat;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import kundenverwaltung.dao.SQLConnection;

import org.apache.commons.csv.QuoteMode;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import java.io.FileWriter;
import java.io.IOException;

public class SQLQuery_to_CSV {
 String csvFilePath="";
 
 public String getCsvFilePath() {
  return csvFilePath;
}

 public void setCsvFilePath(String csvFilePath) {
  this.csvFilePath = csvFilePath;
 }

 char quote='"'; 
 
 
 public char getQuote() {
  return quote;
}

 public void setQuote(char quote) {
  this.quote = quote;
 }

 QuoteMode quoteMode=QuoteMode.NON_NUMERIC;
 
 
 public QuoteMode getQuoteMode() {
  return quoteMode;
}

 public void setQuoteMode(QuoteMode quoteMode) {
  this.quoteMode = quoteMode;
 }

 public int to_CSV(String query) 
 {
  int retcode=Constants.NO_ERROR;
  
  try (Connection conn = SQLConnection.getCon();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(query))
     {
       
     ResultSetMetaData rsmd = rs.getMetaData();

     CSVFormat csvFormat = CSVFormat.EXCEL.builder().setHeader(rsmd).setQuoteMode(this.quoteMode).setQuote(this.quote).get();
     
     try (FileWriter writer = new FileWriter(this.csvFilePath);
        CSVPrinter printer = new CSVPrinter(writer, csvFormat)) {
        printer.printRecords(rs);
        System.out.println("Basic CSV file written successfully!"); 
      } catch (IOException e) {
        retcode=Constants.FILE_ERROR;
        System.out.println("Fehler bei Write CSV File " + e.getMessage());
      
        e.printStackTrace();
      }
     
 } catch (SQLException e)
 {
   retcode=Constants.SQL_ERROR;
   System.out.println("Fehler bei der Ausführung der SQL-Abfrage: " + e.getMessage());
     e.printStackTrace();
 }
  return retcode;
}
 
 public int to_CSV(ResultSet rs) 
 {
  int retcode=Constants.NO_ERROR;
     try {
       ResultSetMetaData rsmd = rs.getMetaData();
       CSVFormat csvFormat = CSVFormat.EXCEL.builder().setHeader(rsmd).setQuoteMode(this.quoteMode).setQuote(this.quote).get();

       // Get metadata to fetch column names
       int columnCount = rsmd.getColumnCount();

       // Write to CSV
       try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(this.csvFilePath), csvFormat)) {
           // Print data
           while (rs.next()) {
               for (int i = 1; i <= columnCount; i++) {
                   Object value = rs.getObject(i);
                   csvPrinter.print(value != null ? value : "");
               }
               csvPrinter.println();
           }
       }
       
     } 
     catch (SQLException e) {
       System.out.println("Fehler beim Exportieren nach CSV: " + e.getMessage());
       return Constants.SQL_ERROR;
     } 
     catch (IOException e) {
       System.out.println("Fehler beim Exportieren nach CSV: " + e.getMessage());
       return Constants.FILE_ERROR;
     }
     return retcode;  
   }
 
}
 

