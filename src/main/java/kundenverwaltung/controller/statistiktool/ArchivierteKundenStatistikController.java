package kundenverwaltung.controller.statistiktool;

import kundenverwaltung.dao.HaushaltDAO;
import kundenverwaltung.dao.HaushaltDAOimpl;
import kundenverwaltung.model.statistiktool.ArchivierteKunden;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kundenverwaltung.service.TablePreferenceServiceImpl;

import java.sql.SQLException;

public class ArchivierteKundenStatistikController
{

	@FXML
	private MenuItem handleViewStatistics;
	@FXML
	private MenuItem handleExit;

	@FXML
	private TableView<ArchivierteKunden> customerTable;
	@FXML
	private TableColumn<ArchivierteKunden, String> kdNrColumn;
	@FXML
	private TableColumn<ArchivierteKunden, String> nameColumn;
	@FXML
	private TableColumn<ArchivierteKunden, String> strasseColumn;
	@FXML
	private TableColumn<ArchivierteKunden, String> plzColumn;
	@FXML
	private TableColumn<ArchivierteKunden, String> ortColumn;

	@FXML
	private ComboBox<String> customerTypeComboBox;

	private HaushaltDAO haushaltDAO; // DAO-Instanz, die die Daten lädt
	@FXML
	private Button saveCustomerButton; // Für den Speicher-Button
	/**
     */
	public void setHaushaltDAO(HaushaltDAO haushaltDAO)
	{
		this.haushaltDAO = haushaltDAO;
		if (haushaltDAO != null)
		{
			handleCustomerTypeChange(); // Lade die Kunden basierend auf der aktuellen Auswahl im Dropdown
		} else
		{
			showAlert(Alert.AlertType.ERROR, "Fehler", "DAO nicht gesetzt", "Das HaushaltDAO wurde nicht initialisiert.");
		}
	}

	@FXML
	public void initialize()
	{
		// Initialisiere DAO nur einmal
		if (haushaltDAO == null)
		{
			haushaltDAO = new HaushaltDAOimpl();
		}

		// CellValueFactory für TableColumns setzen
		kdNrColumn.setCellValueFactory(cellData -> cellData.getValue().kdNrProperty());
		kdNrColumn.setId("kdNr");

		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		nameColumn.setId("name");

		strasseColumn.setCellValueFactory(cellData -> cellData.getValue().strasseProperty());
		strasseColumn.setId("strasse");

		plzColumn.setCellValueFactory(cellData -> cellData.getValue().plzProperty());
		plzColumn.setId("plz");

		ortColumn.setCellValueFactory(cellData -> cellData.getValue().ortProperty());
		ortColumn.setId("ort");

		// ComboBox-Optionen hinzufügen
		customerTypeComboBox.getItems().addAll(); // Hier fügen Sie die Optionen hinzu

		// Handle für Änderung des Kunden-Typs
		customerTypeComboBox.setOnAction(e -> handleCustomerTypeChange());

		// Standardauswahl in der ComboBox setzen
		if (!customerTypeComboBox.getItems().isEmpty())
		{
			customerTypeComboBox.setValue(customerTypeComboBox.getItems().get(0));
		}

		// Kunden basierend auf der Auswahl laden
		handleCustomerTypeChange(); // Diese Methode sollte hier aufgerufen werden

		TablePreferenceServiceImpl.getInstance().setupPersistence(customerTable, "CustomerTableArchivierteKundenStatistik");
	}


	// Archivierte Kunden laden
	private void loadArchivedCustomers()
	{
		try
		{
			ObservableList<ArchivierteKunden> kunden = haushaltDAO.getArchivierteKunden();
			customerTable.setItems(kunden);
		} catch (SQLException e)
		{
			showAlert(Alert.AlertType.ERROR, "Fehler", "Datenbankfehler", "Daten konnten nicht geladen werden: " + e.getMessage());
		}
	}

	// Gesperrte Kunden laden
	private void loadBlockedCustomers()
	{
		try
		{
			ObservableList<ArchivierteKunden> gesperrteKunden = haushaltDAO.getGesperrteKunden();
			customerTable.setItems(gesperrteKunden);
		} catch (SQLException e)
		{
			showAlert(Alert.AlertType.ERROR, "Fehler", "Datenbankfehler", "Daten konnten nicht geladen werden: " + e.getMessage());
		}
	}

	// Typwechsel der Kunden ComboBox
	@FXML
	private void handleCustomerTypeChange()
	{
		refreshCustomerTable();
	}

	private void refreshCustomerTable()
	{
		String selectedType = customerTypeComboBox.getValue();
		if ("Archivierte Kunden".equals(selectedType))
		{
			loadArchivedCustomers();
		} else if ("Gesperrte Kunden".equals(selectedType))
		{
			loadBlockedCustomers();
		}
	}

