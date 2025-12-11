package kundenverwaltung.dao;

import java.sql.Connection;
import java.util.ArrayList;

import kundenverwaltung.model.Nation;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public interface NationDAO
{
    boolean create(Nation nation);
    boolean update(Nation nation);
    boolean delete(Nation nation);

    // WICHTIG: wieder als Instanzmethode, NICHT static
    Nation read(int nation);

    boolean migrate(Connection alteDbCon, Connection conNewdDb);
    ArrayList<Nation> getAllNationen();
    ArrayList<Nation> getAllEnabledNationen();

    IntegerProperty ANZAHL = new SimpleIntegerProperty();

    ArrayList<Nation> getAllNationenMitAnzahl();

    void saveNationStatistics(ArrayList<Nation> nationList);
}
