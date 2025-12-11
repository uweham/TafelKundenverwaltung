package kundenverwaltung.controller.admintool;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.UserDAOimpl;
import kundenverwaltung.model.User;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;
import kundenverwaltung.toolsandworkarounds.CheckUserInput;
import kundenverwaltung.toolsandworkarounds.PasswordEncoding;
import kundenverwaltung.toolsandworkarounds.IndividualExceptions.ConfirmPassword;
import kundenverwaltung.toolsandworkarounds.IndividualExceptions.DuplicateUserName;
import kundenverwaltung.toolsandworkarounds.IndividualExceptions.InvalidPasswordLength;
import kundenverwaltung.toolsandworkarounds.IndividualExceptions.UserInputIsEmpty;
import kundenverwaltung.toolsandworkarounds.IndividualExceptions.UserInputTooLong;


/**
 * This class add an new user or change an existing user.
 *
 * @Author Richard Kromm
 * @Date 14.09.2018
 * @Version 1.0
 */
public class UserAddChangeController
{
	private static final String HEADING_CHANGE_USER_DATA = "Verändern Sie die Daten des aktuellen Benuzers";
	private static final String NOTIFICATION_TITEL_DEFAULT = "Achtung!";
	private static final String NOTIFICATION_NOT_COMPLETED_ALL_REQUIRED_FIELDS = "Bitte füllen Sie alle Felder aus.";
	private static final String NOTIFICATION_DUPLICATE_USER_NAME = "Der Benutzername wird schon verwendet.";
	private static final String NOTIFICATION_PASSWORDS_UNEQUEL = "Die Passwörter stimmen nicht überein.";
	private static final int MINIMUM_PASSWORD_LENGHT = 6;
	private static final int MAXIMUM_PASSWORD_LENGHT = 20;
	private static final int MAXIMUM_TEXTFIELD_LENGHT = 45;
	private static final String NOTIFICATION_ILLEGAL_LENGHT_PASSWORDS = "Das Passwort ist zu kurz oder zu lang.";
	private static final String NOTIFICATION_ILLEGAL_LENGHT_TEXTFIELDS = "Benutzer-, Vor- oder Nachname ist zu lang."
			+ "\nMaximal erlaubte länge: " + MAXIMUM_TEXTFIELD_LENGHT;


	@FXML
	private Text labelHeader;
	@FXML
    private TextField textFieldUserName;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldSurname;
    @FXML
    private PasswordField passwordFieldPassword;
    @FXML
    private PasswordField passwordFieldRepeatPassword;
	@FXML
	private DatePicker datePickerBirthday;
	@SuppressWarnings("rawtypes")
	@FXML
	private ComboBox comboboxUserRights;
	@FXML
	private CheckBox checkboxDefaultPassword;

	private CheckUserInput checkUserInput = new CheckUserInput();
	private ChangeDateFormat changeDateFormat = new ChangeDateFormat();
	@SuppressWarnings("unused")
	private PasswordEncoding passwordEncoding = new PasswordEncoding();
	private ArrayList<User> userArrayList = new UserDAOimpl().getAllUsers();
	private User user;

	/**
     * Initializes the controller class. Sets up the necessary elements and event handlers.
     */
	public void initialize()
	{
		datePickerBirthday.setConverter(changeDateFormat.convertDatePickerFormat());
		changeDateFormat.checkUserInputDate(datePickerBirthday);

		comboboxUserRights.getSelectionModel().selectFirst();

		checkboxDefaultPassword.setOnAction(event ->
		{
			if (checkboxDefaultPassword.isSelected())
			{
				passwordFieldPassword.setText("");
				passwordFieldPassword.setDisable(true);
				passwordFieldRepeatPassword.setText("");
				passwordFieldRepeatPassword.setDisable(true);
			} else
				{
					passwordFieldPassword.setDisable(false);
					passwordFieldRepeatPassword.setDisable(false);
				}
		});
	}


	/**
	 * This function checks user inputs, inserts a new user or update for an existing user.
	 */
	@FXML
	public void saveUser()
	{
		if (checkInput2(user == null))
		{
			String passwordInput = (checkboxDefaultPassword.isSelected() ? datePickerBirthday.getEditor().getText() : passwordFieldPassword.getText());
			if (user == null)
			{
				new User(textFieldUserName.getText(), textFieldFirstName.getText(), textFieldSurname.getText(),
						datePickerBirthday.getValue(), passwordInput,
						(String) comboboxUserRights.getSelectionModel().getSelectedItem());
			} else
				{
					User changedUser = PasswordEncoding.hashPassword(new User(user.getUserId(), textFieldUserName.getText(), textFieldFirstName.getText(), textFieldSurname.getText(),
							datePickerBirthday.getValue(), passwordInput,
							(String) comboboxUserRights.getSelectionModel().getSelectedItem()));

					new UserDAOimpl().update(changedUser);
				}
			canceledWindow();
		}
	}

