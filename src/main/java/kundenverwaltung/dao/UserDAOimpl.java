package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import kundenverwaltung.model.User;
import kundenverwaltung.toolsandworkarounds.PasswordEncoding;
import kundenverwaltung.toolsandworkarounds.PropertiesFileController;

/**
 * This class run different database operations.
 *
 * @author Richard Kromm
 * @Date 13.09.2018
 * @Version 1.0
 */
public class UserDAOimpl implements UserDAO
{
	private static final String COLUMN_LABEL_USER_ID = "userId";
	private static final String COLUMN_LABEL_USER_NAME = "userName";
	private static final String COLUMN_LABEL_FIRST_NAME = "firstName";
	private static final String COLUMN_LABEL_SURNAME = "surname";
	private static final String COLUMN_LABEL_BIRTHDAY = "birthday";
	private static final String COLUMN_LABEL_PASSWORD = "password";
	private static final String COLUMN_LABEL_USER_RIGHTS = "userRights";
	private static final String COLUMN_LABEL_BLOCKED_UNTIL = "blockedUntil";
	private static final String COLUMN_LABEL_NUMBER_OF_MISTRIALS = "numberOfMistrials";

	private static final int SYSTEM_DATA_ID = 1;
	private static final int ILLEGAL_NUMBER_OF_MISTRIALS = -1;
	private static final int DEFAULT_NUMBER_OF_MISTRIALS = 0;


	@SuppressWarnings("unused")
	private PasswordEncoding passwordEncoding = new PasswordEncoding();
	private String databaseName = PropertiesFileController.getDbName();

	/**
	 * This function insert a new user (with hashed password) in the database.
	 *
	 * @param user
	 * @return true: insert successfull; false: insert failed
	 */
	@Override
	public boolean insert(User user)
	{
		int userId;
		int parameterIndex = 1;
		String nextUserId = "Select AUTO_INCREMENT "
				+ "FROM INFORMATION_SCHEMA.TABLES "
				+ "WHERE TABLE_SCHEMA = " + "'" + databaseName + "'"
				+ "AND TABLE_NAME = 'users'";

		String inserSql = "INSERT INTO users VALUES (?,?,?,?,?,?,?,?,?)";

		try
		{
			Connection connection = SQLConnection.getCon();
			Statement statementUserId = connection.createStatement();
			ResultSet resultSetUserId = statementUserId.executeQuery(nextUserId);
			resultSetUserId.next();
			userId = resultSetUserId.getInt(1);
			user.setUserId(userId);
			user = PasswordEncoding.hashPassword(user);
			statementUserId.close();

			PreparedStatement preparedStatement = connection.prepareStatement(inserSql);
			preparedStatement.setInt(parameterIndex++, user.getUserId());
			preparedStatement.setString(parameterIndex++, user.getUserName());
			preparedStatement.setString(parameterIndex++, user.getFirstName());
			preparedStatement.setString(parameterIndex++, user.getSurname());
			preparedStatement.setDate(parameterIndex++, Date.valueOf(user.getBirthday()));
			preparedStatement.setString(parameterIndex++, user.getPassword());
			preparedStatement.setString(parameterIndex++, user.getUserRights());
			preparedStatement.setTimestamp(parameterIndex++, Timestamp.valueOf(LocalDateTime.now()));
			preparedStatement.setInt(parameterIndex, DEFAULT_NUMBER_OF_MISTRIALS);
			preparedStatement.executeUpdate();
			preparedStatement.close();

			return true;

		} catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
	}

