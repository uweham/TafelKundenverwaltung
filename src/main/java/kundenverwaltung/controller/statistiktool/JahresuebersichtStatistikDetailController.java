package kundenverwaltung.controller.statistiktool;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import kundenverwaltung.dao.VerteilstelleDAO;
import kundenverwaltung.dao.VerteilstelleDAOimpl;
import kundenverwaltung.model.Verteilstelle;
import kundenverwaltung.service.Constants;
import javafx.scene.control.CheckBox;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller für die Jahresübersicht-Statistik.
 * Diese Klasse verwaltet die Benutzeroberfläche und die Interaktionen für die Jahresübersicht-Statistik.
 */
public class JahresuebersichtStatistikDetailController extends StatistiktoolMasterClassController<JahresuebersichtStatistikDetailController>
{

	@FXML
	private ComboBox<Integer> yearDropdown; // Dropdown-Menü zur Auswahl des Jahres

	@FXML
	private ComboBox<rangestatistic> dataDropdown;
	
	@FXML
	private MenuItem handleExit; // Menüpunkt zum Beenden der Anwendung
	
    @FXML
    private CheckBox ckbxSummen;

	/**
	 * Initialisiert den Controller.
	 * Diese Methode wird automatisch aufgerufen, nachdem die FXML-Datei geladen wurde.
	 */
	public void initialize()
	{
	  
	    setupYearDropdown(); // Initialisiert das Dropdown-Menü für die Jahresauswahl
	    initVerteilstelle(); 
	    initDataDropdown();
	    ckbxSummen.setSelected(true);
	    loadheader("Tafel Statistik - Angemeldet als :","Statistik:Jahresstatistik Detail");
	    loadresultview(this,childResultContainer.getPrefWidth(),childResultContainer.getPrefHeight()) ;
	    
	}
	
	   public String getCurrentSQLQuery()
	    {
	       int verteilstellenId=getSelectedVerteilstelle();
	    
	       boolean summenflg=ckbxSummen.isSelected();
	       Integer selectedYear = getSelectedYear();
	       Integer selectedData = getSelectedData();
	       String query = statistikDAO.buildSqlQueryJahresstatistikDetail(verteilstellenId,selectedYear,selectedData, summenflg);
	       statistikDAO.addSqlPar(1, selectedYear);
	       //statistikDAO.addSqlPar(1,verteilstellenId);
	             
	        return query.toString();
	    }

	/*
	 * Initialisiert das Dropdown-Menü für die Jahresauswahl.
	 */
	private void setupYearDropdown()
	{
		List<Integer> years = getAvailableYears(); // Holt die verfügbaren Jahre
		ObservableList<Integer> yearList = FXCollections.observableArrayList(years);
		yearDropdown.setItems(yearList); // Setzt die Jahre in das Dropdown-Menü
	}

	/**
	 * Gibt die verfügbaren Jahre zurück.
	 * @return Eine Liste der verfügbaren Jahre.
	 */
	private List<Integer> getAvailableYears()
	{
		List<Integer> years = new ArrayList<>();
		for (int i = 2030; i >= 2020; i--)
		{
			years.add(i); // Fügt die Jahre von 2024 bis 2020 zur Liste hinzu
		}
		return years;
	}

	 public int getSelectedYear()
	  {
	    if (yearDropdown.getSelectionModel().getSelectedItem()==null)
	    {
	      System.out.println("Combo Range=null");
	      return Year.now().getValue();
	    }
	    int selectedYear=yearDropdown.getValue();
	    
	    return selectedYear;
	    
	  }
	 
	   public int getSelectedData()
	      {
	        if (dataDropdown.getSelectionModel().getSelectedItem()==null)
	        {
	          System.out.println("Combo Range=null");
	          return Constants.STATISTIK_FIELD_ALL;
	        }
	        int selectedData=dataDropdown.getSelectionModel().getSelectedItem().getId();
	        
	        return selectedData;
	        
	      }
	 
	 public void initDataDropdown()
	  {
	    dataDropdown.getItems().clear(); // ComboBox leeren
	    dataDropdown.getItems().add(new rangestatistic(Constants.STATISTIK_FIELD_ALL, "je Nation+Bescheid"));
        dataDropdown.getItems().add(new rangestatistic(Constants.STATISTIK_FIELD_NATION, "je Nation"));
        dataDropdown.getItems().add(new rangestatistic(Constants.STATISTIK_FIELD_BESCHEID, "je Bescheid"));
	    
	  }

}
