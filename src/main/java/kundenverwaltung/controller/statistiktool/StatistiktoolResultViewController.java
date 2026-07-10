package kundenverwaltung.controller.statistiktool;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kundenverwaltung.controller.admintool.StatistiktoolSQLController;
import kundenverwaltung.dao.StatistiktoolDAO;
import kundenverwaltung.dao.StatistiktoolDAOimpl;
import kundenverwaltung.logger.event.GlobalEventLogger;
import kundenverwaltung.service.Constants;
import kundenverwaltung.service.SQLQuery_to_CSV;
import kundenverwaltung.service.ShowAlert;
import kundenverwaltung.service.TableView_to_PDF;
import kundenverwaltung.service.UtilErrorLog;


public class StatistiktoolResultViewController <T>{
 
    private T callController;
    
    @FXML
    private Button executeCSVExportButton;

    @FXML
    private Button executeQueryButton;

    @FXML
    private Button openSQLQueryToolButton;

    @FXML
    private Button printButton;

    @FXML
    private TableView<ObservableList<String>> queryResultTable;
    
    private UtilErrorLog utilerrorlog = new UtilErrorLog();
    private ShowAlert showalert=new ShowAlert();
    
    private StatistiktoolDAO statistikDAO;
    
    
    public StatistiktoolDAO getStatistikDAO() {
      return statistikDAO;
    }

    public void setStatistikDAO(StatistiktoolDAO statistikDAO) {
      this.statistikDAO = statistikDAO;
    }

    /**
     * @return the callController
     */
    public T getCallController() {
      return callController;
    }

    /**
     * @param callController the callController to set
     */
    public void setCallController(T callController) {
      this.callController = callController;
    }

    public void setPrefWidth(double prefwidth)
    {
      if (prefwidth>0)
      {
        queryResultTable.setPrefWidth(prefwidth - 20.0);
      }
 
    }
    
    public void setPrefHeight(double prefheight)
    {
      if (prefheight>0)
      {
        queryResultTable.setPrefHeight(prefheight - 70.0 );
      }
      
    }
    private String callControllergetCurrentSQLQuery="getCurrentSQLQuery";
    
    
    public String getCallControllergetCurrentSQLQuery() {
      return callControllergetCurrentSQLQuery;
    }

    public void setCallControllergetCurrentSQLQuery(String callControllergetCurrentSQLQuery) {
      this.callControllergetCurrentSQLQuery = callControllergetCurrentSQLQuery;
    }

    @FXML
    public void initialize()
    {
      System.out.println("Init :"+this.toString());
    }
 

    @FXML
    private void exportCSVStatistik()
    {
      queryStatistik(Constants.STATISTIK_OUT_CSV);
    }

    @FXML
    public void loadStatistik()
    {
      queryStatistik(Constants.STATISTIK_OUT_LIST);
    }
    
    private String getCurrentSQLQuery()
    {
      String sqlquery=""; 
      try {
        Method getdata = callController.getClass().getMethod(callControllergetCurrentSQLQuery);
        sqlquery=(String)(getdata.invoke(callController));
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        utilerrorlog.addError(Constants.ERROR_MSG_APPEND, e.getMessage());
        sqlquery="";
        e.printStackTrace();
      }
      return sqlquery;
    }
    
