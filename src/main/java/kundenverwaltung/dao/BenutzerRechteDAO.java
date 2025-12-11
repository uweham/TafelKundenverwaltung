package kundenverwaltung.dao;

import java.sql.Connection;
import kundenverwaltung.model.Benutzer;
import kundenverwaltung.model.Recht;

/**
 * Created by Florian-PC on 03.11.2017.
 */
public interface BenutzerRechteDAO
{
    boolean rechtHinzufuegen(Benutzer benutzer, Recht recht);
    boolean rechtEntfernen(Benutzer benutzer, Recht recht);
    boolean migrate(Connection alteDbCon, Connection conNewdDb);
}
