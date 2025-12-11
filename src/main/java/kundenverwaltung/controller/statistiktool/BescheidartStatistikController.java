package kundenverwaltung.controller.statistiktool;

import kundenverwaltung.dao.BescheidDAO;
import kundenverwaltung.dao.BescheidDAOimpl;
import kundenverwaltung.dao.VerteilstelleDAO;
import kundenverwaltung.dao.VerteilstelleDAOimpl;
import kundenverwaltung.dao.FamilienmitgliedDAO;
import kundenverwaltung.dao.FamilienmitgliedDAOimpl;
import kundenverwaltung.model.Bescheid;
import kundenverwaltung.model.Verteilstelle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class BescheidartStatistikController
{

    @FXML
    private ComboBox<String> verteilstelleComboBox; // ComboBox für Verteilstellen
    @FXML
    private ComboBox<String> statusComboBox; // ComboBox für Gültig/Nicht gültig
    @FXML
    private TableView<Bescheid> bescheidartenTable; // Tabelle für Bescheide
    @FXML
    private TableColumn<Bescheid, String> columnBescheidart; // Spalte für Bescheidart
    @FXML
    private TableColumn<Bescheid, Integer> columnAnzahl; // Spalte für Anzahl Personen

    private VerteilstelleDAO verteilstelleDAO;
    private BescheidDAO bescheidDAO; // Hinzufügen des BescheidDAO
    @SuppressWarnings("unused")
    private FamilienmitgliedDAO familienmitgliedDAO;
    /**
     */
    public void initialize()
    {
        verteilstelleDAO = new VerteilstelleDAOimpl();
        bescheidDAO = new BescheidDAOimpl();
        familienmitgliedDAO = new FamilienmitgliedDAOimpl();

        loadVerteilstellen();

        // Beispielhafte Status Optionen
        statusComboBox.getItems().addAll("Gültig", "Nicht gültig");

        columnBescheidart.setCellValueFactory(cellData ->
        {
            Bescheid bescheid = cellData.getValue();
            return new SimpleStringProperty(
                bescheid.getBescheidart() != null ? bescheid.getBescheidart().getName() : "Unbekannt"
            );
        });

        columnAnzahl.setCellValueFactory(new PropertyValueFactory<>("anzahlPersonen")); // Bindung für Anzahl Personen
    }

    @FXML
    private void loadBescheidarten()
    {
        try
        {
            String selectedVerteilstelleName = verteilstelleComboBox.getValue();
            String selectedStatus = statusComboBox.getValue();

            if (selectedVerteilstelleName == null || selectedStatus == null)
            {
                showAlert(AlertType.WARNING, "Warnung", "Bitte wählen Sie eine Verteilstelle und einen Status aus.");
                return;
            }

            Verteilstelle verteilstelle = verteilstelleDAO.readByName(selectedVerteilstelleName);

            if (verteilstelle == null)
            {
                showAlert(AlertType.ERROR, "Fehler", "Keine Verteilstelle gefunden.");
                return;
            }

            List<Bescheid> bescheide = bescheidDAO.readBescheid(verteilstelle, selectedStatus);

            for (Bescheid bescheid : bescheide)
            {
                String bescheidartName = bescheid.getBescheidart() != null ? bescheid.getBescheidart().getName() : "Unbekannt";
                int anzahlPersonen = bescheid.getAnzahlPersonen();

                // Speichern der Bescheidstatistik
                bescheidDAO.saveBescheidStatistik(bescheidartName, anzahlPersonen);
            }

            if (bescheide.isEmpty())
            {
                showAlert(AlertType.ERROR, "Fehler", "Keine Bescheide gefunden.");
            } else
            {
                bescheidartenTable.getItems().clear(); // Vorherige Einträge löschen
                bescheidartenTable.getItems().addAll(bescheide); // Neue Einträge hinzufügen
                System.out.println("Bescheide wurden zur Tabelle hinzugefügt.");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Fehler", "Ein unerwarteter Fehler ist aufgetreten: " + e.getMessage());
        }
    }

    private void loadVerteilstellen()
    {
        List<Verteilstelle> verteilstellen = verteilstelleDAO.readAll();
        verteilstelleComboBox.getItems().clear();
        if (verteilstellen == null || verteilstellen.isEmpty())
        {
            System.out.println("Keine Verteilstellen gefunden.");
        } else
        {
            for (Verteilstelle verteilstelle : verteilstellen)
            {
                String name = verteilstelle.getName();
                // Falls der Name null ist, einen Standardwert verwenden
                if (name == null)
                {
                    name = "Unbekannt";
                }
                System.out.println("Lade in ComboBox: [" + name + "] mit Länge: " + name.length());
                verteilstelleComboBox.getItems().add(name.trim());
            }
        }
    }

    @FXML
    private void handleClose()
    {
        // Schließt das Fenster
        ((Stage) bescheidartenTable.getScene().getWindow()).close();
    }

    private void showAlert(AlertType alertType, String title, String message)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
