package kundenverwaltung.toolsandworkarounds;


import java.time.LocalDate;
import java.util.ArrayList;
import kundenverwaltung.model.User;
import kundenverwaltung.toolsandworkarounds.IndividualExceptions.ConfirmPassword;
import kundenverwaltung.toolsandworkarounds.IndividualExceptions.DuplicateUserName;
import kundenverwaltung.toolsandworkarounds.IndividualExceptions.InvalidPasswordLength;
import kundenverwaltung.toolsandworkarounds.IndividualExceptions.UserInputIsEmpty;
import kundenverwaltung.toolsandworkarounds.IndividualExceptions.UserInputTooLong;

/**
 * This class implemented own Exceptions.
 *
 * @author Richard Kromm
 * @Date 27.09.2018
 * @version 1.0
 */
public class CheckUserInput
{
	private static final int MAXIMUM_TEXTFIELD_LENGHT = 44;
	private static final int MINIMUM_PASSWORD_LENGHT = 6;
	private static final int MAXIMUM_PASSWORD_LENGHT = 20;

	/**
	 * This function throws an Exception, if username, surname, firstname or birthday is empty.
	 *
	 * @param username
	 * @param firstName
	 * @param surname
	 * @param birthday
	 * @return true: fields are not empty; UserInputIsEmpty Exception: fields are empty
	 */
	public boolean addChangeUserIsNotEmpty(String username, String firstName, String surname, LocalDate birthday) throws UserInputIsEmpty
	{
		if (username.isEmpty()
				|| firstName.isEmpty()
				|| surname.isEmpty()
				|| birthday == null)
		{
			throw new UserInputIsEmpty();
		} else
		{
			return true;
		}
	}

	/**
	 * This function checks the maximum allowed length for textfields.
	 *
	 * @param username
	 * @param firstName
	 * @param surname
	 * @return true: check is successful; UserInputTooLong Exception: check is not successful
	 */
	public boolean addChangeUserInputTooLong(String username, String firstName, String surname) throws UserInputTooLong
	{
		if (username.length() > MAXIMUM_TEXTFIELD_LENGHT
				|| firstName.length() > MAXIMUM_TEXTFIELD_LENGHT
				|| surname.length() > MAXIMUM_TEXTFIELD_LENGHT)
		{
			throw new UserInputTooLong();
		} else
		{
			return true;
		}
	}

	/**
	 * This function checks two similar user names.
	 *
	 * @param newUserName
	 * @param existingUsers
	 * @return true: user name is unique; DuplicateUserName Exception user name is multiple
	 */
	public boolean addChangeUserInputDuplicateUserName(String newUserName, ArrayList<User> existingUsers) throws DuplicateUserName
	{
		for (User existingUser : existingUsers)
		{
			if (newUserName.toUpperCase().equals(existingUser.getUserName().toUpperCase()))
			{
				throw new DuplicateUserName();
			}
		}
		return true;
	}

	/**
	 * This function checks the password length.
	 *
	 * @param password
	 * @return true: password length is valid; InvalidPasswordLength Exception: password length is invalid
	 */
	public boolean addChangeUserInputValidPasswordLenght(String password) throws InvalidPasswordLength
	{
		if (password.length() > MAXIMUM_PASSWORD_LENGHT
				|| password.length() < MINIMUM_PASSWORD_LENGHT)
		{
			throw new InvalidPasswordLength();
		} else
		{
			return true;
		}
	}

	/**
	 * This function checks passwords for equality.
	 *
	 * @param password
	 * @param repeatPassword
	 * @return true: passwords equal; ConfirmPassword Exception: passwords unequal
	 */
	public boolean addChangeUserPasswordsEquels(String password, String repeatPassword) throws ConfirmPassword
	{
		if (!password.equals(repeatPassword))
		{
			throw new ConfirmPassword();
		} else
		{
			return true;
		}
	}
	// In kundenverwaltung.toolsandworkarounds.CheckUserInput

    /**
     * Prüft, ob ein String genau 5 Zeichen lang ist.
     * @param plz Der zu prüfende String
     * @return true, wenn genau 5 Zeichen, sonst false
     */
    public static boolean checkPLZLength(String plz) {
        if (plz == null) {
            return false;
        }
        return plz.trim().length() == 5;
    }
}
