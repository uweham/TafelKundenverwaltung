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

public class AusgabegruppenStatistikController extends StatistiktoolMasterClassController<AusgabegruppenStatistikController>
{

    @FXML
    private ComboBox<Verteilstelle> verteilstelleComboBox;

    @FXML
    private MenuItem handleExit;
    
    
    /**
     */
    @FXML
    public void initialize()
    {
      initRange();
      initVerteilstelle();  
      loadheader("Tafel Statistik - Angemeldet als :","Statistik:Ausgabegruppenstatistik");
      loadresultview(this,childResultContainer.getPrefWidth(),childResultContainer.getPrefHeight()) ;
    }
    
    public String getCurrentSQLQuery()
    {
       int verteilstellenId=getSelectedVerteilstelle();
       int rangeId=getSelectedRange();
       
       String query = statistikDAO.buildSqlQueryAusgabegruppenstatistik(verteilstellenId,rangeId);
       statistikDAO.addSqlPar(1,verteilstellenId);
             
        return query.toString();
    }

}

