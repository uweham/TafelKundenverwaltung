package kundenverwaltung.service;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateService {

    // GitLab API URL für Releases (Sortiert nach Datum)
    private static final String GITLAB_API = "https://gitlab.com/api/v4/projects/tafel_software-hs-osnabrueck%2FTafel_Projekt/releases?per_page=1";

    public void checkForUpdates() {
        // In eigenem Thread starten, damit GUI nicht einfriert
        Thread t = new Thread(() -> {
            try {
                String localVersion = getLocalVersion();
                String remoteVersion = getRemoteVersion();

                System.out.println("Version Check: Lokal=" + localVersion + " | Remote=" + remoteVersion);

                if (isNewer(remoteVersion, localVersion)) {
                    // Zurück in den UI Thread für das Popup
                    Platform.runLater(() -> showUpdateDialog(remoteVersion));
                }

            } catch (Exception e) {
                System.err.println("Update-Check fehlgeschlagen: " + e.getMessage());
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private String getLocalVersion() {
        // Wird durch MainWindowController.loadVersion() gesetzt
        String v = System.getProperty("app.version");
        return (v != null && !v.startsWith("${")) ? v : "0.0.0";
    }

    private String getRemoteVersion() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(GITLAB_API)).GET().build();
        
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        
        if (res.statusCode() != 200) throw new IOException("GitLab API Error: " + res.statusCode());

        // Simple Regex für "tag_name":"..." (spart JSON Library)
        Matcher m = Pattern.compile("\"tag_name\"\\s*:\\s*\"([^\"]+)\"").matcher(res.body());
        if (m.find()) {
            return m.group(1).trim();
        }
        throw new IOException("Keine Version im JSON gefunden.");
    }

    private boolean isNewer(String remote, String local) {
        remote = remote.replaceAll("[^0-9.]", ""); // "v2.5.1" -> "2.5.1"
        local = local.replaceAll("[^0-9.]", "");
        
        // Einfacher String-Vergleich reicht oft, aber besser ist Split
        return !remote.equals(local) && remote.compareTo(local) > 0;
    }

    private void showUpdateDialog(String newVer) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update verfügbar");
        alert.setHeaderText("Neue Version " + newVer + " verfügbar!");
        alert.setContentText("Soll das Update jetzt heruntergeladen und installiert werden?\nDas Programm wird neu gestartet.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            runUpdateScript();
        }
    }

    private void runUpdateScript() {
      try {
          // 1. Pfad zur aktuellen JAR ermitteln
          // (Funktioniert auch, wenn die App nicht in Program Files liegt)
          String jarPath = UpdateService.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
          // Windows-Pfad Korrektur (führenden Slash entfernen, z.B. /C:/... -> C:/...)
          if (jarPath.startsWith("/") && System.getProperty("os.name").toLowerCase().contains("win")) {
              jarPath = jarPath.substring(1);
          }
          
          java.io.File jarFile = new java.io.File(jarPath);
          Path appDir = jarFile.getParentFile().toPath(); // Der Ordner, in dem die JAR liegt
          
          // 2. Pfad zum Skript ermitteln (../scripts/Update-TafelApp.ps1 relativ zur JAR)
          Path scriptPath = appDir.getParent().resolve("scripts").resolve("Update-TafelApp.ps1").normalize();

          // Check ob Skript da ist
          if (!Files.exists(scriptPath)) {
              Platform.runLater(() -> {
                  Alert a = new Alert(Alert.AlertType.ERROR);
                  a.setHeaderText("Update-Fehler");
                  a.setContentText("Das Update-Skript wurde nicht gefunden:\n" + scriptPath + 
                                   "\n\nBitte installieren Sie die Anwendung neu.");
                  a.show();
              });
              return;
          }

          System.out.println("Starte Updater Skript: " + scriptPath);
          System.out.println("Update Ziel-Verzeichnis: " + appDir);

          // 3. Befehl bauen MIT Parametern
          // Wir übergeben -AppDir explizit, damit auch Test-Installationen geupdatet werden können
          String cmd = "powershell.exe -ExecutionPolicy Bypass -File \"" + scriptPath.toAbsolutePath() + "\" " +
                       "-AppDir \"" + appDir.toAbsolutePath() + "\"";
          
          // Prozess starten
          Runtime.getRuntime().exec(cmd);
          
          // 4. App hart beenden (wichtig, damit PowerShell die JAR löschen kann)
          System.exit(0);
          
      } catch (Exception e) {
          e.printStackTrace();
          Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Fehler beim Starten des Updaters: " + e.getMessage()).show());
      }
  }
}