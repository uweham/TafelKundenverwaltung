package kundenverwaltung.controller.admintool;

import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.controller.MainController;
import kundenverwaltung.dao.WarentypDAO;
import kundenverwaltung.dao.WarentypDAOimpl;
import kundenverwaltung.model.Warentyp;

public class WarentypenController implements Initializable
{

    @FXML
    private Button btnChange;

    @FXML
    private Button btnNew;

    @FXML
    private CheckBox chkActive;

    @FXML
    private CheckBox chkCompute;


    @FXML
    private Text lbl1;

    @FXML
    private Text lbl2;

    @FXML
    private Text lbl3;

    @FXML
    private Text lbl4;

    @FXML
    private CheckBox chkLimitierung;

    @FXML
    private Button btnSave;

    @FXML
    private TextField inpErwachsener;

    @FXML
    private TextField inpKind;

    @FXML
    private TextField inpPauschal;

    @FXML
    private TextField inpDeckel;

    @FXML
    private TextField inpMinimum;

    @FXML
    private ComboBox<Warentyp> cbWTyp;

    @FXML
    private ComboBox<Warentyp> cbJumpTo;

    @FXML
    private TextField inpAnzahl;

    @FXML
    private TextField inpAbstand;
    @FXML
    private ComboBox<?> cbBuchung;

    @FXML
    private ComboBox<?> cbZuordnung;

    @FXML
    private ComboBox<?> cbAnzahl;

    @FXML
    private ComboBox<?> cbAnzahl2;

    private WarentypDAO warentypDAO = new WarentypDAOimpl();
    private ArrayList<Warentyp> warentypArrayList;
    private ObservableList<Warentyp> obsWarentyp = FXCollections.observableArrayList();

    /**
     * This method saves the current warentyp settings.
     *
     * @param event The ActionEvent triggered by the save button.
     */
    @FXML
    void speichern(ActionEvent event)
    {
        try
        {
            Warentyp aktuellerWarentyp = cbWTyp.getSelectionModel().getSelectedItem();
            aktuellerWarentyp.setPreisErwachsene(Float.parseFloat(inpErwachsener.getText()));
            aktuellerWarentyp.setPreisKinder(Float.parseFloat(inpKind.getText()));
            aktuellerWarentyp.setAktiv(!chkActive.isSelected());
            aktuellerWarentyp.setNaechsterWarentypid(cbJumpTo.getValue().getWarentypId());
            aktuellerWarentyp.setZuordnungBuchungstext(cbBuchung.getSelectionModel().getSelectedIndex());
            aktuellerWarentyp.setZuordnungPerson(cbZuordnung.getSelectionModel().getSelectedIndex());

            if (!chkLimitierung.isSelected())
            {
                aktuellerWarentyp.setWarentyplimitanzahl(-1);
                aktuellerWarentyp.setWarentyplimitart(-1);
                aktuellerWarentyp.setWarentyplimitabstand(-1);
                aktuellerWarentyp.setWarentyplimitabstandart(-1);
            }
            else
            {
                aktuellerWarentyp.setWarentyplimitanzahl(Integer.parseInt(inpAnzahl.getText()));
                aktuellerWarentyp.setWarentyplimitart(cbAnzahl2.getSelectionModel().getSelectedIndex());
                aktuellerWarentyp.setWarentyplimitabstand(Integer.parseInt(inpAbstand.getText()));
            }

            aktuellerWarentyp.setManuelleBerechnung(chkCompute.isSelected());
            aktuellerWarentyp.setHaushaltspauschale(Float.parseFloat(inpPauschal.getText()));
            aktuellerWarentyp.setDeckelbetrag(Float.parseFloat(inpDeckel.getText()));
            aktuellerWarentyp.setMinbetrag(Float.parseFloat(inpMinimum.getText()));


            warentypDAO.update(aktuellerWarentyp);
            Benachrichtigung.infoBenachrichtigung("Speichern erfolgreich", "Der Warentyp " + aktuellerWarentyp + " wurde erfolgreich aktualsiert");
        } catch (NumberFormatException e)
        {
            Benachrichtigung.warnungBenachrichtigung("Speichern fehlgeschlagen", "Bitte prüfen Sie die Eingaben. ");
            e.printStackTrace();
        }
    }
    /**
     * This method opens a window to create a new warentyp.
     *
     * @param event The ActionEvent triggered by the new button.
     */
    @FXML
    void warentypErstellen(ActionEvent event)
    {
        MainController.getInstance().oeffneNeuerWarentyp();
    }
    /**
     * This method opens a window to rename the selected warentyp.
     *
     * @param event The ActionEvent triggered by the change button.
     */
    @FXML
    void warentypUmbenennen(ActionEvent event)
    {
        MainController.getInstance().oeffneChangeWarentyp(cbWTyp.getSelectionModel().getSelectedItem());

    }

