package kundenverwaltung.controller.statistiktool;

import kundenverwaltung.dao.HaushaltDAO;
import kundenverwaltung.dao.PLZDAO;
import kundenverwaltung.dao.PLZDaoImpl;
import kundenverwaltung.model.PLZ;
import kundenverwaltung.model.statistiktool.Herkunft;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HerkunftStatistikController
{

    // Tabelle
    @FXML private TableView<Herkunft> herkunftTable;
    @FXML private TableColumn<Herkunft, String> ortColumn;
    @FXML private TableColumn<Herkunft, String> hausnummerColumn;
    @FXML private TableColumn<Herkunft, String> plzColumn;
    @FXML private TableColumn<Herkunft, Integer> anzahlHaushalteColumn;

    // Filter
    @FXML private ComboBox<String> auswahlComboBox;
    @FXML private ComboBox<String> kundenStatusComboBox;
    @FXML private DatePicker startDatumPicker;
    @FXML private DatePicker endDatumPicker;

    // Diagramm & UI (horizontal: X = Number, Y = Category)
    @FXML private ComboBox<String> topNCombo;
    @FXML private Button resetFilterBtn;
    @FXML private BarChart<Number, String> plzBarChart;
    @FXML private NumberAxis countAxis;
    @FXML private CategoryAxis plzAxis;

    private HaushaltDAO haushaltDAO;
    private final PLZDAO plzDAO = new PLZDaoImpl();     // dein vorhandenes DAO

    @FXML private Stage primaryStage;

    private final ObservableList<Herkunft> masterData = FXCollections.observableArrayList();

    // Mapping PLZ -> Ort (lazy geladen)
    private Map<String, String> plzOrtMap = null;

    // Wie Kategorien im Chart gelabelt werden (Standard: nur PLZ)
    private Function<String, String> plzLabelProvider = plz -> plz;

    public HerkunftStatistikController()
    {

    }
    /**
    *
    */
    public void setHaushaltDAO(HaushaltDAO haushaltDAO)
    { this.haushaltDAO = haushaltDAO; }
    /**
    *
    */
    public void setPrimaryStage(Stage primaryStage)
    { this.primaryStage = primaryStage; }
    /**
    *
    */
    @SuppressWarnings("unchecked")
    @FXML
    public void initialize()
    {
        // TableView-Spalten
        ortColumn.setCellValueFactory(new PropertyValueFactory<>("strasse"));
        hausnummerColumn.setCellValueFactory(new PropertyValueFactory<>("hausnummer"));
        plzColumn.setCellValueFactory(new PropertyValueFactory<>("plz"));
        anzahlHaushalteColumn.setCellValueFactory(new PropertyValueFactory<>("anzahlHaushalte"));

        // Standardsortierung: PLZ
        plzColumn.setSortable(true);
        plzColumn.setSortType(TableColumn.SortType.ASCENDING);
        herkunftTable.getSortOrder().setAll(plzColumn);

        // Chart-Basics
        if (plzBarChart != null)
        {
            plzBarChart.setLegendVisible(false);
            plzBarChart.setAnimated(false);
            plzBarChart.setCategoryGap(6.0);
            plzBarChart.setBarGap(2.0);
        }

        // Y-Achse sicher sichtbar halten
        plzAxis.setTickLabelRotation(0);
        plzAxis.setTickLabelGap(4);
        plzAxis.setTickMarkVisible(true);
        plzAxis.setTickLabelsVisible(true);

        // X-Achse als ganze Zahlen (Feintuning in updatePlzBarChart)
        countAxis.setForceZeroInRange(true);
        countAxis.setMinorTickCount(0);

        // Top-N
        if (topNCombo != null)
        {
            if (topNCombo.getItems().isEmpty())
            {
                topNCombo.getItems().addAll("Top 5", "Top 10", "Top 20", "Alle");
            }
            topNCombo.getSelectionModel().select("Top 10");
            topNCombo.valueProperty().addListener((obs, o, n) -> updatePlzBarChart(masterData));
        }

        if (resetFilterBtn != null) resetFilterBtn.setDisable(true);

        // SortedList an Tabelle
        SortedList<Herkunft> sorted = new SortedList<>(masterData);
        sorted.comparatorProperty().bind(herkunftTable.comparatorProperty());
        herkunftTable.setItems(sorted);

        // PLZ->Ort Map laden (lazy) und Label-Provider setzen
        ensurePlzMapLoaded();
        plzLabelProvider = plz ->
        {
            String ort = plzOrtMap.get(plz);
            return (ort != null && !ort.isBlank()) ? (ort + " (" + plz + ")") : plz;
        };
    }

    @SuppressWarnings("unchecked")
    @FXML
    private void loadHerkunftStatistik()
    {
        if (startDatumPicker.getValue() == null || endDatumPicker.getValue() == null)
        {
            showAlert(Alert.AlertType.WARNING, "Eingabefehler", "Bitte Start- und Enddatum wählen.");
            return;
        }
        if (kundenStatusComboBox.getValue() == null)
        {
            showAlert(Alert.AlertType.WARNING, "Eingabefehler", "Bitte Kundenstatus wählen.");
            return;
        }
        if (auswahlComboBox.getValue() == null)
        {
            showAlert(Alert.AlertType.WARNING, "Eingabefehler", "Bitte Typ (Haushalt/Personen) wählen.");
            return;
        }

        String status = kundenStatusComboBox.getValue();
        boolean isPerson = "Personen".equals(auswahlComboBox.getValue());
        LocalDate start = startDatumPicker.getValue();
        LocalDate end = endDatumPicker.getValue();

        List<Herkunft> data = haushaltDAO.getHouseholdsWithLocation(status, isPerson, start, end);

        masterData.setAll(data);
        herkunftTable.getSortOrder().setAll(plzColumn);

        updatePlzBarChart(data);
        if (resetFilterBtn != null) resetFilterBtn.setDisable(true);
    }

    @SuppressWarnings("unchecked")
    private void updatePlzBarChart(List<Herkunft> data)
    {
        if (data == null) data = List.of();

        // Aggregation
        Map<String, Integer> aggregated = data.stream()
                .collect(Collectors.groupingBy(Herkunft::getPlz,
                        Collectors.summingInt(Herkunft::getAnzahlHaushalte)));

        int total = aggregated.values().stream().mapToInt(Integer::intValue).sum();

        plzBarChart.getData().clear();
        plzAxis.setCategories(FXCollections.observableArrayList());

        if (total == 0)
        {
            applyIntegerAxisBounds(0);
            return;
        }

        // Sortiert (desc)
        List<Map.Entry<String, Integer>> sorted = aggregated.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .toList();

        // Top-N
        int topN = switch (topNCombo != null ? topNCombo.getValue() : "Top 10")
            {
            case "Top 5" -> 5;
            case "Top 20" -> 20;
            case "Alle" -> Integer.MAX_VALUE;
            default -> 10;
        };

        List<Map.Entry<String, Integer>> top = sorted.stream().limit(topN).toList();
        int others = sorted.stream().skip(topN).mapToInt(Map.Entry::getValue).sum();

        // Kategorien (Y) bauen – mit „Ort (PLZ)“
        List<String> categories = new ArrayList<>();
        for (Map.Entry<String, Integer> e : top)
        {
            categories.add(plzLabelProvider.apply(e.getKey()));
        }
        if (others > 0) categories.add("Weitere");
        plzAxis.setCategories(FXCollections.observableArrayList(categories));

        // Serie
        XYChart.Series<Number, String> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> e : top)
        {
            addDataPoint(series, e.getKey(), e.getValue(), total);
        }
        if (others > 0) addDataPoint(series, "Weitere", others, total);

        plzBarChart.getData().setAll(series);

        // X-Achse: Ticks auf ganze Zahlen
        int maxCount = Math.max(
                top.stream().mapToInt(Map.Entry::getValue).max().orElse(0),
                others
        );
        applyIntegerAxisBounds(maxCount);

        // Drill-Down
        for (XYChart.Data<Number, String> dp : series.getData())
        {
            dp.nodeProperty().addListener((obs, oldNode, node) ->
            {
                if (node != null)
                {
                    String rawPlz = restoreRawPlzKey(dp.getYValue());
                    node.setOnMouseClicked(e ->
                    {
                        if (!"Weitere".equals(rawPlz))
                        {
                            filterTableByPlz(rawPlz);
                        }
                    });
                }
            });
        }
    }

    /** X-Achse als 0..max+1, TickUnit=1 (nur ganze Zahlen). */
    private void applyIntegerAxisBounds(int max)
    {
        int upper = Math.max(1, max);
        int padded = upper + 1; // etwas Luft rechts für Labels
        countAxis.setAutoRanging(false);
        countAxis.setLowerBound(0);
        countAxis.setUpperBound(padded);
        countAxis.setTickUnit(1);
        countAxis.setMinorTickCount(0);
    }

    /** Falls die Y-Kategorie „Ort (PLZ)“ ist, hier wieder die Roh-PLZ extrahieren. */
    private String restoreRawPlzKey(String categoryLabel)
    {
        int open = categoryLabel.lastIndexOf('(');
        int close = categoryLabel.lastIndexOf(')');
        if (open >= 0 && close > open)
        {
            String inner = categoryLabel.substring(open + 1, close).trim();
            if (!inner.isEmpty()) return inner;
        }
        return categoryLabel;
    }

    private void addDataPoint(XYChart.Series<Number, String> series, String plzRawKey, int count, int total)
    {
        String label = "Weitere".equals(plzRawKey) ? "Weitere" : plzLabelProvider.apply(plzRawKey);
        XYChart.Data<Number, String> dp = new XYChart.Data<>(count, label);
        series.getData().add(dp);

        dp.nodeProperty().addListener((o, old, node) ->
        {
            if (node == null) return;

            double pct = (count * 100.0) / Math.max(1, total);
            String tip = "%s: %,d (%.1f%%)".formatted(label, count, pct);
            Tooltip.install(node, new Tooltip(tip));

            // Zahlenlabel rechts neben dem Balken
            Label valueLabel = new Label(String.format("%,d", count));
            valueLabel.getStyleClass().add("bar-label");
            node.parentProperty().addListener((pObs, oldP, parent) ->
            {
                if (parent instanceof Group g) g.getChildren().add(valueLabel);
            });
            node.boundsInParentProperty().addListener((bObs, bOld, bNew) ->
            {
                valueLabel.setLayoutX(bNew.getMaxX() + 6);
                valueLabel.setLayoutY((bNew.getMinY() + bNew.getMaxY()) / 2 - valueLabel.getHeight() / 2);
            });
        });
    }

    private void filterTableByPlz(String plzRawKey)
    {
        SortedList<Herkunft> sorted = new SortedList<>(
                masterData.filtered(h -> Objects.equals(h.getPlz(), plzRawKey))
        );
        sorted.comparatorProperty().bind(herkunftTable.comparatorProperty());
        herkunftTable.setItems(sorted);
        if (resetFilterBtn != null) resetFilterBtn.setDisable(false);
    }

    @SuppressWarnings("unchecked")
    @FXML
    private void resetFilters()
    {
        SortedList<Herkunft> sorted = new SortedList<>(masterData);
        sorted.comparatorProperty().bind(herkunftTable.comparatorProperty());
        herkunftTable.setItems(sorted);
        herkunftTable.getSortOrder().setAll(plzColumn);
        if (resetFilterBtn != null) resetFilterBtn.setDisable(true);
    }

    @FXML
    private void handleExit()
    {
        Stage stage = (Stage) herkunftTable.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleViewStatistics()
    {
        showAlert(Alert.AlertType.INFORMATION, "Statistikansicht", "Statistikansicht wird angezeigt.");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // --- Helper: PLZ->Ort Map einmalig laden -------------------------------

    private void ensurePlzMapLoaded()
    {
        if (this.plzOrtMap != null) return;
        Map<String, String> map = new HashMap<>();
        try
        {
            ArrayList<PLZ> alle = plzDAO.readAll();
            if (alle != null)
            {
                for (PLZ p : alle)
                {
                    if (p == null) continue;
                    String plz = trimOrNull(p.getPlz());
                    String ort = trimOrNull(p.getOrt());
                    if (plz != null && !plz.isEmpty())
                    {
                        // falls doppelt: ersten Eintrag behalten
                        map.putIfAbsent(plz, ort != null ? ort : "");
                    }
                }
            }
        } catch (Exception ex)
        {
            // Map bleibt leer -> wir fallen auf reines PLZ-Label zurück
            System.err.println("PLZ-Map konnte nicht geladen werden: " + ex.getMessage());
        }
        this.plzOrtMap = map;
    }

    private static String trimOrNull(String s)
    {
        return s == null ? null : s.trim();
    }
}
