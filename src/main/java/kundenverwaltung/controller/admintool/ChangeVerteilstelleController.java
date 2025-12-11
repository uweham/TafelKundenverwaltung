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
import kundenverwaltung.dao.PLZDAO;
import kundenverwaltung.dao.PLZDaoImpl;
import kundenverwaltung.dao.VerteilstelleDAO;
import kundenverwaltung.dao.VerteilstelleDAOimpl;
import kundenverwaltung.model.Verteilstelle;

public class ChangeVerteilstelleController implements Initializable
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

    private Boolean update = false;
    private VerteilstelleDAO verteilstelleDAO = new VerteilstelleDAOimpl();
    private Verteilstelle updateVerteilstelle;

    /**
     * Cancels the operation and closes the window.
     *
     * @param event the action event
     */
    @FXML
    void abbrechen(ActionEvent event)
    {
        VerteilstellenController.setVerteilstelleChange(null);
        Stage stage  = (Stage) inpBezeichnung.getScene().getWindow();
        stage.close();
    }
    /**
     * Saves the changes to the distribution center.
     *
     * @param event the action event
     */
    @FXML
    void speichern(ActionEvent event)
    {

        System.out.println(txtBezeichnung.getText());
        System.out.println(txtAdresse.getText());
        VerteilstellenController.setVerteilstelleChange(null);

        if (!inpBezeichnung.getText().equals("") && !inpAdresse.getText().equals(""))
        {
            updateVerteilstelle.setBezeichnung(inpBezeichnung.getText());
            updateVerteilstelle.setAdresse(inpAdresse.getText());
            verteilstelleDAO.update(updateVerteilstelle);

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
		PLZDAO plzdao = new PLZDaoImpl();
        @SuppressWarnings("unused")
		ArrayList<Verteilstelle> verteilstelleArrayList = (ArrayList<Verteilstelle>) verteilstelleDAO.readAll();
        updateVerteilstelle = VerteilstellenController.getVerteilstelleChange();
        inpAdresse.setText(updateVerteilstelle.getAdresse());
        inpBezeichnung.setText(updateVerteilstelle.getBezeichnung());
        //plzdao.r
    }
    /**
     * Gets the update status.
     *
     * @return the update status
     */
    public Boolean getUpdate()
    {
      return update;
  }
    /**
     * Sets the update status.
     *
     * @param update the update status to set
     */
  public void setUpdate(Boolean update)
  {
      this.update = update;
  }
}
