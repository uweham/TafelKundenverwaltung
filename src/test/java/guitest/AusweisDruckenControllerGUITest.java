package guitest;

import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import kundenverwaltung.logger.event.GlobalEventLogger;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.ComboBoxMatchers;
import org.testfx.matcher.control.LabeledMatchers;

import java.util.concurrent.CountDownLatch;

import static org.testfx.api.FxAssert.verifyThat;

public class AusweisDruckenControllerGUITest extends ApplicationTest {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Kundenverwaltung/FXML/AusweisDrucken.fxml"));

        Scene scene = new Scene(root);
        GlobalEventLogger.attachTo("AusweisDrucken", scene);
        stage.setScene(scene);

        stage.show();
        primaryStage = stage;
    }

    @Test
    @Tag("gui")
    public void should_contain_label_with_text() {
        verifyThat("#labelTemplate", LabeledMatchers.hasText("Vorlage:"));
    }

    @Test
    @Tag("gui")
    public void should_contain_checkbox_gesperrteKunden() {
        verifyThat("#cbxGesperrteKunden", (CheckBox cb) -> !cb.isSelected() && cb.isDisabled());
    }


    @Test
    @Tag("gui")
    public void should_contain_radiobutton_alleKunden() {
        verifyThat("#rbAlleKunden", (RadioButton rb) -> rb.getText().equals("Alle Kunden folgender Verteilstelle"));
    }

    @Test
    @Tag("gui")
    public void should_contain_combobox_verteilstelle() {
        verifyThat("#cbVerteilstelle", ComboBoxMatchers.hasItems(7));
    }

    @Test
    @Tag("gui")
    public void should_contain_combobox_vorlage() {
        verifyThat("#cbVorlage", ComboBoxMatchers.hasItems(1));
    }

    @Test
    @Tag("gui")
    public void should_contain_label_verteilstelle() {
        verifyThat("#lbVerteilstelle", LabeledMatchers.hasText("Verteilstelle:"));
    }

    @Test
    @Tag("gui")
    public void should_contain_combobox_warentyp() {
        verifyThat("#cbWarentyp", (ComboBox<?> cb) -> cb != null);
    }


    @Test
    @Tag("gui")
    public void should_contain_checkbox_archivierteKunden() {
        verifyThat("#cbxArchivierteKunden", (CheckBox cb) -> !cb.isSelected() && cb.isDisabled());
    }

    @Test
    @Tag("gui")
    public void should_contain_button_create() {
        verifyThat("#buttonCreate", LabeledMatchers.hasText("Erstellen"));
    }

    @Test
    @Tag("gui")
    public void should_contain_checkbox_automatischEinstellen() {
        verifyThat("#cbxAutomatischEinstellen", (CheckBox cb) -> !cb.isSelected() && cb.isDisabled());
    }

    @Test
    @Tag("gui")
    public void should_contain_checkbox_einkaufsberechtigte() {
        verifyThat("#cbxEinkaufsberechtigte", (CheckBox cb) -> !cb.isSelected() && cb.isDisabled());
    }

    @Test
    @Tag("gui")
    public void should_contain_button_cancel() {
        verifyThat("#buttonCancel", LabeledMatchers.hasText("Abbrechen"));
    }


    private boolean isProgressBarVisible() {
        CountDownLatch latch = new CountDownLatch(1);
        final boolean[] isVisible = {false};

        Platform.runLater(() -> {
            try {
                Stage progressBarStage = findProgressBarStage();
                isVisible[0] = progressBarStage != null && progressBarStage.isShowing();
            } finally {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return isVisible[0];
    }


    private Stage findProgressBarStage() {
        for (Window window : Window.getWindows()) {
            if (window instanceof Stage) {
                Stage stage = (Stage) window;
                if (stage.getTitle().equals("Prozess läuft..")) {
                    return stage;
                }
            }
        }
        return null;
    }

}
