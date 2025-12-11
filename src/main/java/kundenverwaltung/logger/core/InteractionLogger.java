package kundenverwaltung.logger.core;

public interface InteractionLogger
{
    /**
     * Logs a user interaction.
     *
     * @param message The message to log, can be a format string.
     * @param args    The arguments for the format string.
     */
    void logInteraction(String message, String... args);
}
