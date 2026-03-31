package kundenverwaltung.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kundenverwaltung.controller.admintool.AdmintoolController;
import kundenverwaltung.controller.admintool.AusgabetagZeitController;
import kundenverwaltung.controller.admintool.AusgabetageVerwaltenController;
import kundenverwaltung.controller.admintool.BenutzerverwaltungNeuController;
import kundenverwaltung.controller.admintool.ChangeVerteilstelleController;
import kundenverwaltung.controller.admintool.NeueAusgabegruppeController;
import kundenverwaltung.controller.admintool.NeueVerteilstelleController;
import kundenverwaltung.controller.admintool.NeuerWarentypController;
import kundenverwaltung.controller.admintool.UserAddChangeController;
import kundenverwaltung.logger.event.GlobalEventLogger;
import kundenverwaltung.model.Benutzer;
import kundenverwaltung.model.Bescheid;
import kundenverwaltung.model.Einkauf;
import kundenverwaltung.model.Familienmitglied;
import kundenverwaltung.model.Haushalt;
import kundenverwaltung.model.User;
import kundenverwaltung.model.Vollmacht;
import kundenverwaltung.model.Warentyp;
import kundenverwaltung.service.Booking_err_warn_list;
import kundenverwaltung.toolsandworkarounds.ChangeFontSize;



public class MainController
{

    private Stage primaryStage;
    private Haushalt haushalt;
    private static MainController instance;
    @SuppressWarnings("unused")
	private static ChangeFontSize changeFontSize = new ChangeFontSize();
    private User user;

    public static MainController getInstance()
    {
        if (instance == null)
        {
            instance = new MainController();
        }

        return instance;
    }


    /**
     * Sets the primary stage for the application.
     *
     * @param primaryStage the primary stage to set.
     */
    public void setPrimaryStage(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
    }

