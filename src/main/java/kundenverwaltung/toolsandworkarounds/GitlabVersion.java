package kundenverwaltung.toolsandworkarounds;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitlabVersion
{

    private static final String GITLAB_RELEASES_URL = "https://gitlab.com/api/v4/projects/54722854/releases";
    private static final Logger LOGGER = LoggerFactory.getLogger(GitlabVersion.class);

    public static String getLatestVersion()
    {
        try
        {
            URL url = new URL(GITLAB_RELEASES_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            List<Release> releases = new Gson().fromJson(reader, new TypeToken<List<Release>>()
            {

            }
            .getType());
            reader.close();

            if (!releases.isEmpty())
            {
                return releases.get(0).tag_name;
            }
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }

        return null;
    }

    static class Release
    {
        String name;
        String tag_name;
    }
}
