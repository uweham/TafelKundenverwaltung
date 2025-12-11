package kundenverwaltung.model;

import java.time.LocalDate;
import kundenverwaltung.dao.UserDAO;
import kundenverwaltung.dao.UserDAOimpl;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;

public class User
{
	private ChangeDateFormat changeDateFormat = new ChangeDateFormat();

	private UserDAO userDAO = new UserDAOimpl();
	private int userId;
	private String userName;
	private String firstName;
	private String surname;
	private LocalDate birthday;
	private String userRights;
	private String password;

	public User(String userName, String firstName, String surname, LocalDate birthday, String password, String userRights)
	{
		this.userName = userName;
		this.firstName = firstName;
		this.surname = surname;
		this.birthday = birthday;
		this.password = password;
		this.userRights = userRights;
		userDAO.insert(this);
	}

	public User(int userId, String userName, String firstName, String surname, LocalDate birthday, String password, String userRights)
	{
		this.userId = userId;
		this.userName = userName;
		this.firstName = firstName;
		this.surname = surname;
		this.birthday = birthday;
		this.password = password;
		this.userRights = userRights;
	}




	 /**
	   *.
	   */
	public int getUserId()
	{
		return userId;
	}
	 /**
	   *.
	   */
	public void setUserId(int userId)
	{
		this.userId = userId;
	}
	 /**
	   *.
	   */
	public String getUserName()
	{
		return userName;
	}
	 /**
	   *.
	   */
	public void setUserName(String userName)
	{
		this.userName = userName;
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
		return getSurname() + " " + getFirstName();
	}
	 /**
	   *.
	   */
	public LocalDate getBirthday()
	{
		return birthday;
	}
	 /**
	   *.
	   */
	public String getBirthdayString()
	{
		return changeDateFormat.changeDateToDefaultString(getBirthday());
	}
	 /**
	   *.
	   */
	public void setBirthday(LocalDate birthday)
	{
		this.birthday = birthday;
	}
	 /**
	   *.
	   */
	public String getUserRights()
	{
		return userRights;
	}
	 /**
	   *.
	   */
	public void setUserRights(String userRights)
	{
		this.userRights = userRights;
	}
	 /**
	   *.
	   */
	public String getPassword()
	{
		return password;
	}
	 /**
	   *.
	   */
	public void setPassword(String password)
	{
		this.password = password;
	}
	 /**
	   *.
	   */
	public Boolean isDefaultPassword(String passwordInput)
	{
		return getBirthdayString().equals(passwordInput);
	}
}