    private void queryStatistik(int statistik_output) {
 
      utilerrorlog.addError(Constants.ERROR_MSG_CLEAR, null );
      String sqlquery=getCurrentSQLQuery();
      if (!sqlquery.isEmpty())
      {
    
        Optional<ResultSet> statistikOpt = statistikDAO.loadStatistik(sqlquery);
        if (statistikOpt.isPresent())
        {
            ResultSet rs = statistikOpt.get();
            try {
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
            } catch (SQLException e) {
              utilerrorlog.addError(Constants.ERROR_MSG_APPEND, e.getMessage());
              e.printStackTrace();
            }
        }
        
       if (statistik_output == Constants.STATISTIK_OUT_CSV && utilerrorlog.getCnterror()==0)
       {     
          sqlquery=getCurrentSQLQuery(); // Query 
          if (!sqlquery.isEmpty())
          {
            statistikOpt = statistikDAO.loadStatistik(sqlquery);
            if (statistikOpt.isPresent())
            {
                ResultSet rs = statistikOpt.get();// Ensure ResultSet is closed after use
                int errorcode=export_to_csv(rs);
                String errormessage = switch (errorcode)
                {
                  case Constants.FILE_ERROR -> "CSV Schreibfehler !";
                  case Constants.FILE_NAME_ERROR -> "Dateiname ist leer ODER Dateityp ist nicht .csv";
                  case Constants.SQL_ERROR -> "SQL Fehler !";
                  default -> "";
                };
                if (!errormessage.isEmpty())
                {
                    utilerrorlog.addError(Constants.ERROR_MSG_APPEND,errormessage);
                }
            }
          }
      }
      if (utilerrorlog.getCnterror()>0)
      {
        showalert.showAlert(Alert.AlertType.ERROR, "Fehler", utilerrorlog.getErrormessage());
      }
     }
    }

  private int export_to_csv(ResultSet rs) 
  {
    int errorcode=Constants.NO_ERROR;
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters()
            .add(new FileChooser.ExtensionFilter("csv Datei", "*.csv"));
     SQLQuery_to_CSV sqltoquery = new SQLQuery_to_CSV();
     String filename=fileChooser.showSaveDialog(null).getAbsolutePath();
     if (filename == null)
      {
        return Constants.FILE_NAME_ERROR;
      }
     if (filename.toUpperCase().endsWith(".CSV"))
     {
       sqltoquery.setCsvFilePath(filename);
       errorcode=sqltoquery.to_CSV(rs);
       if (errorcode != Constants.NO_ERROR)
       {
         return errorcode;
       }
        
     } else
     {
       return Constants.FILE_NAME_ERROR;
     }
     return errorcode;
  }

  /**
   * Druckt die Ergebnisse als PDF.
   */
  @FXML
  private void handlePrint()
  {
      try
      {
          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("PDF speichern");
          fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Dateien", "*.pdf"));
          File file = fileChooser.showSaveDialog(printButton.getScene().getWindow());
          if (file != null)
          {
            TableView_to_PDF<ObservableList<String>> tableviewtopdf = new TableView_to_PDF<>(queryResultTable, file, "Statistik");
            int result=tableviewtopdf.to_PDF();
            if (result==Constants.NO_ERROR)
            {
              showalert.showAlert(AlertType.INFORMATION, "Erfolg", tableviewtopdf.getResultmessage());
            } else
            {
              showalert.showAlert(AlertType.ERROR, "Fehler", tableviewtopdf.getResultmessage());
            }
          }
      } catch (Exception e)
      {
          showalert.showAlert(AlertType.ERROR, "Fehler", "Fehler beim Drucken der Statistik: " + e.getMessage());
      }
  }

  @FXML
  private void openSQLQueryTool(ActionEvent actionEvent)
  {
      String sqlQuery= getCurrentSQLQuery();;
      if (sqlQuery.isEmpty())
      {  
        return ;
      }
      
      try
      {
          // Lade die FXML-Datei und initialisiere den Controller
          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/admintool/StatistiktoolSQL.fxml"));
          Parent root = fxmlLoader.load();

          // Hol den Controller und setze die SQL-Abfrage
          StatistiktoolSQLController sqlController = fxmlLoader.getController();
          sqlController.setSqlQuery(sqlQuery);

          // Erstelle eine neue Stage für das SQL-Abfrage-Tool
          Stage stage = new Stage();
          stage.setTitle("SQL-Abfrage Tool");

          Scene scene = new Scene(root);
          GlobalEventLogger.attachTo("StatistikToolSQL", scene);
          stage.setScene(scene);
          sqlController.setStage(stage);
          stage.show();
      } catch (IOException e)
      {
          // Zeige eine Fehlermeldung an, wenn die FXML-Datei nicht geladen werden kann
          showalert.showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler beim Laden der FXML-Datei: " + e.getMessage());
      }
  }


}
