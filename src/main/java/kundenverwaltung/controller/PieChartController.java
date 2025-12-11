package kundenverwaltung.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import kundenverwaltung.dao.PieChartDAO;
import kundenverwaltung.model.PieChart;
import kundenverwaltung.toolsandworkarounds.PieChartStorage;
import java.sql.SQLException;
import java.util.List;

public class PieChartController
{

	@FXML
	private javafx.scene.chart.PieChart pieChart;

	@FXML
	private TextField queryField;

	private PieChartDAO pieChartDAO;
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	public void initialize()
	{
		pieChartDAO = new PieChartDAO();
		loadPieChartFromStoredData();
	}
	/**
	 * Loads pie chart data based on the SQL query entered in the query field.
	 * Retrieves data from the database and updates the pie chart. If there is an
	 * error executing the SQL query, an error alert is displayed.
	 */
	@FXML
	public void loadPieChartData()
	{
		String query = queryField.getText();
		try
		{
			List<PieChart> data = pieChartDAO.getPieChartData(query);
			pieChart.getData().clear();
			for (PieChart item : data)
			{
				pieChart.getData().add(new Data(item.getLabel(), item.getValue()));
			}
			// Store the data for later use
			PieChartStorage.storeData(data);
		} catch (SQLException e)
		{
			showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler bei der Ausführung der SQL-Abfrage.");
			e.printStackTrace();
		}
	}

	private void loadPieChartFromStoredData()
	{
		List<PieChart> storedData = PieChartStorage.loadData();
		if (storedData != null)
		{
			pieChart.getData().clear();
			for (PieChart item : storedData)
			{
				pieChart.getData().add(new Data(item.getLabel(), item.getValue()));
			}
		}
	}

	private void showAlert(Alert.AlertType alertType, String title, String message)
	{
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
