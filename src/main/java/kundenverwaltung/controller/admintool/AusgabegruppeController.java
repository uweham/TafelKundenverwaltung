package kundenverwaltung.controller.admintool;

import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.AusgabeTagZeitDAO;
import kundenverwaltung.dao.AusgabeTagZeitDAOimpl;
import kundenverwaltung.dao.AusgabegruppeDAO;
import kundenverwaltung.dao.AusgabegruppeDAOimpl;
import kundenverwaltung.dao.AusgabegruppeAusgabeTagZeitDAO;
import kundenverwaltung.dao.AusgabegruppeAusgabeTagZeitDAOimpl;
import kundenverwaltung.model.AusgabeTagZeit;
import kundenverwaltung.model.Ausgabegruppe;

public class AusgabegruppeController implements Initializable
{

    // passt zur FXML (ChoiceBox!)
    @FXML private ChoiceBox<Ausgabegruppe> chGruppe;

    @FXML private Button btnUmbenennen;
    @FXML private Button btnNeu;
    @FXML private CheckBox chkAktiv;

    @FXML private CheckBox chkTMo;
    @FXML private CheckBox chkTDi;
    @FXML private CheckBox chkTMi;
    @FXML private CheckBox chkTDo;
    @FXML private CheckBox chkTFr;
    @FXML private CheckBox chkTSa;
    @FXML private CheckBox chkTSo;


    @FXML private ColorPicker colPick;

    @FXML private TableView<AusgabeTagZeit> tabelleAusgabetage;
    @FXML private TableColumn<AusgabeTagZeit, String>    spalteAusgabetag;
    @FXML private TableColumn<AusgabeTagZeit, LocalTime> spalteStartzeit;
    @FXML private TableColumn<AusgabeTagZeit, LocalTime> spalteEndzeit;

    @FXML private Button btnAusgabetagerstellen;
    @FXML private ComboBox<AusgabeTagZeit> comboAusgabetag;
    @FXML private Button btnAusgabetagverknuepfen;

    private final AusgabegruppeDAO ausgabegruppeDAO = new AusgabegruppeDAOimpl();
    private final ObservableList<Ausgabegruppe> obsAusgabegruppe = FXCollections.observableArrayList();

    private final AusgabeTagZeitDAO ausgabeTagZeitDAO = new AusgabeTagZeitDAOimpl();
    private final ObservableList<AusgabeTagZeit> obsAusgabetagZeit = FXCollections.observableArrayList();

    private final AusgabegruppeAusgabeTagZeitDAO ausgabegruppeausgabeTagZeitDAO =
            new AusgabegruppeAusgabeTagZeitDAOimpl();

    private final ObservableList<AusgabeTagZeit> tabelleobsAusgabeTagZeit = FXCollections.observableArrayList();


    private ChangeListener<Ausgabegruppe> gruppeListener;


    private boolean linking = false;
    /**
    *
    */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        ausgabetagZeitLaden();
        ausgabegruppenLaden();

