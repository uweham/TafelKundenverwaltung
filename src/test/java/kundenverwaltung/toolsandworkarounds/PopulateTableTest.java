package kundenverwaltung.toolsandworkarounds;

import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.EmptySource;

import static org.junit.jupiter.api.Assertions.*;

class PopulateTableTest {

    private PopulateTable populateTable;

    @BeforeEach
    void setUp() {
        populateTable = new PopulateTable("test-file.txt");
    }

    @Test
    @DisplayName("Constructor should create instance with given filename")
    void testConstructor() {
        String fileName = "example.pdf";
        PopulateTable table = new PopulateTable(fileName);

        assertNotNull(table, "PopulateTable instance should not be null");
        assertEquals(fileName, table.getName(), "Constructor should set the filename correctly");
    }

    @Test
    @DisplayName("getName should return the filename set in constructor")
    void getName() {
        String expectedFileName = "document.docx";
        PopulateTable table = new PopulateTable(expectedFileName);

        String actualFileName = table.getName();

        assertEquals(expectedFileName, actualFileName, "getName should return the filename set in constructor");
    }

    @Test
    @DisplayName("getName should return consistent values on multiple calls")
    void getNameConsistency() {
        String fileName = "consistency-test.txt";
        PopulateTable table = new PopulateTable(fileName);

        String firstCall = table.getName();
        String secondCall = table.getName();
        String thirdCall = table.getName();

        assertEquals(firstCall, secondCall, "Multiple calls to getName should return same value");
        assertEquals(secondCall, thirdCall, "Multiple calls to getName should return same value");
        assertEquals(fileName, firstCall, "All calls should return the original filename");
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Constructor should handle null filename")
        void testConstructorWithNull() {
            assertDoesNotThrow(() -> {
                PopulateTable table = new PopulateTable(null);
                String result = table.getName();
                assertTrue(result == null || result.isEmpty(),
                        "Null filename should result in null or empty string");
            }, "Constructor should handle null input gracefully");
        }

        @Test
        @DisplayName("Constructor should handle empty filename")
        void testConstructorWithEmptyString() {
            String emptyFileName = "";
            PopulateTable table = new PopulateTable(emptyFileName);

            assertEquals(emptyFileName, table.getName(),
                    "Constructor should preserve empty string filename");
        }

        @Test
        @DisplayName("Constructor should handle whitespace-only filename")
        void testConstructorWithWhitespace() {
            String whitespaceFileName = "   ";
            PopulateTable table = new PopulateTable(whitespaceFileName);

            assertEquals(whitespaceFileName, table.getName(),
                    "Constructor should preserve whitespace-only filename");
        }
    }

    @Nested
    @DisplayName("Various Filename Types")
    class FilenameTypes {

        @ParameterizedTest
        @DisplayName("Should handle various valid filenames")
        @ValueSource(strings = {
                "simple.txt",
                "file-with-dashes.pdf",
                "file_with_underscores.docx",
                "file with spaces.jpg",
                "UPPERCASE.XML",
                "mixedCase.Html",
                "file.with.multiple.dots.csv",
                "very-long-filename-that-might-be-used-in-some-systems.extension",
                "123456789.numbers",
                "special!@#$%^&()chars.txt",
                "unicode-café-naïve.txt"
        })
        void testVariousFilenames(String fileName) {
            PopulateTable table = new PopulateTable(fileName);
            assertEquals(fileName, table.getName(),
                    "Should correctly handle filename: " + fileName);
        }

        @ParameterizedTest
        @DisplayName("Should handle edge case inputs")
        @NullSource
        @EmptySource
        @ValueSource(strings = {" ", "\t", "\n", "   \t\n   "})
        void testEdgeCaseInputs(String fileName) {
            assertDoesNotThrow(() -> {
                PopulateTable table = new PopulateTable(fileName);
                String result = table.getName();
            }, "Should handle edge case input without throwing exception: '" + fileName + "'");
        }
    }

    @Nested
    @DisplayName("JavaFX Property Integration")
    class JavaFXPropertyTests {

        @Test
        @DisplayName("Internal fileName property should be SimpleStringProperty")
        void testInternalPropertyType() {
            PopulateTable table = new PopulateTable("test.txt");

            String result = table.getName();
            assertInstanceOf(String.class, result,
                    "getName should return String, not Property object");
        }

        @Test
        @DisplayName("Should work with typical JavaFX usage patterns")
        void testJavaFXUsagePattern() {
            String[] testFiles = {"file1.txt", "file2.pdf", "file3.docx"};

            for (String fileName : testFiles) {
                PopulateTable table = new PopulateTable(fileName);

                for (int i = 0; i < 10; i++) {
                    assertEquals(fileName, table.getName(),
                            "Value should remain consistent across multiple accesses");
                }
            }
        }
    }

    @Nested
    @DisplayName("Object State and Immutability")
    class StateTests {

        @Test
        @DisplayName("Object state should remain consistent after creation")
        void testObjectStateConsistency() {
            String fileName = "state-test.txt";
            PopulateTable table = new PopulateTable(fileName);

            String initialName = table.getName();

            for (int i = 0; i < 100; i++) {
                assertEquals(initialName, table.getName(),
                        "Object state should remain consistent across multiple calls");
            }
        }

        @Test
        @DisplayName("Object state should be immutable after construction")
        void testImmutability() {
            String fileName = "immutable-test.txt";
            PopulateTable table = new PopulateTable(fileName);

            String originalName = table.getName();

            String retrievedName = table.getName();
            assertEquals(originalName, retrievedName,
                    "Object state should remain unchanged");
        }

        @Test
        @DisplayName("Different instances should be independent")
        void testInstanceIndependence() {
            String fileName1 = "file1.txt";
            String fileName2 = "file2.txt";

            PopulateTable table1 = new PopulateTable(fileName1);
            PopulateTable table2 = new PopulateTable(fileName2);

            assertEquals(fileName1, table1.getName(), "First instance should have its own filename");
            assertEquals(fileName2, table2.getName(), "Second instance should have its own filename");
            assertNotEquals(table1.getName(), table2.getName(), "Instances should be independent");
        }
    }

    @Test
    @DisplayName("toString behavior should be reasonable")
    void testToStringBehavior() {
        String fileName = "toString-test.txt";
        PopulateTable table = new PopulateTable(fileName);

        String toString = table.toString();

        assertNotNull(toString, "toString should not return null");
        assertFalse(toString.trim().isEmpty(), "toString should not return empty string");
    }

    @Test
    @DisplayName("equals and hashCode behavior should be consistent")
    void testEqualsAndHashCode() {
        String fileName = "equals-test.txt";
        PopulateTable table1 = new PopulateTable(fileName);
        PopulateTable table2 = new PopulateTable(fileName);
        PopulateTable table3 = new PopulateTable("different-file.txt");

        assertEquals(table1, table1, "Object should equal itself");

        boolean areEqual = table1.equals(table2);
        assertEquals(areEqual, table2.equals(table1), "equals should be symmetric");

        if (areEqual) {
            assertEquals(table1.hashCode(), table2.hashCode(),
                    "Equal objects should have equal hash codes");
        }

        assertNotEquals(table1, null, "Object should not equal null");

        assertNotEquals(table1, "string", "Object should not equal different type");
    }
}