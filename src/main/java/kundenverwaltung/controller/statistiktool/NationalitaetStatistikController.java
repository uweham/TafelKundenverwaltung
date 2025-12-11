package kundenverwaltung.controller.statistiktool;

import kundenverwaltung.dao.NationDAO;
import kundenverwaltung.dao.NationDAOimpl;
import kundenverwaltung.model.Nation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import kundenverwaltung.service.TablePreferenceServiceImpl;

import java.util.ArrayList;

public class NationalitaetStatistikController
{

	@FXML
	private ComboBox<String> verteilstelleComboBox;

	@FXML
	private TableView<Nation> nationalitaetTableView;

	@FXML
	private TableColumn<Nation, String> nationalitaetColumn;

	@FXML
	private TableColumn<Nation, Integer> anzahlColumn;

	@FXML
	private Button loadButton;

	@FXML
	private MenuItem handleExit;

	private NationDAO nationDAO;
	/**
     */
	@FXML
	public void initialize()
	{
		// NationDAO-Implementierung initialisieren
		nationDAO = new NationDAOimpl();

		// Setze die Zell-Factory für die Spalten
		nationalitaetColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nationalitaetColumn.setId("name");

		anzahlColumn.setCellValueFactory(new PropertyValueFactory<>("anzahl"));
		anzahlColumn.setId("anzahl");

		// Beispiel für das Hinzufügen von Verteilstellen zur ComboBox
		verteilstelleComboBox.getItems().addAll("Hauptstelle",
				"AWO",
				"Stroot",
				"Bringdienst",
				"Freren",
				"Haren",
				"Lathen",
				"Spelle",
				"Twist");

		// Event-Handler für den Button festlegen
		loadButton.setOnAction(event -> handleLoadNationalitaeten());
		TablePreferenceServiceImpl.getInstance().setupPersistence(nationalitaetTableView, "NationalitaetStatistik");
	}

	@FXML
	private void handleLoadNationalitaeten()
	{
		String selectedVerteilstelle = verteilstelleComboBox.getSelectionModel().getSelectedItem();
		if (selectedVerteilstelle != null)
		{
			System.out.println("Nationalitäten für " + selectedVerteilstelle + " anzeigen.");

			// Daten aus der DAO abrufen
			ArrayList<Nation> nationList = nationDAO.getAllNationenMitAnzahl();
			if (nationList != null)
			{
				ObservableList<Nation> data = FXCollections.observableArrayList(nationList);
				// Füge die Daten zur TableView hinzu
				nationalitaetTableView.setItems(data);

				// Statistiken speichern
				nationDAO.saveNationStatistics(nationList);
			} else
			{
				System.out.println("Keine Nationen gefunden.");
			}
		} else
		{
			System.out.println("Bitte eine Verteilstelle auswählen.");
		}
	}

	@FXML
	private void handleExit()
	{
		Stage stage = (Stage) nationalitaetTableView.getScene().getWindow();
		stage.setOnHidden(event -> handleLoadNationalitaeten()); // Tabelle beim Schließen aktualisieren
		stage.close();
	}

}
