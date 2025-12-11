package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.OrderBy;
import kundenverwaltung.model.statistiktool.ArchivierteKunden;
import kundenverwaltung.model.statistiktool.Herkunft;
import kundenverwaltung.model.Verteilstelle;
import kundenverwaltung.model.Warentyp;
import javafx.collections.ObservableList;

public interface HaushaltDAO
{
    boolean create(Haushalt haushalt);
    boolean update(Haushalt haushalt);
    boolean delete(Haushalt haushalt);
    Haushalt read(int kundennummer);
    ArrayList<Haushalt> getHousehold(int distributionPointId);
    ArrayList<Haushalt> createCustomerList(Verteilstelle distributionPoint, Warentyp productType, OrderBy orderBy,
                                                  Boolean ascending, Boolean showArchivedCustomer,
                                                  Boolean showBlockedCustomer);
    boolean migrate(Connection alteDbCon, Connection conNewdDb);

    List<Herkunft> getHouseholdsWithLocation(String selectedStatus, boolean isPerson, LocalDate startDatum, LocalDate endDatum);

    ObservableList<ArchivierteKunden> getArchivierteKunden() throws SQLException;

    ObservableList<ArchivierteKunden> getGesperrteKunden() throws SQLException;
    List<Herkunft> getHerkunftStatistik(); // Rückgabetyp hier

    void resetCustomerStatus(ArchivierteKunden selectedCustomer) throws SQLException;

    void updateCustomer(ArchivierteKunden selectedCustomer)throws SQLException;
    void saveCustomer(ArchivierteKunden kunden) throws SQLException;

    boolean customerExists(String kdNr, String plz) throws SQLException;

	int getPlzIdByDescription(String plzText) throws SQLException;

    void saveHerkunftStatistik(String ort, String hausnummer, String plz, int anzahlHaushalte);
}
