package kundenverwaltung.server.service;

import kundenverwaltung.toolsandworkarounds.AppVersion;
import kundenverwaltung.server.api.ServerClient;
import kundenverwaltung.server.base.BaseServiceImpl;
import kundenverwaltung.server.dto.TrackingTafelDTO;
import kundenverwaltung.server.exception.TafelServerPingError;
import kundenverwaltung.server.util.JSONConverter;
import kundenverwaltung.toolsandworkarounds.CustomPropertiesStore;

import java.util.UUID;

public class TrackingTafelService extends BaseServiceImpl<TrackingTafelDTO>
{
    /**
     * Sends a ping to the server with tracking information about the client application.
     *
     * @return The HTTP status code of the response.
     * @throws TafelServerPingError if the server returns a non-200 status code.
     * @throws Exception if an error occurs while loading properties.
     */
    public int ping() throws Exception
    {
        TrackingTafelDTO trackingTafelDTO = new TrackingTafelDTO();


        CustomPropertiesStore properties = CustomPropertiesStore.loadTafelInfoPropertiesFileCustomStore();
        UUID generatedUUID = UserEntityService.getInstance().getUserGeneratedAnonymizedUUID();
        String progVersion = AppVersion.getPomVersion();
        String tafelName = properties.getProperty("tafel.name", "Kein Tafel-Name");
        String tafelLocation = properties.getProperty("tafel.location", "Keine Location");

        trackingTafelDTO.setUuid(generatedUUID);
        trackingTafelDTO.setProgVersion(progVersion);
        trackingTafelDTO.setTafelName(tafelName);
        trackingTafelDTO.setTafelLocation(tafelLocation);

        ServerClient client = new ServerClient();

        String path = "/api/" + getAPIKeyWord() + "/ping";
        String jsonPayload = JSONConverter.toJson(trackingTafelDTO);

        int httpStatus = client.sendRequest("POST", path, jsonPayload);

        if (httpStatus != 200)
        {
            LOGGER.error("httpStatus: {}", httpStatus);
            throw new TafelServerPingError("httpStatus: " + httpStatus);
        }

        return httpStatus;
    }

    /**
     * Returns the API keyword for the tracking entity.
     *
     * @return The API keyword as a String.
     */
    @Override
    public String getAPIKeyWord()
    {
        return "tracking";
    }
}