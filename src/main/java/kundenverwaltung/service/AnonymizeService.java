package kundenverwaltung.service;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputControl;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnonymizeService
{
    private static final AnonymizeService ANONYMIZED_SERVICE = new AnonymizeService();
    private final HashMap<TextInputControl, String> anonymizedControls = new HashMap<>();
    private final List<TableView<?>> hiddenTables = new ArrayList<>();
    private final List<ListView<?>> hiddenListViews = new ArrayList<>();

    /**
     * Anonymizes or restores the content of all text input controls and hides/shows tables and lists in all open windows.
     *
     * @param anonymize If true, anonymizes the content. If false, restores the original content.
     */
    public void anonymizeAllOpenWindows(boolean anonymize)
    {
        List<Window> windowList = Window.getWindows();

        for (Window currentWindow : windowList)
        {
            List<Node> allControls = getAllNodes(currentWindow.getScene().getRoot());

            for (Node currentControl : allControls)
            {
                // TextInputControls anonymisieren
                if (currentControl instanceof TextInputControl)
                {
                    TextInputControl textInputControl = (TextInputControl) currentControl;

                    if (anonymize)
                    {
                        this.anonymizedControls.put(textInputControl, textInputControl.getText());

                        int length = textInputControl.getText().length();
                        StringBuilder maskedText = new StringBuilder();

                        for (int i = 0; i < length; i++)
                        {
                            maskedText.append("*");
                        }

                        textInputControl.setText(maskedText.toString());
                    }
                    else if (anonymizedControls.containsKey(textInputControl))
                    {
                        textInputControl.setText(anonymizedControls.get(textInputControl));
                    }
                }

                // TableViews verstecken/anzeigen
                if (currentControl instanceof TableView<?>)
                {
                    TableView<?> tableView = (TableView<?>) currentControl;

                    if (anonymize)
                    {
                        if (tableView.isVisible())
                        {
                            tableView.setVisible(false);
                            hiddenTables.add(tableView);
                        }
                    }
                    else
                    {
                        if (hiddenTables.contains(tableView))
                        {
                            tableView.setVisible(true);
                        }
                    }
                }

                // ListViews verstecken/anzeigen
                if (currentControl instanceof ListView<?>)
                {
                    ListView<?> listView = (ListView<?>) currentControl;

                    if (anonymize)
                    {
                        if (listView.isVisible())
                        {
                            listView.setVisible(false);
                            hiddenListViews.add(listView);
                        }
                    }
                    else
                    {
                        if (hiddenListViews.contains(listView))
                        {
                            listView.setVisible(true);
                        }
                    }
                }
            }
        }

        // Cleanup nach De-Anonymisierung
        if (!anonymize)
        {
            anonymizedControls.clear();
            hiddenTables.clear();
            hiddenListViews.clear();
        }
    }

    /**
     * Recursively traverses the scene graph to collect all nodes.
     *
     * @param root The root parent node from which to start the traversal.
     * @return A list of all nodes in the scene graph.
     */
    private List<Node> getAllNodes(Parent root)
    {
        List<Node> nodes = new ArrayList<>();

        for (Node node : root.getChildrenUnmodifiable())
        {
            nodes.add(node);
            if (node instanceof Parent)
            {
                nodes.addAll(getAllNodes((Parent) node));
            }
        }

        return nodes;
    }

    /**
     * Returns the singleton instance of the AnonymizeService.
     *
     * @return The AnonymizeService instance.
     */
    public static AnonymizeService getInstance()
    {
        return ANONYMIZED_SERVICE;
    }
}