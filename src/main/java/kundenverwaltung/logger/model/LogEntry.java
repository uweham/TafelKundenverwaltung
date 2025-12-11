package kundenverwaltung.logger.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import kundenverwaltung.server.dto.ErrorReportLogDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Represents a single log entry with properties suitable for JavaFX display.
 */

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@SuperBuilder(toBuilder = true, setterPrefix = "set")
public class LogEntry
{
    private final StringProperty timestamp;
    private final StringProperty level;
    private final StringProperty source;
    private final StringProperty message;
    private final String fullMessage;

    /**
     * Constructs a new LogEntry.
     *
     * @param timestamp   The timestamp of the log event.
     * @param level       The log level (e.g., INFO, ERROR).
     * @param source      The source of the log event (e.g., class name).
     * @param message     A short or truncated version of the log message.
     * @param fullMessage The complete, untruncated log message.
     */
    public LogEntry(String timestamp, String level, String source, String message, String fullMessage)
    {
        this.timestamp = new SimpleStringProperty(timestamp);
        this.level = new SimpleStringProperty(level);
        this.source = new SimpleStringProperty(source);
        this.message = new SimpleStringProperty(message);
        this.fullMessage = fullMessage;
    }

    /**
     * Returns the StringProperty for the timestamp.
     *
     * @return The timestamp property.
     */
    public StringProperty timestampProperty()
    {
        return timestamp;
    }

    /**
     * Returns the StringProperty for the log level.
     *
     * @return The level property.
     */
    public StringProperty levelProperty()
    {
        return level;
    }

    /**
     * Returns the StringProperty for the log source.
     *
     * @return The source property.
     */
    public StringProperty sourceProperty()
    {
        return source;
    }

    /**
     * Returns the StringProperty for the short message.
     *
     * @return The message property.
     */
    public StringProperty messageProperty()
    {
        return message;
    }

    /**
     * Returns the full, untruncated log message.
     *
     * @return The full log message.
     */
    public String getFullMessage()
    {
        return fullMessage;
    }

    /**
     * Gets the value of the timestamp property.
     *
     * @return The timestamp as a String.
     */
    public String getTimestamp()
    {
        return timestamp.get();
    }

    /**
     * Gets the value of the level property.
     *
     * @return The log level as a String.
     */
    public String getLevel()
    {
        return level.get();
    }

    /**
     * Gets the value of the source property.
     *
     * @return The log source as a String.
     */
    public String getSource()
    {
        return source.get();
    }

    /**
     * Gets the value of the message property.
     *
     * @return The short log message as a String.
     */
    public String getMessage()
    {
        return message.get();
    }

    /**
     * Creates a LogEntry from an ErrorReportLogDTO.
     *
     * @param dto The Data Transfer Object to convert.
     * @return A new LogEntry instance.
     */
    public static LogEntry fromDTO(ErrorReportLogDTO dto)
    {
        return new LogEntry(
                dto.getTimestamp(),
                dto.getLevel(),
                dto.getSource(),
                dto.getMessage(),
                dto.getFullMessage()
        );
    }
}