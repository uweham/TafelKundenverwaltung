package kundenverwaltung.dao;

import java.sql.Connection;
import java.util.ArrayList;
import kundenverwaltung.model.Vorlage;
import kundenverwaltung.model.Vorlagearten;

public interface VorlageDAO
{
    boolean create(Vorlage vorlage);
    boolean update(Vorlage vorlage);
    boolean delete(Vorlage vorlage);
    Vorlage read(int vorlageID);
    boolean migrate(Connection alteDbCon, Connection conNewdDb);
    ArrayList<Vorlage> getTemplates(Vorlagearten template);
    ArrayList<Vorlage> getAllTemplate();
}
