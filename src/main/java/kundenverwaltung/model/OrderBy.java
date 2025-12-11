package kundenverwaltung.model;

public class OrderBy
{
	private String term;
	private String dbColumn;

	public OrderBy(String term, String dbColumn)
	{
		this.term = term;
		this.dbColumn = dbColumn;
	}




	 /**
	   *.
	   */
	public String getTerm()
	{
		return term;
	}
	 /**
	   *.
	   */
	public void setTerm(String term)
	{
		this.term = term;
	}
	 /**
	   *.
	   */
	public String getDbColumn()
	{
		return dbColumn;
	}
	 /**
	   *.
	   */
	public void setDbColumn(String dbColumn)
	{
		this.dbColumn = dbColumn;
	}
}
