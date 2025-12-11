package kundenverwaltung.model;

import java.sql.Date;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;

/**
 *
 * @Author Richard Kromm
 * @Date 31.07.2018
 */
public class DeletedMemberOfTheFamily
{
	private ChangeDateFormat changeDateFormat = new ChangeDateFormat();
	private int autoincrementId;
	private int householdId;
	private String firstName;
	private String surname;
	private String fullName;
	private String birthday;
	private String deletedOn;
	private String reasonDelete;


	/**
	 * Constructor. Return a object.
	 *
	 * @param autoincrementId
	 * @param householdId
	 * @param firstName
	 * @param surname
	 * @param birthday
	 * @param deletedOn
	 * @param reasonDelete
	 */
	public DeletedMemberOfTheFamily(int autoincrementId, int householdId, String firstName, String surname, Date birthday, Date deletedOn, String reasonDelete)
	{
		this.autoincrementId = autoincrementId;
		this.householdId = householdId;
		this.firstName = firstName;
		this.surname = surname;
		this.fullName = firstName + " " + surname;
		this.birthday = changeDateFormat.changeDateToDefaultString(birthday);
		this.deletedOn = changeDateFormat.changeDateToDefaultString(deletedOn);
		this.reasonDelete = reasonDelete;
	}




	/** Getter and Setter. */

	public int getAutoincrementId()
	{
		return autoincrementId;
	}
	 /**
	   *.
	   */
	public void setAutoincrementId(int autoincrementId)
	{
		this.autoincrementId = autoincrementId;
	}
	 /**
	   *.
	   */
	public int getHouseholdId()
	{
		return householdId;
	}
	 /**
	   *.
	   */
	public void setHouseholdId(int householdId)
	{
		this.householdId = householdId;
	}
	 /**
	   *.
	   */
	public String getFirstName()
	{
		return firstName;
	}
	 /**
	   *.
	   */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	 /**
	   *.
	   */
	public String getSurname()
	{
		return surname;
	}
	 /**
	   *.
	   */
	public void setSurname(String surname)
	{
		this.surname = surname;
	}
	 /**
	   *.
	   */
	public String getFullName()
	{
		return fullName;
	}
	 /**
	   *.
	   */
	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}
	 /**
	   *.
	   */
	public String getBirthday()
	{
		return birthday;
	}
	 /**
	   *.
	   */
	public void setBirthday(Date birthday)
	{
		this.birthday = changeDateFormat.changeDateToDefaultString(birthday);
	}
	 /**
	   *.
	   */
	public String getDeletedOn()
	{
		return deletedOn;
	}
	 /**
	   *.
	   */
	public void setDeletedOn(Date deletedOn)
	{
		this.deletedOn = changeDateFormat.changeDateToDefaultString(deletedOn);
	}
	 /**
	   *.
	   */
	public String getReasonDelete()
	{
		return reasonDelete;
	}
	 /**
	   *.
	   */
	public void setReasonDelete(String reasonDelete)
	{
		this.reasonDelete = reasonDelete;
	}
}
