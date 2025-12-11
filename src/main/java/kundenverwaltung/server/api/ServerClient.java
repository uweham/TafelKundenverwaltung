package kundenverwaltung.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kundenverwaltung.dao.EinstellungenDAO;
import kundenverwaltung.dao.EinstellungenDAOimpl;
import kundenverwaltung.model.Einstellungen;
import kundenverwaltung.server.service.AuthService;
import kundenverwaltung.server.util.JSONConverter;
import lombok.Getter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.stream.Collectors;

@Getter
public class ServerClient
{
    private static final String HMAC_CLIENT_SECRET = "2025::TAFEL_SERVER_SECRET";
    private static String httpBaseURL = "http://localhost:8080";
    private String response;
    private int httpStatus;

    /**
     * Sends a standard HTTP request (GET, POST, PUT, DELETE) to the server.
     *
     * @param method      The HTTP method (e.g., "GET", "POST").
     * @param path        The API endpoint path (e.g., "/users").
     * @param jsonPayload The JSON payload for POST, PUT, or DELETE requests. Can be null.
     * @return The HTTP status code of the response.
     * @throws RuntimeException if an error occurs during the request.
     */
    public int sendRequest(String method, String path, String jsonPayload)
    {
        HttpURLConnection conn = null;

        try
        {
            URL url = new URL(httpBaseURL + path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            addStandardHeaders(conn, method, path);

            if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method))
            {
                if(jsonPayload != null)
                {
                    conn.setDoOutput(true);

                    try (OutputStream os = conn.getOutputStream())
                    {
                        os.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
                    }
                }
            }

            httpStatus = conn.getResponseCode();
            InputStream is = (httpStatus < HttpURLConnection.HTTP_BAD_REQUEST) ? conn.getInputStream() : conn.getErrorStream();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)))
            {
                response = reader.lines().collect(Collectors.joining("\n"));
                return httpStatus;
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            if (conn != null)
            {
                conn.disconnect();
            }
        }
    }

    /**
     * Sends a multipart/form-data request to the server, typically for file uploads.
     *
     * @param path   The API endpoint path.
     * @param object The DTO object to be sent as a JSON part.
     * @param file   The file to be uploaded. Can be null.
     * @return The HTTP status code of the response.
     * @throws RuntimeException if the request fails.
     */
    public int sendMultipartRequest(String path, Object object, File file)
    {
        String boundary = "boundary" + System.currentTimeMillis();
        HttpURLConnection conn = null;

        try
        {
            URL url = new URL(httpBaseURL + path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setRequestProperty("Accept", "application/json");

            addStandardHeaders(conn, "POST", path);

            try (DataOutputStream out = new DataOutputStream(conn.getOutputStream()))
            {
                writeJsonPart(out, boundary, "object", JSONConverter.toJson(object));

                if (file != null)
                {
                    writeFilePart(out, boundary, "file", file);
                }

                out.writeBytes("--" + boundary + "--\r\n");
                out.flush();
            }

            httpStatus = conn.getResponseCode();
            InputStream is = (httpStatus < HttpURLConnection.HTTP_BAD_REQUEST) ? conn.getInputStream() : conn.getErrorStream();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)))
            {
                response = reader.lines().collect(Collectors.joining("\n"));
                return httpStatus;
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to send multipart request", e);
        }
        finally
        {
            if (conn != null)
            {
                conn.disconnect();
            }
        }
    }

    /**
     * Adds standard security headers to the HTTP connection, including a timestamp, HMAC signature, and JWT token.
     *
     * @param conn   The HttpURLConnection to which the headers will be added.
     * @param method The HTTP method of the request.
     * @param path   The path of the request.
     * @throws NoSuchAlgorithmException if the HMAC algorithm is not available.
     * @throws InvalidKeyException      if the secret key is invalid.
     */
    private static void addStandardHeaders(HttpURLConnection conn, String method, String path) throws NoSuchAlgorithmException, InvalidKeyException
    {
        String tsHeader = Long.toString(Instant.now().getEpochSecond());
        String signature = computeHmacSignature(method, path, tsHeader);

        String jwtToken = AuthService.getInstance().getJWTToken();
        if (jwtToken != null)
        {
            conn.setRequestProperty("Authorization", "Bearer " + jwtToken);
        }

        conn.setRequestProperty("X-Client-Timestamp", tsHeader);
        conn.setRequestProperty("X-Client-Signature", signature);
    }

    /**
     * Computes an HMAC-SHA256 signature for the request.
     *
     * @param method    The HTTP method.
     * @param path      The request path.
     * @param timestamp The timestamp of the request.
     * @return The computed HMAC signature as a hex string.
     * @throws NoSuchAlgorithmException if the HmacSHA256 algorithm is not found.
     * @throws InvalidKeyException      if the provided secret key is invalid.
     */
    private static String computeHmacSignature(String method, String path, String timestamp) throws NoSuchAlgorithmException, InvalidKeyException
    {
        String message = method + "|" + path + "|" + timestamp;

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(HMAC_CLIENT_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] raw = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        return bytesToHex(raw);
    }

    /**
     * Writes a JSON part to a multipart request stream.
     *
     * @param out      The DataOutputStream to write to.
     * @param boundary The multipart boundary string.
     * @param name     The name of the form field.
     * @param json     The JSON string to write.
     * @throws IOException if an I/O error occurs.
     */
    private static void writeJsonPart(DataOutputStream out, String boundary, String name, String json) throws IOException
    {
        out.writeBytes("--" + boundary + "\r\n");
        out.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
        out.writeBytes("Content-Type: application/json; charset=UTF-8\r\n\r\n");
        out.writeBytes(json + "\r\n");
    }

    /**
     * Writes a file part to a multipart request stream.
     *
     * @param out      The DataOutputStream to write to.
     * @param boundary The multipart boundary string.
     * @param name     The name of the form field.
     * @param file     The file to write.
     * @throws IOException if an I/O error occurs.
     */
    private static void writeFilePart(DataOutputStream out, String boundary, String name, File file) throws IOException
    {
        out.writeBytes("--" + boundary + "\r\n");
        out.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + file.getName() + "\"\r\n");
        out.writeBytes("Content-Type: image/png\r\n\r\n");

        try (FileInputStream fis = new FileInputStream(file))
        {
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1)
            {
                out.write(buffer, 0, bytesRead);
            }
        }

        out.writeBytes("\r\n");
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes The byte array to convert.
     * @return The hexadecimal string representation.
     */
    private static String bytesToHex(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes)
        {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Initializes the base URL for the server client from the application settings.
     */
    public static void setupServerClient()
    {
        EinstellungenDAO settingsDAO = new EinstellungenDAOimpl();
        Einstellungen settings = settingsDAO.read();

        httpBaseURL = settings.getTafelServerHostAddress();
    }
}