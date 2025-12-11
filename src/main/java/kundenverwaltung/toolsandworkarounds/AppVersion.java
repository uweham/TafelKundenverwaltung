package kundenverwaltung.toolsandworkarounds;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import java.io.File;

public class AppVersion
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AppVersion.class);

    public static String getPomVersion()
    {
        try
        {
            File pomFile = new File("pom.xml");
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(pomFile);
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            String expression = "/project/version";
            String version = (String) xPath.evaluate(expression, doc, XPathConstants.STRING);

            if (version != null && !version.isEmpty())
            {
                return version;
            }
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }

        return "Version nicht gefunden";
    }
}