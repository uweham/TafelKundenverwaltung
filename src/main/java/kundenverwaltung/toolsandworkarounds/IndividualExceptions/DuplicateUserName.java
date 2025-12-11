package kundenverwaltung.toolsandworkarounds.IndividualExceptions;

@SuppressWarnings("serial")
public class DuplicateUserName extends Exception
{
	private static final String DUPLICATE_USER_NAME = "This user name is used";

	public DuplicateUserName()
	{
		super(DUPLICATE_USER_NAME);
	}
}
