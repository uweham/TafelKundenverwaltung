package kundenverwaltung.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetVersionProperties {
 
    {
      try (InputStream input = getClass().getResourceAsStream("/version.properties")) {
    
        Properties props = new Properties();
        if (input != null) {
            props.load(input);
            String nointernet = props.getProperty("app.nointernet", null);
            if (nointernet != null) {
              System.setProperty("app.nointernet", nointernet);
            }
        }
      } catch (IOException ex) {
          System.err.println("Version konnte nicht geladen werden.");
      }
    }
  
    /**
    * checks in version.properties the key app.nointernet 
    * Returns true when app.nointernet set otherwise false 
    *
    * @return      true when app.nointerent set otherwise false 
    */   
    
  public boolean isNoInternetEnvironment() {
    String v = System.getProperty("app.nointernet");
    return v != null && v.startsWith("${"); 
    
    
  }
}
