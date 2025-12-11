package kundenverwaltung.dao;

import java.util.ArrayList;
import kundenverwaltung.model.Ortsteil;

public interface OrtsteilDAO
{
    boolean create(Ortsteil ortsteil);
    boolean update(Ortsteil ortsteil);
    boolean delete(Ortsteil ortsteil);
    ArrayList<Ortsteil> readAll(int plzId);
    Ortsteil read(int ortsteilId);
}
