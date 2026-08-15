package kundenverwaltung.controller;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.UserDAO;
import kundenverwaltung.dao.UserDAOimpl;
import kundenverwaltung.model.User;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;
import kundenverwaltung.toolsandworkarounds.PasswordEncoding;


/**
 * This class allows the user to change his data.
 *
 * @author Richard Kromm
 * @Date 15.09.2018
 * @version 1.0
 */
public class BenutzerprofilController
{
    private static final String NOTIFICATION_TITEL_DEFAULT = "Achtung!";
    private static final String NOTIFICATION_NOT_COMPLETED_ALL_REQUIRED_FIELDS = "Bitte füllen Sie alle Felder aus.";
    private static final String NOTIFICATION_DUPLICATE_USER_NAME = "Der Benutzername wird schon verwendet.";
    private static final String NOTIFICATION_PASSWORDS_UNEQUEL = "Die Passwörter stimmen nicht überein.";
    private static final String NOTIFICATION_WRONG_PASSWORDS = "Das eingegebene Passwort ist falsch.";
    private static final int MINIMUM_PASSWORD_LENGHT = 6;
    private static final int MAXIMUM_PASSWORD_LENGHT = 20;
    private static final int MAXIMUM_TEXTFIELD_LENGHT = 45;
    private static final String NOTIFICATION_ILLEGAL_LENGHT_PASSWORDS = "Das Passwort ist zu kurz oder zu lang.";
    private static final String NOTIFICATION_ILLEGAL_LENGHT_TEXTFIELDS = "Benutzer-, Vor- oder Nachname ist zu lang."
            + "\nMaximal erlaubte länge: " + MAXIMUM_TEXTFIELD_LENGHT;
    private static final String UPDATE_USER_DATA_SUCCESSFUL = "Die Benutzerdaten wurden geändert.";
    private static final String UPDATE_PASSWORD_SUCCESSFUL = "Das Passwort wurden geändert.";


    @FXML
    private Label labelUserHeader;
    @FXML
    private Label labelPasswordHeader;
    @FXML
    private Label labelHeader;
    @FXML
    private Label labelUserName;
    @FXML
    private Label labelShowFirstName;
    @FXML
    private Label labelShowSurname;
    @FXML
    private Label labelOldPassword;
    @FXML
    private Label labelNewPassword;
    @FXML
    private Label labelRepeatNewPassword;
    @FXML
    private Label labelWriteUserRight;
    @FXML
    private Label labelUserRightHeader;
    @FXML
    private Label lablePassword;

    @FXML
    private Button buttonSaveUserName;
    @FXML
    private Button buttonSavePassword;


    @FXML
    private TextField txtBenutzername;
    @FXML
    private TextField txtVorname;
    @FXML
    private TextField txtNachname;

    @FXML
    private PasswordField passwordFieldConfirm;
    @FXML
    private PasswordField passwordFieldOldPassword;
    @FXML
    private PasswordField passwordFieldNewPassword;
    @FXML
    private PasswordField passwordFieldRepeatNewPassword;


    private ChangeFontSize changeFontSize = new ChangeFontSize();
    @SuppressWarnings("unused")
	private PasswordEncoding passwordEncoding = new PasswordEncoding();
    private UserDAO userDAO = new UserDAOimpl();
    private User user;


