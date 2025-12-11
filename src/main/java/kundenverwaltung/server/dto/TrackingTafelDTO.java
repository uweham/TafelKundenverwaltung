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
public class TrackingTafelDTO extends BaseEntityDTO
{
    private UUID uuid;

    private String progVersion;

    private String tafelName;

    private String tafelLocation;

    /**
     * Returns the unique identifier of the tracking entry.
     *
     * @return The UUID of the tracking entry.
     */
    @Override
    public UUID getUuid()
    {
        return uuid;
    }
}