# Tafel Kundenverwaltungsprogramm

Dies ist die offizielle README für das Tafel Kundenverwaltungsprogramm, eine Open-Source-Lösung zur Verwaltung von Tafelorganisationen.

## Überblick

Das Tafel Kundenverwaltungsprogramm unterstützt gemeinnützige Organisationen bei der Beschleunigung, Überprüfung und Auswertung ihrer regelmäßigen Abläufe. Es bietet eine breite Palette an Funktionalitäten, um den Alltag in Ihrer Einrichtung effizienter zu gestalten.

## Funktionen

Zu den Kernfunktionen gehören:
- Schnelles Auffinden von Kunden durch eine integrierte Suchfunktion.
- Automatische Ermittlung des zu zahlenden Betrags pro Person oder Haushalt.
- Berechtigungsüberprüfungen beim Einkauf (Gültigkeit des Bescheids, Kontostand etc.).
- Erstellung von Kassenabrechnungen und Druck in tabellarischer Form.
- Druck von Kundenausweisen und Listen.

## Zusätzliche Funktionen

- Individuelle Benutzerberechtigungen für sicherheitskritische Aktionen.
- Gleichzeitige Nutzung auf verschiedenen Rechnern dank zentralisierter Datenhaltung.
- Vorgefertigte und individuell erstellbare statistische Auswertungen.
- Ausführliches Benutzerhandbuch zur Erleichterung des Einstiegs in die Software.

## Geschichte und Idee

Das Programm entstand aus der Zusammenarbeit zwischen der Lingener Tafel e.V. und der Hochschule Osnabrück durch Prof. Dr. Reinhard Rauscher und Prof. Dr.-Ing. Ralf Buschermöhle. Seit 2007 hilft es, effizientere Prozesse in Tafeln zu implementieren und wurde kontinuierlich durch Rückmeldungen und Wünsche der Nutzer weiterentwickelt.

## Unterstützung und Weiterentwicklung

Die Software wird durch engagierte Studierende und Prof. Dr.-Ing. Ralf Buschermöhle von der Hochschule Osnabrück aktuell betreut und weiterentwickelt.

## Installation und Nutzung

