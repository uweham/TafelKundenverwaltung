package kundenverwaltung.toolsandworkarounds.IndividualExceptions;

@SuppressWarnings("serial")
public class ConfirmPassword extends Exception
{
	private static final String CONFIRMATION_PASSWORD_FALSE = "Password and repeat password unequels.";

	public ConfirmPassword()
	{
		super(CONFIRMATION_PASSWORD_FALSE);
	}
}
