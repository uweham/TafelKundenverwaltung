package kundenverwaltung.dao;

import kundenverwaltung.model.AusgabeTagZeit;
import kundenverwaltung.model.Ausgabegruppe;

/**
 * Created by Florian-PC on 03.11.2017.
 */
public interface AusgabegruppeAusgabeTagZeitDAO
{
  boolean ausgabeTagZeitHinzufuegen(Ausgabegruppe ausgabegruppe, AusgabeTagZeit ausgabeTagZeit);
  boolean ausgabeTagZeitEntfernen(Ausgabegruppe ausgabegruppe, AusgabeTagZeit ausgabeTagZeit);
}
