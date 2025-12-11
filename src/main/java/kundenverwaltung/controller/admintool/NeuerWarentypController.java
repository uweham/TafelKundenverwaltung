package kundenverwaltung.controller.admintool;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kundenverwaltung.controller.MainController;
import kundenverwaltung.dao.WarentypDAO;
import kundenverwaltung.dao.WarentypDAOimpl;
import kundenverwaltung.model.Warentyp;

public class NeuerWarentypController implements Initializable
{
    @FXML
    private Button btnSpeichern;

    @FXML
    private TextField inpName;

    @FXML
    private Label lblName;

    @FXML
    private Button btnAbbrechen;

    private WarentypDAO warentypDAO = new WarentypDAOimpl();
    private Warentyp updatewarentyp;

    @FXML
    void abbrechen(ActionEvent event)
    {

    }
    /**
     * Handles the action of saving the Warentyp.
     *
     * @param event The action event.
     */
    @FXML
    void speichern(ActionEvent event)
    {
        if (updatewarentyp == null)
        {
            @SuppressWarnings("unused")
			Warentyp warentyp = new Warentyp(inpName.getText(), 0, 0, false);
            //warentypDAO.create(warentyp);
        }


        else
        {
            updatewarentyp.setName(inpName.getText());
            warentypDAO.update(updatewarentyp);
        }
        try
        {
            MainController.getInstance().getAdmintoolAnlegenController().openWarentypen(event);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        updatewarentyp = null;
        Stage stage  = (Stage) inpName.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }
    /**
     * Updates the Warentyp information in the form.
     *
     * @param warentyp The Warentyp to be updated.
     */
    public void warentypUpdate(Warentyp warentyp)
    {
        updatewarentyp = warentyp;
        inpName.setText(warentyp.getName());
    }

}
