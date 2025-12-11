package kundenverwaltung.server.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public abstract class BaseEntityDTO implements Serializable
{
    protected LocalDateTime createdAt;

    protected LocalDateTime changedOn;

    /**
     * Returns the unique identifier (UUID) of the entity.
     *
     * @return The UUID of the entity.
     */
    public abstract UUID getUuid();
}