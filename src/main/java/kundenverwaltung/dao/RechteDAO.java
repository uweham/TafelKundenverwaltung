package kundenverwaltung.dao;

import java.sql.Connection;
import kundenverwaltung.model.Recht;

/**
 * Created by Florian-PC on 03.11.2017.
 */
public interface RechteDAO
{
    boolean create(Recht recht);
    boolean update(Recht recht);
    boolean delete(Recht recht);
    Recht read(int rechteId);
    boolean migrate(Connection alteDbCon, Connection conNewdDb);
}
