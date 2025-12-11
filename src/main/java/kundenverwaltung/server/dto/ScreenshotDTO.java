package kundenverwaltung.server.dto;

import kundenverwaltung.server.base.BaseEntityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true, setterPrefix = "set")
public class ScreenshotDTO extends BaseEntityDTO
{
    private UUID uuid;

    private byte[] screenshot;

    private FileType fileType;

    /**
     * Returns the unique identifier of the screenshot.
     *
     * @return The UUID of the screenshot.
     */
    @Override
    public UUID getUuid()
    {
        return uuid;
    }

    /**
     * Represents the file type of the screenshot image.
     */
    public enum FileType
    {
        /**
         * JPG file format.
         */
        JPG,
        /**
         * JPEG file format.
         */
        JPEG,
        /**
         * PNG file format.
         */
        PNG;
    }
}