    /**
     * Initializes the controller class.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @SuppressWarnings("unchecked")
	@Override
    public void initialize(URL location, ResourceBundle resources)
    {
        warentypArrayList = warentypDAO.readAll();
        for (Warentyp element : warentypArrayList)
        {
            obsWarentyp.add(element);
        }
        cbWTyp.setItems(obsWarentyp);
        cbJumpTo.setItems(obsWarentyp);
        cbJumpTo.getSelectionModel().selectFirst();
        cbWTyp.getSelectionModel().selectFirst();

        cbWTyp.valueProperty().addListener(new ChangeListener<Warentyp>()
        {
            @Override
            public void changed(ObservableValue<? extends Warentyp> observable, Warentyp oldValue, Warentyp newValue)
            {

                if (newValue != null)
                {
                    warentypLaden();

                }


            }
        });
        chkLimitierung.selectedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                limitSichtbarkeit();



             //   System.out.println("Test");
            }
        });


        @SuppressWarnings("rawtypes")
		ObservableList obsZurodnung = FXCollections.observableArrayList();
        obsZurodnung.addAll("nicht möglich", "erforderlich ", "optional / möglich");
        cbBuchung.setItems(obsZurodnung);
        cbBuchung.getSelectionModel().selectFirst();
        cbZuordnung.setItems(obsZurodnung);
        cbZuordnung.getSelectionModel().selectFirst();

        @SuppressWarnings("rawtypes")
		ObservableList obsLimitierung = FXCollections.observableArrayList();
        obsLimitierung.addAll("Kalenderwoche(SO-SA)", "Arbeitswoche (Mo-FR) ", "7 Tage", "Monat", "Quartal", "Jahr");
        cbAnzahl2.setItems(obsLimitierung);
        cbAnzahl2.getSelectionModel().selectFirst();


        if (cbWTyp.getValue() != null)
        {
            warentypLaden();
        }

    }

    private void limitSichtbarkeit()
    {
        lbl1.setVisible(chkLimitierung.isSelected());
        lbl2.setVisible(chkLimitierung.isSelected());
        lbl3.setVisible(chkLimitierung.isSelected());
        lbl4.setVisible(chkLimitierung.isSelected());
        cbAnzahl2.setVisible(chkLimitierung.isSelected());
        inpAbstand.setVisible(chkLimitierung.isSelected());
        inpAnzahl.setVisible(chkLimitierung.isSelected());
    }
    /**
     * This method handles the event triggered by toggling the warentyp limit checkbox.
     *
     * @param event The ActionEvent triggered by the checkbox.
     */
    @FXML
    void warentypLimit(ActionEvent event)
    {
        System.out.println(chkLimitierung.isSelected());
    }

    private void warentypLaden()
    {
        Warentyp warentyp = cbWTyp.getValue();
        chkActive.setSelected(!warentyp.isAktiv());
        inpErwachsener.setText(String.valueOf(warentyp.getPreisErwachsene()));
        inpKind.setText(String.valueOf(warentyp.getPreisKinder()));
        inpPauschal.setText(String.valueOf(warentyp.getHaushaltspauschale()));
        inpDeckel.setText(String.valueOf(warentyp.getDeckelbetrag()));
        inpMinimum.setText(String.valueOf(warentyp.getMinbetrag()));
        inpAnzahl.setText(String.valueOf(warentyp.getWarentyplimitanzahl()));
        inpAbstand.setText(String.valueOf(warentyp.getWarentyplimitabstand()));
        int buchungstext = warentyp.getZuordnungBuchungstext();
        int zuordnungperson = warentyp.getZuordnungPerson();
        int warenlimitart = warentyp.getWarentyplimitart();
        int nachesterwarentypid = warentyp.getNaechsterWarentypid();
        Warentyp naechsterWarentyp = null;
        for (Warentyp element : warentypArrayList)
        {
            if (element.getWarentypId() == nachesterwarentypid)
            {
                naechsterWarentyp = element;
                break;
            }
        }
        if (naechsterWarentyp != null)
        {
            cbJumpTo.getSelectionModel().select(naechsterWarentyp);
        }

        cbZuordnung.getSelectionModel().select(zuordnungperson);
        cbAnzahl2.getSelectionModel().select(warenlimitart);
        cbBuchung.getSelectionModel().select(buchungstext);

        if (warentyp.getWarentyplimitanzahl() == -1 || warentyp.getWarentyplimitabstand() == -1)
        {
            chkLimitierung.setSelected(false);
            limitSichtbarkeit();
        }
        else
        {
            chkLimitierung.setSelected(true);
            limitSichtbarkeit();
        }


    }
}
