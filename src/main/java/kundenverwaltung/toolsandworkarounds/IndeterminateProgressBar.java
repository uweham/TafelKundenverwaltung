package kundenverwaltung.toolsandworkarounds;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import kundenverwaltung.logger.event.GlobalEventLogger;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * IndeterminateProgressBar.
 * @Author Adam Starobrzanski
 * @Date 20.09.2018
 * Last Change:
 * @Author Adam Starobrzanski
 * @Date 21.09.2018
 * @Author Adam Starobrzanski
 * @Date 22.09.2018
 */

public class IndeterminateProgressBar extends Application
{

    /**
     * This method starts a indeterminate progress bar on a diffrent thread.
     * @param stage an empty stage
     */

    @Override public void start(Stage stage)
    {

        VBox mainPane = new VBox();
        mainPane.setPadding(new Insets(10));
        mainPane.setSpacing(10.0d);

        Scene mainScene = new Scene(mainPane);
        GlobalEventLogger.attachTo("IndeterminateProgressBar_Main", mainScene);
        stage.setScene(mainScene);

        ProgressIndicator progressBar = new ProgressIndicator();
        final Label label = new Label("Bitte warten...");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(Font.font(16));
        FlowPane root = new FlowPane();
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.getChildren().addAll(progressBar, label);

        Scene scene = new Scene(root, 200, 100);
        GlobalEventLogger.attachTo("IndeterminateProgressBar_Root", scene);
        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Prozess läuft..");
        stage.centerOnScreen();
        stage.setOpacity(0.8);
        stage.show();

    }

}
