package kundenverwaltung.toolsandworkarounds.IndividualExceptions;

@SuppressWarnings("serial")
public class InvalidPasswordLength extends Exception
{
	private static final String INVALID_PASSWORD_LENGHT = "Invalid password lenght. Min lenght = 6, max lenght = 20.";

	public InvalidPasswordLength()
	{
		super(INVALID_PASSWORD_LENGHT);
	}
}
