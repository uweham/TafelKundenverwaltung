package kundenverwaltung.dao;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import kundenverwaltung.model.User;

public interface UserDAO
{
	boolean insert(User user);
	boolean delete(int userId);
	boolean update(User user);
	User read(int userId);
	User read(String userName);
	ArrayList<User> getAllUsers();
	User createUser(ResultSet resultSet);
	boolean updateNumberOfMistrials(User user, Boolean increment);
	int getNumberOfMistrials(User user);
	boolean setBlockedUntil(User user, LocalDateTime blockedUntil);
	LocalDateTime getBlockedUntil(User user);
	String getPepper();
}
