package kundenverwaltung.dao;


import java.sql.Connection;
import java.util.ArrayList;
import kundenverwaltung.model.Warentyp;

public interface WarentypDAO
{
    boolean create(Warentyp warentyp);
    boolean update(Warentyp warentyp);
    boolean delete(Warentyp warentyp);
    Warentyp read(int warentypId);
    ArrayList<Warentyp> readAll();
    ArrayList<Warentyp> readAllAktiv();
    boolean migrate(Connection alteDbCon, Connection conNewdDb);
}
