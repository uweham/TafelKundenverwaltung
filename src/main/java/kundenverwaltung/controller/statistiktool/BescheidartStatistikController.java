package kundenverwaltung.controller.statistiktool;

import kundenverwaltung.controller.statistiktool.StatistiktoolMasterClassController.rangestatistic;
import kundenverwaltung.dao.NationDAO;
import kundenverwaltung.dao.NationDAOimpl;
import kundenverwaltung.dao.VerteilstelleDAO;
import kundenverwaltung.dao.VerteilstelleDAOimpl;
import kundenverwaltung.model.Nation;
import kundenverwaltung.model.Verteilstelle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.CheckBox;

import javafx.stage.Stage;
import kundenverwaltung.service.Constants;
import kundenverwaltung.service.TablePreferenceServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class BescheidartStatistikController extends StatistiktoolMasterClassController<BescheidartStatistikController>
{

    @FXML
    private ComboBox<Verteilstelle> verteilstelleComboBox;

    @FXML
    private MenuItem handleExit;
    
    @FXML
    private CheckBox ckbxSummen;
    
    @FXML
    private ComboBox<bescheidstatus> statusComboBox; // ComboBox für Gültig/Nicht gültig

    record bescheidstatus(int pos,String value) {
      @Override
      public String toString() {
          return value;
      }
      public int getId() {
        return pos;
      }
    };

    
    /**
     */
    @FXML
    public void initialize()
    {
      initRange();
      initVerteilstelle();  
      ckbxSummen.setSelected(true);
      statusComboBox.getItems().add(new bescheidstatus(Constants.STATISTIK_NOTIFICATION_TYPE_ALL,"Alle"));
      statusComboBox.getItems().add(new bescheidstatus(Constants.STATISTIK_NOTIFICATION_TYPE_VALID,"Gültig"));
      statusComboBox.getItems().add(new bescheidstatus(Constants.STATISTIK_NOTIFICATION_TYPE_INVALID,"Ungültig"));
      loadheader("Tafel Statistik - Angemeldet als :","Statistik:Bescheidartstatistik");
      loadresultview(this,childResultContainer.getPrefWidth(),childResultContainer.getPrefHeight()) ;
    }
    
    public String getCurrentSQLQuery()
    {
       int verteilstellenId=getSelectedVerteilstelle();
       int rangeId=getSelectedRange();
       int statusId=getSelectedNotification();
       boolean summenflg=ckbxSummen.isSelected();
       System.out.println("summenflg "+summenflg);
       String query = statistikDAO.buildSqlQueryBescheidartstatistik(verteilstellenId,rangeId,statusId, summenflg);
       statistikDAO.addSqlPar(1,verteilstellenId);
             
        return query.toString();
    }

    public int getSelectedNotification()
    {
      if (statusComboBox.getSelectionModel().getSelectedItem()==null)
      {
        System.out.println("Combo Notification=null");
        return Constants.STATISTIK_NOTIFICATION_TYPE_ALL;
      }
      int statusId=statusComboBox.getSelectionModel().getSelectedItem().getId();
      
      System.out.printf("S-Id : %d Name : %s\n",statusId,statusComboBox.getSelectionModel().getSelectedItem().toString());
      return statusId;
      
    }
}

