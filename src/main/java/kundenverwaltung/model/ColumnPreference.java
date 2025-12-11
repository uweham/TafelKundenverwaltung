package kundenverwaltung.model;

public class ColumnPreference
{
    private final int userId;
    private final String tableName;
    private final String columnId;
    private final double width;
    private final int orderIndex;
    private final boolean visible;
    private final String sortType;

    public ColumnPreference(int userId, String tableName, String columnId,
                            double width, int orderIndex, boolean visible, String sortType)
    {
        this.userId = userId;
        this.tableName = tableName;
        this.columnId = columnId;
        this.width = width;
        this.orderIndex = orderIndex;
        this.visible = visible;
        this.sortType = sortType;
    }

    public int getUserId()
    {
        return userId;
    }

    public String getTableName()
    {
        return tableName;
    }

    public String getColumnId()
    {
        return columnId;
    }

    public double getWidth()
    {
        return width;
    }

    public int getOrderIndex()
    {
        return orderIndex;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public String getSortType()
    {
        return sortType;
    }
}
