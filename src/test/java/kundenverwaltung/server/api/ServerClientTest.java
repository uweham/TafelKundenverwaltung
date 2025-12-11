package kundenverwaltung.server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerClientTest
{

    @BeforeEach
    void setUp()
    {
        ServerClient.setupServerClient();
    }

    @Test
    void sendRequest()
    {
        ServerClient client = new ServerClient();
        assertThrows(RuntimeException.class, () ->
        {
            client.sendRequest("GET", "/nonexistent", null);
        }, "sendRequest should throw RuntimeException for connection errors");
    }

    @Test
    void sendMultipartRequest()
    {
    }

    @Test
    void setupServerClient()
    {
    }

    @Test
    void getResponse()
    {
        ServerClient client = new ServerClient();
        assertNull(client.getResponse(), "getResponse should be null initially or after a failed request");
    }

    @Test
    void getHttpStatus()
    {
        ServerClient client = new ServerClient();
        assertEquals(0, client.getHttpStatus(), "getHttpStatus should be 0 initially or after a failed request");
    }
}