package kundenverwaltung.toolsandworkarounds;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import kundenverwaltung.dao.UserDAOimpl;
import kundenverwaltung.model.User;


/**
 * This class hash user passwords. Is used the "Secure Hash Algorithm 512" (SHA-512).
 *
 * @Author Richard Kromm
 * @Date 12.09.2018
 * @Version 1.0
 */
public class PasswordEncoding
{
	private static final int WORKLOAD = 1000;

	/**
	 * This function create a hash value of user input (plaintext password), salt and pepper.
	 *
	 * @param user with plaintext password
	 * @return same user with hashed password or null at "NoSuchAlgorithmException"
	 */
	public static User hashPassword(User user)
	{
		String hashedPassword = generateSalt(user) + user.getPassword() + getPepper();

		try
		{
			for (int i = 0; i < WORKLOAD; i++)
			{
				MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
				messageDigest.update(hashedPassword.getBytes(StandardCharsets.UTF_8));
				byte[] digest = messageDigest.digest();

				hashedPassword = Base64.getEncoder().encodeToString(digest);
			}
			return new User(user.getUserId(), user.getUserName(), user.getFirstName(), user.getSurname(),
					user.getBirthday(), hashedPassword, user.getUserRights());

		} catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
				return null;
			}
	}

	/**
	 * This function check password for correctness.
	 *
	 * @param user with hashed password
	 * @param plaintextPassword the user input in the password field
	 * @return true: password is correct; false: password is not correct
	 */
	public static Boolean checkPassword(User user, String plaintextPassword)
	{
		User tempUser = hashPassword(new User(user.getUserId(), user.getUserName(), user.getFirstName(),
				user.getSurname(), user.getBirthday(), plaintextPassword, user.getUserRights()));

		return user.getPassword().equals(tempUser.getPassword());
	}



	// später löschen...
	public static String hashText(String plainText)
	{
		try
		{
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
			messageDigest.update(plainText.getBytes(StandardCharsets.UTF_8));
			byte[] digest = messageDigest.digest();


			String hashedPassword = Base64.getEncoder().encodeToString(digest);

			return hashedPassword;
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * This function create a individual salt value for a safer password.
	 *
	 * @param user
	 * @return individual salt string
	 */
	private static String generateSalt(User user)
	{
		String salt = String.valueOf(user.getUserId());

		for (int runVar = 0; runVar < user.getUserName().length(); runVar++)
		{
			if (runVar % 2 == 0)
			{
				salt += String.valueOf(user.getUserName().charAt(runVar)).toLowerCase();
			} else
			{
				salt += String.valueOf(user.getUserName().charAt(runVar)).toUpperCase();
			}
		}
		return salt;
	}

	/**
	 * This function get the pepper value for a safer password.
	 *
	 * @return pepper string
	 */
	private static String getPepper()
	{
		return new UserDAOimpl().getPepper();
	}
}
