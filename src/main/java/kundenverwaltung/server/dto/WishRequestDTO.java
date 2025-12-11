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
public class WishRequestDTO extends BaseEntityDTO
{
    private UUID uuid;

    private UUID userUUID;

    private UUID anonymizedUserUUID;

    private ScreenshotDTO screenshot;

    private String title;

    private String description;

    private String internNotes;

    private Status status;

    private UserEntityDTO user;

    /**
     * Represents the status of a wish request.
     */
    public enum Status
    {
        /**
         * The wish is open and has not been addressed.
         */
        Offen,
        /**
         * The wish is currently being worked on.
         */
        In_Bearbeitung,
        /**
         * The wish has been implemented or fulfilled.
         */
        Abgeschlossen;
    }

    /**
     * Returns the unique identifier of the wish request.
     *
     * @return The UUID of the wish request.
     */
    @Override
    public UUID getUuid()
    {
        return uuid;
    }
}