package kundenverwaltung.toolsandworkarounds;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GitlabVersionTest
{

    /** Test that the GitlabVersion class can be loaded */
    @Test
    void testClassExists()
    {
        assertNotNull(GitlabVersion.class, "GitlabVersion class should be available");
    }

    /** Test Release inner class functionality */
    @Test
    void testReleaseClass()
    {
        GitlabVersion.Release release = new GitlabVersion.Release();
        release.name = "Test Release";
        release.tag_name = "v1.0.0";

        assertEquals("Test Release", release.name, "Release name should be set correctly");
        assertEquals("v1.0.0", release.tag_name, "Release tag_name should be set correctly");
    }

    /** Test JSON deserialization with Gson */
    @Test
    void testJsonDeserialization()
    {
        String jsonResponse = "[{\"name\":\"Release 1\",\"tag_name\":\"v1.0.0\"},{\"name\":\"Release 2\",\"tag_name\":\"v2.0.0\"}]";
        Gson gson = new Gson();

        List<GitlabVersion.Release> releases = gson.fromJson(jsonResponse, new TypeToken<List<GitlabVersion.Release>>(){}.getType());

        assertEquals(2, releases.size(), "Should deserialize two releases");
        assertEquals("Release 1", releases.get(0).name, "First release name should match");
        assertEquals("v1.0.0", releases.get(0).tag_name, "First release tag should match");
        assertEquals("Release 2", releases.get(1).name, "Second release name should match");
        assertEquals("v2.0.0", releases.get(1).tag_name, "Second release tag should match");
    }

    /** Test JSON deserialization with null values */
    @Test
    void testJsonDeserializationWithNulls()
    {
        String jsonResponse = "[{\"name\":null,\"tag_name\":\"v1.0.0\"},{\"name\":\"Release 2\",\"tag_name\":null}]";
        Gson gson = new Gson();

        List<GitlabVersion.Release> releases = gson.fromJson(jsonResponse, new TypeToken<List<GitlabVersion.Release>>(){}.getType());

        assertEquals(2, releases.size(), "Should deserialize two releases");
        assertNull(releases.get(0).name, "First release name should be null");
        assertEquals("v1.0.0", releases.get(0).tag_name, "First release tag should match");
        assertEquals("Release 2", releases.get(1).name, "Second release name should match");
        assertNull(releases.get(1).tag_name, "Second release tag should be null");
    }

    /** Test JSON deserialization with empty values */
    @Test
    void testJsonDeserializationWithEmptyValues()
    {
        String jsonResponse = "[{\"name\":\"\",\"tag_name\":\"\"},{\"name\":\"Release 2\",\"tag_name\":\"v2.0.0\"}]";
        Gson gson = new Gson();

        List<GitlabVersion.Release> releases = gson.fromJson(jsonResponse, new TypeToken<List<GitlabVersion.Release>>(){}.getType());

        assertEquals(2, releases.size(), "Should deserialize two releases");
        assertEquals("", releases.get(0).name, "First release name should be empty string");
        assertEquals("", releases.get(0).tag_name, "First release tag should be empty string");
        assertEquals("Release 2", releases.get(1).name, "Second release name should match");
        assertEquals("v2.0.0", releases.get(1).tag_name, "Second release tag should match");
    }

    /** Test empty JSON array */
    @Test
    void testJsonDeserializationEmptyArray()
    {
        String jsonResponse = "[]";
        Gson gson = new Gson();

        List<GitlabVersion.Release> releases = gson.fromJson(jsonResponse, new TypeToken<List<GitlabVersion.Release>>(){}.getType());

        assertEquals(0, releases.size(), "Should deserialize empty list");
        assertTrue(releases.isEmpty(), "List should be empty");
    }

    /** Test malformed JSON handling */
    @Test
    void testJsonDeserializationMalformed()
    {
        String invalidJsonResponse = "{invalid json}";
        Gson gson = new Gson();

        assertThrows(Exception.class, () -> {
            gson.fromJson(invalidJsonResponse, new TypeToken<List<GitlabVersion.Release>>(){}.getType());
        }, "Should throw exception for malformed JSON");
    }

    /** Test Release class with different values */
    @Test
    void testReleaseClassVariousValues()
    {
        GitlabVersion.Release release1 = new GitlabVersion.Release();
        release1.name = "Version 2.1.0";
        release1.tag_name = "v2.1.0";

        assertEquals("Version 2.1.0", release1.name);
        assertEquals("v2.1.0", release1.tag_name);

        GitlabVersion.Release release2 = new GitlabVersion.Release();
        release2.name = null;
        release2.tag_name = null;

        assertNull(release2.name);
        assertNull(release2.tag_name);

        GitlabVersion.Release release3 = new GitlabVersion.Release();
        release3.name = "";
        release3.tag_name = "";

        assertEquals("", release3.name);
        assertEquals("", release3.tag_name);
    }

    /** Test Gson TypeToken functionality */
    @Test
    void testTypeTokenCreation()
    {
        TypeToken<List<GitlabVersion.Release>> typeToken = new TypeToken<List<GitlabVersion.Release>>(){};

        assertNotNull(typeToken.getType(), "TypeToken should provide a Type");
        assertTrue(typeToken.getType().getTypeName().contains("GitlabVersion$Release"),
                "Type should reference GitlabVersion.Release");
    }

    /** Test JSON with extra fields (should be ignored) */
    @Test
    void testJsonDeserializationWithExtraFields()
    {
        String jsonResponse = "[{\"name\":\"Release 1\",\"tag_name\":\"v1.0.0\",\"extra_field\":\"ignored\",\"another_field\":123}]";
        Gson gson = new Gson();

        List<GitlabVersion.Release> releases = gson.fromJson(jsonResponse, new TypeToken<List<GitlabVersion.Release>>(){}.getType());

        assertEquals(1, releases.size(), "Should deserialize one release");
        assertEquals("Release 1", releases.get(0).name, "Release name should match");
        assertEquals("v1.0.0", releases.get(0).tag_name, "Release tag should match");
    }

    /** Test multiple releases with mixed valid/invalid data */
    @Test
    void testJsonDeserializationMixedData()
    {
        String jsonResponse = "[" +
                "{\"name\":\"Valid Release\",\"tag_name\":\"v1.0.0\"}," +
                "{\"name\":null,\"tag_name\":\"v0.9.0\"}," +
                "{\"name\":\"Another Release\",\"tag_name\":\"\"}," +
                "{\"name\":\"\",\"tag_name\":\"v0.8.0\"}" +
                "]";
        Gson gson = new Gson();

        List<GitlabVersion.Release> releases = gson.fromJson(jsonResponse, new TypeToken<List<GitlabVersion.Release>>(){}.getType());

        assertEquals(4, releases.size(), "Should deserialize all four releases");

        assertEquals("Valid Release", releases.get(0).name);
        assertEquals("v1.0.0", releases.get(0).tag_name);

        assertNull(releases.get(1).name);
        assertEquals("v0.9.0", releases.get(1).tag_name);

        assertEquals("Another Release", releases.get(2).name);
        assertEquals("", releases.get(2).tag_name);

        assertEquals("", releases.get(3).name);
        assertEquals("v0.8.0", releases.get(3).tag_name);
    }
}
