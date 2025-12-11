package kundenverwaltung.dao;

import kundenverwaltung.model.Verteilstelle;

import java.sql.Connection;
import java.util.List;

public interface VerteilstelleDAO
{
    boolean create(Verteilstelle verteilstelle);
    boolean update(Verteilstelle verteilstelle);
    boolean delete(Verteilstelle verteilstelle);
    Verteilstelle read(int verteilstelleId);
    List<Verteilstelle> readAll();
    boolean migrate(Connection alteDbCon, Connection conNewdDb);

    List<Verteilstelle> getAllVerteilstelle();

    Verteilstelle readByName(String selectedVerteilstelleName);
}
