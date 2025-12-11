package kundenverwaltung.toolsandworkarounds;

import javafx.beans.property.SimpleStringProperty;

public class PopulateTable
{
    private final SimpleStringProperty fileName;
    /*private final SimpleStringProperty date;*/
    public PopulateTable(String fileName)
    {
        super();

        this.fileName = new SimpleStringProperty(fileName);
    /*    this.date = new SimpleStringProperty(date);*/
    }
    /**
     *.
     */
    public String getName()
    {
        return fileName.get();
    }
   /* public String getDate() {
        return date.get();
    }*/
}
