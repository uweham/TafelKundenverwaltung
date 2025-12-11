package kundenverwaltung.server.dto;

import kundenverwaltung.server.base.BaseEntityDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true, setterPrefix = "set")
public class ErrorReportLogDTO extends BaseEntityDTO
{
    private UUID uuid;

    private String message;

    private String fullMessage;

    private String level;

    private String source;

    private String timestamp;

    private UUID errorReportUuid;

    /**
     * Returns the unique identifier of the log entry.
     *
     * @return The UUID of the log entry.
     */
    @Override
    public UUID getUuid()
    {
        return uuid;
    }
}