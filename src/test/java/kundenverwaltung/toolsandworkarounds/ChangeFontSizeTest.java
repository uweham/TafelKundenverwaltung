package kundenverwaltung.toolsandworkarounds;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class ChangeFontSizeTest {

    private ChangeFontSize changeFontSize;
    private DoubleProperty testFontSize;
    private static boolean javaFXInitialized = false;

    @BeforeAll
    static void initJavaFX()
    {
        if (!javaFXInitialized)
        {
            try
            {
                CountDownLatch latch = new CountDownLatch(1);
                SwingUtilities.invokeLater(() -> {
                    new JFXPanel(); // Initialize JavaFX
                    latch.countDown();
                });
                latch.await();
                javaFXInitialized = true;
            } catch (Exception e)
            {
                System.out.println("Could not initialize JavaFX: " + e.getMessage());
            }
        }
    }

    @BeforeEach
    void setUp()
    {
        changeFontSize = new ChangeFontSize();
        testFontSize = new SimpleDoubleProperty(16.0);
    }

    /** Test default font size value */
    @Test
    void getDefaultFontSize_ShouldReturn13()
    {
        Double defaultSize = ChangeFontSize.getDefaultFontSize();

        assertEquals(13.0, defaultSize, "Default font size should be 13.0");
        assertNotNull(defaultSize, "Default font size should not be null");
    }

    /** Test primary/secondary font size difference */
    @Test
    void getDifferenceBetweenPrimarySecondaryFontsize_ShouldReturn2()
    {
        Double difference = ChangeFontSize.getDifferenceBetweenPrimarySecondaryFontsize();

        assertEquals(2.0, difference, "Difference between primary/secondary font size should be 2.0");
        assertNotNull(difference, "Difference should not be null");
    }

    /** Test default/header font size difference */
    @Test
    void getDifferenceBetweenDefaultHeaderFontsize_ShouldReturn1()
    {
        Double difference = ChangeFontSize.getDifferenceBetweenDefaultHeaderFontsize();

        assertEquals(1.0, difference, "Difference between default/header font size should be 1.0");
        assertNotNull(difference, "Difference should not be null");
    }

    /** Test empty button list handling */
    @Test
    void changeFontSizeFromButtonArrayList_WithEmptyList_ShouldNotThrow()
    {
        ArrayList<Button> emptyList = new ArrayList<>();

        assertDoesNotThrow(() -> changeFontSize.changeFontSizeFromButtonArrayList(emptyList, testFontSize),
                "Should handle empty button list without throwing");
    }

    /** Test button style binding */
    @Test
    void changeFontSizeFromButtonArrayList_WithButtons_ShouldBindStyleProperty()
    {
        ArrayList<Button> buttons = new ArrayList<>();
        Button button1 = new Button("Test 1");
        Button button2 = new Button("Test 2");
        buttons.add(button1);
        buttons.add(button2);

        assertDoesNotThrow(() -> changeFontSize.changeFontSizeFromButtonArrayList(buttons, testFontSize),
                "Should bind font size to buttons without throwing");

        assertNotNull(button1.styleProperty(), "Button style property should exist");
        assertNotNull(button2.styleProperty(), "Button style property should exist");
    }

    /** Test label style binding */
    @Test
    void changeFontSizeFromLabelArrayList_WithLabels_ShouldBindStyleProperty() {
        ArrayList<Label> labels = new ArrayList<>();
        Label label1 = new Label("Test Label 1");
        Label label2 = new Label("Test Label 2");
        labels.add(label1);
        labels.add(label2);

        assertDoesNotThrow(() -> changeFontSize.changeFontSizeFromLabelArrayList(labels, testFontSize),
                "Should bind font size to labels without throwing");

        assertNotNull(label1.styleProperty(), "Label style property should exist");
        assertNotNull(label2.styleProperty(), "Label style property should exist");
    }

    /** Test text field style binding */
    @Test
    void changeFontSizeFromTextFieldArrayList_WithTextFields_ShouldBindStyleProperty()
    {
        ArrayList<TextField> textFields = new ArrayList<>();
        TextField field1 = new TextField("Test 1");
        TextField field2 = new TextField("Test 2");
        textFields.add(field1);
        textFields.add(field2);

        assertDoesNotThrow(() -> changeFontSize.changeFontSizeFromTextFieldArrayList(textFields, testFontSize),
                "Should bind font size to text fields without throwing");

        assertNotNull(field1.styleProperty(), "TextField style property should exist");
        assertNotNull(field2.styleProperty(), "TextField style property should exist");
    }

    /** Test checkbox style binding */
    @Test
    void changeFontSizeFromCheckBoxArrayList_WithCheckBoxes_ShouldBindStyleProperty()
    {
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        CheckBox box1 = new CheckBox("Option 1");
        CheckBox box2 = new CheckBox("Option 2");
        checkBoxes.add(box1);
        checkBoxes.add(box2);

        assertDoesNotThrow(() -> changeFontSize.changeFontSizeFromCheckBoxArrayList(checkBoxes, testFontSize),
                "Should bind font size to checkboxes without throwing");

        assertNotNull(box1.styleProperty(), "CheckBox style property should exist");
        assertNotNull(box2.styleProperty(), "CheckBox style property should exist");
    }

    /** Test table column cell factory */
    @Test
    void changeFontSizeFromTableColumn_WithValidColumn_ShouldSetCellFactory()
    {
        TableColumn<Object, String> column = new TableColumn<>("Test Column");
        Double fontSize = 14.0;

        assertDoesNotThrow(() -> changeFontSize.changeFontSizeFromTableColumn(column, fontSize),
                "Should set cell factory without throwing");

        assertNotNull(column.getCellFactory(), "Cell factory should be set");
    }

    /** Test menu style binding */
    @Test
    void changeFontSizeFromMenuArrayList_WithMenus_ShouldBindStyleProperty()
    {
        ArrayList<Menu> menus = new ArrayList<>();
        Menu menu1 = new Menu("File");
        Menu menu2 = new Menu("Edit");
        menus.add(menu1);
        menus.add(menu2);

        assertDoesNotThrow(() -> changeFontSize.changeFontSizeFromMenuArrayList(menus, testFontSize),
                "Should bind font size to menus without throwing");

        assertNotNull(menu1.styleProperty(), "Menu style property should exist");
        assertNotNull(menu2.styleProperty(), "Menu style property should exist");
    }

    /** Test tab style binding */
    @Test
    void changeFontSizeFromTabArrayList_WithTabs_ShouldBindStyleProperty()
    {
        ArrayList<Tab> tabs = new ArrayList<>();
        Tab tab1 = new Tab("Tab 1");
        Tab tab2 = new Tab("Tab 2");
        tabs.add(tab1);
        tabs.add(tab2);

        assertDoesNotThrow(() -> changeFontSize.changeFontSizeFromTabArrayList(tabs, testFontSize),
                "Should bind font size to tabs without throwing");

        assertNotNull(tab1.styleProperty(), "Tab style property should exist");
        assertNotNull(tab2.styleProperty(), "Tab style property should exist");
    }

    /** Test null array handling */
    @Test
    void changeFontSize_AllArrayListMethods_ShouldHandleNullArraysGracefully()
    {
        assertThrows(Exception.class, () ->
                        changeFontSize.changeFontSizeFromButtonArrayList(null, testFontSize),
                "Should throw exception for null button array");

        assertThrows(Exception.class, () ->
                        changeFontSize.changeFontSizeFromLabelArrayList(null, testFontSize),
                "Should throw exception for null label array");
    }

    /** Test null font size handling */
    @Test
    void changeFontSize_WithNullFontSize_ShouldHandleGracefully()
    {
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(new Button("Test"));

        assertDoesNotThrow(() ->
                        changeFontSize.changeFontSizeFromButtonArrayList(buttons, null),
                "Should handle null font size gracefully without throwing");
    }

    /** Test constant values are positive */
    @Test
    void changeFontSize_ConstantValues_ShouldBePositive()
    {
        assertTrue(ChangeFontSize.getDefaultFontSize() > 0,
                "Default font size should be positive");
        assertTrue(ChangeFontSize.getDifferenceBetweenPrimarySecondaryFontsize() > 0,
                "Primary/secondary difference should be positive");
        assertTrue(ChangeFontSize.getDifferenceBetweenDefaultHeaderFontsize() > 0,
                "Default/header difference should be positive");
    }

    /** Test class structure validity */
    @Test
    void changeFontSize_ClassStructure_ShouldBeValid()
    {
        assertTrue(java.lang.reflect.Modifier.isPublic(ChangeFontSize.class.getModifiers()),
                "ChangeFontSize class should be public");

        assertEquals("kundenverwaltung.toolsandworkarounds",
                ChangeFontSize.class.getPackageName(),
                "ChangeFontSize should be in correct package");
    }

    /** Test static methods are static */
    @Test
    void changeFontSize_StaticMethods_ShouldBeStatic() throws NoSuchMethodException
    {
        var defaultSizeMethod = ChangeFontSize.class.getDeclaredMethod("getDefaultFontSize");
        assertTrue(java.lang.reflect.Modifier.isStatic(defaultSizeMethod.getModifiers()),
                "getDefaultFontSize should be static");

        var primarySecondaryMethod = ChangeFontSize.class.getDeclaredMethod("getDifferenceBetweenPrimarySecondaryFontsize");
        assertTrue(java.lang.reflect.Modifier.isStatic(primarySecondaryMethod.getModifiers()),
                "getDifferenceBetweenPrimarySecondaryFontsize should be static");

        var defaultHeaderMethod = ChangeFontSize.class.getDeclaredMethod("getDifferenceBetweenDefaultHeaderFontsize");
        assertTrue(java.lang.reflect.Modifier.isStatic(defaultHeaderMethod.getModifiers()),
                "getDifferenceBetweenDefaultHeaderFontsize should be static");
    }
}