package kundenverwaltung.toolsandworkarounds.IndividualExceptions;

@SuppressWarnings("serial")
public class UserInputIsEmpty extends Exception
{
	private static final String INVALID_USER_INPUT = "Invalid user input. Input is empty.";

	public UserInputIsEmpty()
	{
		super(INVALID_USER_INPUT);
	}
}
