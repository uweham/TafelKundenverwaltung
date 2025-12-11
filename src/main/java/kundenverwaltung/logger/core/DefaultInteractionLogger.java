package kundenverwaltung.logger.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultInteractionLogger implements InteractionLogger
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultInteractionLogger.class);

    /**
     * Logs a user interaction with a given message and arguments.
     *
     * @param message The main message to be logged.
     * @param args    Optional arguments to be formatted into the message.
     */
    @Override
    public void logInteraction(String message, String... args)
    {
        LOGGER.info(message, (Object) args);
    }
}
