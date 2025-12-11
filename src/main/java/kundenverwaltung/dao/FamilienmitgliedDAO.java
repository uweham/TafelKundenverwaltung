package kundenverwaltung.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Verteilstelle;

/**
 * Created by Florian-PC on 02.11.2017.
 */
public interface FamilienmitgliedDAO
{
    boolean create(Familienmitglied familienmitglied);
    boolean update(Familienmitglied familienmitglied);
    boolean delete(Familienmitglied familienmitglied);
    boolean insertDeletedMemberOfTheFamiliy(Familienmitglied memberOfTheFamily, String reasenDelete);
    int getHousholdDirector(int householdId);
    int getMaxPersonId();
    Familienmitglied read(int personId);
    ArrayList<Familienmitglied> getAllFamilienmitglieder(int haushaltsid);
    ArrayList<Familienmitglied> getAllFamilienmitglieder(String name);
    ArrayList<Familienmitglied> getAllFamilienmitglieder(String suche, int filter, boolean genaueSuche);
    boolean migrate(Connection alteDbCon, Connection conNewdDb);

	List<Familienmitglied> readByVerteilstelle(Verteilstelle verteilstelle);
}
