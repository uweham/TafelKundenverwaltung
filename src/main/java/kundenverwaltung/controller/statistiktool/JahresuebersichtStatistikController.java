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
import kundenverwaltung.service.Constants;
import javafx.scene.control.CheckBox;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller für die Jahresübersicht-Statistik.
 * Diese Klasse verwaltet die Benutzeroberfläche und die Interaktionen für die Jahresübersicht-Statistik.
 */
public class JahresuebersichtStatistikController extends StatistiktoolMasterClassController<JahresuebersichtStatistikController>
{

	@FXML
	private ComboBox<Integer> yearDropdown; // Dropdown-Menü zur Auswahl des Jahres


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
	    ckbxSummen.setSelected(true);
	    loadheader("Tafel Statistik - Angemeldet als :","Statistik:Jahresstatistik");
	    loadresultview(this,childResultContainer.getPrefWidth(),childResultContainer.getPrefHeight()) ;
	    
	      
/*		setupYearDropdown(); // Initialisiert das Dropdown-Menü für die Jahresauswahl
		setupTableColumns(); // Initialisiert die Tabellenspalten

		// Setzt die Aktion für das Dropdown-Menü
		yearDropdown.setOnAction(event ->
		{
			Integer selectedYear = yearDropdown.getValue();
			if (selectedYear != null)
			{
				filterByYear(selectedYear); // Filtert die Daten nach dem ausgewählten Jahr
				saveData(); // Speichert die Daten dynamisch nach dem Filtern
			}
		});
		*/
	}
	
	   public String getCurrentSQLQuery()
	    {
	       int verteilstellenId=getSelectedVerteilstelle();
	    
	       boolean summenflg=ckbxSummen.isSelected();
	       Integer selectedYear = getSelectedYear();
	       String query = statistikDAO.buildSqlQueryJahresstatistik(verteilstellenId,selectedYear,summenflg);
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


}
