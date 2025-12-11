package kundenverwaltung.controller.admintool;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import kundenverwaltung.dao.PLZDAO;
import kundenverwaltung.dao.PLZDaoImpl;
import kundenverwaltung.model.PLZ;

public class OrtsteileController implements Initializable
{

    @FXML
    private TextField inpPlz;


    @FXML
    private TextField inpOrt;

    @FXML
    private Button btnOrtNeu;

    @FXML
    private Button btnOrtAendern;

    @FXML
    private ComboBox<?> comboOrtsteil;

    @FXML
    private ListView<?> listOrtsteile;

    @FXML
    private Button btnOrtsteilNeu;

    @FXML
    private Button btnOrtsteilAendern;

    @FXML
    private Button btnOrtsteilLoeschen;

    @FXML
    private Text txtOrt;

    @FXML
    void neuerOrthinzu(ActionEvent event)
    {

    /*    PLZ plz = new PLZ(InpPlz.getText(),InpOrt.getText());
        PLZDAO plzdao = new PLZDaoImpl();
        plzdao.create(plz);*/

    }

    @FXML
    void ortAendern(ActionEvent event)
    {

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
        PLZ plz = new PLZ("49835", "Wietmarschen");
        PLZDAO plzdao = new PLZDaoImpl();
        plzdao.create(plz);
    }
}
