package kundenverwaltung.toolsandworkarounds;

/**
 * @Author Richard Kromm & Adam Starobrzanski
 * @Date 12.08.2018
 */

public class ReplaceGermanCharacters
{
    private static final String UPPERCASE_A_UMLAUT = "Ä";
    private static final String LOWERCASE_A_UMLAUT = "ä";
    private static final String UPPERCASE_O_UMLAUT = "Ö";
    private static final String LOWERCASE_O_UMLAUT = "ö";
    private static final String UPPERCASE_U_UMLAUT = "Ü";
    private static final String LOWERCASE_U_UMLAUT = "ü";
    private static final String SZ_CHARACTER = "ß";
    private static final String EURO_CHARACTER = "€";

    private static final String HTML_UPPERCASE_A_UMLAUT = "&Auml;";
    private static final String HTML_LOWERCASE_A_UMLAUT = "&auml;";
    private static final String HTML_UPPERCASE_O_UMLAUT = "&Ouml;";
    private static final String HTML_LOWERCASE_O_UMLAUT = "&ouml;";
    private static final String HTML_UPPERCASE_U_UMLAUT = "&Uuml;";
    private static final String HTML_LOWERCASE_U_UMLAUT = "&uuml;";
    private static final String HTML_SZ_CHARACTER = "&szlig;";
    private static final String HTML_EURO_CHARACTER = "&euro;";



    /**
     * This function replaces german umlauts with string that are readable for html.
     * @param string
     * @return string
     */

    public String replaceGermanUmlauts(String string)
    {
        if (string == null)
        {
            return null;
        }
        String character = null;
        String switchResult = string;

        System.out.println("INPUT:" + string);

        for (int runVar = 0; runVar < string.length(); runVar++)
        {
            character = String.valueOf(string.charAt(runVar));

            System.out.println("Char: " + character);

            switch (character)
            {
                case UPPERCASE_A_UMLAUT:
                    switchResult = switchResult.replace(UPPERCASE_A_UMLAUT, HTML_UPPERCASE_A_UMLAUT);
                    break;
                case LOWERCASE_A_UMLAUT:
                    switchResult = switchResult.replace(LOWERCASE_A_UMLAUT, HTML_LOWERCASE_A_UMLAUT);
                    break;
                case UPPERCASE_O_UMLAUT:
                    switchResult = switchResult.replace(UPPERCASE_O_UMLAUT, HTML_UPPERCASE_O_UMLAUT);
                    break;
                case LOWERCASE_O_UMLAUT:
                    switchResult = switchResult.replace(LOWERCASE_O_UMLAUT, HTML_LOWERCASE_O_UMLAUT);
                    break;
                case UPPERCASE_U_UMLAUT:
                    switchResult = switchResult.replace(UPPERCASE_U_UMLAUT, HTML_UPPERCASE_U_UMLAUT);
                    break;
                case LOWERCASE_U_UMLAUT:
                    switchResult = switchResult.replace(LOWERCASE_U_UMLAUT, HTML_LOWERCASE_U_UMLAUT);
                    break;
                case SZ_CHARACTER:
                    switchResult = switchResult.replace(SZ_CHARACTER, HTML_SZ_CHARACTER);
                    break;
                case EURO_CHARACTER:
                    switchResult = switchResult.replace(EURO_CHARACTER, HTML_EURO_CHARACTER);
                    break;
                default:
                    break;
            }
        }
        return switchResult;
    }
}
