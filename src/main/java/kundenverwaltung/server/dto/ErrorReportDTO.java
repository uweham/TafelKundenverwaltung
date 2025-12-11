package kundenverwaltung.server.dto;

import kundenverwaltung.server.base.BaseEntityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true, setterPrefix = "set")
public class ErrorReportDTO extends BaseEntityDTO
{
    private UUID uuid;

    private UUID userUUID;

    private UUID anonymizedUserUUID;

    private ScreenshotDTO screenshot;

    private String title;

    private String description;

    private String userDescription;

    private String internNotes;

    private Status status;

    private List<ErrorReportLogDTO> logs;

    private UserEntityDTO user;

    /**
     * Returns the unique identifier of the error report.
     *
     * @return The UUID of the error report.
     */
    @Override
    public UUID getUuid()
    {
        return uuid;
    }

    /**
     * Represents the status of an error report.
     */
    public enum Status
    {
        /**
         * The report is open and has not been addressed.
         */
        Offen,
        /**
         * The report is currently being worked on.
         */
        In_Bearbeitung,
        /**
         * The reported issue has been resolved.
         */
        Behoben;
    }
}