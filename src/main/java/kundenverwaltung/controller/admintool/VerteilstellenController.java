package kundenverwaltung.controller.admintool;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.controller.MainController;
import kundenverwaltung.dao.EinstellungenDAO;
import kundenverwaltung.dao.EinstellungenDAOimpl;
import kundenverwaltung.dao.HaushaltDAOimpl;
import kundenverwaltung.dao.VerteilstelleDAO;
import kundenverwaltung.dao.VerteilstelleDAOimpl;
import kundenverwaltung.model.Einstellungen;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.Verteilstelle;
import kundenverwaltung.service.TablePreferenceServiceImpl;

/**
 * This class show all distribution points in a tableview and allows distribution points to add, update or delete.
 *
 * @author Group_1
 * @author Richard Kromm
 * Last change:
 * 		By: Richard Kromm
 * 		On: 02.08.2018
 */
public class VerteilstellenController implements Initializable
{
    @FXML
    private Button btnChange;
    @FXML
    private Button btnNew;
    @FXML
    private CheckBox chkCustomer;
    @FXML
    private Button btnSave;
    @FXML
    private ComboBox<Verteilstelle> cbVerteil;
    @FXML
    private TableView<Verteilstelle> tabelleVerteilstellen;
    @SuppressWarnings("rawtypes")
	@FXML
    private TableColumn colReihenfolge;
    @SuppressWarnings("rawtypes")
	@FXML
    private TableColumn colBezeichnung;
    @SuppressWarnings("rawtypes")
	@FXML
    private TableColumn colAdresse;

    private VerteilstelleDAO verteilstelleDAO = new VerteilstelleDAOimpl();
    private ArrayList<Verteilstelle> verteilstellenliste = new ArrayList<>();
    private EinstellungenDAO einstellungenDAO = new EinstellungenDAOimpl();
    private Einstellungen einstellungen;
    private static Verteilstelle verteilstelleChange;

    /**
     * Initializes the controller class.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
	@SuppressWarnings("unchecked")
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        verteilstellenliste = (ArrayList<Verteilstelle>) verteilstelleDAO.readAll();

        @SuppressWarnings("rawtypes")
        ObservableList obsverteilstellen = FXCollections.observableArrayList();
        for (Verteilstelle element : verteilstellenliste)
        {
            obsverteilstellen.add(element);
        }

        cbVerteil.setItems(obsverteilstellen);
        cbVerteil.getSelectionModel().select(0);

        // Tabelle
        colBezeichnung.setCellValueFactory(new PropertyValueFactory<>("Bezeichnung"));
        colBezeichnung.setId("Bezeichnung");

        colAdresse.setCellValueFactory(new PropertyValueFactory<>("Adresse"));
        colAdresse.setId("Adresse");

        colReihenfolge.setCellValueFactory(new PropertyValueFactory<>("Listennummer"));
        colReihenfolge.setId("Listennummer");

        tabelleVerteilstellen.setItems(obsverteilstellen);

        // Einstellung Zugehörigkeit vom Kunden Prüfen
        einstellungen = einstellungenDAO.read();
        chkCustomer.setSelected(einstellungen.isVerteilstellenzugehoerigkeit());

        TablePreferenceServiceImpl.getInstance().setupPersistence(tabelleVerteilstellen, "VerteilstellenTabelle");
    }

    /**
     * Saves the settings.
     *
     * @param event The action event.
     */
    @FXML
    void einstellungSpeichern(ActionEvent event)
    {

        einstellungen.setVerteilstellenzugehoerigkeit(chkCustomer.isSelected());
        einstellungenDAO.update(einstellungen);
        Benachrichtigung.infoBenachrichtigung("Speichern erfolgreich", "Die Verteilstellenzugehörigkeit wurde aktualsiert");
    }
    /**
     * Opens a window to create a new distribution point.
     *
     * @param event The action event.
     */
    @FXML
    void neueVerteilstelleErstellen(ActionEvent event)
    {
        MainController.getInstance().oeffneNeueVerteilstelle();

    }


    public static Verteilstelle getVerteilstelleChange()
    {
        return verteilstelleChange;
    }

    public static void setVerteilstelleChange(Verteilstelle verteilstelleChange)
    {
        VerteilstellenController.verteilstelleChange = verteilstelleChange;
    }
    /**
     * Opens a window to change the selected distribution point.
     *
     * @param event The action event.
     */
    @FXML
    void verteilstelleAendern(ActionEvent event)
    {
        verteilstelleChange = cbVerteil.getSelectionModel().getSelectedItem();
        if (verteilstelleChange != null)
        {
            MainController.getInstance().oeffneChangeVerteilstelle();
        }

        //MainController.getInstance().getNeueVerteilstelleController().UpdateVerteilstelle();
    }


    /**
     * This function delete a distribution point and transmit all customer from deleted distribution point to main distribution point.
     */
    public void deleteDistributionsPoint()
    {
    	HaushaltDAOimpl haushaltDAOimpl = new HaushaltDAOimpl();
        Verteilstelle deleteDistributionsPoint = cbVerteil.getSelectionModel().getSelectedItem();

        ArrayList<Verteilstelle> withoutDeletedDistributionPoint = new ArrayList<>();
        for (int runVar = 0; runVar < verteilstellenliste.size(); runVar++)
		{
			if (deleteDistributionsPoint.getVerteilstellenId() != verteilstellenliste.get(runVar).getVerteilstellenId())
			{
				withoutDeletedDistributionPoint.add(verteilstellenliste.get(runVar));
			}
		}
        Verteilstelle newDistributionPoint = Benachrichtigung.deleteDistributionPoint(withoutDeletedDistributionPoint, deleteDistributionsPoint.getBezeichnung());
        if (newDistributionPoint != null)
		{
			ArrayList<Haushalt> householdsFromDeletedDistributionPoint = haushaltDAOimpl.getHousehold(deleteDistributionsPoint.getVerteilstellenId());
			for (Haushalt element : householdsFromDeletedDistributionPoint)
			{
				Haushalt household = new Haushalt(element.getKundennummer(),
						element.getStrasse(),
						element.getHausnummer(),
						element.getPlz(),
						element.getTelefonnummer(),
						element.getMobilnummer(),
						element.getBemerkungen(),
						element.getKundeSeit(),
						element.getSaldo(),
						newDistributionPoint,
						element.getIstArchiviert(),
						element.getIstGesperrt(),
						element.getAusgabegruppe(),
						element.isBelieferung(),
						element.isDatenschutzerklaerung());

				haushaltDAOimpl.update(household);
			}
			verteilstelleDAO.delete(deleteDistributionsPoint);
			refreshTableView();
		}
    }

    /**
     * This function refresh the tableview and combobox.
     */
    public void refreshTableView()
    {
        verteilstellenliste = (ArrayList<Verteilstelle>) verteilstelleDAO.readAll();
        ObservableList<Verteilstelle> distributionsPointsObservableList = FXCollections.observableList(verteilstellenliste);
        cbVerteil.setItems(distributionsPointsObservableList);
        cbVerteil.getSelectionModel().selectFirst();
        tabelleVerteilstellen.setItems(distributionsPointsObservableList);
    }
}
