package kundenverwaltung.controller;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.FamilienmitgliedDAOimpl;
import kundenverwaltung.model.Einkauf;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;

/**
 * Controller class for handling bookings in the application.
 */
public class BuchungController
{

    @FXML
    private Text textContinue;
    @FXML
    private ListView<String> lwWarnungen;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private ButtonType yes;
    @FXML
    private ButtonType no;
    @FXML
    private ButtonType cancel;

    // Buchungsdetails
    @FXML
    private Label labelHeader;
    @FXML
    private Label lblBuchungstext;
    @FXML
    private Label lblEinkaufDurch;
    @FXML
    private Label lblEinkaufDurch1erforderlich;
    @FXML
    private Label lblBuchungstext1erforderlich;
    @FXML
    private ComboBox<Familienmitglied> cbEinkaufDurch;
    @FXML
    private TextField txtBuchungstext;
    @FXML
    private Button btnBuchungOK;
    @FXML
    private Button btnBuchungAbbrechen;

    private ChangeFontSize changeFontSize = new ChangeFontSize();

    private ArrayList<Familienmitglied> familienmitglieder;
    private ObservableList<Familienmitglied> familienmitgliederOL = FXCollections.observableArrayList();
    @SuppressWarnings("unused")
    private Haushalt haushalt;
    private Einkauf buchungResults = new Einkauf(0, null, null, null, null, null, null, null, 0, 0, null, 0, 0);
    private Boolean buchungsTextErforderlich = false;
    private Boolean einkkaufDurchErforderlich = false; // zur Überprüfung ob erforderlich
    private Boolean abort=false;
    /**
     * Initializes the controller with the necessary settings.
     *
     * @param buchungsText       indicates if booking text is required (1 if required, 0 otherwise)
     * @param zugeordnetePerson  indicates if a person is required (1 if required, 0 otherwise)
     */
    public void manuellinitialize(int buchungsText, int zugeordnetePerson)
    {
        if (buchungsText == 0)
        {
            txtBuchungstext.setVisible(false);
            lblBuchungstext.setVisible(false);
        }
        if (zugeordnetePerson == 0)
        {
            cbEinkaufDurch.setVisible(false);
            lblEinkaufDurch.setVisible(false);
        }
        if (buchungsText != 1)
        {
            lblBuchungstext1erforderlich.setVisible(false);
        } else
        {
            buchungsTextErforderlich = true;
        }

        if (zugeordnetePerson != 1)
        {
            lblEinkaufDurch1erforderlich.setVisible(false);
        } else
        {
            einkkaufDurchErforderlich = true;
        }
    }

    /**
     * Handles the action when the "OK" button is clicked.
     * Validates input and saves the booking details.
     */
    @FXML
    public void btnBuchungOK()
    {
        buchungResults.setPerson(cbEinkaufDurch.getSelectionModel().getSelectedItem());
        if (einkkaufDurchErforderlich && buchungResults.getPerson() == null)
        {
            Benachrichtigung.warnungBenachrichtigung("Fehlende Eingabe", "Bitte eine Person angeben.");
            return;
        }
        buchungResults.setBuchungstext(txtBuchungstext.getText());
        if (buchungsTextErforderlich && buchungResults.getBuchungstext() == null)
        {
            Benachrichtigung.warnungBenachrichtigung("Fehlende Eingabe", "Bitte einen Buchungstext angeben.");
            return;
        }

        Stage stage = (Stage) btnBuchungOK.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the action when the "Cancel" button is clicked.
     * Closes the booking window without saving.
     */
    @FXML
    public void btnBuchungAbbrechen()
    {
        abort=true;
        
        Stage stage = (Stage) btnBuchungAbbrechen.getScene().getWindow();
        stage.close();
    }

    /**
     * Gets the booking results.
     *
     * @return the booking results
     */
    public Einkauf getBuchungResults()
    {
        if (abort)
        {
          return null;
        }
        return buchungResults;
    }

    /**
     * Sets the household for the booking and initializes related data.
     *
     * @param haushalt the household to set
     */
    public void setHaushalt(Haushalt haushalt)
    {
        this.haushalt = haushalt;
        familienmitglieder = new FamilienmitgliedDAOimpl().getAllFamilienmitglieder(haushalt.getKundennummer());
        familienmitgliederOL.addAll(familienmitglieder);
        cbEinkaufDurch.setItems(familienmitgliederOL);
    }

    /**
     * Sets the alert text with the given warnings.
     *
     * @param warnungen the list of warnings to display
     */
    public void setAlertText(ArrayList<String> warnungen)
    {
        ObservableList<String> warnungenOL = FXCollections.observableArrayList();
        warnungenOL.addAll(warnungen);
        lwWarnungen.setItems(warnungenOL);
    }

    /**
     * Sets the font size for the UI components.
     *
     * @param fontSize the font size to set
     */
    public void setFontSize(Double fontSize)
    {
        DoubleProperty newFontSize = new SimpleDoubleProperty(fontSize);

        ArrayList<Label> labelArrayList = new ArrayList<>(Arrays.asList(labelHeader, lblBuchungstext, lblEinkaufDurch,
                lblEinkaufDurch1erforderlich, lblBuchungstext1erforderlich));
        changeFontSize.changeFontSizeFromLabelArrayList(labelArrayList, newFontSize);

        @SuppressWarnings("rawtypes")
        ArrayList<ComboBox> comboBoxArrayList = new ArrayList<>(Arrays.asList(cbEinkaufDurch));
        changeFontSize.changeFontSizeFromComboBoxArrayList(comboBoxArrayList, newFontSize);

        ArrayList<TextField> textFieldArrayList = new ArrayList<>(Arrays.asList(txtBuchungstext));
        changeFontSize.changeFontSizeFromTextFieldArrayList(textFieldArrayList, newFontSize);

        ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(btnBuchungAbbrechen, btnBuchungOK));
        changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, newFontSize);
    }
}
