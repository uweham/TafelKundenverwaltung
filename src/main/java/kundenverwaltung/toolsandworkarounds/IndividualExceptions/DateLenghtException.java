package kundenverwaltung.toolsandworkarounds.IndividualExceptions;

@SuppressWarnings("serial")
public class DateLenghtException extends Exception
{
	private static String wRONGDATELENGHT = "Wrong date format. Correct Date format: \"dd.MM.yyyy\"";

	public DateLenghtException()
	{
		super(wRONGDATELENGHT);
	}
}
