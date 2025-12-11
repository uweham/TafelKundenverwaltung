package kundenverwaltung.logger.event;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import kundenverwaltung.logger.core.DefaultInteractionLogger;
import java.util.Arrays;

public class GlobalEventLogger
{
    private static DefaultInteractionLogger interactionLogger = new DefaultInteractionLogger();

    private static final String INTERACTION_MESSAGE_BUTTON = "Benutzer interagierte mit einem Button auf Fenster {}";
    private static final String INTERACTION_MESSAGE_TEXT_FIELD = "Benutzer eingabe in ein Textfield auf Fenster {}";
    private static final String INTERACTION_MESSAGE_TEXT_AREA = "Benutzer eingabe in ein TextArea auf Fenster {}";
    private static final String INTERACTION_MESSAGE_CHECKBOX = "Benutzer klickte eine Checkbox auf Fenster {}";
    private static final String INTERACTION_MESSAGE_RADIOBUTTON = "Benutzer klickte einen RadioButton auf Fenster {}";
    private static final String INTERACTION_MESSAGE_COMBOBOX = "Benutzer wählte einen Wert in einer ComboBox auf Fenster {}";
    private static final String INTERACTION_MESSAGE_CHOICEBOX = "Benutzer wählte einen Wert in einer ChoiceBox auf Fenster {}";
    private static final String INTERACTION_MESSAGE_DATEPICKER = "Benutzer wählte ein Datum in einem DatePicker auf Fenster {}";

    private static final String[] FIELDS_TO_IGNORE = new String[]
            {
                "password"
            };

    /**
     * Attaches event listeners to a scene to track user interactions with various UI controls.
     *
     * @param sceneName The name of the scene to be used in log messages.
     * @param scene     The Scene object to attach the listeners to.
     */
    public static void attachTo(String sceneName, Scene scene)
    {
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
        {
            Node target = (Node) event.getTarget();

            // Buttons
            Button btn = findParentButton(target);
            if (btn != null)
            {
                interactionLogger.logInteraction(
                        INTERACTION_MESSAGE_BUTTON,
                        sceneName,
                        btn.getId(),
                        btn.getText()
                );
                return;
            }

            // CheckBox
            if (target instanceof CheckBox)
            {
                CheckBox cb = (CheckBox) target;
                interactionLogger.logInteraction(
                        INTERACTION_MESSAGE_CHECKBOX,
                        sceneName,
                        cb.getId(),
                        Boolean.toString(cb.isSelected())
                );
                return;
            }

            // RadioButton
            if (target instanceof RadioButton)
            {
                RadioButton rb = (RadioButton) target;
                interactionLogger.logInteraction(
                        INTERACTION_MESSAGE_RADIOBUTTON,
                        sceneName,
                        rb.getId(),
                        Boolean.toString(rb.isSelected())
                );
                return;
            }

            // ChoiceBox
            if (target instanceof ChoiceBox)
            {
                ChoiceBox<?> choiceBox = (ChoiceBox<?>) target;
                Object selected = choiceBox.getValue();
                interactionLogger.logInteraction(
                        INTERACTION_MESSAGE_CHOICEBOX,
                        sceneName,
                        choiceBox.getId(),
                        selected != null ? selected.toString() : "null"
                );
                return;
            }

            // ComboBox
            if (target instanceof ComboBox)
            {
                ComboBox<?> comboBox = (ComboBox<?>) target;
                Object selected = comboBox.getValue();
                interactionLogger.logInteraction(
                        INTERACTION_MESSAGE_COMBOBOX,
                        sceneName,
                        comboBox.getId(),
                        selected != null ? selected.toString() : "null"
                );
            }
        });

        scene.addEventFilter(KeyEvent.KEY_TYPED, event ->
        {
            Node target = (Node) event.getTarget();

            // TextField
            if (target instanceof TextField)
            {
                TextField textField = (TextField) target;
                boolean shouldIgnore = Arrays.stream(FIELDS_TO_IGNORE)
                        .anyMatch(ignore -> textField.getId() != null
                                && textField.getId().toLowerCase().contains(ignore.toLowerCase()));

                if (!shouldIgnore)
                {
                    interactionLogger.logInteraction(
                            INTERACTION_MESSAGE_TEXT_FIELD,
                            sceneName,
                            textField.getId(),
                            textField.getText()
                    );
                }
            }

            // TextArea
            if (target instanceof TextArea)
            {
                TextArea textArea = (TextArea) target;
                interactionLogger.logInteraction(
                        INTERACTION_MESSAGE_TEXT_AREA,
                        sceneName,
                        textArea.getId(),
                        textArea.getText()
                );
            }
        });

        scene.getRoot().lookupAll(".date-picker").forEach(node ->
        {
            if (node instanceof DatePicker)
            {
                DatePicker datePicker = (DatePicker) node;
                datePicker.valueProperty().addListener((obs, oldVal, newVal) ->
                        interactionLogger.logInteraction(
                                INTERACTION_MESSAGE_DATEPICKER,
                                sceneName,
                                datePicker.getId(),
                                newVal != null ? newVal.toString() : "null"
                        )
                );
            }
        });
    }

    /**
     * Finds the parent Button of a given Node in the scene graph.
     * This is useful because a click event might originate from a child Node within the Button, like a label or graphic.
     *
     * @param node The starting Node from which to search upwards.
     * @return The parent Button if found, otherwise null.
     */
    private static Button findParentButton(Node node)
    {
        while (node != null && !(node instanceof Button))
        {
            node = node.getParent();
        }

        return (node != null) ? (Button) node : null;
    }
}