        spalteAusgabetag.setCellValueFactory(new PropertyValueFactory<>("ausgabetagName"));
        spalteStartzeit.setCellValueFactory(new PropertyValueFactory<>("startzeit"));
        spalteEndzeit.setCellValueFactory(new PropertyValueFactory<>("endzeit"));
    }

    private void ausgabetagZeitLaden()
    {
        obsAusgabetagZeit.clear();
        obsAusgabetagZeit.addAll(ausgabeTagZeitDAO.readAll());
        comboAusgabetag.setItems(obsAusgabetagZeit);
        if (!obsAusgabetagZeit.isEmpty())
        {
            comboAusgabetag.getSelectionModel().selectFirst();
        }
    }

    private void ausgabegruppenLaden()
    {
        // aktuelle Auswahl merken (falls Helper vorhanden)
        Integer keepId = null;
        try
        {
            keepId = AdmintoolController.getLastSelectedAusgabegruppeId();
        } catch (Throwable ignore)
        { /* Helper evtl. nicht vorhanden */ }

        obsAusgabegruppe.clear();
        obsAusgabegruppe.addAll(ausgabegruppeDAO.readAll());
        chGruppe.setItems(obsAusgabegruppe);

        // Listener nicht mehrfach anhängen
        if (gruppeListener != null)
        {
            chGruppe.valueProperty().removeListener(gruppeListener);
        }
        gruppeListener = new ChangeListener<Ausgabegruppe>()
        {
            @Override
            public void changed(ObservableValue<? extends Ausgabegruppe> observable,
                                Ausgabegruppe oldValue, Ausgabegruppe newValue)
            {
                if (newValue != null)
                {
                    try
                    {
                        AdmintoolController.setLastSelectedAusgabegruppeId(newValue.getAusgabegruppeId());
                    } catch (Throwable ignore)
                    { /* optional */ }
                    ausgabegruppeDatenLaden(newValue);
                }
            }
        };
        chGruppe.valueProperty().addListener(gruppeListener);

        // Auswahl wiederherstellen
        if (keepId != null)
        {
            for (Ausgabegruppe g : obsAusgabegruppe)
            {
                if (g.getAusgabegruppeId() == keepId)
                {
                    chGruppe.getSelectionModel().select(g);
                    break;
                }
            }
        }
        if (chGruppe.getSelectionModel().isEmpty() && !obsAusgabegruppe.isEmpty())
        {
            chGruppe.getSelectionModel().selectFirst();
        }

        if (chGruppe.getValue() != null)
        {
            ausgabegruppeDatenLaden(chGruppe.getValue());
        }
    }

    private void ausgabegruppeDatenLaden(Ausgabegruppe ausgabegruppe)
    {
        chkAktiv.setSelected(ausgabegruppe.isAktiv());
        colPick.setValue(ausgabegruppe.getAnzeigeFarbe());
        ausgabegruppeTabelleLaden();
    }

    private void ausgabegruppeTabelleLaden()
    {
        tabelleobsAusgabeTagZeit.clear();
        Ausgabegruppe g = chGruppe.getValue();
        if (g != null && g.getAusgabeTagZeiten() != null)
        {
            // Anzeige-Deduplikation rein über ID (robust, auch wenn equals/hashCode fehlen)
            Map<Integer, AusgabeTagZeit> unique = new LinkedHashMap<>();
            for (AusgabeTagZeit z : g.getAusgabeTagZeiten())
            {
                if (z != null) unique.putIfAbsent(z.getAusgabezeitId(), z);
            }
            tabelleobsAusgabeTagZeit.addAll(unique.values());
        }
        tabelleAusgabetage.setItems(tabelleobsAusgabeTagZeit);
    }

    /**
    *
    */
    @FXML
    void ausgabetagerstellen(ActionEvent event)
    {
        try
        {
            if (chGruppe.getValue() != null)
            {
                try
                {
                    AdmintoolController.setLastSelectedAusgabegruppeId(chGruppe.getValue().getAusgabegruppeId());
                } catch (Throwable ignore)
                { /* optional */ }
            }
            kundenverwaltung.controller.MainController.getInstance().oeffneAusgabetagZeit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
    *
    */
    @FXML
    void ausgabetagverknuepfen(ActionEvent event)
    {
        if (linking) return;          // Reentrancy-Guard
        linking = true;
        // UI-Doppelklickschutz
        if (btnAusgabetagverknuepfen != null) btnAusgabetagverknuepfen.setDisable(true);
        try
        {
            Ausgabegruppe g = chGruppe.getValue();
            AusgabeTagZeit z = comboAusgabetag.getValue();
            if (g != null && z != null)
            {
                if (ausgabegruppeausgabeTagZeitDAO.ausgabeTagZeitHinzufuegen(g, z))
                {
                    // lokal ergänzen & Tabelle aktualisieren – sicherheitshalber per ID prüfen
                    if (g.getAusgabeTagZeiten() == null)
                    {
                        g.setAusgabeTagZeiten(new ArrayList<>());
                    }
                    boolean exists = false;
                    for (AusgabeTagZeit x : g.getAusgabeTagZeiten())
                    {
                        if (x != null && x.getAusgabezeitId() == z.getAusgabezeitId())
                        { exists = true; break; }
                    }
                    if (!exists)
                    {
                        g.getAusgabeTagZeiten().add(z);
                    }
                    ausgabegruppeTabelleLaden();
                }
            }
        } finally
        {
            if (btnAusgabetagverknuepfen != null) btnAusgabetagverknuepfen.setDisable(false);
            linking = false;
        }
    }
    /**
    *
    */
    @FXML
    void ausgabetageVerwalten(ActionEvent event)
    {
        try
        {
            kundenverwaltung.controller.MainController.getInstance().oeffneAusgabetageVerwalten();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            // nach Rückkehr neu laden
            ausgabetagZeitLaden();
        }
    }
    /**
    *
    */
    @FXML
    void ausgabegruppeSpeichern(ActionEvent event)
    {
        Ausgabegruppe g = chGruppe.getSelectionModel().getSelectedItem();
        if (g == null) return;

        g.setAktiv(chkAktiv.isSelected());
        g.setAnzeigeFarbe(colPick.getValue());
        if (new AusgabegruppeDAOimpl().update(g))
        {
            Benachrichtigung.infoBenachrichtigung(
                    "Speichern erfolgreich",
                    "Die Ausgabegruppe \"" + g.getName() + "\" wurde aktualisiert."
            );
        }
    }
    /**
    *
    */
    @FXML
    void ausgabegruppeUmbenennen(ActionEvent event)
    {
        Ausgabegruppe g = chGruppe.getSelectionModel().getSelectedItem();
        if (g == null) return;

        TextInputDialog dlg = new TextInputDialog(g.getName());
        dlg.setTitle("Ausgabegruppe umbenennen");
        dlg.setHeaderText("Neuen Namen eingeben:");
        Optional<String> res = dlg.showAndWait();
        if (!res.isPresent()) return;

        String name = res.get().trim();
        if (name.isEmpty()) return;

        g.setName(name);
        if (new AusgabegruppeDAOimpl().update(g))
        {
            // ChoiceBox besitzt kein refresh(); Element im ObservableList ersetzen
            int idx = chGruppe.getSelectionModel().getSelectedIndex();
            obsAusgabegruppe.set(idx, g);
            chGruppe.getSelectionModel().select(idx);
            Benachrichtigung.infoBenachrichtigung("Umbenannt",
                    "Die Ausgabegruppe heißt nun \"" + name + "\".");
        }
    }
    /**
    *
    */
    @FXML
    void neueAusgabegrupperstellen(ActionEvent event)
    {
        TextInputDialog dlg = new TextInputDialog("Neue Gruppe");
        dlg.setTitle("Ausgabegruppe erstellen");
        dlg.setHeaderText("Name der neuen Ausgabegruppe:");
        Optional<String> res = dlg.showAndWait();
        if (!res.isPresent()) return;

        String name = res.get().trim();
        if (name.isEmpty()) name = "Neue Gruppe";

        Ausgabegruppe neu = new Ausgabegruppe(
                0,               // ID wird im DAO gesetzt
                name,
                true,
                Color.WHITE,
                new ArrayList<>()
        );
        if (new AusgabegruppeDAOimpl().create(neu))
        {
            // Liste neu ziehen und neue Gruppe selektieren
            ausgabegruppenLaden();
            for (Ausgabegruppe g : obsAusgabegruppe)
            {
                if (g.getAusgabegruppeId() == neu.getAusgabegruppeId())
                {
                    chGruppe.getSelectionModel().select(g);
                    break;
                }
            }
            Benachrichtigung.infoBenachrichtigung("Angelegt",
                    "Ausgabegruppe \"" + name + "\" wurde erstellt.");
        }
    }

    /**
    *
    */
    public ChoiceBox<Ausgabegruppe> getChGruppe()
    { return chGruppe; }
    /**
    *
    */
    public ComboBox<AusgabeTagZeit> getComboAusgabetag()
    { return comboAusgabetag; }
}
