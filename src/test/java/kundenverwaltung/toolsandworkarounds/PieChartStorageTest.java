package kundenverwaltung.toolsandworkarounds;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import kundenverwaltung.model.PieChart;

public class PieChartStorageTest {

    @BeforeEach
    void clearStorage() {
        PieChartStorage.storeData(Collections.emptyList());
    }

    @Test
    @Tag("unit")
    void testStoreAndLoadData() {
        PieChart p1 = new PieChart("A", 10);
        PieChart p2 = new PieChart("B", 20);

        PieChartStorage.storeData(Arrays.asList(p1, p2));

        List<PieChart> loaded = PieChartStorage.loadData();
        assertEquals(2, loaded.size());
        assertTrue(loaded.contains(p1));
        assertTrue(loaded.contains(p2));
    }

    @Test
    @Tag("unit")
    void testLoadDataReturnsCopy() {
        PieChart p1 = new PieChart("A", 10);
        PieChartStorage.storeData(Collections.singletonList(p1));

        List<PieChart> loaded = PieChartStorage.loadData();
        loaded.clear();  // Manipulation der geladenen Liste

        List<PieChart> loadedAgain = PieChartStorage.loadData();
        assertEquals(1, loadedAgain.size());
    }

    @Test
    @Tag("unit")
    void testStoreDataClearsPreviousData() {
        PieChart p1 = new PieChart("A", 10);
        PieChart p2 = new PieChart("B", 20);

        PieChartStorage.storeData(Collections.singletonList(p1));
        PieChartStorage.storeData(Collections.singletonList(p2));

        List<PieChart> loaded = PieChartStorage.loadData();
        assertEquals(1, loaded.size());
        assertEquals(p2, loaded.get(0));
    }
}