	/**
	 * This function delete an existing user.
	 *
	 * @param userId
	 * @return true: delete successfull; false: delete failed
	 */
	@Override
	public boolean delete(int userId)
	{
		if (userId == SYSTEM_DATA_ID)
		{
			return false;
		}
		String sql = "DELETE FROM users WHERE userId = ?";
		try
		{
			Connection connection = SQLConnection.getCon();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, userId);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			return true;
		} catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
	}

	/**
	 * This function update an existing user.
	 *
	 * @param user
	 * @return true: update successfull; false: update failed
	 */
	@Override
	public boolean update(User user)
	{
		int parameterIndex = 1;
		String sql = "UPDATE users SET "
		+
				"userName = ?, firstName = ?, surname = ?, birthday = ?, password = ?, userRights = ? "
		+
				"WHERE userId = ?";
		try
		{
			Connection connection = SQLConnection.getCon();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(parameterIndex++, user.getUserName());
			preparedStatement.setString(parameterIndex++, user.getFirstName());
			preparedStatement.setString(parameterIndex++, user.getSurname());
			preparedStatement.setDate(parameterIndex++, Date.valueOf(user.getBirthday()));
			preparedStatement.setString(parameterIndex++, user.getPassword());
			preparedStatement.setString(parameterIndex++, user.getUserRights());
			preparedStatement.setInt(parameterIndex, user.getUserId());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			return true;

		} catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
	}

	/**
	 * This function read an user.
	 *
	 * @param userId
	 * @return an user or null by invalid user id
	 */
	@Override
	public User read(int userId)
	{
		if (userId == SYSTEM_DATA_ID)
		{
			return null;
		}
		String sql = "SELECT * FROM users WHERE userId = ?";
		try
		{
			Connection connection = SQLConnection.getCon();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			return createUser(resultSet);

		} catch (SQLException e)
			{
				e.printStackTrace();
				return null;
			}
	}

	/**
	 * This function read an user.
	 *
	 * @param userName
	 * @return an user or null by invalid username
	 */
	@Override
	public User read(String userName)
	{
		String sql = "SELECT * FROM users WHERE userName = ?";
		try
		{
			Connection connection = SQLConnection.getCon();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userName);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next())
			{
				User resultUser = createUser(resultSet);
				if (resultUser.getUserId() != SYSTEM_DATA_ID)
				{
					return resultUser;
				}
			}
			return null;

		} catch (SQLException e)
			{
				e.printStackTrace();
				return null;
			}
	}

	/**
	 * This function read all users (except system user) from database.
	 *
	 * @return an arraylist with all users
	 */
	@Override
	public ArrayList<User> getAllUsers()
	{
		ArrayList<User> userArrayList = new ArrayList<>();
		String sql = "SELECT * FROM users WHERE userId != ?";
		try
		{
			Connection connection = SQLConnection.getCon();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, SYSTEM_DATA_ID);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next())
			{
				User resultUser = createUser(resultSet);
				if (resultUser != null)
				{
					userArrayList.add(resultUser);
				}
			}
			return userArrayList;
		} catch (SQLException e)
			{
				e.printStackTrace();
				return null;
			}
	}

	/**
	 * This function create an user from a resultset.
	 * @param resultSet
	 * @return an user
	 */
	@Override
	public User createUser(ResultSet resultSet)
	{
		try
		{
			int userId = resultSet.getInt(COLUMN_LABEL_USER_ID);
			String userName = resultSet.getString(COLUMN_LABEL_USER_NAME);
			String firstName = resultSet.getString(COLUMN_LABEL_FIRST_NAME);
			String surname = resultSet.getString(COLUMN_LABEL_SURNAME);
			LocalDate birthday = resultSet.getDate(COLUMN_LABEL_BIRTHDAY).toLocalDate();
			String password = resultSet.getString(COLUMN_LABEL_PASSWORD);
			String userRights = resultSet.getString(COLUMN_LABEL_USER_RIGHTS);

			return new User(userId, userName, firstName, surname, birthday, password, userRights);

		} catch (SQLException e)
			{
				e.printStackTrace();
				return null;
			}
	}

	/**
	 * This function update the number of mistrials from an user.
	 *
	 * @param user
	 * @param increment true: numberOfMistrials + 1; false: numberOfMistrials = 0
	 * @return true: update successfull; false: update failed
	 */
	@Override
	public boolean updateNumberOfMistrials(User user, Boolean increment)
	{
		String sql = (increment
		    ?
				"UPDATE users SET numberOfMistrials = numberOfMistrials + 1 WHERE userId = ?"
		        :
				"UPDATE users SET numberOfMistrials = 0 WHERE userId = ?");
		try
		{
			Connection connection = SQLConnection.getCon();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, user.getUserId());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			return true;
		} catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
	}

	/**
	 * This function set the column "blockedUntil" from an user.
	 *
	 * @param user
	 * @param blockedUntil
	 * @return true: update successful; false: update failed
	 */
	@Override
	public boolean setBlockedUntil(User user, LocalDateTime blockedUntil)
	{
		String sql = "UPDATE users SET blockedUntil = ? WHERE userId = ?";
		try
		{
			Connection connection = SQLConnection.getCon();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setTimestamp(1, Timestamp.valueOf(blockedUntil));
			preparedStatement.setInt(2, user.getUserId());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			return true;
		} catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
	}

	/**
	 * This function get the number of mistrials from an user.
	 *
	 * @param user
	 * @return
	 */
	@Override
	public int getNumberOfMistrials(User user)
	{
		String sql = "SELECT numberOfMistrials FROM users WHERE userId = ?";
		try
		{
			Connection connection = SQLConnection.getCon();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, user.getUserId());
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			return resultSet.getInt(COLUMN_LABEL_NUMBER_OF_MISTRIALS);
		} catch (SQLException e)
			{
				e.printStackTrace();
				return ILLEGAL_NUMBER_OF_MISTRIALS;
			}
	}

	/**
	 * This function get the "blocked-until-date" from an user.
	 *
	 * @param user
	 * @return localDateTime
	 */
	@Override
	public LocalDateTime getBlockedUntil(User user)
	{
		String sql = "SELECT blockedUntil FROM users WHERE userId = ?";
		try
		{
			Connection connection = SQLConnection.getCon();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, user.getUserId());
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			return resultSet.getTimestamp(COLUMN_LABEL_BLOCKED_UNTIL).toLocalDateTime();
		} catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This function get pepper from database for a safer password.
	 *
	 * @return String
	 */
	@Override
	public String getPepper()
	{
		String sql = "SELECT * FROM users WHERE userId = ?";
		try
		{
			Connection connection = SQLConnection.getCon();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, SYSTEM_DATA_ID);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			return resultSet.getString(COLUMN_LABEL_PASSWORD);
		} catch (SQLException e)
			{
				e.printStackTrace();
				return null;
			}
	}


}