	// Kunde Speichern
	@FXML
	private void handleSaveCustomer()
	{
		ArchivierteKunden selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

		if (selectedCustomer != null)
		{
			// Die Werte aus den TableColumns des ausgewählten Kunden holen
			String kdNr = selectedCustomer.getKdNr();
			String name = selectedCustomer.getName();
			String strasse = selectedCustomer.getStrasse();
			String plzText = selectedCustomer.getPlz(); // PLZ als String
			String ort = selectedCustomer.getOrt();

			// Überprüfen, ob die PLZ genau 5 Ziffern enthält
			if (!plzText.matches("\\d{5}"))
			{
				showAlert(Alert.AlertType.ERROR, "Ungültige Eingabe", "PLZ ungültig", "Bitte geben Sie eine gültige 5-stellige PLZ ein.");
				return;
			}

			// Aktualisierte Werte setzen
			selectedCustomer.setName(name);
			selectedCustomer.setStrasse(strasse);
			selectedCustomer.setPlz(plzText);
			selectedCustomer.setOrt(ort);

			try
			{
				// Überprüfen, ob der Kunde existiert
				if (haushaltDAO.customerExists(kdNr, plzText))
				{
					haushaltDAO.saveCustomer(selectedCustomer); // Kunde aktualisieren
					showAlert(Alert.AlertType.INFORMATION, "Erfolg", "Kunde aktualisiert", "Die Kundendaten wurden erfolgreich aktualisiert.");
					handleCustomerTypeChange(); // Tabelle aktualisieren nach dem Speichern
				} else
				{
					showAlert(Alert.AlertType.WARNING, "Warnung", "Kunde nicht gefunden", "Der Kunde existiert nicht in der Datenbank.");
				}
			} catch (SQLException e)
			{
				showAlert(Alert.AlertType.ERROR, "Fehler", "Datenbankfehler", "Der Kunde konnte nicht gespeichert oder aktualisiert werden: " + e.getMessage());
			}
		}
	}

	// Kunde bearbeiten
	@FXML
	private void handleEditCustomer()
	{
		ArchivierteKunden selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
		if (selectedCustomer != null)
		{
			// Felder für den Bearbeitungsdialog erstellen
			TextField nameField = new TextField(selectedCustomer.getName());
			TextField strasseField = new TextField(selectedCustomer.getStrasse());
			TextField plzField = new TextField(selectedCustomer.getPlz());
			TextField ortField = new TextField(selectedCustomer.getOrt());

			// Layout für den Dialog
			VBox dialogPane = new VBox(10, new Label("Name:"), nameField,
					new Label("Straße:"), strasseField,
					new Label("PLZ:"), plzField,
					new Label("Ort:"), ortField);

			// Dialog initialisieren
			Alert editDialog = new Alert(Alert.AlertType.NONE, "", ButtonType.OK, ButtonType.CANCEL);
			editDialog.setTitle("Kunde bearbeiten");
			editDialog.setHeaderText("Ändern Sie die Kundendaten");
			editDialog.getDialogPane().setContent(dialogPane);

			// Zeige den Dialog
			editDialog.showAndWait().ifPresent(response ->
			{
				if (response == ButtonType.OK)
				{
					// Aktualisierte Werte setzen
					selectedCustomer.setName(nameField.getText());
					selectedCustomer.setStrasse(strasseField.getText());
					selectedCustomer.setPlz(plzField.getText());
					selectedCustomer.setOrt(ortField.getText());

					try
					{
						// Kundeninfos aktualisieren
						haushaltDAO.updateCustomer(selectedCustomer);
						showAlert(Alert.AlertType.INFORMATION, "Erfolg", "Kunde aktualisiert", "Die Kundendaten wurden erfolgreich aktualisiert.");
						// Hier keine zusätzliche Aktualisierung der Tabelle mehr nötig
					} catch (SQLException e)
					{
						showAlert(Alert.AlertType.ERROR, "Fehler", "Datenbankfehler", "Die Kundendaten konnten nicht aktualisiert werden: " + e.getMessage());
					}
				}
			});
		} else
		{
			showAlert(Alert.AlertType.WARNING, "Fehler", "Kein Kunde ausgewählt", "Bitte wählen Sie einen Kunden aus, um ihn zu bearbeiten.");
		}
	}

	@SuppressWarnings("unused")
  private int getPlzId(String plzText) throws SQLException
	{
		int plzId = haushaltDAO.getPlzIdByDescription(plzText); // DAO-Methode, die die ID anhand der Beschreibung (PLZ) holt
		if (plzId == -1)
		{
			showAlert(Alert.AlertType.ERROR, "Ungültige PLZ", "PLZ nicht gefunden", "Die eingegebene PLZ existiert nicht in der Datenbank.");
			throw new SQLException("PLZ nicht gefunden");
		}
		return plzId;
	}

	// Kunde löschen
	@FXML
	private void handleDeleteCustomer()
	{
		ArchivierteKunden selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
		if (selectedCustomer != null)
		{
			try
			{
				haushaltDAO.resetCustomerStatus(selectedCustomer); // Kunde löschen
				showAlert(Alert.AlertType.INFORMATION, "Erfolg", "Kunde gelöscht", "Der Kunde wurde erfolgreich gelöscht.");
				handleCustomerTypeChange(); // Tabelle aktualisieren
			} catch (SQLException e)
			{
				showAlert(Alert.AlertType.ERROR, "Fehler", "Datenbankfehler", "Der Kunde konnte nicht gelöscht werden: " + e.getMessage());
			}
		} else
		{
			showAlert(Alert.AlertType.WARNING, "Fehler", "Kein Kunde ausgewählt", "Bitte wählen Sie einen Kunden aus, um ihn zu löschen.");
		}
	}

	// Hilfsmethode zur Anzeige von Alerts
	private void showAlert(Alert.AlertType type, String title, String header, String content)
	{
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	// Statistiken anzeigen
	@FXML
	private void handleViewStatistics()
	{
		// Implementierung der Statistik anzeigen
	}

	// Anwendung beenden
	@FXML
	private void handleExit()
	{
		Stage stage = (Stage) customerTable.getScene().getWindow();
		stage.setOnHidden(event -> handleCustomerTypeChange()); // Tabelle beim Schließen aktualisieren
		stage.close();
	}

}
