package kundenverwaltung.dao;

import java.sql.Connection;
import java.util.ArrayList;
import kundenverwaltung.model.Vollmacht;

/**
 * Created by Florian-PC on 03.11.2017.
 */
public interface VollmachtDAO
{
    boolean create(Vollmacht vollmacht);
    boolean update(Vollmacht vollmacht);
    boolean delete(Vollmacht vollmacht);
    Vollmacht read(int vollmachtId);
    ArrayList<Vollmacht> getAllVollmachtenById(int haushaltsId);
    boolean migrate(Connection alteDbCon, Connection conNewdDb);
}
