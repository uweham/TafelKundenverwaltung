package kundenverwaltung.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieChartTest {

    private PieChart pieChart;

    @BeforeEach
    void setUp() {
        pieChart = new PieChart("Test Label", 123.45);
    }

    @Test
    @Tag("unit")
    void constructor_setsAllFieldsCorrectly() {
        assertAll(
                () -> assertEquals("Test Label", pieChart.getLabel()),
                () -> assertEquals(123.45, pieChart.getValue())
        );
    }

    @Test
    @Tag("unit")
    void gettersAndSetters_workAsExpected() {
        pieChart.setLabel("Neues Label");
        assertEquals("Neues Label", pieChart.getLabel());

        pieChart.setValue(987.65);
        assertEquals(987.65, pieChart.getValue());
    }

    @Test
    @Tag("unit")
    void getData_returnsFormattedString() {
        String expected = "Label: Test Label, Value: 123.45";
        assertEquals(expected, pieChart.getData());

        pieChart.setLabel("Anderes Label");
        pieChart.setValue(0.5);
        String newExpected = "Label: Anderes Label, Value: 0.5";
        assertEquals(newExpected, pieChart.getData());
    }
}