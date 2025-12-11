package kundenverwaltung.dao;

import java.sql.Connection;
import java.util.ArrayList;
import kundenverwaltung.model.AusgabeTagZeit;

/**
 * Created by Florian-PC on 03.11.2017.
 */
public interface AusgabeTagZeitDAO
{
  boolean create(AusgabeTagZeit ausgabeTagZeit);

  boolean update(AusgabeTagZeit ausgabeTagZeit);

  boolean delete(AusgabeTagZeit ausgabeTagZeit);

  AusgabeTagZeit read(int ausgabeTagZeitId);

  ArrayList<AusgabeTagZeit> readAll();

  boolean migrate(Connection alteDbCon, Connection conNewdDb);
}
