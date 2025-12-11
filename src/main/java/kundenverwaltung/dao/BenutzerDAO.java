
package kundenverwaltung.dao;

import java.sql.Connection;
import java.util.ArrayList;
import kundenverwaltung.model.Benutzer;

/**
 * Created by Florian-PC on 03.11.2017.
 */
public interface BenutzerDAO
{
    boolean create(Benutzer benutzer);
    boolean update(Benutzer benutzer);
    boolean delete(Benutzer benutzer);
    boolean readRecht(Benutzer benutzer, int rechtid);
    Benutzer read(int benutzerId);
    ArrayList<Benutzer> readAll();
    boolean migrate(Connection alteDbCon, Connection conNewdDb);

}
