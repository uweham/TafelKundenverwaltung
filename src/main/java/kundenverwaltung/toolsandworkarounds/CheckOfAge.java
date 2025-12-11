package kundenverwaltung.toolsandworkarounds;

import java.time.LocalDate;
import java.time.Period;

/**
 * This class checks the age of a person.
 */
public class CheckOfAge
{
	private static final int OF_AGE = 18;

	/**
	 * This function checks if a person is of legal age.
	 *
	 * @param date
	 * @return
	 */
	public Boolean isAdult(LocalDate date)
	{
		Period period = Period.between(date, LocalDate.now());
		int age = period.getYears();

		return age >= OF_AGE;
	}
}
