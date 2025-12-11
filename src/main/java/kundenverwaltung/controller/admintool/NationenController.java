package kundenverwaltung.controller.admintool;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import kundenverwaltung.controller.MainController;
import kundenverwaltung.dao.NationDAO;
import kundenverwaltung.dao.NationDAOimpl;
import kundenverwaltung.model.Nation;


public class NationenController implements Initializable
{
    private static final int INVALID_NATION_ID = -1;


    @FXML
    private ListView<Nation> listDeaktiviert;
    @FXML
    private ListView<Nation> listAktiviert;
    @FXML
    private Button btnAuswaehlen;
    @FXML
    private Button btnAbwaehlen;


    private NationDAO nationDAO = new NationDAOimpl();
    private ArrayList<Nation> nationenliste;
    private ObservableList<Nation> obsNationenaktiv = FXCollections.observableArrayList();
    private ObservableList<Nation> obsNationendeaktiv = FXCollections.observableArrayList();

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
        nationenliste = nationDAO.getAllNationen();

        if (nationenliste.size() < 1)
        {
            nationenEinfuegen();
        }

        for (Nation element : nationenliste)
        {
            if (element.getNationId() != INVALID_NATION_ID)
            {
                if (element.getAktiv())
                {
                    obsNationenaktiv.add(element);
                } else
                {
                    obsNationendeaktiv.add(element);
                }
            }
        }
        listAktiviert.setItems(obsNationenaktiv);
        listDeaktiviert.setItems(obsNationendeaktiv);
    }



    private void nationenEinfuegen()
    {
        Nation nation = new Nation("Deutschland", "deutsch", true);
        Nation nation2 = new Nation("England", "englisch", true);
        nationDAO.create(nation);
        nationDAO.create(nation2);

        try
        {
            MainController.getInstance().getAdmintoolAnlegenController().openNationen();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Handles the action of deselecting a nation.
     *
     * @param event The action event.
     */
    @SuppressWarnings("unchecked")
	@FXML
    void nationAbwaehlen(ActionEvent event)
    {
        Nation n = (Nation) listAktiviert.getSelectionModel().getSelectedItem();
        if (n != null)
        {
            n.setAktiv(false);
            nationDAO.update(n);
            obsNationenaktiv.remove(n);
            obsNationendeaktiv.add(n);
        }
    }
    /**
     * Handles the action of selecting a nation.
     *
     * @param event The action event.
     */
    @SuppressWarnings("unchecked")
	@FXML
    void nationAuswaehlen(ActionEvent event)
    {
        Nation n = (Nation) listDeaktiviert.getSelectionModel().getSelectedItem();
        if (n != null)
        {
            n.setAktiv(true);
            nationDAO.update(n);
            obsNationendeaktiv.remove(n);
            obsNationenaktiv.add(n);
        }
    }

}
