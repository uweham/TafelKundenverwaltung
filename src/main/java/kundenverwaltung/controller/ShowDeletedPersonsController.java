package kundenverwaltung.controller;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import kundenverwaltung.dao.DeletedMemberOfTheFamilyDAOimpl;
import kundenverwaltung.model.DeletedMemberOfTheFamily;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.service.TablePreferenceServiceImpl;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;


/**
 * This class shows all deleted family member in a table and the reason of the removal.
 *
 * @Author Richard Kromm
 * @Date 31.07.2018
 */
public class ShowDeletedPersonsController
{
    @FXML
    private Label labelHeading;

    @FXML
    private Label labelListDeletedPersons;

    @FXML
    private Label labelReasonDelete;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableView tableViewDeletedPersons;
	@SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnId;

    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnName;

    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnBirthday;

    @SuppressWarnings("rawtypes")
    @FXML
    private TableColumn columnDeletedOn;
	@FXML
	private TextArea textAreaReasonDeletePerson;
	@FXML
	private Button btnCancel;

	private int householdId;
	private ArrayList<DeletedMemberOfTheFamily> deletedMemberOfTheFamilyArrayList;
	private ObservableList<DeletedMemberOfTheFamily> deletedMemberOfTheFamilyObservableList;

	private ChangeFontSize changeFontSize = new ChangeFontSize();

	 /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
	@SuppressWarnings("unchecked")
	public void initialize()
	{
		columnId.setCellValueFactory(new PropertyValueFactory<>("autoincrementId"));
		columnId.setId("autoincrementId");

		columnName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
		columnName.setId("fullName");

		columnBirthday.setCellValueFactory(new PropertyValueFactory<>("birthday"));
		columnBirthday.setId("birthday");

		columnDeletedOn.setCellValueFactory(new PropertyValueFactory<>("deletedOn"));
		columnDeletedOn.setId("deletedOn");

		tableViewDeletedPersons.setOnMouseClicked(event -> showReasonDelete());

		tableViewDeletedPersons.setOnKeyReleased(event ->
		{
			if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN)
			{
				showReasonDelete();
			}
		});

		TablePreferenceServiceImpl.getInstance().setupPersistence(tableViewDeletedPersons, "TableViewDeletedPersons");
	}



	/**
	 * Gets all deleted family member from householdId.
	 * @param household
	 */
	@SuppressWarnings("unchecked")
	public void showDeletedMemberOfTheFamilyInTable(Haushalt household)
	{
		setHouseholdId(household.getKundennummer());
		setDeletedMemberOfTheFamilyArrayList(new DeletedMemberOfTheFamilyDAOimpl().getAllDeletedMemberOfTheFamily(householdId));
		setDeletedMemberOfTheFamilyObservableList(FXCollections.observableArrayList(deletedMemberOfTheFamilyArrayList));

		tableViewDeletedPersons.setItems(deletedMemberOfTheFamilyObservableList);
	}

	/**
	 * Write the reason for deletion in the textarea.
	 */
	public void showReasonDelete()
	{
		if (tableViewDeletedPersons.getSelectionModel().getSelectedItem() != null)
		{
			int selectedRow = tableViewDeletedPersons.getSelectionModel().getSelectedIndex();
			int selectedId = (int) columnId.getCellData(selectedRow);

			int runVarArrayList = 0;
			while (runVarArrayList < deletedMemberOfTheFamilyArrayList.size())
			{
				if (selectedId == deletedMemberOfTheFamilyArrayList.get(runVarArrayList).getAutoincrementId())
				{
					break;
				}
				runVarArrayList++;
			}
			textAreaReasonDeletePerson.setText(deletedMemberOfTheFamilyArrayList.get(runVarArrayList).getReasonDelete());
		}
	}

	/**
	 * This function closes this window.
	 */
	@FXML
	public void btnCancelDeletePerson()
	{
		Stage stage = (Stage) textAreaReasonDeletePerson.getScene().getWindow();
		stage.close();
	}

	/**
	 * Set the corrects font size.
	 * @param fontSize
	 */
	public void setFontSize(Double fontSize)
	{
		DoubleProperty newFontSize = new SimpleDoubleProperty(fontSize);

		ArrayList<Label> labelArrayList = new ArrayList<>(Arrays.asList(labelHeading, labelListDeletedPersons, labelReasonDelete));
		changeFontSize.changeFontSizeFromLabelArrayList(labelArrayList, newFontSize);

		ArrayList<TextArea> textAreaArrayList = new ArrayList<>(Arrays.asList(textAreaReasonDeletePerson));
		changeFontSize.changeFontSizeFromTextAreaArrayList(textAreaArrayList, newFontSize);

		@SuppressWarnings("rawtypes")
		ArrayList<TableColumn> tableColumnArrayList = new ArrayList<>(Arrays.asList(columnId, columnName, columnBirthday, columnDeletedOn));
		changeFontSize.changeFontSizeFromTableColumnArrayList(tableColumnArrayList, newFontSize);

		ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(btnCancel));
		changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, newFontSize);

		changeFontSize.changeFontSizeFromTableColumn(columnName, fontSize);
	}

	/**
     * Returns the household ID.
     *
     * @return the household ID
     */
	public int getHouseholdId()
	{
		return householdId;
	}
	/**
     * Sets the household ID.
     *
     * @param householdId the new household ID
     */
	public void setHouseholdId(int householdId)
	{
		this.householdId = householdId;
	}
	  /**
     * Returns the list of deleted family members.
     *
     * @return the list of deleted family members
     */
	public ArrayList<DeletedMemberOfTheFamily> getDeletedMemberOfTheFamilyArrayList()
	{
		return deletedMemberOfTheFamilyArrayList;
	}
	 /**
     * Sets the list of deleted family members.
     *
     * @param deletedMemberOfTheFamilyArrayList the new list of deleted family members
     */
	public void setDeletedMemberOfTheFamilyArrayList(ArrayList<DeletedMemberOfTheFamily> deletedMemberOfTheFamilyArrayList)
	{
		this.deletedMemberOfTheFamilyArrayList = deletedMemberOfTheFamilyArrayList;
	}
	/**
     * Returns the observable list of deleted family members.
     *
     * @return the observable list of deleted family members
     */
	public ObservableList<DeletedMemberOfTheFamily> getDeletedMemberOfTheFamilyObservableList()
	{
		return deletedMemberOfTheFamilyObservableList;
	}
	/**
     * Sets the observable list of deleted family members.
     *
     * @param deletedMemberOfTheFamilyObservableList the new observable list of deleted family members
     */
	public void setDeletedMemberOfTheFamilyObservableList(ObservableList<DeletedMemberOfTheFamily> deletedMemberOfTheFamilyObservableList)
	{
		this.deletedMemberOfTheFamilyObservableList = deletedMemberOfTheFamilyObservableList;
	}
}
