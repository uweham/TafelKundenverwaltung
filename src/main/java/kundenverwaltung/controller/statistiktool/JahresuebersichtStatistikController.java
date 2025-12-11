package kundenverwaltung.controller.statistiktool;

import kundenverwaltung.dao.JahresuebersichtDAO;
import kundenverwaltung.dao.JahresuebersichtDAOimpl;
import kundenverwaltung.model.statistiktool.Jahresuebersicht;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import kundenverwaltung.service.TablePreferenceServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller für die Jahresübersicht-Statistik.
 * Diese Klasse verwaltet die Benutzeroberfläche und die Interaktionen für die Jahresübersicht-Statistik.
 */
public class JahresuebersichtStatistikController
{

	@FXML
	private ComboBox<Integer> yearDropdown; // Dropdown-Menü zur Auswahl des Jahres

	@FXML
	private TableView<Jahresuebersicht> jahresuebersichtTable; // Tabelle zur Anzeige der Jahresübersicht

	@FXML
	private TableColumn<Jahresuebersicht, String> monatColumn; // Spalte für den Monat

	@FXML
	private TableColumn<Jahresuebersicht, Integer> neuzugaengeColumn; // Spalte für die Anzahl der Neuzugänge

	@FXML
	private TableColumn<Jahresuebersicht, Integer> anzahlPersonenColumn; // Spalte für die Anzahl der Personen

	@FXML
	private TableColumn<Jahresuebersicht, Double> gesamtUmsatzHaushaltColumn; // Spalte für den Gesamtumsatz Haushalt

	@FXML
	private TableColumn<Jahresuebersicht, Double> gesamtUmsatzEinkaufColumn; // Spalte für den Gesamtumsatz Einkauf

	@FXML
	private MenuItem handleExit; // Menüpunkt zum Beenden der Anwendung

	private JahresuebersichtDAO jahresuebersichtDAO = new JahresuebersichtDAOimpl(); // DAO für die Jahresübersicht

	/**
	 * Initialisiert den Controller.
	 * Diese Methode wird automatisch aufgerufen, nachdem die FXML-Datei geladen wurde.
	 */
	public void initialize()
	{
		setupYearDropdown(); // Initialisiert das Dropdown-Menü für die Jahresauswahl
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
	}

	/**
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
		for (int i = 2024; i >= 2020; i--)
		{
			years.add(i); // Fügt die Jahre von 2024 bis 2020 zur Liste hinzu
		}
		return years;
	}

	/**
	 * Initialisiert die Tabellenspalten.
	 */
	private void setupTableColumns()
	{
		monatColumn.setCellValueFactory(new PropertyValueFactory<>("monat"));
		monatColumn.setId("monat");

		neuzugaengeColumn.setCellValueFactory(new PropertyValueFactory<>("neuzugaenge"));
		neuzugaengeColumn.setId("neuzugaenge");

		anzahlPersonenColumn.setCellValueFactory(new PropertyValueFactory<>("anzahlPersonen"));
		anzahlPersonenColumn.setId("anzahlPersonen");

		gesamtUmsatzHaushaltColumn.setCellValueFactory(new PropertyValueFactory<>("gesamtUmsatzHaushalt"));
		gesamtUmsatzHaushaltColumn.setId("gesamtUmsatzHaushalt");

		gesamtUmsatzEinkaufColumn.setCellValueFactory(new PropertyValueFactory<>("gesamtUmsatzEinkauf"));
		gesamtUmsatzEinkaufColumn.setId("gesamtUmsatzEinkauf");

		TablePreferenceServiceImpl.getInstance().setupPersistence(jahresuebersichtTable, "JahresUebersichtStatistik");
	}

	/**
	 * Filtert die Daten nach dem ausgewählten Jahr.
	 * @param year Das ausgewählte Jahr.
	 */
	private void filterByYear(int year)
	{
		ObservableList<Jahresuebersicht> filteredData = jahresuebersichtDAO.getJahresuebersichtByYear(year); // Holt die gefilterten Daten

		if (filteredData == null || filteredData.isEmpty())
		{
			System.out.println("Keine Daten gefunden."); // Gibt eine Nachricht aus, wenn keine Daten gefunden wurden
		} else
		{
			System.out.println("Daten gefunden: " + filteredData.size() + " Einträge."); // Gibt die Anzahl der gefundenen Einträge aus
		}

		jahresuebersichtTable.setItems(filteredData); // Setzt die gefilterten Daten in die Tabelle
	}

	/**
	 * Speichert die Daten dynamisch.
	 */
	private void saveData()
	{
		System.out.println("saveData Methode aufgerufen");
		// Beispiel-Daten, die gespeichert werden sollen
		ObservableList<Jahresuebersicht> dataToSave = jahresuebersichtTable.getItems();
		for (Jahresuebersicht daten : dataToSave)
		{
			jahresuebersichtDAO.saveJahresuebersicht(daten); // Speichert die Daten
		}
		System.out.println("Daten erfolgreich gespeichert.");
	}

	/**
	 * Beendet die Anwendung.
	 */
	@FXML
	private void handleExit()
	{
		Stage stage = (Stage) jahresuebersichtTable.getScene().getWindow();
		stage.close(); // Schließt das Fenster
	}
}
