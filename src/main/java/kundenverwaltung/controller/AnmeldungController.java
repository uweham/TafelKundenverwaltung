package kundenverwaltung.controller;

import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kundenverwaltung.Benachrichtigung;
import kundenverwaltung.toolsandworkarounds.AppVersion;
import kundenverwaltung.toolsandworkarounds.CustomPropertiesStore;
import kundenverwaltung.toolsandworkarounds.GitlabVersion;
import kundenverwaltung.controller.server.ServerSynchronizationNotificationController;
import kundenverwaltung.controller.server.VersionUpdateNotificationController;
import kundenverwaltung.dao.UserDAO;
import kundenverwaltung.dao.UserDAOimpl;
import kundenverwaltung.logger.event.GlobalEventLogger;
import kundenverwaltung.logger.file.LogFileService;
import kundenverwaltung.model.User;
import kundenverwaltung.server.api.ServerClient;
import kundenverwaltung.server.service.ErrorReportService;
import kundenverwaltung.server.service.TrackingTafelService;
import kundenverwaltung.server.service.UserEntityService;
import kundenverwaltung.server.service.WishRequestService;
import kundenverwaltung.service.WindowService;
import kundenverwaltung.toolsandworkarounds.PasswordEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This function allows you to log in to the program.
 *
 * @Author Richard Kromm
 * @Date 13.08.2018
 */
public class AnmeldungController
{
	private static final int SHORT_ACCOUNT_BLOCK_OFF_NUMBER_OF_MISTRIALS = 3;
	private static final int MEDIUM_ACCOUNT_BLOCK_OFF_NUMBER_OF_MISTRIALS = 10;
	private static final int LONG_ACCOUNT_BLOCK_OFF_NUMBER_OF_MISTRIALS = 20;
	private static final int SHORT_ACCOUNT_BLOCK_IN_SECONDS = 60;
	private static final int MEDIUM_ACCOUNT_BLOCK_IN_SECONDS = 300;
	private static final int LONG_ACCOUNT_BLOCK_IN_SECONDS = 1800;
	private static final int SECONDS_PER_MINUTE = 60;
	private static final String BLOCKED_USER_NAME_TITEL = "Benutzername gesperrt";
	private static final String BLOCKED_USER_ERROR_HEADER_TEXT = "Zuviele fehlerhafte Loginversuche!";
	private static final String BLOCKED_USER_ERROR_CONTENT_TEXT = "Versuchen Sie es in ";
	private static final String BLOCKED_USER_ERROR_MINUTES = " Minute/n und ";
	private static final String BLOCKED_USER_ERROR_SECONDS = " Sekunde/n erneut.";

	private static final String NOTIFICATION_TITEL_ILLEGAL_LOGIN_DATA = "Anmeldung fehlgeschlagen";
	private static final String NOTIFICATION_TEXT_ILLEGAL_LOGIN_DATA = "Benutzername und/oder Passwort sind nicht korrekt.";
	private static final String ERROR_TITEL_USE_DEFAULT_PASSWORD = "Achtung!";
	private static final String ERROR_HEADER_TEXT_USE_DEFAULT_PASSWORD = "Sie verwende das vom Administrator festgelegte Standardpasswort";
	private static final String ERROR_CONTENT_TEXT_USE_DEFAULT_PASSWORD = "Bitte ändern Sie ihr Passwort umgehend!"
			+ "\nUnter dem Menüpunkt \"Datei\"->\"Benutzer wechseln\" können Sie ihr Passwort ändern.";
	private static final Double DEFAULT_FONT_SIZE = 13.0;

	private final Logger LOGGER = LoggerFactory.getLogger(AnmeldungController.class);
	private final TrackingTafelService TRACKING_TAFEL_SERVICE = new TrackingTafelService();
	private final ErrorReportService ERROR_REPORT_SERVICE = new ErrorReportService();
	private final WishRequestService WISH_REQUEST_SERVICE = new WishRequestService();

	@FXML
	private TextField textfieldUserName;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Button buttonSignIn;
	@FXML
	private Button buttonExitProgram;

	@SuppressWarnings("unused")
	private PasswordEncoding passwordEncoding = new PasswordEncoding();
	private UserDAO userDAO = new UserDAOimpl();
	private User user;

	public final void initialize()
	{
		textfieldUserName.setOnKeyPressed(event ->
		{
			if (event.getCode() == KeyCode.ENTER)
			{
				signIn();
            }
		});

		passwordField.setOnKeyPressed(event ->
		{
			if (event.getCode() == KeyCode.ENTER)
			{
				signIn();
            }
		});
	}


