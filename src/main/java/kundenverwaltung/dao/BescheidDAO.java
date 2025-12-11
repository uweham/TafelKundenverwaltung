package kundenverwaltung.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import kundenverwaltung.model.Bescheid;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Verteilstelle;

/**
 * Created by Florian-PC on 03.11.2017.
 */
public interface BescheidDAO
{
    boolean create(Bescheid bescheid);
    boolean update(Bescheid bescheid);
    boolean delete(Bescheid bescheid);
    Bescheid read(int bescheidId);
    ArrayList<Bescheid> readAll(Familienmitglied familienmitglied);
    boolean migrate(Connection alteDbCon, Connection conNewdDb);

    List<Bescheid> readBescheid(Verteilstelle verteilstelle, String selectedStatus);
    void saveBescheidStatistik(String bescheidartName, int anzahlPersonen);

}
