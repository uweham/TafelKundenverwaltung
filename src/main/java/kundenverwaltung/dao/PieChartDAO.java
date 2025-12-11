package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import kundenverwaltung.model.PieChart;

public class PieChartDAO
{
  /**
   * Retrieves data for a pie chart based on the provided SQL query.
   *
   * @param query the SQL query to execute
   * @return a list of PieChart objects containing the label and value for each data point
   * @throws SQLException if a database access error occurs
   */
	public List<PieChart> getPieChartData(String query) throws SQLException
	{
		List<PieChart> data = new ArrayList<>();

		try (Connection conn = SQLConnection.getCon();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(query))
		{

			while (rs.next())
			{
				String label = rs.getString(1);
				double value = rs.getDouble(2);
				data.add(new PieChart(label, value));
			}
		}

		return data;
	}
}