	/**
	 * This function checks the login data and blocks the user by multiple input of a wrong password.
	 */
	@FXML
	public void signIn()
	{
		user = userDAO.read(textfieldUserName.getText());

		if (user != null && checkSignInPermission())
		{
			if (PasswordEncoding.checkPassword(user, passwordField.getText()))
			{
				LogFileService.setupUserLogFileHandling(user.getUserName(), String.valueOf(user.getUserId()));

				UserEntityService.getInstance().setUser(user);

				ServerClient.setupServerClient();

				int pingHTTPStatus = -1;

				try
				{
					pingHTTPStatus = TRACKING_TAFEL_SERVICE.ping();
				}
				catch (Exception exception)
				{
					LOGGER.error(exception.getMessage());
				}

				int uploadedErrorReports = 0;
				int uploadedWishRequests = 0;

				if (pingHTTPStatus == HttpURLConnection.HTTP_OK)
				{
					uploadedErrorReports = ERROR_REPORT_SERVICE.uploadOfflineReports();
					uploadedWishRequests = WISH_REQUEST_SERVICE.uploadOfflineWishes();
				}

				if (uploadedErrorReports != 0 || uploadedWishRequests != 0)
				{
					try
					{
						FXMLLoader fxmlLoader = new FXMLLoader(WindowService.class.getResource("/kundenverwaltung/fxml/server/ServerSynchronizationNotification.fxml"));
						Parent parent = fxmlLoader.load();

						Scene scene = new Scene(parent);
						Stage stage = new Stage();

						GlobalEventLogger.attachTo("ServerSynchronizationNotification", scene);

						stage.setTitle("Tafel-Server Synchronisation");
						stage.setScene(scene);
						stage.initStyle(StageStyle.DECORATED);
						stage.initModality(Modality.APPLICATION_MODAL);
						stage.centerOnScreen();

						ServerSynchronizationNotificationController controller = fxmlLoader.getController();

						controller.setSyncInfo(uploadedErrorReports,uploadedWishRequests);


						stage.showAndWait();
					}
					catch (Exception exception)
					{
						LOGGER.error(exception.getMessage());
					}
				}

				checkForUpdates();

				userDAO.updateNumberOfMistrials(user, false);

				MainController.getInstance().mainWindow(user);

				cancelSignInWindow();

				Boolean useDefaultPassword = user.isDefaultPassword(passwordField.getText());

				if (useDefaultPassword)
				{
					Benachrichtigung.errorDialog(ERROR_TITEL_USE_DEFAULT_PASSWORD, ERROR_HEADER_TEXT_USE_DEFAULT_PASSWORD,
							ERROR_CONTENT_TEXT_USE_DEFAULT_PASSWORD);
					MainController.getInstance().oeffneBenutzerprofil(user, DEFAULT_FONT_SIZE);
				}
			}
			else
			{
				Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_ILLEGAL_LOGIN_DATA, NOTIFICATION_TEXT_ILLEGAL_LOGIN_DATA);
				passwordField.setText("");
				passwordField.requestFocus();

				userDAO.updateNumberOfMistrials(user, true);
				int numberOfMistrials = userDAO.getNumberOfMistrials(user);
				switch (numberOfMistrials)
					{
						case SHORT_ACCOUNT_BLOCK_OFF_NUMBER_OF_MISTRIALS:
							userDAO.setBlockedUntil(user, LocalDateTime.now().plusSeconds(SHORT_ACCOUNT_BLOCK_IN_SECONDS));
							break;
						case MEDIUM_ACCOUNT_BLOCK_OFF_NUMBER_OF_MISTRIALS:
							userDAO.setBlockedUntil(user, LocalDateTime.now().plusSeconds(MEDIUM_ACCOUNT_BLOCK_IN_SECONDS));
							break;
						case LONG_ACCOUNT_BLOCK_OFF_NUMBER_OF_MISTRIALS:
							userDAO.setBlockedUntil(user, LocalDateTime.now().plusSeconds(LONG_ACCOUNT_BLOCK_IN_SECONDS));
							break;
						default:
							break;
					}
			}
		}
		else
		{
			Benachrichtigung.warnungBenachrichtigung(NOTIFICATION_TITEL_ILLEGAL_LOGIN_DATA, NOTIFICATION_TEXT_ILLEGAL_LOGIN_DATA);
			passwordField.setText("");
			passwordField.requestFocus();
		}
	}
	/**
	 * Checks if a new version is available and shows a dialog if necessary.
	 */
	private void checkForUpdates()
	{
		try
		{
			if (!VersionUpdateNotificationController.showUpdateNotificationWindow())
			{
				return;
			}
		}
		catch (Exception exception)
		{
			LOGGER.error(exception.getMessage());
		}

		try
		{
			String currentVersion = AppVersion.getPomVersion();
			String latestVersion = GitlabVersion.getLatestVersion();

			if (latestVersion != null && !latestVersion.equals(currentVersion))
			{
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/kundenverwaltung/fxml/server/VersionUpdateNotification.fxml"));
				Parent root = loader.load();

				VersionUpdateNotificationController controller = loader.getController();
				controller.setVersionInfo(currentVersion, latestVersion);

				Stage versionStage = new Stage();
				versionStage.setTitle("Neue Version verfügbar");
				versionStage.initStyle(StageStyle.DECORATED);
				versionStage.initModality(Modality.APPLICATION_MODAL);
				versionStage.centerOnScreen();
				versionStage.setScene(new Scene(root));
				versionStage.showAndWait();
			}
		} catch (Exception e)
		{
			LOGGER.error("Fehler beim Update-Check: ", e);
		}
	}


	/**
	 * This function checks if the user is blocked.
	 * @return true: user is not blocked; false: user is blocked
	 */
	public Boolean checkSignInPermission()
	{
		LocalDateTime userBlockedUntil = userDAO.getBlockedUntil(user);
		long differenceInSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), userBlockedUntil);

		if (differenceInSeconds > 0)
		{
			long waitSeconds = (differenceInSeconds % SECONDS_PER_MINUTE);
			long waitMinutes = (differenceInSeconds - waitSeconds) / SECONDS_PER_MINUTE;

			Benachrichtigung.errorDialog(BLOCKED_USER_NAME_TITEL, BLOCKED_USER_ERROR_HEADER_TEXT,
					BLOCKED_USER_ERROR_CONTENT_TEXT + waitMinutes + BLOCKED_USER_ERROR_MINUTES + waitSeconds + BLOCKED_USER_ERROR_SECONDS);
			return false;
		} else
			{
				return true;
			}
	}

	/**
	 * This function exits the program.
	 */
	@FXML
	public void exitProgram()
	{
		Platform.exit();
	}

	/**
	 * This function closes the sign in window.
	 */
	public void cancelSignInWindow()
	{
		Stage stage = (Stage) textfieldUserName.getScene().getWindow();
		stage.close();
	}
}
