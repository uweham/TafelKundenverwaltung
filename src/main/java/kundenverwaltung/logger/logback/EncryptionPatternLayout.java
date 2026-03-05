package kundenverwaltung.logger.logback;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import kundenverwaltung.logger.file.LogMessageCipher;
import kundenverwaltung.service.GetVersionProperties;

public class EncryptionPatternLayout extends PatternLayout
{
  private GetVersionProperties getversionproperties = new GetVersionProperties()   ;
    /**
     * Formats the logging event and then encrypts the resulting string.
     *
     * @param event The logging event to format.
     * @return The encrypted log message, followed by a line separator.
     */
    @Override
    public String doLayout(ILoggingEvent event)
    {
        String originalMessage = super.doLayout(event);
        if (!getversionproperties.isNoErrorReport()) 
        {
            String encryptedMessage = LogMessageCipher.encryptMessage(originalMessage);
            return encryptedMessage + System.lineSeparator();
        } 
        return originalMessage + System.lineSeparator();
    }
}
