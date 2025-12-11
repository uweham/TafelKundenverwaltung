package kundenverwaltung.toolsandworkarounds;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.toolsandworkarounds.IndividualExceptions.DateLenghtException;
import kundenverwaltung.toolsandworkarounds.IndividualExceptions.DateSeparatorException;


/**
 * This class changes the default date format to the German date format.
 *
 * @Author Richard Kromm
 * @Date 18.07.2018
 * @Version 1.0
 *
 */
public class ChangeDateFormat
{
	private static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy";
	private static final String DEFAULT_DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss";	// HH -> 0-23; kk -> 1-24 hours; hh -> 1-12 hours
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

	private static final String SEPARATOR_DATE = ".";
	private static final int LENGTH_ARRAY_DATE_FORMAT = 3;
	private static final int DAY_DATE_FORMAT_LENGTH = 2;
	private static final int DAY_POSITION_IN_ARRAY = 0;
	private static final int MONTH_DATE_FORMAT_LENGTH = 2;
	private static final int MONTH_POSITION_IN_ARRAY = 1;
	private static final int YEAR_DATE_FORMAT_LENGTH = 4;
	private static final int YEAR_POSITION_IN_ARRAY = 2;

	private static String nOTIFICATIONTITELDEFAULT = "Achtung!";
	private static String nOTIFICATIONTEXTINVALIDDATE = "Ungültiges Datum!\n"
																+ "Das Datum existiert nicht!";
	private static String nOTIFICATIONTEXTINVALIDFORMAT = "Ungültiges Format!\n"
																+ "Erlaubtes Format: " + DEFAULT_DATE_FORMAT;
	private static String nOTIFICATIONTEXTINVALIDSEPARATOR = "Ungültiges Trennzeichen!\n"
																+ "Verwenden Sie als Trennzeichen einen Punkt.";


	/**
	 * This function converts date to string (German format "dd.mm.yyyy").
	 *
	 * @param date
	 * @return date as string
	 */
	public String changeDateToDefaultString(Date date)
	{
		return SIMPLE_DATE_FORMAT.format(date);
	}

	/**
	 * This function converts localDate to string (german format "dd.mm.yyyy").
	 *
	 * @param localDate
	 * @return date as string
	 */
	public String changeDateToDefaultString(LocalDate localDate)
	{
		return localDate.format(DATE_FORMATTER);
	}

	/**
	 * This function converta localDateTime to string (German format "dd.mm.yyyy"),.
	 *
	 * @param localDateTime
	 * @return date as string
	 */
	public String changeDateTimeToDefaultString(LocalDateTime localDateTime)
	{
		return localDateTime.format(DATE_TIME_FORMATTER);
	}


	/**
	 * This function changes the default datepicker format to German format.
	 *
	 * @return modified stringconverter
	 */
	public StringConverter<LocalDate> convertDatePickerFormat()
	{
		StringConverter<LocalDate> stringConverter = new StringConverter<LocalDate>()
		{
			@Override
			public String toString(LocalDate date)
			{
				if (date != null)
				{
					return DATE_FORMATTER.format(date);
				}
				else
				{
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string)
			{
				if (string != null && !string.isEmpty())
				{
					return LocalDate.parse(string, DATE_FORMATTER);
				}
				else
				{
					return null;
				}
			}
		};
		return stringConverter;
	}


	/**
	 * This function adds a listener to the datepicker and sets/checks the user input when jumping out of the datepicker.
	 *
	 * @param datePicker
	 */
	public void checkUserInputDate(DatePicker datePicker)
	{
		datePicker.focusedProperty().addListener((observable, oldValue, newValue) ->
		{
			if (oldValue)
			{
				datePicker.setValue(getCorrectLocalDateFromUserInput(datePicker.getEditor().getText()));
			}
		});
	}


	/**
	 * This function checks the user input for correct separator, correct date format and valid date.
	 *
	 * @param dateString
	 * @return LocalDate: by valid input; null: by invalid input
	 */
	public LocalDate getCorrectLocalDateFromUserInput(String dateString)
	{
		try
		{
			String[] dateArray = divideDateAtTheSeparator(dateString);
			checkDateLength(dateArray);
			int day = Integer.parseInt(dateArray[DAY_POSITION_IN_ARRAY]);
			int month = Integer.parseInt(dateArray[MONTH_POSITION_IN_ARRAY]);
			int year = Integer.parseInt(dateArray[YEAR_POSITION_IN_ARRAY]);
			return LocalDate.of(year, month, day);
		}
		catch (DateSeparatorException separatorException)
		{
			Benachrichtigung.warnungBenachrichtigung(nOTIFICATIONTITELDEFAULT, nOTIFICATIONTEXTINVALIDSEPARATOR);
		}
		catch (DateLenghtException lenghtException)
		{
			Benachrichtigung.warnungBenachrichtigung(nOTIFICATIONTITELDEFAULT, nOTIFICATIONTEXTINVALIDFORMAT);
		}
		catch (NumberFormatException formatException)
		{
			Benachrichtigung.warnungBenachrichtigung(nOTIFICATIONTITELDEFAULT, nOTIFICATIONTEXTINVALIDFORMAT);
		}
		catch (DateTimeException dateTimeException)
		{
			Benachrichtigung.warnungBenachrichtigung(nOTIFICATIONTITELDEFAULT, nOTIFICATIONTEXTINVALIDDATE);
		}
		return null;
	}


	/**
	 * This function divides the datestring at the separator and checks the result. An invalid value will throw an exception.
	 *
	 * @param dateString
	 * @return array with a lenght of three; null by DateSeparatorException
	 */
	public String[] divideDateAtTheSeparator(String dateString) throws DateSeparatorException
	{
		String[] resultStringArray = dateString.split(Pattern.quote(SEPARATOR_DATE));
		if (resultStringArray.length != LENGTH_ARRAY_DATE_FORMAT)
		{
			throw new DateSeparatorException();
		} else
		{
			return resultStringArray;
		}
	}

	/**
	 * This function check the correct date format.
	 *
	 * @param dateArray
	 */
	public boolean checkDateLength(String[] dateArray) throws DateLenghtException
	{
		if (dateArray[DAY_POSITION_IN_ARRAY].length() != DAY_DATE_FORMAT_LENGTH
			|| dateArray[MONTH_POSITION_IN_ARRAY].length() != MONTH_DATE_FORMAT_LENGTH
			|| dateArray[YEAR_POSITION_IN_ARRAY].length() != YEAR_DATE_FORMAT_LENGTH)
		{
			throw new DateLenghtException();
		} else
		{
			return true;
		}
	}
}