Für detaillierte Anweisungen zur Installation, Konfiguration (MariaDB) und Nutzung des Kundenverwaltungsprogramms konsultieren Sie bitte das beigefügte Benutzerhandbuch. Es enthält umfassende Informationen über die Funktionen und Bedienung des Programms.
Besuchen Sie auch die offizielle Projekthomepage der Hochschule Osnabrück - Tafel [hier](https://www.hs-osnabrueck.de/tafel-kundenverwaltungsprogramm/).

## Voraussetzungen
Zum Ausführen des Kundenverwaltungsprogramms und des Einrichtungssetups ist JDK 17 erforderlich. Sie können es [hier herunterladen](https://www.oracle.com/java/technologies/downloads/#java17).

- JDK 17
- Maven
- Git

## MariaDB Installation

### Für Windows-User:

1. Öffnen Sie PowerShell als Administrator.

2. Installieren Sie [Chocolatey](https://chocolatey.org) oder führen Sie diesen Befehl aus:
    ```powershell
    Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))
    ```
3. Führen Sie den folgenden Befehl aus, um MariaDB über Chocolatey zu installieren:
    ```powershell
    choco install mariadb -y
    ```
4. Nach der Installation können Sie MariaDB starten:
    ```powershell
    mysql -u root
    ```
5. Überprüfen Sie, ob MariaDB erfolgreich gestartet wurde:
    ```powershell
    mysql.server status
    ```
6. Richten Sie Ihre Datenbank wie im Benutzerhandbuch beschrieben ein.

   
### Für MacOS-User:

1. Öffnen Sie das Terminal.

2. Installieren Sie [Homebrew](https://brew.sh/) oder führen Sie diesen Befehl aus:
    ```sh
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    ```
3. Führen Sie den folgenden Befehl im Terminal aus, um MariaDB zu installieren:
    ```sh
    brew install mariadb
    ```
4. Nach der Installation können Sie MariaDB starten:
    ```sh
    mysql -u root
    ```
5. Überprüfen Sie, ob MariaDB erfolgreich gestartet wurde:
    ```sh 
    mysql.server status
    ```
6. Richten Sie Ihre Datenbank wie im Benutzerhandbuch beschrieben ein.

## Git Installation und Repository Klonen

### Für Windows-User:

1. Installieren Sie [Git hier](https://git-scm.com/download/win) oder führen Sie diesen Befehl in der PowerShell aus:
    ```powershell
    choco install git -y
    ```
2. Öffnen Sie die Git Bash oder die Windows PowerShell.
3. Konfigurieren Sie Git`
    ```powershell
    git config --global user.name "Ihr Name"
    ```
    ```powershell
    git config --global user.email "Ihre E-Mail-Adresse"
    ```
4. Klonen Sie das Projekt von GitLab:
   ```powershell
    git clone https://gitlab.com/tafel_software-hs-osnabrueck/Tafel_Projekt.git
    ```
5. Navigieren Sie zum geklonten Repository:
    ```powershell
    cd Pfad/zum/geklonten/Repository
    ```
    **Fehlermeldung:** Wenn `git clone` fehlschlägt, überprüfen Sie die GitLab-URL und Ihre Netzwerkverbindung.

### Für MacOS-User:

1. Öffnen Sie das Terminal.
2. Verwenden Sie Homebrew, um Git zu installieren:
    ```sh
    brew install mariadb
    ```
3. Konfigurieren Sie Git
    ```sh
    git config --global user.name "Ihr Name"
    ```
    ```sh
    git config --global user.email "Ihre E-Mail-Adress"
    ```
4. Klonen Sie das Projekt von GitLab:
    ```sh
    git clone https://gitlab.com/tafel_software-hs-osnabrueck/Tafel_Projekt.git
    ```
5. Navigieren Sie zum geklonten Repository:
    ```sh
    cd Pfad/zum/geklonten/Repository
    ```

    **Fehlermeldung:** Wenn `git clone` fehlschlägt, überprüfen Sie die GitLab-URL und Ihre Netzwerkverbindung.

## Maven-Installation

**Hinweis für Eclipse oder IntelliJ-Nutzer:** Wenn Sie Ihr Maven-Projekt direkt in Ihrer IDE öffnen möchten, ist eine manuelle Installation von Maven nicht erforderlich. Die IDEs verfügen über integrierte Unterstützung für Maven und ermöglichen das Importieren und Konfigurieren von Projekten direkt aus Git.

### Für Windows:

`choco install maven -y`

### Für MacOS:

`brew install maven`

## Ausführen der Anwendung über die Eingabeaufforderung/Terminal:

#### Projekteinrichten (Windows):

1. Navigieren Sie mit der Eingabeaufforderung zum `Projekt`-Verzeichnis Ihres Projekts, in dem sich die generierte Jar-Datei befindet.
    ```powershell
    cd Pfad/zum/Ihr_Projektverzeichnis
    ```
2. Kompilieren Sie Ihr Projekt, Sie können den folgenden Befehl verwenden:
    ```powershell
   mvn clean install -DskipTests
    ```
3. Navigieren Sie mit der Eingabeaufforderung zum `target`-Verzeichnis Ihres Projekts, in dem sich die generierte Jar-Datei befindet.
    ```powershell
    cd target
    ```
4. Führen Sie die Jar-Datei mit dem Befehl `java -jar` aus, gefolgt vom Namen Ihrer Jar-Datei.
    ```powershell
    java -jar tafel_2024-0.0.1-SNAPSHOT.jar
    ```

#### Projekteinrichten (MacOS):

1. Öffnen Sie das Terminal und navigieren Sie zum Verzeichnis Ihres Projekts.
```sh
cd Pfad/zum/Ihr_Projektverzeichnis
```
2. Führen Sie den folgenden Befehl aus, um die JavaFX-Anwendung direkt aus Maven heraus zu starten:
```sh
mvn javafx:run
```

## MacOS: Programm für die JAR-Datei mit Automator erstellen

Um die JAR-Datei auf dem Desktop als eine ausführbare Anwendung zu haben, können Sie Automator verwenden, um eine macOS-Anwendung zu erstellen. Befolgen Sie die folgenden Schritte:

1. **Automator öffnen:**
   - Suchen Sie nach „Automator“ in Spotlight und öffnen Sie es. Klicken Sie auf das "Programm", um es zu starten.

2. **Erstellen Sie eine neue Anwendung:**
   - Wählen Sie „Neue Anwendung“ aus und klicken Sie auf „Auswählen“.

3. **Fügen Sie die Aktion „Shell-Skript ausführen“ hinzu:**
   - Suchen Sie nach „Shell-Skript ausführen“ in der Bibliothek auf der linken Seite.
   - Ziehen Sie „Shell-Skript ausführen“ in den Workflow-Bereich auf der rechten Seite.

4. **Geben Sie den Befehl für das Shell-Skript ein:**
   - Ersetzen Sie `/USERNAME/PFAD/Tafel_Projekt/target/tafel_2024-0.0.1-SNAPSHOT.jar` mit dem tatsächlichen Pfad zu Ihrer JAR-Datei. Geben Sie den folgenden Befehl ein:
     ```bash
     cd /USERNAME/PFAD/git/Tafel_Projekt
     mvn clean install -DskipTests
     java -jar target/tafel_2024-0.0.1-SNAPSHOT.jar
     ```

5. **Speichern Sie die Anwendung:**
   - Wählen Sie „Ablage“ -> „Sichern“ aus dem Menü.
   - Geben Sie der Anwendung einen Namen, z. B. „Tafel Kundenverwaltungsprogramm“, und speichern Sie sie auf dem Desktop oder einem anderen gewünschten Ort.
   - Drücken Sie „Option + S“, um die Anwendung zu speichern.

6. **Führen Sie die Anwendung aus:**
   - Doppelklicken Sie auf die neu erstellte `.app`-Datei auf Ihrem Desktop oder dem Speicherort, um Ihre JAR-Datei auszuführen.

**Wichtige Hinweise:**
- **Zugriffsrechte:** Falls die Anwendung beim ersten Ausführen keine Berechtigungen hat, müssen Sie möglicherweise die Zugriffsrechte in den macOS-Systemeinstellungen anpassen, um der Anwendung den Zugriff auf die JAR-Datei zu ermöglichen.
- **Fehlerbehebung:** Wenn beim Ausführen der Anwendung ein Fehler auftritt, überprüfen Sie den Pfad zur JAR-Datei und stellen Sie sicher, dass alle erforderlichen Abhängigkeiten korrekt installiert sind.
- **Versionskontrolle:** Falls sich die Version oder der Speicherort Ihrer JAR-Datei ändert, passen Sie den Pfad im Shell-Skript entsprechend an.



## Kontakt

Für Unterstützung und bei weiteren Fragen wenden Sie sich bitte an die Projektleitung unter 
[tafel-projektteam@hs-osnabrueck.de](mailto:example@example.com).

Wir danken Ihnen für die Nutzung des Tafel Kundenverwaltungsprogramms und freuen uns auf Ihr Feedback und Ihre Mitarbeit.

Ihr Tafel-Projektteam