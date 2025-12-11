package kundenverwaltung.dao;

import kundenverwaltung.model.Einstellungen;

public interface EinstellungenDAO
{
    boolean update(Einstellungen einstellungen);
    Einstellungen read();
}
