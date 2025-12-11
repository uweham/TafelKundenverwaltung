package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import kundenverwaltung.model.Einkauf;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.OrderBy;
import kundenverwaltung.model.Verteilstelle;
import kundenverwaltung.model.Warentyp;
import javafx.collections.ObservableList;

/**
 * Created by Florian-PC on 03.11.2017.
 */
public interface EinkaufDAO
{
    boolean create(Einkauf einkauf);

    boolean update(Einkauf einkauf);

    boolean delete(Einkauf einkauf);

    Einkauf read(int einkaufId);

    boolean migrate(Connection alteDbCon, Connection conNewdDb);

    boolean boughtHouseholdThisProductType(int householdId, Warentyp productType);

    LocalDateTime getLetzerEinkauf(Haushalt haushalt);

    ArrayList<Einkauf> getAllEinkauefe(Haushalt haushalt);

    ArrayList<Einkauf> getAllSalesToday(Timestamp periodeStart, Timestamp periodeEnd, Familienmitglied familyMember,
                                               Verteilstelle distributionPoint, OrderBy orderBy, Boolean ascending,
                                               Warentyp productTyp, Boolean salesCanceledToday, Boolean salesEarlierCanceledToday);

    ArrayList<Einkauf> getAllEinkauefe(Haushalt household, Warentyp warentyp, Verteilstelle verteilstelle, boolean storniert, LocalDate startDatum, LocalDate endDatum);

    void saveGuthabenStatistik(List<Einkauf> einkaeufe);

    List<Einkauf> getEinkaeufe(String type, String verteilstelle);

    List<Einkauf> getEinkaeufeByQuery(String sqlQuery, String verteilstelle);

    List<Einkauf> buildQuery(String type, String verteilstelle);

    String generateSQLQuery(String type, String verteilstelle);

    void saveToGuthabenStatistik(List<Einkauf> einkaeufe);
}





