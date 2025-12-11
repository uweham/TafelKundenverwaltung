package kundenverwaltung.toolsandworkarounds.IndividualExceptions;

@SuppressWarnings("serial")
public class DateSeparatorException extends Exception
{
	private static final String FALSE_SEPARATOR = "Wrong separator in the date.";

	public DateSeparatorException()
	{
		super(FALSE_SEPARATOR);
	}
}
