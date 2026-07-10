package kundenverwaltung.controller.statistiktool;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatistiktoolHeaderController {
 
  private String saveText;
  
  @FXML
  private Label headerLabel;

  @FXML
  private Label headerdescription;
  
  public void setHeaderText(String text) {
    saveText=text;
    headerLabel.setText(text);
  } 
  
  public void updateUI(String addText)
  {
    headerLabel.setText(saveText+addText);
  }
  
  public void setHeaderDescriptionText(String text) {
    headerdescription.setText(text);
}

}
