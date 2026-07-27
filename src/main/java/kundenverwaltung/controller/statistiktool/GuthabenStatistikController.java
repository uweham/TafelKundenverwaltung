package kundenverwaltung.controller.statistiktool;

import kundenverwaltung.dao.EinkaufDAO;
import kundenverwaltung.dao.EinkaufDAOimpl;
import kundenverwaltung.dao.StatistiktoolDAO;
import kundenverwaltung.dao.VerteilstelleDAO;
import kundenverwaltung.dao.VerteilstelleDAOimpl;
import kundenverwaltung.model.Einkauf;
import kundenverwaltung.model.Verteilstelle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kundenverwaltung.service.Constants;
import kundenverwaltung.service.TablePreferenceServiceImpl;

import java.util.List;

public class GuthabenStatistikController extends StatistiktoolMasterClassController<GuthabenStatistikController>
{
  
    record statistictyp(int pos,String value) {
      @Override
      public String toString() {
          return value;
      }
      public int getId() {
        return pos;
      }
    };
    

	@FXML
	private ComboBox<Verteilstelle> verteilstelleComboBox; // ComboBox für Verteilstellen
	@FXML
	private ComboBox<statistictyp> typeComboBox; // ComboBox für Typen

	@FXML
	private MenuItem handleExit;

	private VerteilstelleDAO verteilstelleDAO = new VerteilstelleDAOimpl(); // DAO für Verteilstellen


	@FXML
	private void initialize()
	{
		// ComboBox initialisieren
	    typeComboBox.getItems().add(new statistictyp(Constants.STATISTIK_AMOUNTS_ALL,"Alle"));
	    typeComboBox.getItems().add(new statistictyp(Constants.STATISTIK_AMOUNTS_OUTSTANDING,"Offene Beträge"));
	    typeComboBox.getItems().add(new statistictyp(Constants.STATISTIK_AMOUNTS_CREDITS,"Guthaben"));
	    typeComboBox.getItems().add(new statistictyp(Constants.STATISTIK_AMOUNTS_ERROR,"Fehlerliste"));
        
	 
	       // Lade Verteilstellen in die ComboBox
	    initVerteilstelle();
	    initRange();
        loadheader("Tafel Statistik - Angemeldet als :","Statistik:Guthaben/offene Beträge");
        loadresultview(this,childResultContainer.getPrefWidth(),childResultContainer.getPrefHeight()) ;
   
	}

	 public String getCurrentSQLQuery()
	  {
	     int verteilstellenId=getSelectedVerteilstelle();
	     int statistictypId=getSelectedStatisticType();
	     int rangeId=getSelectedRange();
	     String query = statistikDAO.buildSqlQueryGuthabenstatistik(verteilstellenId, statistictypId,rangeId);
	     statistikDAO.addSqlPar(1,verteilstellenId);
	      
	      return query.toString();
	  }
	 
	 public int getSelectedStatisticType()
	 {
	    if (typeComboBox.getSelectionModel().getSelectedItem()==null)
	    {
	      System.out.println("Combo typeComboBox =null");
	      return Constants.STATISTIK_AMOUNTS_ALL;
	    }
	    int statistictypId=typeComboBox.getSelectionModel().getSelectedItem().getId();
	    
	    System.out.printf("V-Id : %d Name : %s\n",statistictypId,typeComboBox.getSelectionModel().getSelectedItem().toString());
	    return statistictypId;
	 	   
	 }

	private void showAlert(String title, String headerText, String contentText)
	{
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}
/*
	@FXML
	private void handleGenerateSQLQuery()
	{
		String type = typeComboBox.getValue();
		Verteilstelle verteilstelle = verteilstelleComboBox.getValue(); // Holen Sie sich die ausgewählte Verteilstelle

		if (verteilstelle == null || type == null)
		{
			showAlert("Warnung", "Fehlende Filterkriterien", "Bitte alle Filterkriterien ausfüllen.");
			return;
		}

		// Verwende die Methode aus der DAO-Klasse, um die SQL-Abfrage zu generieren
		String sqlQuery = einkaufDAO.generateSQLQuery(type, verteilstelle.getBezeichnung());

		// Zeige die SQL-Abfrage in einem Dialog an
		showSQLQueryDialog(sqlQuery);
	}

	private void showSQLQueryDialog(String sqlQuery)
	{
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle("Generierte SQL-Abfrage");
		dialog.setHeaderText("Hier ist die generierte SQL-Abfrage:");

		// Erstelle ein TextArea, das den SQL-Text enthält und kopierbar ist
		TextArea textArea = new TextArea(sqlQuery);
		textArea.setWrapText(true);
		textArea.setEditable(false);

		// TextArea zur Dialog-Content hinzufügen
		dialog.getDialogPane().setContent(textArea);

		// Hinzufügen von "Schließen"-Button
		ButtonType closeButton = new ButtonType("Schließen", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(closeButton);

		dialog.showAndWait();
	}

	// Methode zum Verarbeiten der Einkäufe
	private void processEinkaeufe(List<Einkauf> einkaeufe)
	{
		einkaufList.setAll(einkaeufe); // Die ObservableList aktualisieren, um die Daten in der Tabelle anzuzeigen
	}

	// Methode zum Laden von Daten
	@FXML
	private void handleLoadData()
	{
		String type = typeComboBox.getValue();
		Verteilstelle verteilstelle = verteilstelleComboBox.getValue(); // Holen Sie sich die ausgewählte Verteilstelle

		if (type == null || verteilstelle == null)
		{
			showAlert("Warnung", "Fehlende Filterkriterien", "Bitte alle Filterkriterien ausfüllen.");
			return;
		}

		List<Einkauf> einkaeufe = einkaufDAO.getEinkaeufeByQuery(type, verteilstelle.getBezeichnung());
		if (einkaeufe == null || einkaeufe.isEmpty())
		{
			showAlert("Fehler", "Keine Daten gefunden.", "Bitte überprüfen Sie die Eingaben.");
			return;
		}

		processEinkaeufe(einkaeufe);

		// Daten in die Guthabenstatistik speichern
		einkaufDAO.saveToGuthabenStatistik(einkaeufe);
	}
*/
	

}
