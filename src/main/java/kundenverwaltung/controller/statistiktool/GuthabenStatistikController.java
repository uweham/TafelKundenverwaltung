package kundenverwaltung.controller.statistiktool;

import kundenverwaltung.dao.EinkaufDAO;
import kundenverwaltung.dao.EinkaufDAOimpl;
import kundenverwaltung.dao.VerteilstelleDAO;
import kundenverwaltung.dao.VerteilstelleDAOimpl;
import kundenverwaltung.model.Einkauf;
import kundenverwaltung.model.Verteilstelle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import kundenverwaltung.service.TablePreferenceServiceImpl;

import java.util.List;

public class GuthabenStatistikController
{

	@FXML
	private ComboBox<Verteilstelle> verteilstelleComboBox; // ComboBox für Verteilstellen
	@FXML
	private ComboBox<String> typeComboBox; // ComboBox für Typen
	@FXML
	private TableView<Einkauf> resultTableView; // Tabelle für Einkäufe

	@FXML
	private TableColumn<Einkauf, Integer> einkaufIdColumn;
	@FXML
	private TableColumn<Einkauf, String> warentypColumn;
	@FXML
	private TableColumn<Einkauf, String> kundeColumn;
	@FXML
	private TableColumn<Einkauf, Float> summeEinkaufColumn;
	@FXML
	private TableColumn<Einkauf, Float> summeZahlungColumn;
	@FXML
	private TableColumn<Einkauf, Double> saldoColumn;

	@FXML
	private TableColumn<Einkauf, Integer> anzahlKinderColumn;
	@FXML
	private TableColumn<Einkauf, Integer> anzahlErwachseneColumn;

	@FXML
	private MenuItem handleExit;

	private ObservableList<Einkauf> einkaufList = FXCollections.observableArrayList();
	private EinkaufDAO einkaufDAO = new EinkaufDAOimpl();
	private VerteilstelleDAO verteilstelleDAO = new VerteilstelleDAOimpl(); // DAO für Verteilstellen

	@FXML
	private void initialize()
	{
		// Spaltenbindung
		einkaufIdColumn.setCellValueFactory(new PropertyValueFactory<>("einkaufId"));
		einkaufIdColumn.setId("einkaufId");

		warentypColumn.setCellValueFactory(new PropertyValueFactory<>("warentyp"));
		warentypColumn.setId("warentyp");

		kundeColumn.setCellValueFactory(new PropertyValueFactory<>("kundeName"));
		kundeColumn.setId("kundeName");

		summeEinkaufColumn.setCellValueFactory(new PropertyValueFactory<>("summeEinkauf"));
		summeEinkaufColumn.setId("summeEinkauf");

		summeZahlungColumn.setCellValueFactory(new PropertyValueFactory<>("summeZahlung"));
		summeZahlungColumn.setId("summeZahlung");

		anzahlKinderColumn.setCellValueFactory(new PropertyValueFactory<>("anzahlKinder"));
		anzahlKinderColumn.setId("anzahlKinder");

		anzahlErwachseneColumn.setCellValueFactory(new PropertyValueFactory<>("anzahlErwachsene"));
		anzahlErwachseneColumn.setId("anzahlErwachsene");

		saldoColumn.setCellValueFactory(new PropertyValueFactory<>("saldo"));
		saldoColumn.setId("saldo");

		// Null-Werte behandeln
		setupNullValueHandling();

		// Tabelle an ObservableList binden
		resultTableView.setItems(einkaufList);

		// ComboBox initialisieren
		typeComboBox.setItems(FXCollections.observableArrayList("Offene Beträge", "Guthaben"));

		// Lade Verteilstellen in die ComboBox
		loadVerteilstellen();

		TablePreferenceServiceImpl.getInstance().setupPersistence(resultTableView, "GuthabenStatistikResultTableView");
	}


	private void loadVerteilstellen()
	{
		List<Verteilstelle> verteilstellen = verteilstelleDAO.readAll(); // Alle Verteilstellen abrufen
		verteilstelleComboBox.setItems(FXCollections.observableArrayList(verteilstellen)); // In die ComboBox setzen
	}

	private void setupNullValueHandling()
	{
		// CellFactory für saldoColumn
		saldoColumn.setCellFactory(column -> new TableCell<Einkauf, Double>()
		{
			@Override
			protected void updateItem(Double item, boolean empty)
			{
				super.updateItem(item, empty);
				if (empty || item == null)
				{
					setText("");
				} else
				{
					setText(String.format("%.2f", item));
				}
			}
		});

		// CellFactory für summeEinkaufColumn
		summeEinkaufColumn.setCellFactory(column -> new TableCell<Einkauf, Float>()
		{
			@Override
			protected void updateItem(Float item, boolean empty)
			{
				super.updateItem(item, empty);
				if (empty || item == null)
				{
					setText("");
				} else
				{
					setText(String.format("%.2f", item));
				}
			}
		});

		// CellFactory für summeZahlungColumn
		summeZahlungColumn.setCellFactory(column -> new TableCell<Einkauf, Float>()
		{
			@Override
			protected void updateItem(Float item, boolean empty)
			{
				super.updateItem(item, empty);
				if (empty || item == null)
				{
					setText("");
				} else
				{
					setText(String.format("%.2f", item));
				}
			}
		});

		// CellFactory für anzahlKinderColumn
		anzahlKinderColumn.setCellFactory(column -> new TableCell<Einkauf, Integer>()
		{
			@Override
			protected void updateItem(Integer item, boolean empty)
			{
				super.updateItem(item, empty);
				setText(empty || item == null ? "" : item.toString());
			}
		});

		// CellFactory für anzahlErwachseneColumn
		anzahlErwachseneColumn.setCellFactory(column -> new TableCell<Einkauf, Integer>()
		{
			@Override
			protected void updateItem(Integer item, boolean empty)
			{
				super.updateItem(item, empty);
				setText(empty || item == null ? "" : item.toString());
			}
		});
	}

	private void showAlert(String title, String headerText, String contentText)
	{
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}

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

	@FXML
	private void handleExit()
	{
		// Aktuelles Fenster abrufen und schließen
		Stage stage = (Stage) resultTableView.getScene().getWindow(); // Hole das aktuelle Fenster über eine UI-Komponente
		stage.close();
	}
}
