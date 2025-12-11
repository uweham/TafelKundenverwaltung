package kundenverwaltung.toolsandworkarounds.IndividualExceptions;


@SuppressWarnings("serial")
public class UserInputTooLong extends Exception
{
	private static final String USER_INPUT_TOO_LONG = "Invalid user input. Input too long.";

	public UserInputTooLong()
	{
		super(USER_INPUT_TOO_LONG);
	}
}
