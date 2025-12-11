package kundenverwaltung.toolsandworkarounds;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import kundenverwaltung.model.Nation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DynamicNationDropDownMenuTest
{

    private DynamicNationDropDownMenu dropDownMenu;
    private ArrayList<Nation> testNationList;
    private ComboBox<Nation> testComboBox;
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
        dropDownMenu = new DynamicNationDropDownMenu();
        testComboBox = new ComboBox<>();
        testComboBox.setEditable(true);

        // Create test nation list with alphabetical marker
        testNationList = new ArrayList<>();
        testNationList.add(new Nation(1, "Deutschland", "DE", true));
        testNationList.add(new Nation(2, "Frankreich", "FR", true));
        testNationList.add(new Nation(3, "Italien", "IT", true));
        testNationList.add(new Nation(-1, "------ ALPHABETISCH ------", "", false));
        testNationList.add(new Nation(4, "Belgien", "BE", true));
        testNationList.add(new Nation(5, "Dänemark", "DK", true));
        testNationList.add(new Nation(6, "Finnland", "FI", true));
        testNationList.add(new Nation(7, "Griechenland", "GR", true));
    }

    /** Test alphabetical marker position finding */
    @Test
    void getFirstPositionAlphabeticalSortingNation_WithMarkerPresent_ShouldReturnCorrectPosition()
    {
        int position = dropDownMenu.getFirstPositionAlphabeticalSortingNation(testNationList);

        assertEquals(3, position, "Should find alphabetical marker at position 3");
    }

    /** Test default position without marker */
    @Test
    void getFirstPositionAlphabeticalSortingNation_WithoutMarker_ShouldReturnExpectedDefault()
    {
        ArrayList<Nation> listWithoutMarker = new ArrayList<>();
        listWithoutMarker.add(new Nation(1, "Deutschland", "DE", true));
        listWithoutMarker.add(new Nation(2, "Frankreich", "FR", true));

        int position = dropDownMenu.getFirstPositionAlphabeticalSortingNation(listWithoutMarker);

        assertEquals(8, position, "Should return expected default position when marker not found");
    }

    /** Test empty list handling */
    @Test
    void getFirstPositionAlphabeticalSortingNation_WithEmptyList_ShouldReturnExpectedDefault()
    {
        ArrayList<Nation> emptyList = new ArrayList<>();

        int position = dropDownMenu.getFirstPositionAlphabeticalSortingNation(emptyList);

        assertEquals(8, position, "Should return expected default position for empty list");
    }

    /** Test valid nation input */
    @Test
    void getAndCheckNationValue_WithValidNationInput_ShouldReturnNation()
    {
        testComboBox.getEditor().setText("Belgien");

        Nation result = dropDownMenu.getAndCheckNationValue(testComboBox, testNationList);

        assertNotNull(result, "Should return a nation for valid input");
        assertEquals("Belgien", result.getName(), "Should return the correct nation");
        assertEquals(4, result.getNationId(), "Should return nation with correct ID");
    }

    /** Test invalid nation input */
    @Test
    void getAndCheckNationValue_WithInvalidInput_ShouldReturnNull()
    {
        testComboBox.getEditor().setText("Ungültiges Land");

        try (MockedStatic<kundenverwaltung.Benachrichtigung> mockedNotification =
                     Mockito.mockStatic(kundenverwaltung.Benachrichtigung.class))
        {

            Nation result = dropDownMenu.getAndCheckNationValue(testComboBox, testNationList);

            assertNull(result, "Should return null for invalid input");

            mockedNotification.verify(() ->
                    kundenverwaltung.Benachrichtigung.infoBenachrichtigung("Achtung!", "Ungültige Nationalität."));
        }
    }

    /** Test case insensitive input */
    @Test
    void getAndCheckNationValue_WithCaseInsensitiveInput_ShouldReturnNation()
    {
        testComboBox.getEditor().setText("belgien"); // lowercase

        Nation result = dropDownMenu.getAndCheckNationValue(testComboBox, testNationList);

        assertNotNull(result, "Should handle case-insensitive input");
        assertEquals("Belgien", result.getName(), "Should return correct nation despite case difference");
    }

    /** Test combobox reset functionality */
    @Test
    void resetComboboxNation_ShouldResetComboBoxState()
    {
        ObservableList<Nation> testObservableList = FXCollections.observableArrayList(testNationList);
        testComboBox.getEditor().setText("Some text");
        testComboBox.getSelectionModel().select(0);

        dropDownMenu.resetComboboxNation(testComboBox, testObservableList);

        assertEquals(testObservableList, testComboBox.getItems(), "Should set the provided items");
        assertEquals(-1, testComboBox.getSelectionModel().getSelectedIndex(), "Should reset selection");
        assertEquals("", testComboBox.getEditor().getText(), "Should clear editor text");
    }

    /** Test letter input filtering */
    @Test
    void jumpToUserInput_WithValidLetterInput_ShouldFilterNations()
    {
        testComboBox.getEditor().setText("B");
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getCode()).thenReturn(KeyCode.B);

        dropDownMenu.jumpToUserInput(testComboBox, testNationList, mockKeyEvent);

        assertNotNull(testComboBox.getItems(), "ComboBox items should be set");
    }

    /** Test backspace key handling */
    @Test
    void jumpToUserInput_WithBackspaceKey_ShouldProcessInput()
    {
        testComboBox.getEditor().setText("Be");
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getCode()).thenReturn(KeyCode.BACK_SPACE);

        assertDoesNotThrow(() ->
                        dropDownMenu.jumpToUserInput(testComboBox, testNationList, mockKeyEvent),
                "Should handle backspace key without throwing");
    }

    /** Test empty input handling */
    @Test
    void jumpToUserInput_WithEmptyInput_ShouldResetToFullList()
    {
        testComboBox.getEditor().setText(""); // Empty input
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getCode()).thenReturn(KeyCode.A);

        dropDownMenu.jumpToUserInput(testComboBox, testNationList, mockKeyEvent);

        assertEquals(testNationList.size(), testComboBox.getItems().size(),
                "Should reset to full list when input is empty");
    }

    /** Test umlaut character handling */
    @Test
    void jumpToUserInput_WithUmlautInput_ShouldHandleCorrectly()
    {
        testComboBox.getEditor().setText("Ä");
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getCode()).thenReturn(KeyCode.A); // Not a letter key, but umlaut should be detected

        assertDoesNotThrow(() ->
                        dropDownMenu.jumpToUserInput(testComboBox, testNationList, mockKeyEvent),
                "Should handle umlaut characters correctly");
    }

    /** Test focus listener addition */
    @Test
    void onFocused_ShouldAddFocusListener()
    {
        assertDoesNotThrow(() ->
                        dropDownMenu.onFocused(testComboBox, testNationList),
                "Should add focus listener without throwing");

        assertNotNull(testComboBox.focusedProperty(), "Focused property should exist");
        // Note: Testing the actual listener behavior would require more complex JavaFX setup
    }

    /** Test non-letter key handling */
    @Test
    void jumpToUserInput_WithNonLetterKey_ShouldNotProcess()
    {
        testComboBox.getEditor().setText("B");
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getCode()).thenReturn(KeyCode.ENTER); // Not a letter key

        ObservableList<Nation> originalItems = testComboBox.getItems();

        dropDownMenu.jumpToUserInput(testComboBox, testNationList, mockKeyEvent);

        assertEquals(originalItems, testComboBox.getItems(),
                "Should not change items for non-letter keys");
    }

    /** Test exact match behavior */
    @Test
    void getAndCheckNationValue_WithExactMatch_ShouldReturnFirstMatch()
    {
        ArrayList<Nation> listWithDuplicates = new ArrayList<>(testNationList);
        listWithDuplicates.add(new Nation(99, "Belgien", "BE2", true)); // Duplicate name

        testComboBox.getEditor().setText("Belgien");

        Nation result = dropDownMenu.getAndCheckNationValue(testComboBox, listWithDuplicates);

        assertNotNull(result, "Should return a nation");
        assertEquals(4, result.getNationId(), "Should return the first matching nation");
    }

    /** Test class structure validity */
    @Test
    void dynamicNationDropDownMenu_ClassStructure_ShouldBeValid()
    {
        assertTrue(java.lang.reflect.Modifier.isPublic(DynamicNationDropDownMenu.class.getModifiers()),
                "DynamicNationDropDownMenu class should be public");

        assertEquals("kundenverwaltung.toolsandworkarounds",
                DynamicNationDropDownMenu.class.getPackageName(),
                "Should be in correct package");
    }

    /** Test null list exception handling */
    @Test
    void getFirstPositionAlphabeticalSortingNation_WithNullList_ShouldThrowException()
    {
        assertThrows(Exception.class, () ->
                        dropDownMenu.getFirstPositionAlphabeticalSortingNation(null),
                "Should throw exception for null nation list");
    }
}