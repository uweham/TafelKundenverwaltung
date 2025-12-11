package kundenverwaltung.server.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserEntityDTO extends BaseEntityDTO
{
    private UUID uuid;

    private String username;

    private String passwordHash;

    private Role role;

    /**
     * Returns the unique identifier of the user.
     *
     * @return The UUID of the user.
     */
    @Override
    public UUID getUuid()
    {
        return uuid;
    }

    /**
     * Represents the role of a user in the system.
     */
    public enum Role
    {
        /**
         * Administrator with full access.
         */
        Admin,
        /**
         * Student assistant with limited access.
         */
        Studentische_Hilfskraft
    }
}