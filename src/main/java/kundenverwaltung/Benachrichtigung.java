package kundenverwaltung;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import org.controlsfx.control.Notifications;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.util.Duration;
import kundenverwaltung.model.Verteilstelle;

public final class Benachrichtigung
{
    private static final String TITEL_DELETE_DISTRIBUTION_POINT = "Verteilstelle löschen";
    private static final String[] HEADER_TEXT_DELETE_DISTRIBUTION_POINT = {"Alle Kunden der Verteilstelle ",
            "\nwerden einer anderen Verteilstelle hinzugefügt."};
    private static final String CONTENT_TEXT_DELETE_DISTRIBUTION_POINT = "Kunden zur folgenden Verteilstelle hinzufügen: ";

     private Benachrichtigung()
     {
     }

    private static Notifications noteficationBuilder;

    public static void infoBenachrichtigung(String titel, String text)
    {
        noteficationBuilder = Notifications.create()
                .title(titel)
                .text(text)

                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(new EventHandler<ActionEvent>()
                {
                    @Override
                    public void handle(ActionEvent event)
                    {
                        System.out.println("Klick auf die Notifiaction");
                    }
                });
        noteficationBuilder.showInformation();
    }

    public static void warnungBenachrichtigung(String titel, String text)
    {
        noteficationBuilder = Notifications.create()
                .title(titel)
                .text(text)

                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(new EventHandler<ActionEvent>()
                {
                    @Override
                    public void handle(ActionEvent event)
                    {
                        System.out.println("Klick auf die Notifiaction");
                    }
                });
        noteficationBuilder.showWarning();
    }

    public static boolean deleteConfirmationDialog(String headerText, String displayInfo)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sind Sie sicher?");
        alert.setHeaderText(headerText);
        alert.setContentText(displayInfo);

        ButtonType okButton = new ButtonType("Löschen", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(okButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == okButton;
    }

    public static boolean deleteConfirmationDialog(String headerText, String btnOKText, String btnCancelText, String displayInfo)
    {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Sind Sie sicher?");
        dialog.setHeaderText(headerText);
        dialog.setContentText("");

        @SuppressWarnings("unused")
        ButtonType buttonTypeOne = new ButtonType(btnOKText, ButtonBar.ButtonData.YES);

        @SuppressWarnings("unused")
        ButtonType buttonTypeTwo = new ButtonType(btnCancelText, ButtonBar.ButtonData.CANCEL_CLOSE);

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent())
        {
            if (Objects.equals(result.get().toLowerCase().trim(), "löschen"))
            {
                return true;
            }

            warnungBenachrichtigung("Fehlschlag", "Wenn Sie " + displayInfo + " entgültig löschen möchten, geben Sie bitte das Wort 'LÖSCHEN' in das Textfeld ein");
        }

        return false;
    }

    @SuppressWarnings("rawtypes")
    public static Verteilstelle deleteDistributionPoint(ArrayList distributionPointsArrayList, String nameFromDeletedDistributionPoint)
    {
        @SuppressWarnings("unchecked")
        ChoiceDialog<Verteilstelle> choiceDialog = new ChoiceDialog<>(distributionPointsArrayList.get(0), distributionPointsArrayList);
        choiceDialog.setTitle(TITEL_DELETE_DISTRIBUTION_POINT);
        choiceDialog.setHeaderText(HEADER_TEXT_DELETE_DISTRIBUTION_POINT[0] + nameFromDeletedDistributionPoint + HEADER_TEXT_DELETE_DISTRIBUTION_POINT[1]);
        choiceDialog.setContentText(CONTENT_TEXT_DELETE_DISTRIBUTION_POINT);

        Optional<Verteilstelle> result = choiceDialog.showAndWait();

        if (result.isPresent())
        {
            return result.get();
        }

        return null;
    }


  public static Boolean confirmDelete(String titel, String headerText, String text)
  {
     Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
     alert.setTitle(titel);
     alert.setHeaderText(headerText);
     alert.setContentText(text);

     Optional<ButtonType> result = alert.showAndWait();

     return result.get() == ButtonType.OK;
  }

  public static void errorDialog(String titel, String headerText, String contentText)
  {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(titel);
      alert.setHeaderText(headerText);
      alert.setContentText(contentText);
      alert.showAndWait();
  }
}
