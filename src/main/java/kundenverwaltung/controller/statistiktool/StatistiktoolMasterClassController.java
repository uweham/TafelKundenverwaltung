package kundenverwaltung.controller.statistiktool;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kundenverwaltung.controller.statistiktool.StatistiktoolResultViewController;
import kundenverwaltung.dao.StatistiktoolDAO;
import kundenverwaltung.dao.StatistiktoolDAOimpl;
import kundenverwaltung.dao.VerteilstelleDAO;
import kundenverwaltung.dao.VerteilstelleDAOimpl;
import kundenverwaltung.model.User;
import kundenverwaltung.model.Verteilstelle;
import kundenverwaltung.service.Constants;
import kundenverwaltung.service.ShowAlert;
import kundenverwaltung.service.UtilErrorLog;

public class StatistiktoolMasterClassController<T> {

  public UtilErrorLog utilerrorlog = new UtilErrorLog();
  
  public ShowAlert showalert=new ShowAlert();
  public  StatistiktoolDAO statistikDAO = new StatistiktoolDAOimpl();
  @FXML

  public ComboBox<Verteilstelle> verteilstelleComboBox;

  @FXML
  public VBox childResultContainer;
  @FXML
  public HBox childHeaderContainer;
  
  @FXML
  public MenuItem handleExit;
  
  public User user;
  
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
    System.out.println("Set User "+user.toString());
    if (user != null)
    {
      childController.updateUI(user.getUserName());
    }     
  }

  private StatistiktoolHeaderController childController;
  
  /**
   * 
   *
   */
  
  public void loadheader(String headertext,String description)
  {
    try {
        // Load the child header FXML
        FXMLLoader childLoader = new FXMLLoader(
            getClass().getResource("/kundenverwaltung/fxml/statistiktool/StatistiktoolHeader.fxml")
        );

        // Load the child FXML into the container
        HBox childPane = childLoader.load();

        // Get the child controller
        @SuppressWarnings("rawtypes")
        StatistiktoolHeaderController childController = childLoader.getController();
        // Set the parent controller as the callController
        childController.setHeaderDescriptionText(description);
        childController.setHeaderText(headertext);
        this.childController=childController;
        // Add the child pane to the container
        childHeaderContainer.getChildren().add(childPane);
        
      } catch (Exception e) {
        e.printStackTrace();
      }
  }
  
  @SuppressWarnings("unchecked")
  public void loadresultview(T ocontroller,double prefwidth, double prefheight)
  {
      
      try {
        // Load the child result FXML
        FXMLLoader childLoader = new FXMLLoader(
            getClass().getResource("/kundenverwaltung/fxml/statistiktool/StatistiktoolResultView.fxml")
        );

        // Load the child FXML into the container
        VBox childPane = childLoader.load();
        System.out.println("loadresultview prefwidth: "+prefwidth);
        System.out.println("loadresultview prefheight: "+prefheight);

        if (prefwidth>0) 
        {
          childPane.setPrefWidth(prefwidth);
          
        }
        if (prefheight>0)
        {
          childPane.setPrefHeight(prefheight);
        }
        // Get the child controller
        @SuppressWarnings("rawtypes")
        StatistiktoolResultViewController childController = childLoader.getController();

        // Set the parent controller as the callController
        childController.setCallController(ocontroller);
        childController.setStatistikDAO(statistikDAO);
        childController.setPrefWidth(prefwidth);
        childController.setPrefHeight(prefheight);
        
        // Add the child pane to the container
        childResultContainer.getChildren().add(childPane);

    } catch (Exception e) {
        e.printStackTrace();
    }
      
  }
  @FXML
  private void handleExit()
  {
      // Aktuelles Fenster abrufen und schließen
      Stage stage = (Stage) childHeaderContainer.getScene().getWindow(); // Hole das aktuelle Fenster über eine UI-Komponente
      stage.close();
  }
  
  public String getCurrentSQLQuery()
  {
      String query = "";
      if (query.isEmpty())
      {
        throw new IllegalStateException("no implement getCurrentSQLQuery()");
      }
      return query.toString();
  }
  
  public int getSelectedVerteilstelle()
  {
    if (verteilstelleComboBox.getSelectionModel().getSelectedItem()==null)
    {
      System.out.println("Combo Verteilstelle=null");
      return Constants.ALL_DISTRIBUTION_POINTS;
    }
    int verteilstellenId=verteilstelleComboBox.getSelectionModel().getSelectedItem().getId();
    
    System.out.printf("V-Id : %d Name : %s\n",verteilstellenId,verteilstelleComboBox.getSelectionModel().getSelectedItem().getBezeichnung());
    return verteilstellenId;
    
  }
}
