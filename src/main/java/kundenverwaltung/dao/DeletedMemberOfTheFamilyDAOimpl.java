package kundenverwaltung.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import kundenverwaltung.model.DeletedMemberOfTheFamily;


/**
 * This class implement DeletedMemberOfTheFamilyDAO and create a connection to database.
 *
 * @Author Richard Kromm
 * @Date 31.07.2018
 * @Version 1.0
 */
public class DeletedMemberOfTheFamilyDAOimpl implements DeletedMemberOfTheFamilyDAO
{
	private static final String CULUMN_AUTOINCREMENT_ID = "Id";
	private static final String COLUMN_HOUSEHOLD_ID = "householdId";
	private static final String COLUMN_FIRST_NAME = "firstName";
	private static final String COLUMN_SURNAME = "surname";
	private static final String COLUMN_REASON_DELETE = "reasonDelete";
	private static final String COLUMN_BIRTHDAY = "birthday";
	private static final String COLUMN_DELETED_ON = "deletedOn";


	/**
	 * This function get all deleted member of the family from database.
	 *
	 * @param householdId
	 * @return arraylist
	 */
	@Override
	public ArrayList<DeletedMemberOfTheFamily> getAllDeletedMemberOfTheFamily(int householdId)
	{
		String sql = "SELECT * FROM deleted_MemberOfTheFamily WHERE householdId = ?";

		try
		{
			ArrayList<DeletedMemberOfTheFamily> deletedMemberOfTheFamilyArrayList = new ArrayList<>();

			Connection connection = SQLConnection.getCon();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, householdId);
			ResultSet deletedMemberOfTheFamilyResult = preparedStatement.executeQuery();

			while (deletedMemberOfTheFamilyResult.next())
			{
				int autoincrementIdResult = deletedMemberOfTheFamilyResult.getInt(CULUMN_AUTOINCREMENT_ID);
				int householdIdResult = deletedMemberOfTheFamilyResult.getInt(COLUMN_HOUSEHOLD_ID);
				String firstNameResult = deletedMemberOfTheFamilyResult.getString(COLUMN_FIRST_NAME);
				String surenameResult = deletedMemberOfTheFamilyResult.getString(COLUMN_SURNAME);
				Date birthdayResult = deletedMemberOfTheFamilyResult.getDate(COLUMN_BIRTHDAY);
				String reasonDeleteResult = deletedMemberOfTheFamilyResult.getString(COLUMN_REASON_DELETE);
				Date deletedOnResult = deletedMemberOfTheFamilyResult.getDate(COLUMN_DELETED_ON);

				DeletedMemberOfTheFamily deletedMemberOfTheFamily = new DeletedMemberOfTheFamily(autoincrementIdResult,
						householdIdResult, firstNameResult, surenameResult, birthdayResult, deletedOnResult, reasonDeleteResult);
				deletedMemberOfTheFamilyArrayList.add(deletedMemberOfTheFamily);
			}

			return deletedMemberOfTheFamilyArrayList;
		} catch (SQLException e)
			{
				e.printStackTrace();
			}
		return null;
	}
}
