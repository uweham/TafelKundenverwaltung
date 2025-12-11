package kundenverwaltung.controller;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.EinkaufDAOimpl;
import kundenverwaltung.dao.FamilienmitgliedDAOimpl;
import kundenverwaltung.dao.HaushaltDAOimpl;
import kundenverwaltung.model.Einkauf;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;

/**
 * This class delete person and save the reason.
 *
 * @Author Richard Kromm
 * @Date 30.07.2018
 * @version 1.0
 */
public class DeletePersonController
{
    private static final int MAX_LENGTH_DELETE_REASON_INPUT = 255;
    private static final String BIRTHDAY_FORMAT = "dd.MM.yyyy";
    private static final String NOTIFICATION_TITLE_DEFAULT = "Achtung!";
    private static final String NOTIFICATION_TEXT_REASON_DELETE_TOO_SHORT = "Bitte geben Sie einen Grund für das Löschen ein.";
    private static final String NOTIFICATION_TEXT_REASON_DELETE_TOO_LONG = "Die Begründung ist zu lang. Erlaubte Länge: max. " + MAX_LENGTH_DELETE_REASON_INPUT;
    private static final String NOTIFICATION_TITLE_DELETE_SUCCESSFUL = "Löschen erfolgreich.";
    private static final String NOTIFICATION_TEXT_DELETE_SUCCESSFUL = "Die Person wurde erfolgreich gelöscht.";
    private static final String NOTIFICATION_TITLE_DELETE_NOT_SUCCESSFUL = "Person konnte nicht gelöscht werden.";
    private static final String NOTIFICATION_TEXT_DELETE_NOT_SUCCESSFUL = "Bitte verbinden Sie die Datenbank neu und probieren Sie es erneut.";
    private static final String NOTIFICATION_TITLE_DELETE_NO_PERSON_SELECTED = "Keine Person ausgewählt.";
    private static final String NOTIFICATION_TEXT_DELETE_NO_PERSON_SELECTED = "Bitte wählen Sie eine Person aus, die gelöscht werden soll.";

    private ChangeFontSize changeFontSize = new ChangeFontSize();
    private Familienmitglied memberOfTheFamily;

    @FXML
    private TextArea textAreaReasonDeletePerson;

    @FXML
    private Button btnDeletePerson;

    @FXML
    private Button btnCancelDeletePerson;

    @FXML
    private Label labelHeading;

    @FXML
    private Label labelNameInput;

    @FXML
    private Label labelBirthdayInput;

    @FXML
    private Label labelName;

    @FXML
    private Label labelBirthday;

    @FXML
    private Label labelDeletedReason;

    /**
     * Moves a family member from the database table "familienmitglied" to "deleted_MemberOfTheFamily" and adds a reason.
     */
    @FXML
    public void btnDeletePerson()
    {
        String reasonDeletePerson = textAreaReasonDeletePerson.getText();

        Haushalt household = new HaushaltDAOimpl().read(memberOfTheFamily.getKundennummer());
        EinkaufDAOimpl shopping = new EinkaufDAOimpl();
        int householdDirectorId = new FamilienmitgliedDAOimpl().getHousholdDirector(household.getKundennummer());
        ArrayList<Einkauf> allShopping = shopping.getAllEinkauefe(household);

        if (reasonDeletePerson.length() <= MAX_LENGTH_DELETE_REASON_INPUT)
        {
            if (!reasonDeletePerson.trim().isEmpty())
            {
                if (memberOfTheFamily != null)
                {
                    for (Einkauf element : allShopping)
                    {
                        if (element.getPerson().getPersonId() == memberOfTheFamily.getPersonId())
                        {
                            shopping.updateOnDeletePerson(householdDirectorId, element.getEinkaufId());
                        }
                    }

                    if (new FamilienmitgliedDAOimpl().insertDeletedMemberOfTheFamiliy(memberOfTheFamily, reasonDeletePerson))
                    {
                        Benachrichtigung.infoBenachrichtigung(NOTIFICATION_TITLE_DELETE_SUCCESSFUL, NOTIFICATION_TEXT_DELETE_SUCCESSFUL);
                    } else
                    {
                        Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITLE_DELETE_NOT_SUCCESSFUL, NOTIFICATION_TEXT_DELETE_NOT_SUCCESSFUL);
                    }

                } else
                {
                    Benachrichtigung.infoBenachrichtigung(NOTIFICATION_TITLE_DELETE_NO_PERSON_SELECTED, NOTIFICATION_TEXT_DELETE_NO_PERSON_SELECTED);
                }
            } else
            {
                Benachrichtigung.infoBenachrichtigung(NOTIFICATION_TITLE_DEFAULT, NOTIFICATION_TEXT_REASON_DELETE_TOO_SHORT);
            }
        } else
        {
            Benachrichtigung.infoBenachrichtigung(NOTIFICATION_TITLE_DEFAULT, NOTIFICATION_TEXT_REASON_DELETE_TOO_LONG);
        }

        btnCancelDeletePerson();
    }

    /**
     * Closes the window.
     */
    @FXML
    public void btnCancelDeletePerson()
    {
        Stage stage = (Stage) textAreaReasonDeletePerson.getScene().getWindow();
        stage.close();
    }

    /**
     * Shows person information in labels.
     *
     * @param memberOfTheFamily the family member to display
     */
    public void setPersonData(Familienmitglied memberOfTheFamily)
    {
        Format formatter = new SimpleDateFormat(BIRTHDAY_FORMAT);
        String birthday = formatter.format(memberOfTheFamily.getGeburtsdatum());

        labelNameInput.setText(memberOfTheFamily.getvName() + " " + memberOfTheFamily.getnName());
        labelBirthdayInput.setText(birthday);
    }

    /**
     * Sets the correct font size.
     * @param fontSize the new font size
     */
    public void setFontSize(Double fontSize)
    {
        DoubleProperty newFontSize = new SimpleDoubleProperty(fontSize);

        ArrayList<Label> labelArrayList = new ArrayList<>(Arrays.asList(
                labelHeading, labelNameInput, labelBirthdayInput, labelName, labelBirthday, labelDeletedReason));
        changeFontSize.changeFontSizeFromLabelArrayList(labelArrayList, newFontSize);

        ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(
                btnCancelDeletePerson, btnDeletePerson));
        changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, newFontSize);

        ArrayList<TextArea> textAreaArrayList = new ArrayList<>(Arrays.asList(textAreaReasonDeletePerson));
        changeFontSize.changeFontSizeFromTextAreaArrayList(textAreaArrayList, newFontSize);
    }

    /**
     * Getter Setter.
     */
    public void setMemberOfTheFamily(Familienmitglied memberOfTheFamily)
    {
        this.memberOfTheFamily = memberOfTheFamily;
    }
    /**
     * Getter Setter.
     */
    public Familienmitglied getMemberOfTheFamily()
    {
        return memberOfTheFamily;
    }
}
