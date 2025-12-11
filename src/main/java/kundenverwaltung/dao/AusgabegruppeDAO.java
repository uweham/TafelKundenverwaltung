package kundenverwaltung.dao;

import java.sql.Connection;
import java.util.ArrayList;
import kundenverwaltung.model.Ausgabegruppe;

/**
 * Created by Florian-PC on 03.11.2017.
 */
public interface AusgabegruppeDAO
{
    boolean create(Ausgabegruppe ausgabegruppe);
    boolean update(Ausgabegruppe ausgabegruppe);
    boolean delete(Ausgabegruppe ausgabegruppe);
    Ausgabegruppe read(int ausgabegruppeId);
    ArrayList<Ausgabegruppe> readAll();
    boolean migrate(Connection alteDbCon, Connection conNewdDb);
}
