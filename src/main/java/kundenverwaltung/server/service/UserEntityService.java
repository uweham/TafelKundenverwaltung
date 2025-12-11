package kundenverwaltung.server.service;

import kundenverwaltung.model.User;
import kundenverwaltung.server.base.BaseServiceImpl;
import kundenverwaltung.server.dto.UserEntityDTO;
import lombok.Getter;
import lombok.Setter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserEntityService extends BaseServiceImpl<UserEntityDTO>
{
    private final static UserEntityService USER_SERVICE = new UserEntityService();
    private final String USER_GENERATION_UUID_PHRASE = "2025::USER_SERVICE_00XFLESKQOISX21";

    private User user;
    private List<UserEntityDTO> cachedUserEntityList;

    /**
     * Retrieves all user entities from the remote server.
     *
     * @param userEntityDTOClass The class of the user entity DTO.
     * @return A list of all user entities.
     */
    @Override
    public List<UserEntityDTO> getAll(Class<UserEntityDTO> userEntityDTOClass)
    {
        return super.getAll(userEntityDTOClass);
    }

    /**
     * Returns the API keyword for the user entity.
     *
     * @return The API keyword as a String.
     */
    @Override
    public String getAPIKeyWord()
    {
        return "users";
    }

    /**
     * Retrieves a cached list of user entities. If the cache is empty, it fetches the list from the server.
     *
     * @return A list of cached user entities.
     */
    public List<UserEntityDTO> getCachedUserEntityList()
    {
        if(cachedUserEntityList != null)
        {
            return cachedUserEntityList;
        }

        try
        {
            cachedUserEntityList = getAll(UserEntityDTO.class);
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            cachedUserEntityList = new ArrayList<>();
        }

        return cachedUserEntityList;
    }

    /**
     * Resets the cached list of user entities, forcing a refresh from the server on the next request.
     */
    public void resetCachedUserEntityList()
    {
        this.cachedUserEntityList = null;
    }

    /**
     * Generates a anonymized, deterministic UUID for the current user based on their username and ID.
     *
     * @return A UUID generated for the user.
     */
    public UUID getUserGeneratedAnonymizedUUID()
    {
        User user = getUser();
        String textToGenerateUUIDFrom = USER_GENERATION_UUID_PHRASE + user.getUserName() + user.getUserId();
        return UUID.nameUUIDFromBytes(textToGenerateUUIDFrom.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Returns the singleton instance of the UserEntityService.
     *
     * @return The UserEntityService instance.
     */
    public static UserEntityService getInstance()
    {
        return USER_SERVICE;
    }
}