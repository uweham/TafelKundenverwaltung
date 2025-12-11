package kundenverwaltung.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kundenverwaltung.server.api.ServerClient;
import kundenverwaltung.server.dto.AuthDTO;
import kundenverwaltung.server.dto.UserEntityDTO;
import kundenverwaltung.server.exception.AuthLoginError;
import kundenverwaltung.server.util.JSONConverter;

import java.io.IOException;

public class AuthService
{
    private static final AuthService authService = new AuthService();

    private final String AUTH_PATH_SIGN_IN = "/api/auth/signin";

    private AuthDTO loggedInAuthDTO;
    private String jwToken;

    private AuthService()
    {

    }

    /**
     * Authenticates a user with the given username and password.
     *
     * @param userName The user's username.
     * @param password The user's password.
     * @throws AuthLoginError if the authentication fails.
     * @throws RuntimeException if a communication or parsing error occurs.
     */
    public void signIn(String userName, String password) throws AuthLoginError
    {
        ServerClient client = new ServerClient();

        UserEntityDTO user = new UserEntityDTO();
        user.setUsername(userName);
        user.setPasswordHash(password);

        try
        {
            String jsonPayload = JSONConverter.toJson(user);

            int httpStatus = client.sendRequest("POST", AUTH_PATH_SIGN_IN, jsonPayload);

            if (httpStatus != 200)
            {
                throw new AuthLoginError();
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            try
            {
                String jsonResponse = client.getResponse();

                if (jsonResponse == null || jsonResponse.trim().isEmpty())
                {
                    throw new AuthLoginError();
                }

                loggedInAuthDTO = objectMapper.readValue(jsonResponse, AuthDTO.class);
                this.jwToken = loggedInAuthDTO.getJwtToken();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the currently logged-in user has the Admin role.
     *
     * @return true if the user is an Admin, false otherwise.
     */
    public boolean isAdmin()
    {
        if (loggedInAuthDTO != null)
        {
            return loggedInAuthDTO.hasRole(UserEntityDTO.Role.Admin);
        }

        return false;
    }

    /**
     * Checks if the currently logged-in user has the Studentische_Hilfskraft role or is an Admin.
     *
     * @return true if the user is a Studentische_Hilfskraft or an Admin, false otherwise.
     */
    public boolean isStudentischeHilfskraft()
    {
        if (isAdmin())
        {
            return true;
        }

        if (loggedInAuthDTO != null)
        {
            return loggedInAuthDTO.hasRole(UserEntityDTO.Role.Studentische_Hilfskraft);
        }

        return false;
    }

    /**
     * Returns the JWT token for the currently authenticated user.
     *
     * @return The JWT token as a String, or null if not authenticated.
     */
    public String getJWTToken()
    {
        return this.jwToken;
    }

    /**
     * Returns the singleton instance of the AuthService.
     *
     * @return The AuthService instance.
     */
    public static AuthService getInstance()
    {
        return authService;
    }
}