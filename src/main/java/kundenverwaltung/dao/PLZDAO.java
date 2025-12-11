package kundenverwaltung.dao;

import java.sql.Connection;
import java.util.ArrayList;
import kundenverwaltung.model.PLZ;


/**
 * Created by Florian-PC on 02.11.2017.
 */
public interface PLZDAO
{
    boolean create(PLZ plz);
    boolean update(PLZ plz);
    boolean delete(PLZ plz);
    PLZ read(int plz);
    ArrayList<PLZ> readAll();
    boolean migrate(Connection alteDbCon, Connection conNewdDb);
}
