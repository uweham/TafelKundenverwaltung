package kundenverwaltung.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.dao.BerechtigungDAOimpl;
import kundenverwaltung.dao.BescheidDAOimpl;
import kundenverwaltung.dao.FamilienmitgliedDAOimpl;
import kundenverwaltung.dao.NationDAOimpl;
import kundenverwaltung.model.Anrede;
import kundenverwaltung.model.Berechtigung;
import kundenverwaltung.model.Bescheid;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Gender;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.Nation;
import kundenverwaltung.toolsandworkarounds.ChangeDateFormat;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;
import kundenverwaltung.toolsandworkarounds.CheckOfAge;
import kundenverwaltung.toolsandworkarounds.DynamicNationDropDownMenu;
import kundenverwaltung.toolsandworkarounds.FirstLetterToUppercase;

/**
 * This class change a family member.
 *
 * @author Group_1
 * @author Richard Kromm
 * Last change:
 * 		By: Richard Kromm
 * 		On: 15.08.2018
 */
public class PersonAendernController
{
	private static final int DEFAULT_SELECTED_NATION_INDEX = 1;
	private static final int DEFAULT_SELECTED_AUTHORIZATION_INDEX = 3;
	private static final String HOUSEHOLDER_DIRECTOR_HAS_NO_ASSESSMENTS = "(Der Haushaltsvorstand hat keinen Bescheid!)";
	private static final String[] LABEL_TEST_SHOW_ASSESSMENTS = {"(Bescheide vom Haushaltsvorstand:", ")."};
	private static final String SEPERATOR = ", ";
	private static final String SPACE = " ";
	private static final int FIRST_ARRAY_POSITION = 0;
	private static final int SECOND_ARRAY_POSITION = 1;

	private static final int GENDER_ID_SONSTIGES = 73;
	private static final int GENDER_ID_KEINE_ANGABE = 74;

	private DynamicNationDropDownMenu dynamicNationDropDownMenu = new DynamicNationDropDownMenu();
	private ChangeDateFormat changeDateFormat = new ChangeDateFormat();
	private FirstLetterToUppercase firstLetterToUppercase = new FirstLetterToUppercase();
	private ChangeFontSize changeFontSize = new ChangeFontSize();
	private CheckOfAge checkOfAge = new CheckOfAge();



	//Personen aendern
	@FXML
	private Label labelKundenSuchen;
	@FXML
	private Label labelFolgendesDurchsuchen;
	@FXML
	private Label labelHaushaltsvorstand;
	@FXML
	private Label labelVerteilstelle;
	@FXML
	private Label labelKundennummer;
	@FXML
	private Label labelAnzahlFamilienmitglieder;
	@FXML
	private Label labelGruppenfarbe;
	@FXML
	private Label labelLetzterEinkauf;
	@FXML
	private Label labelWeitereInformationen;
	@FXML
	private Label labelErfassungsVerteilstelle;
	@FXML
	private Label labelBemerkungen;
	@FXML
	private Label labelWarentyp;
	@FXML
	private Label labelZuZahlen;
	@FXML
	private Label labelHeuteKassiert;
	@FXML
	private Label labelKontosaldo;
	@FXML
	private Label labelHeading;
	@FXML
	private Label labelFirstName;
	@FXML
	private Label labelSurname;
	@FXML
	private Label labelBirthday;
	@FXML
	private Label labelGender;
	@FXML
	private Label labelNation;
	@FXML
	private Label labelAuthorization;
	@FXML
	private Label labelComment;
	@FXML
	private Label labelPersonIsHouseholdDirector;
	@FXML
	private Label labelPersonIsShoppingEntitled;
	@FXML
	private Label labelShowAssessments;
	@FXML
	private Label labelAnrede;
	@FXML
	private Button btnSavePerson;
	@FXML
	private Button btnCancel;
	@FXML
	private TextField txtPAVorname;
	@FXML
	private TextField txtPANachname;
	@FXML
	private TextField txtPABemerkungen;
	@FXML
	private ComboBox<Nation> cbPANationalitaet;
	@FXML
	private ComboBox<Berechtigung> cbPABesBerechtigungen;
	@FXML
	private ComboBox<Anrede> cbPAAnrede;
	@FXML
	private ComboBox<Gender> cbPAGender;
	@FXML
	private DatePicker datePAGeburtsdatum;
	@FXML
	private CheckBox cbxEinkaufsberechtigt;
	@FXML
	private CheckBox cbxHaushaltsvorstand;
	@FXML
	private CheckBox cbxKundenausweis;
	@FXML
	private CheckBox cbxGebuehren;
	@FXML
	private CheckBox cbxDseSubmitted;
	@FXML
	private CheckBox cbxCopyAssessment;

