package kundenverwaltung.controller.server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import kundenverwaltung.server.dto.TrackingTafelDTO;
import kundenverwaltung.server.service.TrackingTafelService;
import kundenverwaltung.service.TablePreferenceServiceImpl;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TrackingListController {

    @FXML
    private TextField suchFeld;

    @FXML
    private TableView<TrackingTafelDTO> trackingTabelle;

    @FXML
    private TableColumn<TrackingTafelDTO, String> uuidSpalte;

    @FXML
    private TableColumn<TrackingTafelDTO, String> versionSpalte;

    @FXML
    private TableColumn<TrackingTafelDTO, String> letzteAktivitaetSpalte;

    @FXML
    private TableColumn<TrackingTafelDTO, String> erstelltAmSpalte;

    @FXML
    private TableColumn<TrackingTafelDTO, String> tafelNameSpalte;

    @FXML
    private TableColumn<TrackingTafelDTO, String> tafelLocationSpalte; // <- korrekt benannt wie in der FXML

    private final ObservableList<TrackingTafelDTO> trackingListe = FXCollections.observableArrayList();

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final TrackingTafelService TRACKING_TAFEL_SERVICE = new TrackingTafelService();

    /**
     * Initializes the tracking list controller and sets up the table view.
     */
    @FXML
    public void initialize() {

        uuidSpalte.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getUuid() != null
                                ?
                                cellData.getValue().getUuid().toString() : "")
        );
        uuidSpalte.setId("uuid");

        versionSpalte.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getProgVersion())
        );
        versionSpalte.setId("version");

        letzteAktivitaetSpalte.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getChangedOn() != null
                                ?
                                cellData.getValue().getChangedOn().format(dateTimeFormatter) : "")
        );
        letzteAktivitaetSpalte.setId("letzteAktivitaet");

        erstelltAmSpalte.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCreatedAt() != null
                                ?
                                cellData.getValue().getCreatedAt().format(dateTimeFormatter) : "")
        );
        erstelltAmSpalte.setId("erstelltAm");

        tafelNameSpalte.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getTafelName() != null ?
                                cellData.getValue().getTafelName() : "")
        );
        tafelNameSpalte.setId("tafelName");

        tafelLocationSpalte.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getTafelLocation() != null ?
                                cellData.getValue().getTafelLocation() : "")
        );
        tafelLocationSpalte.setId("TafelLocation");

        FilteredList<TrackingTafelDTO> gefiltert = new FilteredList<>(trackingListe, p -> true);

        suchFeld.textProperty().addListener((obs, alt, neu) -> {
            gefiltert.setPredicate(tracking -> {
                if (neu == null || neu.isBlank()) {
                    return true;
                }

                String lower = neu.toLowerCase();

                return (tracking.getUuid() != null && tracking.getUuid().toString().toLowerCase().contains(lower))
                        ||
                        (tracking.getProgVersion() != null && tracking.getProgVersion().toLowerCase().contains(lower))
                        ||
                        (tracking.getChangedOn() != null && tracking.getChangedOn().format(dateTimeFormatter).toLowerCase().contains(lower))
                        ||
                        (tracking.getCreatedAt() != null && tracking.getCreatedAt().format(dateTimeFormatter).toLowerCase().contains(lower)) ||
                        (tracking.getTafelName() != null && tracking.getTafelName().toLowerCase().contains(lower)) ||
                        (tracking.getTafelLocation() != null && tracking.getTafelLocation().toLowerCase().contains(lower));
            });
        });

        SortedList<TrackingTafelDTO> sortiert = new SortedList<>(gefiltert);
        sortiert.comparatorProperty().bind(trackingTabelle.comparatorProperty());
        trackingTabelle.setItems(sortiert);

        TablePreferenceServiceImpl.getInstance().setupPersistence(trackingTabelle, "TrackingListEntries", true);

        ladeDaten();
    }

    /**
     * Loads tracking data from the server.
     */
    private void ladeDaten() {
        List<TrackingTafelDTO> trackingDTOs = TRACKING_TAFEL_SERVICE.getAll(TrackingTafelDTO.class);
        trackingListe.setAll(trackingDTOs);
    }

    /**
     * Handles the refresh action to reload tracking data.
     */
    @FXML
    private void handleAktualisieren()
    {
        ladeDaten();
    }

    /**
     * Handles closing the tracking list window.
     */
    @FXML
    private void handleSchliessen() {
        Stage stage = (Stage) trackingTabelle.getScene().getWindow();
        stage.close();
    }
}
