package kundenverwaltung.dao;

import java.sql.Connection;
import java.util.ArrayList;
import kundenverwaltung.model.Bescheidart;

/**
 * Created by Florian-PC on 03.11.2017.
 */
public interface BescheidartDAO
{
    boolean create(Bescheidart bescheidart);
    boolean update(Bescheidart bescheidart);
    boolean delete(Bescheidart bescheidart);
    Bescheidart read(int bescheidArtId);
    ArrayList<Bescheidart> readAll();
    ArrayList<Bescheidart> readAssessments(Boolean enabled);
    boolean migrate(Connection alteDbCon, Connection conNewdDb);
}
