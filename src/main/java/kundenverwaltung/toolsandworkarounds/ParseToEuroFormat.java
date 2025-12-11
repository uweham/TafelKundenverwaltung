package kundenverwaltung.toolsandworkarounds;

/**
 * This class parse a float to a String and change in euro format.
 *
 * @Author Richard Kromm
 * @Date 30.08.2018
 * @Version 1.0
 */
public class ParseToEuroFormat
{
	private static final int FIRST_ARRAY_POSITION = 0;
	private static final int SECOND_ARRAY_POSITION = 1;
	private static final int DECIMAL_PLACE = 2;
	private static final String  SEPERATOR_DOT = "\\.";
	private static final char  SEPERATOR_COMMA = ',';
	private static final String EURO_SYMBOL = " €";
	private static final Double ROUND_FACTOR_TWO_DECIMAL = 100.0;
	private static final String PLACEHOLDER_ZERO = "0";

	/**
	 * This function convert float value to string (money format xxx,yy).
	 *
	 * @param value
	 * @param euroSymbol true: add a euro symbol; false: without euro symbol
	 * @return amount of money as string value
	 */
	public String parseFloatToEuroString(Float value, Boolean euroSymbol)
	{
		Double roundValue = Math.round(value * ROUND_FACTOR_TWO_DECIMAL) / ROUND_FACTOR_TWO_DECIMAL;
		String[] arrayValue = String.valueOf(roundValue).split(SEPERATOR_DOT);
		if (arrayValue[SECOND_ARRAY_POSITION].length() < DECIMAL_PLACE)
		{
			arrayValue[SECOND_ARRAY_POSITION] += PLACEHOLDER_ZERO;
		}
		return arrayValue[FIRST_ARRAY_POSITION] + SEPERATOR_COMMA + arrayValue[SECOND_ARRAY_POSITION] + (euroSymbol ? EURO_SYMBOL : "");
	}

	/**
	 * This function convert double value to string (money format xxx,yy).
	 *
	 * @param value
	 * @param euroSymbol true: add a euro symbol; false: without euro symbol
	 * @return amount of money as string value
	 */
	public String parseFloatToEuroString(Double value, Boolean euroSymbol)
	{
		Double roundValue = Math.round(value * ROUND_FACTOR_TWO_DECIMAL) / ROUND_FACTOR_TWO_DECIMAL;
		String[] arrayValue = String.valueOf(roundValue).split(SEPERATOR_DOT);
		if (arrayValue[SECOND_ARRAY_POSITION].length() < DECIMAL_PLACE)
		{
			arrayValue[SECOND_ARRAY_POSITION] += PLACEHOLDER_ZERO;
		}

		return arrayValue[FIRST_ARRAY_POSITION] + SEPERATOR_COMMA + arrayValue[SECOND_ARRAY_POSITION] + (euroSymbol ? EURO_SYMBOL : "");
	}
}