	/**
	 * This function closes the window.
	 */
	@FXML
	public void canceledWindow()
	{
		Stage stage = (Stage) textFieldUserName.getScene().getWindow();
		stage.close();
	}


	/**
	 * This function checks user inputs for correctness and shows notification in case of errors.
	 * @param addNewUser
	 * @return true: user input is correct; false: user input is not correct
	 */
	public boolean checkInput(Boolean addNewUser)
	{
		if (!textFieldFirstName.getText().isEmpty()
				&& !textFieldSurname.getText().isEmpty()
				&& datePickerBirthday.getValue() != null
				&& !textFieldUserName.getText().isEmpty())
		{

			if (textFieldFirstName.getText().length() < MAXIMUM_TEXTFIELD_LENGHT
					&& textFieldSurname.getText().length() < MAXIMUM_TEXTFIELD_LENGHT
					&& textFieldUserName.getText().length() < MAXIMUM_TEXTFIELD_LENGHT)
			{
				if (addNewUser)
				{
					for (User element : userArrayList)
					{
						if (textFieldUserName.getText().toUpperCase().equals(element.getUserName().toUpperCase()))
						{
							Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_DUPLICATE_USER_NAME);
							textFieldUserName.requestFocus();
							textFieldUserName.selectRange(0, textFieldUserName.getText().length());
							return false;
						}
					}
				}

				if (!checkboxDefaultPassword.isSelected())
				{
					if (passwordFieldPassword.getText().length() < MINIMUM_PASSWORD_LENGHT
							&& passwordFieldPassword.getText().length() > MAXIMUM_PASSWORD_LENGHT)
					{
						Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_ILLEGAL_LENGHT_PASSWORDS);
						return false;
					}

					if (!passwordFieldPassword.getText().equals(passwordFieldRepeatPassword.getText()))
					{
						Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_PASSWORDS_UNEQUEL);
						passwordFieldPassword.requestFocus();
						passwordFieldPassword.selectRange(0, passwordFieldPassword.getText().length());
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
	}


	/**
	 * This function checks user inputs for correctness and shows notification in case of errors.
	 * @param addNewUser
	 * @return true: user input is correct; false: user input is false
	 */
	public boolean checkInput2(Boolean addNewUser)
	{
		String userName = textFieldUserName.getText();
		String firstName = textFieldFirstName.getText();
		String surname = textFieldSurname.getText();
		LocalDate birthday = datePickerBirthday.getValue();

		try
		{
			checkUserInput.addChangeUserIsNotEmpty(userName, firstName, surname, birthday);
			checkUserInput.addChangeUserInputTooLong(userName, firstName, surname);
			checkUserInput.addChangeUserInputDuplicateUserName(userName, userArrayList);

			if (!checkboxDefaultPassword.isSelected())
			{
				checkUserInput.addChangeUserInputValidPasswordLenght(passwordFieldPassword.getText());
				checkUserInput.addChangeUserPasswordsEquels(passwordFieldPassword.getText(), passwordFieldRepeatPassword.getText());
			}
			return true;
		}
		catch (UserInputIsEmpty isEmpty)
		{
			isEmpty.printStackTrace();
			Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_NOT_COMPLETED_ALL_REQUIRED_FIELDS);
		}
		catch (UserInputTooLong tooLong)
		{
			tooLong.printStackTrace();
			Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_ILLEGAL_LENGHT_TEXTFIELDS);
		}
		catch (DuplicateUserName duplicateUserName)
		{
			duplicateUserName.printStackTrace();
			Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_DUPLICATE_USER_NAME);
			textFieldUserName.requestFocus();
			textFieldUserName.selectRange(0, textFieldUserName.getText().length());
		}
		catch (InvalidPasswordLength invalidPasswordLength)
		{
			invalidPasswordLength.printStackTrace();
			Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_ILLEGAL_LENGHT_PASSWORDS);
		}
		catch (ConfirmPassword confirmPassword)
		{
			confirmPassword.printStackTrace();
			Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_PASSWORDS_UNEQUEL);
			passwordFieldPassword.requestFocus();
			passwordFieldPassword.selectRange(0, passwordFieldPassword.getText().length());
		}
		return false;
	}


	/**
	 * This function sets user information (username, first name, surname, ...) in the gui elements.
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void setUserData(User user)
	{
		labelHeader.setText(HEADING_CHANGE_USER_DATA);
		textFieldFirstName.setText(user.getFirstName());
		textFieldSurname.setText(user.getSurname());
		textFieldUserName.setText(user.getUserName());
		datePickerBirthday.setValue(user.getBirthday());
		comboboxUserRights.getSelectionModel().select(user.getUserRights());
	}


	/**
	 * Gets the user.
	 *
	 * @return the current user
	 */
	public User getUser()
	{
	    return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the user to set
	 */
	public void setUser(User user)
	{
	    this.user = user;
	}
}
