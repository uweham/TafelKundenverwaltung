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
public class AuthDTO extends BaseEntityDTO
{
    private UUID uuid;

    private String jwtToken;

    private UserEntityDTO userDTO;

    /**
     * Checks if the authenticated user has a specific role.
     *
     * @param role The role to check for.
     * @return true if the user has the specified role, false otherwise.
     */
    public boolean hasRole(UserEntityDTO.Role role)
    {
        return userDTO.getRole().equals(role);
    }
}