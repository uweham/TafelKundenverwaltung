package kundenverwaltung.controller.admintool;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.BescheidartDAOimpl;
import kundenverwaltung.model.Bescheidart;

/**
 * The class shows all assessments in two different listviews (enabled listview and disabled listview). The admin can
 * add, delete, enable or disable assessments.
 *
 * @Author Philipp Wilm
 * @Date 20.08.2018
 * @Version 1.0
 */
public class BescheidartenController implements Initializable
{
    private static final String DEFAULT_TITEL_TEXT = "Achtung!";
    private static final String ASSESSMENT_SELECTION_WARNING = "Sie haben keine Bescheidart ausgewählt.";
    @FXML
    private Button buttonEnabled;

    @FXML
    private Button buttonDisabled;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonAdd;
    @SuppressWarnings("rawtypes")

    @FXML
    private ListView listViewEnabled;

    @SuppressWarnings("rawtypes")
    @FXML
    private ListView listViewDisabled;
    @FXML
    private TextField textFieldAssessmentName;

    private BescheidartDAOimpl bescheidartDAOimpl = new BescheidartDAOimpl();
    private ArrayList<Bescheidart> enabledAssessmentsArrayList;
    private ArrayList<Bescheidart> disabledAssessmentsArrayList;
    private ObservableList<Bescheidart> enabledAssessmentsObservableList;
    private ObservableList<Bescheidart> disabledAssessmentsObservableList;

    /**
     * Initializes the controller. This method is automatically called after the FXML file has been loaded.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        enabledAssessmentsArrayList = bescheidartDAOimpl.readAssessments(true);
        disabledAssessmentsArrayList = bescheidartDAOimpl.readAssessments(false);

        enabledAssessmentsObservableList = FXCollections.observableList(enabledAssessmentsArrayList);
        disabledAssessmentsObservableList = FXCollections.observableList(disabledAssessmentsArrayList);

        listViewEnabled.setItems(enabledAssessmentsObservableList);
        listViewDisabled.setItems(disabledAssessmentsObservableList);

        textFieldAssessmentName.setOnKeyReleased(event ->
        {
            if (textFieldAssessmentName.getText().length() > 0)
            {
                buttonAdd.setDisable(false);
            } else
                {
                    buttonAdd.setDisable(true);
                }
        });
    }

    /**
     * The function proofs if an enabled or disabled assessment is selected.
     *
     * @param actionEvent
     */
    public void disabledAssessment(ActionEvent actionEvent)
    {
        Boolean disabled;
        Bescheidart tempAssessment;
        Bescheidart assessmentType = (Bescheidart) listViewEnabled.getSelectionModel().getSelectedItem();
        if (assessmentType == null)
        {
            assessmentType = (Bescheidart) listViewDisabled.getSelectionModel().getSelectedItem();
        }

        if (assessmentType != null)
        {
            if (buttonDisabled.equals(actionEvent.getSource()))
            {
                disabled = false;
                tempAssessment = new Bescheidart(assessmentType.getBescheidartId(),
                        assessmentType.getName(), disabled);
                bescheidartDAOimpl.update(tempAssessment);
                refreshListView();
            }

            if (buttonEnabled.equals(actionEvent.getSource()))
            {
                disabled = true;
                tempAssessment = new Bescheidart(assessmentType.getBescheidartId(),
                        assessmentType.getName(), disabled);
                bescheidartDAOimpl.update(tempAssessment);
                refreshListView();
            }
        } else
            {
                Benachrichtigung.infoBenachrichtigung(DEFAULT_TITEL_TEXT, ASSESSMENT_SELECTION_WARNING);
            }
    }

    /**
     * This function updates the table.
     *
     */
    @SuppressWarnings("unchecked")
    public void refreshListView()
    {
        disabledAssessmentsArrayList = bescheidartDAOimpl.readAssessments(false);
        enabledAssessmentsArrayList = bescheidartDAOimpl.readAssessments(true);

        enabledAssessmentsObservableList = FXCollections.observableList(enabledAssessmentsArrayList);
        disabledAssessmentsObservableList = FXCollections.observableList(disabledAssessmentsArrayList);

        listViewEnabled.setItems(enabledAssessmentsObservableList);
        listViewDisabled.setItems(disabledAssessmentsObservableList);
    }

    /**
     * Deletes a selected assessment.
     *
     */
    public void deleteAssessment()
    {
        Bescheidart assessment;
        assessment = (Bescheidart) listViewEnabled.getSelectionModel().getSelectedItem();
        if (assessment == null)
        {
            assessment = (Bescheidart) listViewDisabled.getSelectionModel().getSelectedItem();
        }

        if (assessment != null)
        {
            bescheidartDAOimpl.delete(assessment);
            refreshListView();
        }
    }

    /**
     * Adds an assessment.
     *
     */
    public void addAssesment()
    {
        String assessmentName = textFieldAssessmentName.getText();
        if (assessmentName.length() > 0)
        {
            // KORRIGIERT: Manuelles Speichern über DAO, statt im Model-Konstruktor
            Bescheidart neueBescheidart = new Bescheidart(assessmentName, true);
            
            boolean erfolg = bescheidartDAOimpl.create(neueBescheidart);
            
            if(erfolg) {
                textFieldAssessmentName.setText("");
                buttonAdd.setDisable(true);
                refreshListView();
                Benachrichtigung.infoBenachrichtigung("Erfolg", "Bescheidart wurde hinzugefügt.");
            } else {
                Benachrichtigung.warnungBenachrichtigung("Fehler", "Bescheidart konnte nicht gespeichert werden.");
            }
        }
    }
}