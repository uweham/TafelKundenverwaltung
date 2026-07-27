package kundenverwaltung.controller.statistiktool;

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
import javafx.stage.Stage;
import kundenverwaltung.service.Constants;
import kundenverwaltung.service.TablePreferenceServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class NationalitaetStatistikController extends StatistiktoolMasterClassController<NationalitaetStatistikController>
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
      loadheader("Tafel Statistik - Angemeldet als :","Statistik:Nationalitätenstatistik");
      loadresultview(this,childResultContainer.getPrefWidth(),childResultContainer.getPrefHeight()) ;
	}
	
    public String getCurrentSQLQuery()
    {
       int verteilstellenId=getSelectedVerteilstelle();
       int rangeId=getSelectedRange();
       String query = statistikDAO.buildSqlQueryNationaltaetenstatistik(verteilstellenId,rangeId);
       statistikDAO.addSqlPar(1,verteilstellenId);
             
        return query.toString();
    }


}
