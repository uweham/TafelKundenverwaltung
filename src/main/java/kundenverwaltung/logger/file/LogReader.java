package kundenverwaltung.logger.file;

import kundenverwaltung.logger.model.LogEntry;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogReader
{
    private static final Pattern LOG_PATTERN = Pattern.compile(
            "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) (\\w+)\\s+(.+?):\\d+ - (.+)"
    );

    /**
     * Reads and decrypts log entries from a specified log file.
     *
     * @param filePath The path to the log file.
     * @return A list of LogEntry objects parsed from the file.
     * @throws IOException if an I/O error occurs reading the file.
     */
    public static List<LogEntry> readLogsFromFile(String filePath) throws IOException
    {
        List<LogEntry> entries = new ArrayList<>();

        String[] decryptedMessages = LogFileService.getDecryptedMessagesByFile(filePath);

        for (String message : decryptedMessages)
        {
            if (!message.trim().isEmpty()
                    && !message.equals("[Keine Logdatei gefunden]")
                    && !message.equals("[Fehler beim Entschlüsseln]"))
            {
                processDecryptedMessage(message, entries);
            }
        }

        return entries;
    }

    /**
     * Processes a single decrypted log message, parses it into a LogEntry, and adds it to a list.
     * If the message does not match the expected log pattern, it is added as an 'UNKNOWN' entry.
     *
     * @param message The decrypted log message string.
     * @param entries The list of LogEntry objects to add the new entry to.
     */
    private static void processDecryptedMessage(String message, List<LogEntry> entries)
    {
        Matcher matcher = LOG_PATTERN.matcher(message);

        if (matcher.find())
        {
            String timestamp = matcher.group(1);
            String level = matcher.group(2);
            String source = matcher.group(3);
            String msgContent = matcher.group(4);

            String shortMessage = msgContent.length() > 100
                    ? msgContent.substring(0, 100) + "..." : msgContent;

            entries.add(new LogEntry(timestamp, level, source, shortMessage, message));
        }
        else
        {
            entries.add(new LogEntry(
                    LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    "UNKNOWN",
                    "UNKNOWN",
                    message,
                    message
            ));
        }
    }
}