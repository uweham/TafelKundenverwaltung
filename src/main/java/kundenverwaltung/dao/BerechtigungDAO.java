package kundenverwaltung.dao;


import java.sql.Connection;
import java.util.ArrayList;
import kundenverwaltung.model.Berechtigung;

public interface BerechtigungDAO
{
    boolean create(Berechtigung berechtigung);
    boolean update(Berechtigung berechtigung);
    boolean delete(Berechtigung berechtigung);
    Berechtigung read(int berechtigungID);
    ArrayList<Berechtigung> getAllBerechtigungen();
    boolean migrate(Connection alteDbCon, Connection conNewdDb);
}
