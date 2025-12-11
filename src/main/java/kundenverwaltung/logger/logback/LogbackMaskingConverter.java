package kundenverwaltung.logger.logback;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogbackMaskingConverter extends MessageConverter
{
    private static final Pattern SENSITIVE_INPUT_PATTERN = Pattern.compile(
            "(Eingabe: |Auswahl: |Datum: )(.+?)(?=[),]|$)"
    );

    private static final Pattern BRACKET_INPUT_PATTERN = Pattern.compile(
            "\\[([^,\\]]*?),\\s*([^,\\]]*?),\\s*([^\\]]*?)\\]"
    );

    /**
     * Masks sensitive user input within a log message.
     * This method finds patterns that indicate user input and replaces the actual data with asterisks.
     *
     * @param event The logging event containing the message to be converted.
     * @return The log message with sensitive information masked.
     */
    @Override
    public String convert(ILoggingEvent event)
    {
        String message = event.getFormattedMessage();

        Matcher matcher = SENSITIVE_INPUT_PATTERN.matcher(message);
        StringBuilder masked = new StringBuilder();

        while (matcher.find())
        {
            String prefix = matcher.group(1);
            String input = matcher.group(2);
            matcher.appendReplacement(masked, Matcher.quoteReplacement(prefix + "*".repeat(input.length())));
        }

        matcher.appendTail(masked);
        String result = masked.toString();

        Matcher bracketMatcher = BRACKET_INPUT_PATTERN.matcher(result);
        StringBuilder bracketMasked = new StringBuilder();

        while (bracketMatcher.find())
        {
            String first = bracketMatcher.group(1);
            String second = bracketMatcher.group(2);
            String sensitiveInput = bracketMatcher.group(3);
            bracketMatcher.appendReplacement(bracketMasked,
                    Matcher.quoteReplacement("[" + first + ", " + second + ", " + "*".repeat(sensitiveInput.length()) + "]"));
        }

        bracketMatcher.appendTail(bracketMasked);

        return bracketMasked.toString();
    }
}