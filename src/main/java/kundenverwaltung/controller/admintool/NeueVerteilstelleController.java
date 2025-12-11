package kundenverwaltung.controller.admintool;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kundenverwaltung.controller.MainController;
import kundenverwaltung.dao.PLZDaoImpl;
import kundenverwaltung.dao.VerteilstelleDAO;
import kundenverwaltung.dao.VerteilstelleDAOimpl;
import kundenverwaltung.model.Verteilstelle;

public class NeueVerteilstelleController implements Initializable
{
    @FXML
    private TextField inpBezeichnung;

    @FXML
    private Text txtBezeichnung;

    @FXML
    private Text txtAdresse;

    @FXML
    private TextField inpAdresse;

    @FXML
    private Button btnSpeichern;

    @FXML
    private Button btnAbbrechen;

    @SuppressWarnings("unused")
    private Boolean update = false;
    private VerteilstelleDAO verteilstelleDAO = new VerteilstelleDAOimpl();
    /**
     * Updates the fields for an existing Verteilstelle.
     */
    public void updateVerteilstelle()
    {
        update = true;
        inpAdresse.setText("Update");
        inpBezeichnung.setText("Update");
    }

    @FXML
    void abbrechen(ActionEvent event)
    {

    }


/**
 * Handles the action of saving the Verteilstelle.
 *
 * @param event The action event.
 */
    @FXML
    void speichern(ActionEvent event)
    {

        System.out.println(txtBezeichnung.getText());
        System.out.println(txtAdresse.getText());


            if (!inpBezeichnung.getText().equals("") && !inpAdresse.getText().equals(""))
            {
                @SuppressWarnings("unused")
				Verteilstelle verteilstelle = new Verteilstelle(inpBezeichnung.getText(), inpAdresse.getText(), verteilstelleDAO.readAll().size() + 1);

               // Verteilstelle verteilstelle = new Verteilstelle()

                try
                {
                    MainController.getInstance().getAdmintoolAnlegenController().openVerteilstellen(event);
                    Stage stage  = (Stage) inpBezeichnung.getScene().getWindow();
                    stage.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }

            }

    }

    /**
     * Initializes the controller class.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        @SuppressWarnings("unused")
		PLZDaoImpl plzdao = new PLZDaoImpl();
        @SuppressWarnings("unused")
		ArrayList<Verteilstelle> verteilstelleArrayList = (ArrayList<Verteilstelle>) verteilstelleDAO.readAll();

        //plzdao.r
    }
}
