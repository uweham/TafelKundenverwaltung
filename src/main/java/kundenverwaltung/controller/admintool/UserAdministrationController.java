package kundenverwaltung.controller.admintool;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.controller.MainController;
import kundenverwaltung.dao.UserDAO;
import kundenverwaltung.dao.UserDAOimpl;
import kundenverwaltung.model.User;
import kundenverwaltung.service.TablePreferenceServiceImpl;


/**
 * This class show all users in a tableview and allows users to add, update or delete.
 *
 * @Autor Richard Kromm
 * @Date 13.09.2018
 * @Version 1.0
 */
public class UserAdministrationController
{
    private static final String CONFIRM_DELETE_USER_TITEL = "Achtung!";
    private static final String CONFIRM_DELETE_USER_HEADER_TEXT = "Benutzername: ";
    private static final String CONFIRM_DELETE_USER_CONTENT_TEXT = "Sind Sie sicher, dass Sie den Benutzer endgültig löschen möchten?";

    @FXML
    private Button buttonAddUser;

    @FXML
    private Button buttonDeleteUser;

    @FXML
    private Button buttonChangeUser;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableView tableViewAllUsers;
	@SuppressWarnings("rawtypes")
    @FXML
    private TableColumn tableColumnNumber;

    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn tableColumnUser;

    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn tableColumnName;

    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn tableColumnBirthday;

    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn tableColumnUserRights;

	private UserDAO userDAO = new UserDAOimpl();
	private ObservableList<User> userObservableList = FXCollections.observableArrayList(userDAO.getAllUsers());
	private User user;

	 /**
     * Initializes the controller class.
     * Sets up the table columns and binds the table data to the list of users.
     */
	 @SuppressWarnings("unchecked")
	 public void initialize()
	 {
		 tableColumnNumber.setCellValueFactory(new PropertyValueFactory<>("userId"));
		 tableColumnNumber.setId("userId");

		 tableColumnUser.setCellValueFactory(new PropertyValueFactory<>("userName"));
		 tableColumnUser.setId("userName");

		 tableColumnName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
		 tableColumnName.setId("fullName");

		 tableColumnBirthday.setCellValueFactory(new PropertyValueFactory<>("birthdayString"));
		 tableColumnBirthday.setId("birthdayString");

		 tableColumnUserRights.setCellValueFactory(new PropertyValueFactory<>("userRights"));
		 tableColumnUserRights.setId("userRights");

		 tableViewAllUsers.setItems(userObservableList);

		 tableViewAllUsers.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->
		 {
			 if (newValue != null)
			 {
				 user = (User) tableViewAllUsers.getSelectionModel().getSelectedItem();
				 disabledButtons(false);
			 }
		 });

		 TablePreferenceServiceImpl.getInstance().setupPersistence(tableViewAllUsers, "UsersAdministrationController");
	 }


	/**
	 * This function open a new window, where you can change or add user.
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	@FXML
	public void openUserAddChange(ActionEvent event)
	{
		MainController.getInstance().openUserAddChange((event.getSource().equals(buttonAddUser) ? null : user));
		userObservableList.clear();
		userObservableList = FXCollections.observableArrayList(userDAO.getAllUsers());
		tableViewAllUsers.setItems(userObservableList);
	}


	/**
	 * This function delete an user.
	 */
	@FXML
	public void deleteUser()
	{
		if (Benachrichtigung.confirmDelete(CONFIRM_DELETE_USER_TITEL,
				CONFIRM_DELETE_USER_HEADER_TEXT + user.getUserName(), CONFIRM_DELETE_USER_CONTENT_TEXT))
		{
			if (userDAO.delete(user.getUserId()))
			{
				userObservableList.remove(user);
			}
		}
	}

	/**
	 * This function disabled or enabled buttons.
	 * @param disabled true: button disabled; false: button enabled
	 */
	public void disabledButtons(Boolean disabled)
	{
		buttonChangeUser.setDisable(disabled);
		buttonDeleteUser.setDisable(disabled);
	}



	  /**
     * Gets the user.
     *
     * @return the current user
     */
    public User getUser()
    {
        return user;
    }

    /**
     * Sets the user.
     *
     * @param user the user to set
     */
    public void setUser(User user)
    {
        this.user = user;
    }
}
