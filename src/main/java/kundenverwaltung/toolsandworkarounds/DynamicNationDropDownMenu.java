package kundenverwaltung.toolsandworkarounds;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.model.Nation;


/**
 * This class create a dynamic drop down menu for all nations.
 *
 * @Author Richard Kromm
 * @Date 11.07.2018
 * @Version 2.0
 * Last change:
 * 		By: Richard Kromm
 * 		On: 20.07.2018
 */
public class DynamicNationDropDownMenu
{
	private static final Nation ILLEGAL_NATION_ORDER_BY_ALPHABETICAL = new Nation(-1, "------ ALPHABETISCH ------", "", false);
	private static final int EXPECTED_ALPHABETICAL_SORTING_NATIONLIST_FIRST_POSITION = 8;
	private static final int NOT_SELECTED_INDEX_IN_COMBOBOX = -1;
	private static final String UMLAUTS_AE = "Ä";
	private static final String UMLAUTS_OE = "Ö";
	private static final String UMLAUTS_UE = "Ü";

	private static final String NOTIFICATION_TITEL_DEFAULT = "Achtung!";
	private static final String NOTIFICATION_TEXT_INVALID_NATION = "Ungültige Nationalität.";


	/**
	 * This function reads the user input from combobox, searches for a matching nation and sets this nation on first position.
	 *
	 * @param comboBox
	 * @param defaultNationArrayList
	 * @param keyEvent
	 */
	@SuppressWarnings("unchecked")
	public void jumpToUserInput(@SuppressWarnings("rawtypes") ComboBox comboBox, ArrayList<Nation> defaultNationArrayList, KeyEvent keyEvent)
	{
		ArrayList<Nation> resultNationArrayList = new ArrayList<>();
		ObservableList<Nation> resultNationObservableList;

		if (comboBox.getEditor().getText().length() > 0)
		{
			String userInput = comboBox.getEditor().getText().toUpperCase();
			boolean isUmlauts = userInput.substring(userInput.length() - 1).equals(UMLAUTS_AE)
		        || userInput.substring(userInput.length() - 1).equals(UMLAUTS_OE)
		        || userInput.substring(userInput.length() - 1).equals(UMLAUTS_UE);

			if (keyEvent.getCode().isLetterKey()
					|| isUmlauts
					|| keyEvent.getCode() == KeyCode.BACK_SPACE
					|| keyEvent.getCode() == KeyCode.DELETE)
			{
				boolean findMatchingNation = false;
				int runVarSearchMatchingNation = getFirstPositionAlphabeticalSortingNation(defaultNationArrayList);
				String subStringNation;

				while (runVarSearchMatchingNation < defaultNationArrayList.size()
						&& !findMatchingNation)
				{
					try
					{
						subStringNation = defaultNationArrayList.get(runVarSearchMatchingNation).getName().substring(0, userInput.length()).toUpperCase();
					} catch (StringIndexOutOfBoundsException e)
						{
							subStringNation = defaultNationArrayList.get(runVarSearchMatchingNation).getName().toUpperCase();
						}

					if (userInput.equals(subStringNation))
					{
						findMatchingNation = true;
					} else
					{
						runVarSearchMatchingNation++;
					}
				}

				while (runVarSearchMatchingNation < defaultNationArrayList.size()
						&& findMatchingNation)
				{
					resultNationArrayList.add(defaultNationArrayList.get(runVarSearchMatchingNation));
					runVarSearchMatchingNation++;
				}

				resultNationObservableList = FXCollections.observableArrayList(resultNationArrayList);
				comboBox.setItems(resultNationObservableList);
			}
		} else
			{
				resultNationObservableList = FXCollections.observableArrayList(defaultNationArrayList);
				comboBox.setItems(resultNationObservableList);
			}
	}


	/**
	 * This function reads the user input and controlled for correctness.
	 *
	 * @param defaultNationArrayList
	 * @throws .NullPointerException by illegal nation
	 * @return the selected nation or null by invalid user input
	 */
	public Nation getAndCheckNationValue(@SuppressWarnings("rawtypes") ComboBox comboBox, ArrayList<Nation> defaultNationArrayList)
	{
		String input = comboBox.getEditor().getText().toUpperCase();

		int runVarSearchNation = getFirstPositionAlphabeticalSortingNation(defaultNationArrayList);
		while (runVarSearchNation < defaultNationArrayList.size())
		{
			if (defaultNationArrayList.get(runVarSearchNation).getName().toUpperCase().equals(input))
			{
				return defaultNationArrayList.get(runVarSearchNation);
			} else
			{
				runVarSearchNation++;
			}
		}

		Benachrichtigung.infoBenachrichtigung(NOTIFICATION_TITEL_DEFAULT, NOTIFICATION_TEXT_INVALID_NATION);
		ObservableList<Nation> defaultNationObservableList = FXCollections.observableArrayList(defaultNationArrayList);
		resetComboboxNation(comboBox, defaultNationObservableList);
		return null;
	}

	/**
	 * This function opens the nations combobox by focused.
	 *
	 * @param comboBoxNation
	 * @param defaultNationArrayList
	 */
	public void onFocused(@SuppressWarnings("rawtypes") ComboBox comboBoxNation, ArrayList<Nation> defaultNationArrayList)
	{
		comboBoxNation.focusedProperty().addListener((observable, oldValue, newValue) ->
		{
			if (newValue)
			{
				Platform.runLater(() -> comboBoxNation.show());
			} else
			{
				getAndCheckNationValue(comboBoxNation, defaultNationArrayList);
			}
		});
	}

	/**
	 * This function resets the nations combobox.
	 *
	 * @param comboBox
	 * @param defaultObservableList
	 */
	@SuppressWarnings("unchecked")
	public void resetComboboxNation(@SuppressWarnings("rawtypes") ComboBox comboBox, @SuppressWarnings("rawtypes") ObservableList defaultObservableList)
	{
		comboBox.setItems(defaultObservableList);
		comboBox.getSelectionModel().select(NOT_SELECTED_INDEX_IN_COMBOBOX);
		comboBox.getEditor().setText("");
	}


	//Getter and Setter

	/**
	 * Get the first position where the alphabetical sorting starts.
	 *
	 * @param nationArrayList
	 * @return
	 */
	public int getFirstPositionAlphabeticalSortingNation(ArrayList<Nation> nationArrayList)
	{
		int runVar = 0;
		while (runVar < nationArrayList.size())
		{
			if (nationArrayList.get(runVar).getName().equals(ILLEGAL_NATION_ORDER_BY_ALPHABETICAL.getName()))
			{
				return runVar;
			} else
			{
				runVar++;
			}
		}
		return EXPECTED_ALPHABETICAL_SORTING_NATIONLIST_FIRST_POSITION;
	}
}