	private Familienmitglied familienmitglied;
	private Haushalt haushalt;
	private Familienmitglied householdDirector;
	private int householdDirectorId;
	private Boolean neuHinzufuegen;
	private ArrayList<Nation> nationen;
	private ArrayList<Bescheid> assessmentArrayList;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	public void initialize()
	{
		ArrayList<Berechtigung> berechtigungen = new BerechtigungDAOimpl().getAllBerechtigungen();
		ObservableList<Berechtigung> bliste = FXCollections.observableArrayList(berechtigungen);
		cbPABesBerechtigungen.setItems(bliste);

		cbPABesBerechtigungen.getSelectionModel().selectLast();
		cbPAAnrede.getSelectionModel().selectFirst();
		cbPAGender.getSelectionModel().selectFirst();

		nationen = new NationDAOimpl().getAllEnabledNationen();
		ObservableList<Nation> nliste = FXCollections.observableArrayList(nationen);
		cbPANationalitaet.setItems(nliste);
		cbPANationalitaet.getSelectionModel().select(DEFAULT_SELECTED_NATION_INDEX);
		cbPANationalitaet.setOnShowing(event -> dynamicNationDropDownMenu.resetComboboxNation(cbPANationalitaet, nliste));
		cbPANationalitaet.setOnKeyReleased(event -> dynamicNationDropDownMenu.jumpToUserInput(cbPANationalitaet, nationen, event));
		dynamicNationDropDownMenu.onFocused(cbPANationalitaet, nationen);

		datePAGeburtsdatum.setConverter(changeDateFormat.convertDatePickerFormat());
		changeDateFormat.checkUserInputDate(datePAGeburtsdatum);

		firstLetterToUppercase.firstLetterUppercase(txtPANachname);
		firstLetterToUppercase.firstLetterUppercase(txtPAVorname);

		cbPAGender.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				evaluateShowingAnrede();
			}
		});
	}

	private boolean evaluateShowingAnrede()
	{
		Gender gender = getGenderByComboBoxIndex(cbPAGender.getSelectionModel().getSelectedIndex());

		if (gender != null)
		{
			boolean showAnrede = (gender.getGenderId() != GENDER_ID_SONSTIGES
					&& gender.getGenderId() != GENDER_ID_KEINE_ANGABE);

			cbPAAnrede.setVisible(showAnrede);
			labelAnrede.setVisible(showAnrede);
			return showAnrede;
		}

		return true;
	}

	private Gender getGenderByComboBoxIndex(int selectedIndex)
	{
        return switch (cbPAGender.getSelectionModel().getSelectedIndex())
		{
            case 0 -> new Gender(71);
            case 1 -> new Gender(72);
            case 2 -> new Gender(GENDER_ID_SONSTIGES);
            case 3 -> new Gender(GENDER_ID_KEINE_ANGABE);
            default -> null;
        };
    }


	/**
	 * Handles the event when the OK button is clicked to change a person.
	 */
	@FXML
	public void btnPersonAendernOK()
	{
		Boolean haushaltsvorstandVorher;
		try
		{
			haushaltsvorstandVorher = familienmitglied.isHaushaltsVorstand();  // by add person --> NullPointerException
		} catch (NullPointerException exception)
			{
				haushaltsvorstandVorher = true;
			}

		String vname = ersteBuchstabenGross(txtPAVorname.getText());
		String nname = ersteBuchstabenGross(txtPANachname.getText());
		String bemerkung = txtPABemerkungen.getText();
		LocalDate gDatum = datePAGeburtsdatum.getValue();
		Boolean gebuehren = cbxGebuehren.isSelected();
		Boolean ausweis = cbxKundenausweis.isSelected();
		Boolean dse = cbxDseSubmitted.isSelected();
		Boolean vorstand = cbxHaushaltsvorstand.isSelected();
		Boolean einkaufsberechtigt = cbxEinkaufsberechtigt.isSelected();
		Anrede anrede = null;
		Gender gender = null;
		Nation nation = dynamicNationDropDownMenu.getAndCheckNationValue(cbPANationalitaet, nationen);   //cbPANationalitaet.getValue();
		Berechtigung berechtigung = cbPABesBerechtigungen.getValue();

		switch (cbPAAnrede.getSelectionModel().getSelectedIndex())
		{
			case 0:
				anrede = new Anrede(32);
				break;
			case 1:
				anrede = new Anrede(31);
				break;
			case 2:
				anrede = new Anrede(34);
				break;
			case 3:
				anrede = new Anrede(33);
				break;
			case 4:
				anrede = new Anrede(36);
				break;
			case 5:
				anrede = new Anrede(35);
				break;
			case 6:
				anrede = new Anrede(38);
				break;
			case 7:
				anrede = new Anrede(37);
				break;
			default:
				break;
		}

		gender = getGenderByComboBoxIndex(cbPAGender.getSelectionModel().getSelectedIndex());

		boolean useAnrede = evaluateShowingAnrede();

		if (!useAnrede)
		{
			anrede = null;
		}

		Boolean checkVorstand = true;

		if (!haushaltsvorstandVorher && vorstand)
		{
			checkVorstand = haushaltsvorstandAendern();
		}

		if (pruefeFelder())
		{
			if (checkAge(gDatum, vorstand))
			{
				if (checkVorstand)
				{
					if (!neuHinzufuegen)
					{
						familienmitglied.setvName(vname);
						familienmitglied.setnName(nname);
						familienmitglied.setBemerkung(bemerkung);
						familienmitglied.setBerechtigung(cbPABesBerechtigungen.getValue());
						familienmitglied.setAnrede(anrede);
						familienmitglied.setGender(gender);
						familienmitglied.setNation(nation);
						familienmitglied.setgDatum(gDatum);
						familienmitglied.setGebuehrenBefreiung(gebuehren);
						familienmitglied.setAufAusweis(ausweis);
						familienmitglied.setDseSubmitted(dse);
						familienmitglied.setHaushaltsVorstand(vorstand);
						familienmitglied.setEinkaufsBerechtigt(einkaufsberechtigt);
						familienmitglied.setGeaendertAm(LocalDateTime.now());


						Boolean checkUpdate = new FamilienmitgliedDAOimpl().update(familienmitglied);


						if (checkUpdate)
						{
							Benachrichtigung.infoBenachrichtigung("Bearbeiten erfolgreich.", "Die Person wurde erfolgreich bearbeitet.");
						} else
						{
							Benachrichtigung.warnungBenachrichtigung("Person konnte nicht bearbeitet werden.", "Bitte überprüfen Sie Ihre Verbindung zur Datenbank und probieren Sie es erneut.");
						}

					} else
					{
						familienmitglied = new Familienmitglied(0, haushalt, anrede, gender, vname, nname, gDatum, bemerkung, vorstand, einkaufsberechtigt, gebuehren, nation, berechtigung, ausweis, dse, LocalDateTime.now(), LocalDateTime.now());
						Boolean checkInsert = new FamilienmitgliedDAOimpl().create(familienmitglied);

						if (checkInsert && cbxCopyAssessment.isSelected())
						{
							ArrayList<Bescheid> assessmentArrayList = new BescheidDAOimpl().readAll(householdDirector);
							for (Bescheid element : assessmentArrayList)
							{
								Bescheid assessment = new Bescheid(familienmitglied, element.getBescheidart(), element.getGueltigAb(), element.getGueltigBis());
								@SuppressWarnings("unused")
								Boolean checkInsertAssessment = new BescheidDAOimpl().create(assessment);
							}
						}

						if (checkInsert)
						{
							Benachrichtigung.infoBenachrichtigung("Anlegen erfolgreich.", "Die Person wurde erfolgreich angelegt.");
						} else
						{
							Benachrichtigung.warnungBenachrichtigung("Person konnte nicht angelegt werden.", "Bitte überprüfen Sie Ihre Verbindung zur Datenbank und probieren Sie es erneut.");
						}
					}

					Stage stage = (Stage) cbPAAnrede.getScene().getWindow();
					stage.close();
				}
			}
		} else
			{
				Benachrichtigung.infoBenachrichtigung("Achtung!", "Bitte füllen Sie alle Pflichtfelder aus.");
			}
	}

	/**
	 * Checks the age of the person.
	 *
	 * @param gDatum The birthdate of the person.
	 * @param vorstand Indicates if the person is a household director.
	 * @return Boolean indicating if the age is valid.
	 */
	public Boolean checkAge(LocalDate gDatum, Boolean vorstand)
	{
		if (vorstand)
		{
			if (checkOfAge.isAdult(gDatum))
			{
				return true;
			} else
				{
					Benachrichtigung.infoBenachrichtigung("Achtung!", "Der Haushaltsvorstand muss volljährig sein!");
					return false;
				}
		} else
			{
				if (gDatum.isBefore(LocalDate.now()))
				{
					return true;
				} else
				{
					Benachrichtigung.infoBenachrichtigung("Achtung!", "Das Geburtsdatum liegt in der Zukunft!");
					return false;
				}
			}
	}

	/**
	 * Handles the event when the Cancel button is clicked to cancel the changes.
	 */
	@FXML
	public void btnPersonAendernAbbrechen()
	{
		Stage stage = (Stage) cbPAAnrede.getScene().getWindow();
		stage.close();
	}
	/**
	 * Sets the person data in the form.
	 *
	 * @param familienmitglied The family member whose data is to be set.
	 */
	public void setzePersonendaten(Familienmitglied familienmitglied)
	{
		cbPANationalitaet.setValue(familienmitglied.getNation());
		cbPABesBerechtigungen.setValue(familienmitglied.getBerechtigung());
		txtPAVorname.setText(familienmitglied.getvName());
		txtPANachname.setText(familienmitglied.getnName());

		switch (familienmitglied.getAnrede().getAnredeId())
		{
			case 31:
				cbPAAnrede.getSelectionModel().select(1);
				break;
			case 32:
				cbPAAnrede.getSelectionModel().select(0);
				break;
			case 33:
				cbPAAnrede.getSelectionModel().select(3);
				break;
			case 34:
				cbPAAnrede.getSelectionModel().select(2);
				break;
			case 35:
				cbPAAnrede.getSelectionModel().select(5);
				break;
			case 36:
				cbPAAnrede.getSelectionModel().select(4);
				break;
			case 37:
				cbPAAnrede.getSelectionModel().select(7);
				break;
			case 38:
				cbPAAnrede.getSelectionModel().select(6);
				break;
			default:
				break;
		}

		switch (familienmitglied.getGender().getGenderId())
		{
			case 71:
				cbPAGender.getSelectionModel().select(0);
				break;
			case 72:
				cbPAGender.getSelectionModel().select(1);
				break;
			case 73:
				cbPAGender.getSelectionModel().select(2);
				break;
			case 74:
				cbPAGender.getSelectionModel().select(3);
				break;
			default:
				break;
		}

		evaluateShowingAnrede();

		datePAGeburtsdatum.setValue(familienmitglied.getgDatum());
		txtPABemerkungen.setText(familienmitglied.getBemerkung());

		if (familienmitglied.isEinkaufsBerechtigt())
		{
			cbxEinkaufsberechtigt.setSelected(true);
		} else
		{
			cbxEinkaufsberechtigt.setSelected(false);
		}
		if (familienmitglied.isAufAusweis())
		{
			cbxKundenausweis.setSelected(true);
		} else
		{
			cbxKundenausweis.setSelected(false);
		}
		if (familienmitglied.dseSubmitted())
		{
			cbxDseSubmitted.setSelected(true);
		} else
		{
			cbxDseSubmitted.setSelected(false);
		}

		if (familienmitglied.isHaushaltsVorstand())
		{
			cbxHaushaltsvorstand.setSelected(true);
			cbxHaushaltsvorstand.setDisable(true);
			cbxEinkaufsberechtigt.setSelected(true);
			cbxEinkaufsberechtigt.setDisable(true);
			cbxKundenausweis.setSelected(true);
			cbxKundenausweis.setDisable(true);
		} else
		{
			cbxHaushaltsvorstand.setSelected(false);
			cbxEinkaufsberechtigt.setDisable(false);
			cbxKundenausweis.setDisable(false);
		}

		if (familienmitglied.isGebuehrenBefreiung())
		{
			cbxGebuehren.setSelected(true);
		} else
		{
			cbxGebuehren.setSelected(false);
		}
	}

	/**
	 * Changes the household director.
	 *
	 * @return Boolean indicating if the change was successful.
	 */
	public Boolean haushaltsvorstandAendern()
	{
		Boolean checkUpdate = true;

		ArrayList<Familienmitglied> alleFamilienmitglieder = new FamilienmitgliedDAOimpl().getAllFamilienmitglieder(haushalt.getKundennummer());

		for (Familienmitglied element : alleFamilienmitglieder)
		{
			if (element.isHaushaltsVorstand())
			{
				element.setHaushaltsVorstand(false);
				checkUpdate = new FamilienmitgliedDAOimpl().update(element);
				break;
			}
		}

		return checkUpdate;
	}

	/**
	 * Checks if all required fields are filled.
	 *
	 * @return Boolean indicating if all required fields are filled.
	 */
	public Boolean pruefeFelder()
	{
		return !(txtPAVorname.getText().trim().isEmpty() || (datePAGeburtsdatum.getValue() == null || datePAGeburtsdatum.getValue().toString().isEmpty()) || txtPANachname.getText().trim().isEmpty());
	}

	/**
	 * Converts the first letter of each word in the given string to uppercase.
	 *
	 * @param string The input string.
	 * @return The formatted string.
	 */
	public String ersteBuchstabenGross(String string)
	{

		string = string.toLowerCase();
		String ergebnisstring = "";
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(string);
		while (scanner.hasNext())
		{
			String word = scanner.next();
			ergebnisstring += Character.toUpperCase(word.charAt(0)) + word.substring(1) + " ";
		}
		return ergebnisstring.trim();
	}
	/**
	 * Sets the household director and related fields.
	 */
	@FXML
	public void setzeHaushaltsvorstand()
	{
		if (cbxHaushaltsvorstand.isSelected())
		{
			cbxEinkaufsberechtigt.setSelected(true);
			cbxEinkaufsberechtigt.setDisable(true);
			cbxKundenausweis.setSelected(true);
			cbxKundenausweis.setDisable(true);
		} else
		{
			cbxEinkaufsberechtigt.setDisable(false);
			cbxKundenausweis.setDisable(false);
		}
	}


	/**
	 * Set the correct font size.
	 * @param fontSize
	 */
	public void setFontSize(Double fontSize)
	{
		DoubleProperty primaryFontSize = new SimpleDoubleProperty(fontSize);
		DoubleProperty secondaryFontSize = new SimpleDoubleProperty(fontSize - ChangeFontSize.getDifferenceBetweenPrimarySecondaryFontsize());

		ArrayList<Label> primarylabelArrayList = new ArrayList<>(Arrays.asList(
					labelHeading, labelFirstName, labelSurname, labelBirthday, labelGender,
					labelNation, labelAuthorization, labelComment, labelAnrede
				));
		changeFontSize.changeFontSizeFromLabelArrayList(primarylabelArrayList, primaryFontSize);

		ArrayList<Label> secondarylabelArrayList = new ArrayList<>(Arrays.asList(
					labelPersonIsHouseholdDirector, labelPersonIsShoppingEntitled, labelShowAssessments
				));
		changeFontSize.changeFontSizeFromLabelArrayList(secondarylabelArrayList, secondaryFontSize);

		ArrayList<Button> buttonArrayList = new ArrayList<>(Arrays.asList(btnSavePerson, btnCancel));
		changeFontSize.changeFontSizeFromButtonArrayList(buttonArrayList, primaryFontSize);

		@SuppressWarnings("rawtypes")
		ArrayList<ComboBox> comboBoxArrayList = new ArrayList<>(Arrays.asList(
					cbPAAnrede, cbPABesBerechtigungen, cbPAGender, cbPANationalitaet
				));
		changeFontSize.changeFontSizeFromComboBoxArrayList(comboBoxArrayList, primaryFontSize);

		ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>(Arrays.asList(
						cbxEinkaufsberechtigt, cbxHaushaltsvorstand, cbxKundenausweis, cbxGebuehren, cbxDseSubmitted,
						cbxCopyAssessment
				));
		changeFontSize.changeFontSizeFromCheckBoxArrayList(checkBoxArrayList, primaryFontSize);

		ArrayList<DatePicker> datePickerArrayList = new ArrayList<>(Arrays.asList(datePAGeburtsdatum));
		changeFontSize.changeFontSizeFromDatePickerArrayList(datePickerArrayList, primaryFontSize);

	}


		//Getter and Setter
	/**
	 * This function set values in PersonAendern.fxml when a new person is added.
	 *
	 * @Author Richard Kromm
	 * @param memberOfTheFamily
	 * @param addNew
	 */
	public void setFamilienmitglied(Familienmitglied memberOfTheFamily, Boolean addNew)
	{
		this.familienmitglied = memberOfTheFamily;
		this.neuHinzufuegen = addNew;

		if (addNew)
		{
			cbPABesBerechtigungen.getSelectionModel().select(DEFAULT_SELECTED_AUTHORIZATION_INDEX);
			cbPANationalitaet.getSelectionModel().select(DEFAULT_SELECTED_NATION_INDEX);

			setHouseholdDirectorId(new FamilienmitgliedDAOimpl().getHousholdDirector(getHaushalt().getKundennummer()));
			householdDirector = new FamilienmitgliedDAOimpl().read(getHouseholdDirectorId());
			assessmentArrayList = new BescheidDAOimpl().readAll(householdDirector);

			if (assessmentArrayList.size() > 0)
			{
				String allAssessmentsFromHouseholdDirector = LABEL_TEST_SHOW_ASSESSMENTS[FIRST_ARRAY_POSITION];
				for (int i = 0; i < assessmentArrayList.size(); i++)
				{
					if (i > 0)
					{
						allAssessmentsFromHouseholdDirector += SEPERATOR + assessmentArrayList.get(i).getBescheidName();
					} else
					{
						allAssessmentsFromHouseholdDirector += SPACE + assessmentArrayList.get(i).getBescheidName();
					}
				}
				allAssessmentsFromHouseholdDirector += LABEL_TEST_SHOW_ASSESSMENTS[SECOND_ARRAY_POSITION];
				labelShowAssessments.setText(allAssessmentsFromHouseholdDirector);
			} else
				{
					cbxCopyAssessment.setDisable(true);
					labelShowAssessments.setText(HOUSEHOLDER_DIRECTOR_HAS_NO_ASSESSMENTS);
					labelShowAssessments.setDisable(true);
				}
		} else
			{
				cbxCopyAssessment.setDisable(true);
				labelShowAssessments.setText("");
			}
	}
	/**
	 * Gets the family member.
	 *
	 * @return The family member.
	 */
	public Familienmitglied getFamilienmitglied()
	{
		return familienmitglied;
	}
	/**
	 * Sets the household.
	 *
	 * @param haushalt The household to set.
	 */
	public void setHaushalt(Haushalt haushalt)
	{
		this.haushalt = haushalt;
	}
	/**
	 * Gets the household.
	 *
	 * @return The household.
	 */
	public Haushalt getHaushalt()
	{
		return haushalt;
	}
	/**
	 * Gets the household director ID.
	 *
	 * @return The household director ID.
	 */
	public int getHouseholdDirectorId()
	{
		return householdDirectorId;
	}
	/**
	 * Sets the household director ID.
	 *
	 * @param householdDirectorId The household director ID to set.
	 */
	public void setHouseholdDirectorId(int householdDirectorId)
	{
		this.householdDirectorId = householdDirectorId;
	}
}