    /**
     * Loads and shows the main window.
     *
     * @param user the user data to initialize the main window with.
     */
    public void mainWindow(User user)
    {
        this.user = user;

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/MainWindow.fxml"));
            AnchorPane pane = loader.load();

            Scene mainScene = new Scene(pane);
            GlobalEventLogger.attachTo("MainWindow", mainScene);

            MainWindowController mainWindowController = loader.getController();
            mainWindowController.setUserRights(user);

            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Tafel-Verwaltungsprogramm");
            primaryStage.setMinWidth(1024);
            primaryStage.setMinHeight(686);

            primaryStage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Opens the user profile window.
     *
     * @param user the user data to display in the profile.
     * @param fontSize the font size to set in the profile window.
     */
    public void oeffneBenutzerprofil(User user, Double fontSize)
    {
        try
        {

        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/Benutzerprofil.fxml"));
            AnchorPane pane = loader.load();
            Stage benutzerProfilStage = new Stage();

            Scene benutzerProfilScene = new Scene(pane);
            GlobalEventLogger.attachTo("Benutzerprofil", benutzerProfilScene);
            benutzerProfilStage.setScene(benutzerProfilScene);

            BenutzerprofilController benutzerprofilController = loader.getController();
            benutzerprofilController.setUserData(user);
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                benutzerprofilController.setFontSize(fontSize);
            }
            benutzerProfilStage.setTitle("Benutzerprofil verwalten");
            benutzerProfilStage.initStyle(StageStyle.DECORATED);
            benutzerProfilStage.initModality(Modality.APPLICATION_MODAL);
            benutzerProfilStage.centerOnScreen();
            benutzerProfilStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Opens the login window.
     */
    public void oeffneAnmeldung()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/Anmeldung.fxml"));
            AnchorPane pane = loader.load();
            Stage anmeldungStage = new Stage();

            Scene anmeldungScene = new Scene(pane);
            GlobalEventLogger.attachTo("Anmeldung", anmeldungScene);
            anmeldungStage.setScene(anmeldungScene);

            @SuppressWarnings("unused")
            AnmeldungController anmeldungController = loader.getController();
            anmeldungStage.setTitle("Anmeldung");
            anmeldungStage.setMinWidth(500);
            anmeldungStage.setMinHeight(320);
            anmeldungStage.setMaxWidth(500);
            anmeldungStage.setMaxHeight(320);
            anmeldungStage.initStyle(StageStyle.DECORATED);
            anmeldungStage.initModality(Modality.APPLICATION_MODAL);
            anmeldungStage.centerOnScreen();
            anmeldungStage.show();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

/**
 * Opens the ID card printing window.
 *
 * @param familymember the family member data to use for printing.
 * @param fontSize the font size to set in the printing window.
 */
    public void oeffneAusweisDrucken(Familienmitglied familymember, Double fontSize)
    {
        try
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/AusweisDrucken.fxml"));
            AnchorPane pane = loader.load();
            Stage ausweisDruckenStage = new Stage();

            Scene ausweisDruckenScene = new Scene(pane);
            GlobalEventLogger.attachTo("AusweisDrucken", ausweisDruckenScene);
            ausweisDruckenStage.setScene(ausweisDruckenScene);

            AusweisDruckenController ausweisDruckenController = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                ausweisDruckenController.setFontSize(fontSize);
            }

            if (familymember != null
                )
            {
                ausweisDruckenController.setRbAktuellerKundeDisabled(false);
                ausweisDruckenController.setFamilymember(familymember);
            }

            ausweisDruckenStage.setTitle("Ausweise drucken");
            ausweisDruckenStage.initStyle(StageStyle.DECORATED);
            ausweisDruckenStage.initModality(Modality.APPLICATION_MODAL);
            ausweisDruckenStage.centerOnScreen();
            ausweisDruckenStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Opens the receipt printing window.
     *
     * @param familyMember the family member data to use for printing.
     * @param fontSize the font size to set in the printing window.
     */
    public KassenbelegDruckenController oeffneKassenbelegDrucken(Familienmitglied familyMember, Double fontSize)
    {
      try
      {
          // FXML-Datei laden
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/KassenbelegDrucken.fxml"));
          AnchorPane pane = loader.load();

          // Neue Stage und Scene erstellen
          Stage kassenbelegDruckenStage = new Stage();

          Scene kassenbelegDruckenScene = new Scene(pane);
          GlobalEventLogger.attachTo("KassenbelegDrucken", kassenbelegDruckenScene);
          kassenbelegDruckenStage.setScene(kassenbelegDruckenScene);

          // Controller holen
          KassenbelegDruckenController kassenbelegDruckenController = loader.getController();

          // FamilyMember und RadioButton-Einstellungen setzen
          if (familyMember != null)
          {
              kassenbelegDruckenController.setFamilyMember(familyMember);
              kassenbelegDruckenController.disabledRadioButtonCurrentCustomer(false);
          }

          // Schriftgröße setzen, falls nicht Standard
          if (fontSize != ChangeFontSize.getDefaultFontSize())
          {
              kassenbelegDruckenController.setFontSize(fontSize);
          }

          // Stage-Einstellungen
          kassenbelegDruckenStage.setTitle("Kassenbeleg erstellen");
          kassenbelegDruckenStage.initStyle(StageStyle.DECORATED);
          kassenbelegDruckenStage.initModality(Modality.APPLICATION_MODAL);
          kassenbelegDruckenStage.centerOnScreen();
          kassenbelegDruckenStage.show();

          // Den Controller zurückgeben
          return kassenbelegDruckenController;

      } catch (IOException e)
      {
          e.printStackTrace();
          return null; // Fehlerbehandlung, falls etwas schiefgeht
      }
  }

    /**
     * Opens the customer list creation window.
     *
     * @param fontSize the font size to set in the creation window.
     */
    public void oeffneKundenlisteErstellen(Double fontSize)
    {
        try
        {

        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/KundenlisteErstellen.fxml"));
            AnchorPane pane = loader.load();
            Stage kundenlisteErstellenStage = new Stage();

            Scene kundenlisteErstellenScene = new Scene(pane);
            GlobalEventLogger.attachTo("KundenlisteErstellen", kundenlisteErstellenScene);
            kundenlisteErstellenStage.setScene(kundenlisteErstellenScene);

            KundenlisteErstellenController kundenlisteErstellenController = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                kundenlisteErstellenController.setFontSize(fontSize);
            }
            kundenlisteErstellenStage.setTitle("Kundenliste erstellen");
            kundenlisteErstellenStage.initStyle(StageStyle.DECORATED);
            kundenlisteErstellenStage.initModality(Modality.APPLICATION_MODAL);
            kundenlisteErstellenStage.centerOnScreen();
            kundenlisteErstellenStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Opens the miscellaneous printing window.
     *
     * @param familymember the family member data to use for printing.
     * @param fontSize the font size to set in the printing window.
     */
    public void oeffneSonstigesDrucken(Familienmitglied familymember, Double fontSize)
    {
        try
        {

            FXMLLoader loader = new FXMLLoader(kundenverwaltung.Main.class.getResource(
                    "/kundenverwaltung/fxml/PrintConsentForm.fxml"));
            AnchorPane pane = loader.load();
            Stage sonstigesDruckenStage = new Stage();

            Scene sonstigesDruckenScene = new Scene(pane);
            GlobalEventLogger.attachTo("PrintConsentForm", sonstigesDruckenScene);
            sonstigesDruckenStage.setScene(sonstigesDruckenScene);

            PrintConsentForm printConsentForm = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                printConsentForm.setFontSize(fontSize);
            }
            if (familymember != null
                )
            {
                printConsentForm.setRbAktuellerKundeDisabled(false);
                printConsentForm.setFamilymember(familymember);
            }
            sonstigesDruckenStage.setTitle("Kundenliste erstellen");
            sonstigesDruckenStage.initStyle(StageStyle.DECORATED);
            sonstigesDruckenStage.initModality(Modality.APPLICATION_MODAL);
            sonstigesDruckenStage.centerOnScreen();
            sonstigesDruckenStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Opens the database connection window.
     *
     * @param fontSize the font size to set in the database connection window.
     */
    public void oeffneDBVerbindung(Double fontSize)
    {
        try
        {

        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/DBVerbindung.fxml"));
            AnchorPane pane = loader.load();
            Stage dbVerbindungStage = new Stage();

            Scene dbVerbindungScene = new Scene(pane);
            GlobalEventLogger.attachTo("DBVerbindung", dbVerbindungScene);
            dbVerbindungStage.setScene(dbVerbindungScene);

            DatabaseConnectionController dbVerbindungController = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                dbVerbindungController.setFontSize(fontSize);
            }
            dbVerbindungStage.setTitle("Datenbank neu verbinden");
            dbVerbindungStage.initStyle(StageStyle.DECORATED);
            dbVerbindungStage.initModality(Modality.APPLICATION_MODAL);
            dbVerbindungStage.centerOnScreen();
            dbVerbindungStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private NeueVerteilstelleController neueVerteilstelleController;

    /**
     * Returns the NeueVerteilstelleController instance.
     *
     * @return the NeueVerteilstelleController instance
     */
    public NeueVerteilstelleController getNeueVerteilstelleController()
    {
      return neueVerteilstelleController;
  }

  /**
   * Sets the NeueVerteilstelleController instance.
   *
   * @param neueVerteilstelleController the NeueVerteilstelleController to set
   */
  public void setNeueVerteilstelleController(NeueVerteilstelleController neueVerteilstelleController)
  {
      this.neueVerteilstelleController = neueVerteilstelleController;
  }
    /**
     * Opens the window for adding or changing a user.
     *
     * @param user the user data to modify, or null to add a new user.
     */
    public void openUserAddChange(User user)
    {
        try
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/admintool/UserAddChange.fxml"));
            BorderPane borderPane = loader.load();
            Stage userAddChangeStage = new Stage();

            Scene userAddChangeScene = new Scene(borderPane);
            GlobalEventLogger.attachTo("UserAddChange", userAddChangeScene);
            userAddChangeStage.setScene(userAddChangeScene);

            UserAddChangeController userAddChangeController = loader.getController();
            if (user != null)
            {
                userAddChangeController.setUserData(user);
                userAddChangeController.setUser(user);
            }
            userAddChangeStage.setTitle((user != null) ?  "Benutzer ändern" : "Benutzer erstellen");
            userAddChangeStage.initStyle(StageStyle.DECORATED);
            userAddChangeStage.initModality(Modality.APPLICATION_MODAL);
            userAddChangeStage.centerOnScreen();
            userAddChangeStage.showAndWait();

        } catch (IOException e)
        {
            e.printStackTrace();
        }


    }
    /**
     * Opens the window for creating a new user.
     */
    public void oeffneNeuerBenutzer()
    {
        try
        {

        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/admintool/BenutzerverwaltungNeu.fxml"));
            BorderPane pane = loader.load();
            Stage neueVerteilstelleStage = new Stage();

            Scene neueVerteilstelleScene = new Scene(pane);
            GlobalEventLogger.attachTo("BenutzerverwaltungNeu", neueVerteilstelleScene);
            neueVerteilstelleStage.setScene(neueVerteilstelleScene);

            @SuppressWarnings("unused")
			BenutzerverwaltungNeuController benutzerverwaltungNeuController = loader.getController();
            neueVerteilstelleStage.setTitle("Benutzer erstellen");
            neueVerteilstelleStage.initStyle(StageStyle.DECORATED);
            neueVerteilstelleStage.initModality(Modality.APPLICATION_MODAL);
            neueVerteilstelleStage.centerOnScreen();
            neueVerteilstelleStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Opens the window for modifying an existing user.
     *
     * @param benutzer the user data to modify.
     */
    public void oeffneChangeBenutzer(Benutzer benutzer)
    {
        try
        {

        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/admintool/BenutzerverwaltungNeu.fxml"));
            BorderPane pane = loader.load();
            Stage neueVerteilstelleStage = new Stage();

            Scene neueVerteilstelleScene = new Scene(pane);
            GlobalEventLogger.attachTo("BenutzerverwaltungNeu", neueVerteilstelleScene);
            neueVerteilstelleStage.setScene(neueVerteilstelleScene);

            BenutzerverwaltungNeuController benutzerverwaltungNeuController = loader.getController();
            benutzerverwaltungNeuController.updateBenutzer(benutzer);
            neueVerteilstelleStage.setTitle("Benutzer erstellen");
            neueVerteilstelleStage.initStyle(StageStyle.DECORATED);
            neueVerteilstelleStage.initModality(Modality.APPLICATION_MODAL);
            neueVerteilstelleStage.centerOnScreen();
            neueVerteilstelleStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Opens the window for creating a new issue date and time.
     */
    public void oeffneAusgabetagZeit()
    {
        try
        {

        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/admintool/AusgabetagZeit.fxml"));
            AnchorPane pane = loader.load();
            Stage neueVerteilstelleStage = new Stage();

            Scene neueVerteilstelleScene = new Scene(pane);
            GlobalEventLogger.attachTo("AusgabetagZeit", neueVerteilstelleScene);
            neueVerteilstelleStage.setScene(neueVerteilstelleScene);

            @SuppressWarnings("unused")
			AusgabetagZeitController ausgabetagZeitController = loader.getController();
            neueVerteilstelleStage.setTitle("Ausgabetag und Zeit erstellen");
            neueVerteilstelleStage.initStyle(StageStyle.DECORATED);
            neueVerteilstelleStage.initModality(Modality.APPLICATION_MODAL);
            neueVerteilstelleStage.centerOnScreen();
            neueVerteilstelleStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Opens the window for managing issue dates.
     */
    public void oeffneAusgabetageVerwalten()
    {
        try
        {

        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/admintool/AusgabetageVerwalten.fxml"));
            AnchorPane pane = loader.load();
            Stage neueVerteilstelleStage = new Stage();

            Scene neueVerteilstelleScene = new Scene(pane);
            GlobalEventLogger.attachTo("AusgabetageVerwalten", neueVerteilstelleScene);
            neueVerteilstelleStage.setScene(neueVerteilstelleScene);

            @SuppressWarnings("unused")
			AusgabetageVerwaltenController ausgabetagZeitController = loader.getController();
            neueVerteilstelleStage.setTitle("Ausgabetage verwalten");
            neueVerteilstelleStage.initStyle(StageStyle.DECORATED);
            neueVerteilstelleStage.initModality(Modality.APPLICATION_MODAL);
            neueVerteilstelleStage.centerOnScreen();
            neueVerteilstelleStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Opens the window for creating a new distribution point.
     */
    public void oeffneNeueVerteilstelle()
    {
        try
        {

        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/admintool/NeueVerteilstelle.fxml"));
            AnchorPane pane = loader.load();
            Stage neueVerteilstelleStage = new Stage();

            Scene neueVerteilstelleScene = new Scene(pane);
            GlobalEventLogger.attachTo("NeueVerteilstelle", neueVerteilstelleScene);
            neueVerteilstelleStage.setScene(neueVerteilstelleScene);

            @SuppressWarnings("unused")
			NeueVerteilstelleController neueVerteilstelleController = loader.getController();
            neueVerteilstelleStage.setTitle("Neue Verteilstelle");
            neueVerteilstelleStage.initStyle(StageStyle.DECORATED);
            neueVerteilstelleStage.initModality(Modality.APPLICATION_MODAL);
            neueVerteilstelleStage.centerOnScreen();
            neueVerteilstelleStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Opens the window for modifying an existing distribution point.
     */
    public void oeffneChangeVerteilstelle()
    {
        try
        {

        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/admintool/ChangeVerteilstelle.fxml"));
            AnchorPane pane = loader.load();
            Stage neueVerteilstelleStage = new Stage();

            Scene neueVerteilstelleScene = new Scene(pane);
            GlobalEventLogger.attachTo("ChangeVerteilstelle", neueVerteilstelleScene);
            neueVerteilstelleStage.setScene(neueVerteilstelleScene);

            @SuppressWarnings("unused")
			ChangeVerteilstelleController changeVerteilstelleController = loader.getController();
            neueVerteilstelleStage.setTitle("Verteilstelle ändern");
            neueVerteilstelleStage.initStyle(StageStyle.DECORATED);
            neueVerteilstelleStage.initModality(Modality.APPLICATION_MODAL);
            neueVerteilstelleStage.centerOnScreen();
            neueVerteilstelleStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Opens the window for creating a new issue group.
     */
    public void oeffneNeueAusgabegruppe()
    {
        try
        {

        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/admintool/NeueAusgabegruppe.fxml"));
            AnchorPane pane = loader.load();
            Stage neueVerteilstelleStage = new Stage();

            Scene neueVerteilstelleScene = new Scene(pane);
            GlobalEventLogger.attachTo("NeueAusgabegruppe", neueVerteilstelleScene);
            neueVerteilstelleStage.setScene(neueVerteilstelleScene);

            @SuppressWarnings("unused")
			NeueAusgabegruppeController neueAusgabegruppeController = loader.getController();
            neueVerteilstelleStage.setTitle("Neue Ausgabegruppe");
            neueVerteilstelleStage.initStyle(StageStyle.DECORATED);
            neueVerteilstelleStage.initModality(Modality.APPLICATION_MODAL);
            neueVerteilstelleStage.centerOnScreen();
            neueVerteilstelleStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Opens the window for creating a new product type.
     */
    public void oeffneNeuerWarentyp()
    {
        try
        {

        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/admintool/NeuerWarentyp.fxml"));
            AnchorPane pane = loader.load();
            Stage neueVerteilstelleStage = new Stage();

            Scene neueVerteilstelleScene = new Scene(pane);
            GlobalEventLogger.attachTo("NeuerWarentyp", neueVerteilstelleScene);
            neueVerteilstelleStage.setScene(neueVerteilstelleScene);

            @SuppressWarnings("unused")
			NeuerWarentypController neuerWarentypController = loader.getController();
            neueVerteilstelleStage.setTitle("Neuer Warentyp");
            neueVerteilstelleStage.initStyle(StageStyle.DECORATED);
            neueVerteilstelleStage.initModality(Modality.APPLICATION_MODAL);
            neueVerteilstelleStage.centerOnScreen();
            neueVerteilstelleStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Opens the window for modifying an existing product type.
     *
     * @param warentyp the product type data to modify.
     */
    public void oeffneChangeWarentyp(Warentyp warentyp)
    {
        try
        {

        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/admintool/NeuerWarentyp.fxml"));
            AnchorPane pane = loader.load();
            Stage neueVerteilstelleStage = new Stage();

            Scene neueVerteilstelleScene = new Scene(pane);
            GlobalEventLogger.attachTo("NeuerWarentyp", neueVerteilstelleScene);
            neueVerteilstelleStage.setScene(neueVerteilstelleScene);

            NeuerWarentypController neuerWarentypController = loader.getController();
            neuerWarentypController.warentypUpdate(warentyp);
            neueVerteilstelleStage.setTitle("Warentyp umbenennen");
            neueVerteilstelleStage.initStyle(StageStyle.DECORATED);
            neueVerteilstelleStage.initModality(Modality.APPLICATION_MODAL);
            neueVerteilstelleStage.centerOnScreen();
            neueVerteilstelleStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Opens the window for adding a new household.
     *
     * @param fontSize the font size to set in the creation window.
     */
    public Familienmitglied oeffneHaushaltHinzufuegen(Double fontSize)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/HaushaltAnlegen.fxml"));
            AnchorPane pane = loader.load();
            Stage haushaltAnlegenStage = new Stage();

            Scene haushaltAnlegenScene =  new Scene(pane);
            GlobalEventLogger.attachTo("HaushaltAnlegen", haushaltAnlegenScene);
            haushaltAnlegenStage.setScene(haushaltAnlegenScene);

            HaushaltAnlegenController haushaltAnlegenController = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                haushaltAnlegenController.setFontSize(fontSize);
            }
            haushaltAnlegenStage.setTitle("Neuen Haushalt anlegen");
            haushaltAnlegenStage.initStyle(StageStyle.DECORATED);
            haushaltAnlegenStage.initModality(Modality.APPLICATION_MODAL);
            haushaltAnlegenStage.centerOnScreen();
            
            // Blockiert, bis das Fenster geschlossen wird
            haushaltAnlegenStage.showAndWait();

            // NEU: Den neu erstellten Kunden an den MainWindowController zurückgeben!
            return haushaltAnlegenController.getNeuAngelegterVorstand();

        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
 // Private Variable
    private AdmintoolController admintoolAnlegenController;

    /**
     * Retrieves the admin tool controller.
     *
     * @return the admin tool controller.
     */
    public AdmintoolController getAdmintoolAnlegenController()
    {
        return admintoolAnlegenController;
    }

    /**
     * Sets the admin tool controller.
     *
     * @param admintoolAnlegenController the admin tool controller to set.
     */
    public void setAdmintoolAnlegenController(AdmintoolController admintoolAnlegenController)
    {
        this.admintoolAnlegenController = admintoolAnlegenController;
    }

    /**
     * Opens the admin tool window.
     */
    public void oeffneAdmintool()
    {
        try
        {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/admintool/Admintool.fxml"));
          AnchorPane pane = loader.load();
          Stage admintoolAnlegenStage = new Stage();

          Scene admintoolAnlegenScene = new Scene(pane);
          GlobalEventLogger.attachTo("Admintool", admintoolAnlegenScene);
          admintoolAnlegenStage.setScene(admintoolAnlegenScene);

            admintoolAnlegenController = loader.getController();
          admintoolAnlegenStage.setTitle("Admintool");
          admintoolAnlegenStage.initStyle(StageStyle.DECORATED);
          admintoolAnlegenStage.initModality(Modality.APPLICATION_MODAL);
          admintoolAnlegenStage.centerOnScreen();
         admintoolAnlegenStage.showAndWait();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Opens the window for managing an existing household.
     *
     * @param haushalt the household data to manage.
     * @param fontSize the font size to set in the management window.
     * @return the updated household data.
     */
    public Haushalt oeffneHaushaltVerwalten(Haushalt haushalt, Double fontSize)
    {
        try
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/HaushaltVerwalten.fxml"));
            AnchorPane pane = loader.load();
            Stage haushaltVerwaltenStage = new Stage();

            Scene haushaltVerwaltenScene = new Scene(pane);
            GlobalEventLogger.attachTo("HaushaltVerwalten", haushaltVerwaltenScene);
            haushaltVerwaltenStage.setScene(haushaltVerwaltenScene);

            HaushaltVerwaltenController haushaltVerwaltenController = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                haushaltVerwaltenController.setFontSize(fontSize);
            }
            haushaltVerwaltenController.setHaushalt(haushalt);
            haushaltVerwaltenController.manuellInitialize();
            haushaltVerwaltenController.fuelleFelder(haushalt);
            haushaltVerwaltenStage.setTitle("Bestehenden Haushalt verwalten");
            haushaltVerwaltenStage.initStyle(StageStyle.DECORATED);
            haushaltVerwaltenStage.initModality(Modality.APPLICATION_MODAL);
            haushaltVerwaltenStage.centerOnScreen();
            haushaltVerwaltenStage.showAndWait();

            haushalt = haushaltVerwaltenController.getHaushalt();

            return haushalt;

        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Opens the window for managing purchases of a household.
     *
     * @param haushalt the household data to manage.
     * @param fontSize the font size to set in the management window.
     */
    public void oeffneBuchungenBearbeiten(Haushalt haushalt, Double fontSize)
    {
        try
        {

        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/BuchungenBearbeiten.fxml"));
            AnchorPane pane = loader.load();
            Stage buchungenBearbeitenStage = new Stage();

            Scene buchungenBearbeitenScene = new Scene(pane);
            GlobalEventLogger.attachTo("BuchungenBearbeiten", buchungenBearbeitenScene);
            buchungenBearbeitenStage.setScene(buchungenBearbeitenScene);

            BuchungenBearbeitenController buchungenBearbeitenController = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                buchungenBearbeitenController.setFontSize(fontSize);
            }
            buchungenBearbeitenController.setHaushalt(haushalt);
            buchungenBearbeitenController.manuellInitialize();
            buchungenBearbeitenStage.setTitle("Einkäufe verwalten");
            buchungenBearbeitenStage.initStyle(StageStyle.DECORATED);
            buchungenBearbeitenStage.initModality(Modality.APPLICATION_MODAL);
            buchungenBearbeitenStage.centerOnScreen();
            buchungenBearbeitenStage.showAndWait();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Opens the window for managing comments of a household.
     *
     * @param haushalt the household data to manage.
     * @param fontSize the font size to set in the management window.
     * @return the updated household data.
     */
    public Haushalt oeffneBemerkungenBearbeiten(Haushalt haushalt, Double fontSize)
    {
        try
        {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/Bemerkungen.fxml"));
        AnchorPane pane = loader.load();
        Stage bemerkungenStage = new Stage();

        Scene bemerkungenScene = new Scene(pane);
        GlobalEventLogger.attachTo("Bemerkungen", bemerkungenScene);
        bemerkungenStage.setScene(bemerkungenScene);

        BemerkungenController bemerkungenController = loader.getController();
        if (fontSize != ChangeFontSize.getDefaultFontSize())
        {
            bemerkungenController.setFontSize(fontSize);
        }
        bemerkungenController.setzeBemerkung(haushalt);
        // mainScene.getStylesheets().add(getClass().getResource("/view/stylesheet.css").toExternalForm());bemerkungenStage.setTitle("Bemerkungen bearbeiten");
        bemerkungenStage.initStyle(StageStyle.DECORATED);
        bemerkungenStage.initModality(Modality.APPLICATION_MODAL);
        bemerkungenStage.centerOnScreen();
        bemerkungenStage.showAndWait();

        haushalt = bemerkungenController.getHaushalt();

        return haushalt;

    } catch (IOException e)
        {
        e.printStackTrace();
        return haushalt;
    }

    }


    /**
     * Open window to input reason and delete member of the family.
     *
     * @Author Richard Kromm
     * @param memberOfTheFamily
     * @return
     */
    public Familienmitglied openDeletePerson(Familienmitglied memberOfTheFamily, Double fontSize)
    {
        try
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/DeletePerson.fxml"));
            AnchorPane pane = loader.load();
            Stage deletePersonStage = new Stage();

            Scene deletePersonScene = new Scene(pane);
            GlobalEventLogger.attachTo("DeletePerson", deletePersonScene);
            deletePersonStage.setScene(deletePersonScene);

            DeletePersonController deletePersonController = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                deletePersonController.setFontSize(fontSize);
            }

            if (memberOfTheFamily != null)
            {
                deletePersonController.setMemberOfTheFamily(memberOfTheFamily);
                deletePersonController.setPersonData(memberOfTheFamily);
            }

            deletePersonStage.initStyle(StageStyle.DECORATED);
            deletePersonStage.initModality(Modality.APPLICATION_MODAL);
            deletePersonStage.centerOnScreen();
            deletePersonStage.showAndWait();


            return memberOfTheFamily;

        } catch (IOException e)
        {
            e.printStackTrace();
            return memberOfTheFamily;
        }
    }

    /**
     * Open window to show the deleted members of the family.
     *
     * @Author Richard Kromm
     */
    public void showDeletedPersons(Haushalt household, double fontSize)
    {
        try
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/ShowDeletedPersons.fxml"));


            AnchorPane pane = loader.load();
            Stage showDeletedPersonsStage = new Stage();

            Scene showDeletedPersonsScene = new Scene(pane);
            GlobalEventLogger.attachTo("ShowDeletedPersons", showDeletedPersonsScene);
            showDeletedPersonsStage.setScene(showDeletedPersonsScene);

            ShowDeletedPersonsController showDeletedPersonsController = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                showDeletedPersonsController.setFontSize(fontSize);
            }
            if (household != null)
            {
                showDeletedPersonsController.showDeletedMemberOfTheFamilyInTable(household);
            }
            showDeletedPersonsStage.initStyle(StageStyle.DECORATED);
            showDeletedPersonsStage.initModality(Modality.APPLICATION_MODAL);
            showDeletedPersonsStage.centerOnScreen();
            showDeletedPersonsStage.showAndWait();




        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Opens the window to input reason and delete member of the family.
     *
     * @param memberOfTheFamily the family member data to delete.
     * @param fontSize the font size to set in the delete window.
     * @return the deleted family member data.
     */
    public Familienmitglied oeffnePersonAendern(Familienmitglied memberOfTheFamily, Haushalt haushalt, Double fontSize)
    {
        try
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/PersonAendern.fxml"));
            AnchorPane pane = loader.load();

            Stage personenAendernStage = new Stage();
            Scene personenAendernScene = new Scene(pane);
            GlobalEventLogger.attachTo("PersonAendern.fxml", personenAendernScene);
            PersonAendernController personAendernController = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                personAendernController.setFontSize(fontSize);
            }

            if (memberOfTheFamily != null)
            {
                personAendernController.setFamilienmitglied(memberOfTheFamily, false);
                personAendernController.setzePersonendaten(memberOfTheFamily);
                personAendernController.setHaushalt(haushalt);
            } else
            {
                personAendernController.setHaushalt(haushalt);
                personAendernController.setFamilienmitglied(null, true);

            }

            personenAendernStage.setTitle("Haushaltsmitglied bearbeiten");
            personenAendernStage.setScene(personenAendernScene);
            personenAendernStage.initStyle(StageStyle.DECORATED);
            personenAendernStage.initModality(Modality.APPLICATION_MODAL);
            personenAendernStage.centerOnScreen();
            personenAendernStage.showAndWait();

            memberOfTheFamily = personAendernController.getFamilienmitglied();

            return memberOfTheFamily;

        } catch (IOException e)
        {
            e.printStackTrace();
            return memberOfTheFamily;
        }
    }
    /**
     * Opens the window for modifying the customer base.
     *
     * @param haushalt the household data to modify.
     * @param fontSize the font size to set in the modification window.
     * @return the updated household data.
     */
    public Haushalt oeffneKundenstammBearbeiten(Haushalt haushalt, Double fontSize)
    {
        try
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/KundenstammBearbeiten.fxml"));
            AnchorPane pane = loader.load();

            Stage kundenstammBearbeitenStage = new Stage();
            Scene kundenstammBearbeitenScene = new Scene(pane);
            GlobalEventLogger.attachTo("KundenstammBearbeiten.fxml", kundenstammBearbeitenScene);
            KundenstammBearbeitenController kundenstammBearbeitenController = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                kundenstammBearbeitenController.setFontSize(fontSize);
            }
            kundenstammBearbeitenController.setzeKundenstammdaten(haushalt);
            kundenstammBearbeitenStage.setTitle("Allgemeine Kundendaten bearbeiten");
            kundenstammBearbeitenStage.setScene(kundenstammBearbeitenScene);
            kundenstammBearbeitenStage.initStyle(StageStyle.DECORATED);
            kundenstammBearbeitenStage.initModality(Modality.APPLICATION_MODAL);
            kundenstammBearbeitenStage.centerOnScreen();
            kundenstammBearbeitenStage.showAndWait();

            return haushalt;

        } catch (IOException e)
        {
            e.printStackTrace();
            return haushalt;
        }
    }
    /**
     * Opens the window for modifying the contact data of a household.
     *
     * @param haushalt the household data to modify.
     * @param fontSize the font size to set in the modification window.
     * @return the updated household data.
     */
    public Haushalt oeffneKontaktdatenBearbeiten(Haushalt haushalt, Double fontSize)
    {
        try
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/KontaktdatenBearbeiten.fxml"));
            AnchorPane pane = loader.load();

            Stage kontaktdatenBearbeitenStage = new Stage();
            Scene kontaktdatenBearbeitenScene = new Scene(pane);
            GlobalEventLogger.attachTo("KontaktdatenBearbeiten.fxml", kontaktdatenBearbeitenScene);
            KontaktdatenBearbeitenController kontaktdatenBearbeitenController = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                kontaktdatenBearbeitenController.setFontSize(fontSize);
            }
            kontaktdatenBearbeitenController.setHaushalt(haushalt);
            kontaktdatenBearbeitenController.kontaktdatenSetzen(haushalt);

            kontaktdatenBearbeitenStage.setTitle("Kontaktdaten des Haushalts bearbeiten");
            kontaktdatenBearbeitenStage.setScene(kontaktdatenBearbeitenScene);
            kontaktdatenBearbeitenStage.initStyle(StageStyle.DECORATED);
            kontaktdatenBearbeitenStage.initModality(Modality.APPLICATION_MODAL);
            kontaktdatenBearbeitenStage.centerOnScreen();
            kontaktdatenBearbeitenStage.showAndWait();
            haushalt = kontaktdatenBearbeitenController.getHaushalt();
            return haushalt;


        } catch (IOException e)
        {
            e.printStackTrace();
            return haushalt;
        }
    }
    /**
     * Opens the window for displaying the existing powers of attorney.
     *
     * @param haushalt the household data to display.
     * @param fontSize the font size to set in the display window.
     * @return the number of existing powers of attorney.
     */
    public Integer oeffneVollmachtenAnzeigen(Haushalt haushalt, Double fontSize)
    {
        try
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/VollmachtenAnzeigen.fxml"));
            AnchorPane pane = loader.load();

            Stage vollmachtenAnzeigenStage = new Stage();
            Scene vollmachtenAnzeigenScene = new Scene(pane);
            GlobalEventLogger.attachTo("VollmachtenAnzeigen.fxml", vollmachtenAnzeigenScene);
            VollmachtenController vollmachtenController = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                vollmachtenController.setFontSizeShowPowerOfAttorney(fontSize);
            }
            vollmachtenController.setHaushalt(haushalt);
            vollmachtenController.erstelleTabelleBestehendeVollmachten();
            vollmachtenAnzeigenStage.setTitle("Bestehende Vollmachten");
            vollmachtenAnzeigenStage.setScene(vollmachtenAnzeigenScene);
            vollmachtenAnzeigenStage.initStyle(StageStyle.DECORATED);
            vollmachtenAnzeigenStage.initModality(Modality.APPLICATION_MODAL);
            vollmachtenAnzeigenStage.centerOnScreen();
            vollmachtenAnzeigenStage.showAndWait();

            Integer anzahlVollmachten = vollmachtenController.getAnzahlVollmachten();

            return anzahlVollmachten;


        } catch (IOException e)
        {
            e.printStackTrace();

            return 0;
        }
    }
    /**
     * Opens the window for adding or modifying powers of attorney.
     *
     * @param haushalt the household data to modify.
     * @param bestehendeVollmacht the existing power of attorney to modify, or null to add a new one.
     * @param fontSize the font size to set in the modification window.
     * @return the updated or new power of attorney.
     */
    public Vollmacht oeffneVollmachtenHinzufuegen(Haushalt haushalt, Vollmacht bestehendeVollmacht, Double fontSize)
    {
        try
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/VollmachtenErstellen.fxml"));
            AnchorPane pane = loader.load();
            Stage vollmachtenErstellenStage = new Stage();
            Scene vollmachtenErstellenScene = new Scene(pane);
            GlobalEventLogger.attachTo("VollmachtenErstellen.fxml", vollmachtenErstellenScene);

            VollmachtenController vollmachtenController = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                vollmachtenController.setFontSizeEditPowerOfAttorney(fontSize);
            }
            vollmachtenController.setHaushalt(haushalt);
            vollmachtenController.erstelleTabelleVollmachthinzufuegen();
            vollmachtenErstellenStage.setTitle("Vollmachten bearbeiten");
            vollmachtenErstellenStage.setScene(vollmachtenErstellenScene);
            vollmachtenErstellenStage.initStyle(StageStyle.DECORATED);
            vollmachtenErstellenStage.initModality(Modality.APPLICATION_MODAL);
            vollmachtenErstellenStage.centerOnScreen();

            if (bestehendeVollmacht == null)
            {
                vollmachtenController.setzeDaten(null, true);
                vollmachtenErstellenStage.showAndWait();
                bestehendeVollmacht = vollmachtenController.getVollmacht();
                return bestehendeVollmacht;
            } else
            {
                vollmachtenController.setzeDaten(bestehendeVollmacht, false);
                vollmachtenErstellenStage.showAndWait();
                bestehendeVollmacht = vollmachtenController.getVollmacht();
                return bestehendeVollmacht;
            }

        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Opens the window for managing assessments of a household.
     *
     * @param haushalt the household data to manage.
     * @param fontSize the font size to set in the management window.
     */
    public void oeffneBescheideBearbeiten(Haushalt haushalt, Double fontSize)
    {
        try
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/BescheideBearbeiten.fxml"));
            AnchorPane pane = loader.load();
            Stage bescheideBearbeitenStage = new Stage();
            Scene bescheideBearbeitenScene = new Scene(pane);
            GlobalEventLogger.attachTo("BescheideBearbeiten.fxml", bescheideBearbeitenScene);
            BescheideBearbeitenController bescheideBearbeitenController = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                bescheideBearbeitenController.setFontSizeShowAssessments(fontSize);
            }
            bescheideBearbeitenController.setzeBescheiddaten(haushalt);
            bescheideBearbeitenController.erstelleTabelleBestehendeBescheide();
            bescheideBearbeitenStage.setTitle("Bescheide bearbeiten");
            bescheideBearbeitenStage.setScene(bescheideBearbeitenScene);
            bescheideBearbeitenStage.initStyle(StageStyle.DECORATED);
            bescheideBearbeitenStage.initModality(Modality.APPLICATION_MODAL);
            bescheideBearbeitenStage.centerOnScreen();
            bescheideBearbeitenStage.showAndWait();


        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Opens the window for adding or modifying an assessment.
     *
     * @param familienmitglieder the family members data to use.
     * @param bescheid the assessment data to modify, or null to add a new one.
     * @param fontSize the font size to set in the modification window.
     * @return the updated or new assessment.
     */
    public Bescheid oeffneBescheideHinzufuegen(ArrayList<Familienmitglied> familienmitglieder, Bescheid bescheid, Double fontSize)
    {
        @SuppressWarnings("unused")
		Familienmitglied familienmitglied;
        try
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/BescheidHinzufuegen.fxml"));
            AnchorPane pane = loader.load();
            Stage bescheidHinzufuegenStage = new Stage();
            Scene bescheidHinzufuegenScene = new Scene(pane);
            GlobalEventLogger.attachTo("BescheidHinzufuegen.fxml", bescheidHinzufuegenScene);
            BescheideBearbeitenController bescheideBearbeitenController = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                bescheideBearbeitenController.setFontSizeAddAssessments(fontSize);
            }
            bescheidHinzufuegenStage.setTitle("Bescheid hinzufügen");
            bescheidHinzufuegenStage.setScene(bescheidHinzufuegenScene);
            bescheidHinzufuegenStage.initStyle(StageStyle.DECORATED);
            bescheidHinzufuegenStage.initModality(Modality.APPLICATION_MODAL);
            bescheidHinzufuegenStage.centerOnScreen();
            bescheideBearbeitenController.setzeDatenBescheidHinzufuegen(familienmitglieder);


            if (bescheid == null)
            {
                bescheideBearbeitenController.setzeDatenBescheidHinzufuegen(null, true);
            } else
            {
                bescheideBearbeitenController.setzeDatenBescheidHinzufuegen(bescheid, false);
            }

            bescheidHinzufuegenStage.showAndWait();            bescheid = bescheideBearbeitenController.getBescheid();

            return bescheid;

        } catch (IOException e)
        {
            e.printStackTrace();
            return bescheid;
        }
    }
    /**
     * Displays booking warnings.
     *
     * @param warnungen the list of warnings to display.
     * @return true if the user confirmed the warnings, false otherwise.
     */
    public Boolean zeigeBuchungswarnungen(ArrayList<Booking_err_warn_list> warnungen)
    {
        try
        {
            javafx.scene.control.Dialog<ButtonType> dialog = new javafx.scene.control.Dialog<>();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/BuchungAlert.fxml"));
            DialogPane pane = loader.load();
            dialog.setDialogPane(pane);
            BuchungController buchungController = loader.getController();
            boolean lError=buchungController.setAlertText(warnungen);
            dialog.setTitle("Warnung!");
        
            Optional<ButtonType> result = dialog.showAndWait();

            return result.isPresent() && result.get() == ButtonType.YES && !lError;

        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Opens the window for creating a new booking.
     *
     * @param haushalt the household data to use.
     * @param buchungsText the booking text to use.
     * @param zugeordnetePerson the assigned person data to use.
     * @param fontSize the font size to set in the creation window.
     * @return the created booking data.
     */
    public Einkauf oeffneBuchungErstellen(Haushalt haushalt, int buchungsText, int zugeordnetePerson, Double fontSize)
    {
        try
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/Buchungsdetails.fxml"));
            AnchorPane pane = loader.load();
            Stage buchungErstellenStage = new Stage();
            Scene buchungErstellenScene = new Scene(pane);
            GlobalEventLogger.attachTo("Buchungsdetails.fxml", buchungErstellenScene);
            BuchungController buchungController = loader.getController();
            if (fontSize != ChangeFontSize.getDefaultFontSize())
            {
                buchungController.setFontSize(fontSize);
            }
            buchungErstellenStage.setTitle("Buchungsdetails");
            buchungErstellenStage.setScene(buchungErstellenScene);
            buchungErstellenStage.initStyle(StageStyle.DECORATED);
            buchungErstellenStage.initModality(Modality.APPLICATION_MODAL);
            buchungErstellenStage.centerOnScreen();
            buchungController.setHaushalt(haushalt);
            buchungController.manuellinitialize(buchungsText, zugeordnetePerson);
            buchungErstellenStage.showAndWait();
            return buchungController.getBuchungResults();

        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Opens the statistics tool window.
     */
    @FXML
    public void oeffneStatistikTool()
    {
        System.out.println("öffneStatistikTool() aufgerufen");
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/Statistiktool.fxml"));
            AnchorPane pane = loader.load();

            Stage statistikStage = new Stage();
            Scene statistikScene = new Scene(pane);
            GlobalEventLogger.attachTo("Statistiktool.fxml", statistikScene);
            statistikStage.setTitle("Statistik-Tool");
            statistikStage.setScene(statistikScene);
            statistikStage.initModality(Modality.APPLICATION_MODAL);
            statistikStage.initStyle(StageStyle.DECORATED);
            statistikStage.centerOnScreen();
            statistikStage.show();

            StatistiktoolController controller = loader.getController();
            controller.initStatistikTool(); // Initialisiere den Statistiktool-Controller

        } catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Fehler beim Laden der FXML-Datei: " + e.getMessage());
        }
    }

    /**
     * Opens error-protocol window.
     */
    @FXML
    public void openErrorProtocolWindow()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/Protocol.fxml"));
            AnchorPane pane = loader.load();
            Stage protocolStage = new Stage();

            Scene protocolScene = new Scene(pane);
            protocolStage.setScene(protocolScene);
            protocolStage.setTitle("Fehlerprotokolle");
            protocolStage.initStyle(StageStyle.DECORATED);
            protocolStage.centerOnScreen();
            protocolStage.show();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error while loading Error-Report window!");
        }
    }

    // Ihre anderen Methoden hier

    /**
     * Retrieves the household data.
     *
     * @return the household data.
     */
    public Haushalt getHaushalt()
    {
        return haushalt;
    }
    /**
     * Sets the household data.
     *
     * @param haushalt the household data to set.
     */
    public void setHaushalt(Haushalt haushalt)
    {
        this.haushalt = haushalt;
    }


    /**
     * Start and create external process / operating system program, NOW for testing: Internet Explorer LATER: Statistik-Tool .
     *
     * @Author Robin Becker
     */
    public void startIexplore() throws URISyntaxException
    {
        CodeSource codeSource = kundenverwaltung.Main.class.getProtectionDomain().getCodeSource();
        File jarFile = new File(codeSource.getLocation().toURI().getPath());
        @SuppressWarnings("unused")
		String jarDir = jarFile.getParentFile().getPath();

//       // ProcessBuilder pb = new ProcessBuilder("C:\\Program Files\\internet explorer\\iexplore.exe");
//        ProcessBuilder pb = new ProcessBuilder(jarDir + "\\Tafel_Statistik.jar");
//
//        System.out.println(pb);
//        System.out.println(jarDir);
//        try {
//            pb.start();
//            System.out.println("Internet Explorer has been started.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

       // try
       // {
       //     Runtime.getRuntime().exec("cmd /c java -jar " + jarDir + "\\Tafel_Statistik.jar");
       // } catch (IOException e)
       // {
        //    e.printStackTrace();
      //  }
    }

    /**
     * Opens the database connection error window.
     */
    public void openDbConnectionError()
    {
      try
      {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/DatabaseConnectionError.fxml"));
          AnchorPane pane = loader.load();
          Stage dbVerbindungStage = new Stage();
          Scene dbVerbindungScene = new Scene(pane);
          GlobalEventLogger.attachTo("DatabaseConnectionError.fxml", dbVerbindungScene);
          @SuppressWarnings("unused")
          DatabaseConnectionErrorController dbConnectionErrorController = loader.getController();
          dbVerbindungStage.setTitle("Datenbank-Einstellungen prüfen");
          dbVerbindungStage.setScene(dbVerbindungScene);
          dbVerbindungStage.initStyle(StageStyle.DECORATED);
          dbVerbindungStage.initModality(Modality.APPLICATION_MODAL);
          dbVerbindungStage.centerOnScreen();
          dbVerbindungStage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private Integer lastSelectedAusgabegruppeId;
    /**
    *
    */
    public Integer getLastSelectedAusgabegruppeId()
    {
      return lastSelectedAusgabegruppeId; }
    /**
    *
    */
    public void setLastSelectedAusgabegruppeId(Integer id)
    {
      this.lastSelectedAusgabegruppeId = id;
    }

    public User getUser()
    {
        return user;
    }
}
