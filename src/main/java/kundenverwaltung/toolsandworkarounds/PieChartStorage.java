package kundenverwaltung.toolsandworkarounds;

import java.util.ArrayList;
import java.util.List;
import kundenverwaltung.model.PieChart;

public class PieChartStorage
{
	private static List<PieChart> storedData = new ArrayList<>();

	public static void storeData(List<PieChart> data)
	{
		storedData.clear();
		storedData.addAll(data);
	}

	public static List<PieChart> loadData()
	{
		return new ArrayList<>(storedData);
	}
}