    /**
     * This function change the personal data (username, first name, surname, ...).
     */
    @FXML
    public void updateUserData()
    {
        if (PasswordEncoding.checkPassword(user, passwordFieldConfirm.getText()))
        {
            if (checkInput(true))
            {
                User updatedUser = PasswordEncoding.hashPassword(new User(user.getUserId(), txtBenutzername.getText(),
                        txtVorname.getText(), txtNachname.getText(), user.getBirthday(), passwordFieldConfirm.getText(),
                        user.getUserRights()));

                if (userDAO.update(updatedUser))
                {
                    Benachrichtigung.infoBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, UPDATE_USER_DATA_SUCCESSFUL);
                    user = updatedUser;
                    passwordFieldConfirm.setText("");
                }
            }
        } else
            {
                Benachrichtigung.infoBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_WRONG_PASSWORDS);
                user = userDAO.read(user.getUserId());
                passwordFieldConfirm.setText("");
                passwordFieldConfirm.requestFocus();
            }
    }

    /**
     * This function change the password from user.
     */
    @FXML
    public void upadateUserPassword()
    {
        if (PasswordEncoding.checkPassword(user, passwordFieldOldPassword.getText()))
        {
            if (checkInput(false))
            {
                User updatedUser = PasswordEncoding.hashPassword(new User(user.getUserId(), user.getUserName(),
                        user.getFirstName(), user.getSurname(), user.getBirthday(), passwordFieldNewPassword.getText(),
                        user.getUserRights()));

                if (userDAO.update(updatedUser))
                {
                    Benachrichtigung.infoBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, UPDATE_PASSWORD_SUCCESSFUL);
                    user = updatedUser;
                    passwordFieldOldPassword.setText("");
                    passwordFieldNewPassword.setText("");
                    passwordFieldRepeatNewPassword.setText("");
                }
            }
        } else
        {
            Benachrichtigung.infoBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_WRONG_PASSWORDS);
            passwordFieldOldPassword.setText("");
            passwordFieldOldPassword.requestFocus();
        }
    }

    /**
     * This function set user data (username, first name, ...) in the gui elements.
     * @param user
     */
    public void setUserData(User user)
    {
        this.user = user;
        txtBenutzername.setText(user.getUserName());
        txtVorname.setText(user.getFirstName());
        txtNachname.setText(user.getSurname());
        labelWriteUserRight.setText(user.getUserRights());
    }

    /**
     * This function check user input before updating.
     * @param checkUserData
     * @return true: user input is correct; false: user input is not correct
     */
    public boolean checkInput(Boolean checkUserData)
    {
        if (checkUserData)
        {
            if (!txtBenutzername.getText().isEmpty()
                    && !txtNachname.getText().isEmpty()
                    && !txtVorname.getText().isEmpty())
            {

                if (txtNachname.getText().length() < MAXIMUM_TEXTFIELD_LENGHT
                        && txtVorname.getText().length() < MAXIMUM_TEXTFIELD_LENGHT
                        && txtBenutzername.getText().length() < MAXIMUM_TEXTFIELD_LENGHT)
                {
                    ArrayList<User> userArrayList = userDAO.getAllUsers();
                    for (User element : userArrayList)
                    {
                        if (txtBenutzername.getText().equals(element.getUserName()))
                        {
                            Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_DUPLICATE_USER_NAME);
                            txtBenutzername.requestFocus();
                            txtBenutzername.selectRange(0, txtBenutzername.getText().length());
                            return false;
                        }
                    }
                    return true;
                } else
                {
                    Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_ILLEGAL_LENGHT_TEXTFIELDS);
                    return false;
                }
            } else
            {
                Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_NOT_COMPLETED_ALL_REQUIRED_FIELDS);
                return false;
            }
        } else
            {
                if (passwordFieldNewPassword.getText().length() < MINIMUM_PASSWORD_LENGHT
                        && passwordFieldNewPassword.getText().length() > MAXIMUM_PASSWORD_LENGHT)
                {
                    Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_ILLEGAL_LENGHT_PASSWORDS);
                    return false;
                }
                if (!passwordFieldNewPassword.getText().equals(passwordFieldRepeatNewPassword.getText()))
                {
                    Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_PASSWORDS_UNEQUEL);
                    passwordFieldNewPassword.requestFocus();
                    passwordFieldRepeatNewPassword.selectRange(0, passwordFieldNewPassword.getText().length());
                    return false;
                }

                return true;
            }
    }
/*
 *                         "labelUserHeader" #      
 *                         "labelUserName" #
 *                         "labelShowFirstName" 
 *                         "txtBenutzername" 
 *                         "txtVorname"
 *                         "buttonSaveUserName" 
 *                         "labelPasswordHeader" # 
                           "labelOldPassword" 
                           "labelNewPassword" 
                           "labelRepeatNewPassword"
                           "buttonSavePassword"
                           "labelShowSurname" 
                           "txtNachname" 
                           "labelUserRightHeader" 
                           "labelWriteUserRight"
                           "passwordFieldConfirm" 
                           "lablePassword"
                           "passwordFieldOldPassword"
                           "passwordFieldNewPassword"
                           "passwordFieldRepeatNewPassword" 
                        <Label layoutX="227.0" layoutY="381.0" prefHeight="87.0" prefWidth="198.0" text="Hinweise: &#10; - Verwenden Sie mind. 6 Zeichen&#10; - Verwenden Sie kombination aus&#10;    klein/groß Buchstaben,&#10;    Zahlen und Sonderzeichen" wrapText="true">
                           <font>
                              <Font size="11.0" />
                           </font></Label>
                     </children>
                  </AnchorPane>
      <Label fx:id="labelHeader" #
 */
    /**
     * This function set the correct font size.
     * @param fontSize
     */
    @SuppressWarnings("rawtypes")
	public void setFontSize(Double fontSize)
    {
        DoubleProperty newFontSize = new SimpleDoubleProperty(fontSize);
        DoubleProperty headerFontSize = new SimpleDoubleProperty(fontSize + ChangeFontSize.getDifferenceBetweenDefaultHeaderFontsize());

        ArrayList<Label> headerLabelArrayList = new ArrayList<>(Arrays.asList(
                    labelUserHeader, labelPasswordHeader));
        changeFontSize.changeFontSizeFromLabelArrayList(headerLabelArrayList, headerFontSize);

        ArrayList<Label> labelArrayList = new ArrayList<>(Arrays.asList(
                    labelHeader, labelUserName, labelWriteUserRight, labelUserRightHeader, labelOldPassword,
                        labelNewPassword, labelRepeatNewPassword, labelShowFirstName, labelShowSurname, lablePassword
                ));
        changeFontSize.changeFontSizeFromLabelArrayList(labelArrayList, newFontSize);

        ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(
                    buttonSaveUserName, buttonSavePassword));
        changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, newFontSize);

        ArrayList<TextField> textFieldArrayList = new ArrayList<>(Arrays.asList(
                    txtBenutzername, txtVorname, txtNachname
                ));
        changeFontSize.changeFontSizeFromTextFieldArrayList(textFieldArrayList, newFontSize);

     }



    /**
     * Gets the user.
     * @return The user.
     */
    public User getUser()
    {
        return user;
    }

    /**
     * Sets the user.
     * @param user The user to set.
     */
    public void setUser(User user)
    {
        this.user = user;
    }
}
