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
            setProp(props, "app.nointernet");
            setProp(props, "app.noerrorreport");
            setProp(props, "app.onlyhouseholddirector");
        }
      } catch (IOException ex) {
          System.err.println("Version konnte nicht geladen werden.");
      }
    }
    
    private void setProp(Properties props, String key)
    {
      String key_val = props.getProperty(key, null);
      if (key_val != null) {
        System.setProperty(key, key_val);
      }
    }
    
    
 /**
   * 
  * checks in version.properties the key app.nointernet 
  * Returns true when app.nointernet set otherwise false 
  *
  * @return      true when app.nointernent set otherwise false 
  */   
  
  public boolean isNoInternetEnvironment() {
    String v = System.getProperty("app.nointernet");
    return v != null && v.startsWith("true"); 
  }
  /**
   * checks in version.properties the key app.noerrorreport 
   * Returns true when app.noerrorreport set otherwise false 
   *
   * @return      true when app.noerrorreport set otherwise false 
   */   
   
 public boolean isNoErrorReport() {
   String v = System.getProperty("app.noerrorreport");
   return v != null && v.startsWith("true"); 
 }
 
 /**
  * checks in version.properties the key app.onlyhouseholddirector 
  * Returns true when app.onlyhouseholddirector set otherwise false 
  *
  * @return      true when app.onlyhouseholddirector set otherwise false 
  */   
  
public boolean onlyhouseholddirector() {
  String v = System.getProperty("app.onlyhouseholddirector");
  return v != null && v.startsWith("true"); 
}
 
}
