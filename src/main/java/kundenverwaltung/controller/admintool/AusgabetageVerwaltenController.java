package kundenverwaltung.controller.admintool;

import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.AusgabeTagZeitDAO;
import kundenverwaltung.dao.AusgabeTagZeitDAOimpl;
import kundenverwaltung.model.AusgabeTagZeit;

public class AusgabetageVerwaltenController implements Initializable
{
    @FXML private TableView<AusgabeTagZeit> tabelleAusgabetage;
    @FXML private TableColumn<AusgabeTagZeit, String>    spalteAusgabetag;
    @FXML private TableColumn<AusgabeTagZeit, LocalTime> spalteStartzeit;
    @FXML private TableColumn<AusgabeTagZeit, LocalTime> spalteEndzeit;

    @FXML private Button btnAusgabetagentfernen;
    @FXML private Button btnAbbrechen;

    private AusgabeTagZeitDAO ausgabeTagZeitDAO = new AusgabeTagZeitDAOimpl();
    private ObservableList<AusgabeTagZeit> tabelleobsAusgabeTagZeit = FXCollections.observableArrayList();
    /**
    *
    */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        spalteAusgabetag.setCellValueFactory(new PropertyValueFactory<>("ausgabetagName"));
        spalteStartzeit.setCellValueFactory(new PropertyValueFactory<>("startzeit"));
        spalteEndzeit.setCellValueFactory(new PropertyValueFactory<>("endzeit"));
        ausgabegruppeTabelleLaden();
    }
    /**
    *
    */
    @FXML
    void ausgabetagentfernen(ActionEvent event)
    {
        AusgabeTagZeit markierter = tabelleAusgabetage.getSelectionModel().getSelectedItem();
        if (markierter == null) return;

        boolean erfolg = ausgabeTagZeitDAO.delete(markierter);
        if (erfolg)
        {
            Benachrichtigung.infoBenachrichtigung("Löschen erfolgreich", "Der Ausgabetag " + markierter + " wurde gelöscht");
            ausgabegruppeTabelleLaden();
        }
        else
        {
            Benachrichtigung.warnungBenachrichtigung("Löschen nicht erfolgreich",
                    "Der Ausgabetag kann nicht gelöscht werden, da er bereits in Verwendung ist");
        }
    }
    /**
    *
    */
    @FXML
    void abbrechen(ActionEvent event)
    {
        Stage stage = (Stage) tabelleAusgabetage.getScene().getWindow();
        stage.close();
    }

    private void ausgabegruppeTabelleLaden()
    {
        tabelleobsAusgabeTagZeit.setAll(ausgabeTagZeitDAO.readAll());
        tabelleAusgabetage.setItems(tabelleobsAusgabeTagZeit);
    }
}
