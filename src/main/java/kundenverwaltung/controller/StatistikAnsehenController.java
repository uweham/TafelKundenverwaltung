package kundenverwaltung.controller;

import java.awt.Desktop;
import java.io.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class StatistikAnsehenController
{
	@FXML
	private TextArea csvContentArea;
	@FXML
	private Button buttonClose;

	@FXML
	private Button buttonPrint;

	@FXML
	private Button buttonDownload;
	private static final String CSV_FILE_PATH = "altersstatistik.csv";

	/**
	 * Initializes the controller by loading and displaying CSV content.
	 */
	public void initialize()
	{
		displayCSVContent();
	}

	private void displayCSVContent()
	{
		try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH)))
		{
			StringBuilder content = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null)
			{
				content.append(line).append("\n");
			}
			csvContentArea.setText(content.toString());
		} catch (IOException e)
		{
			showAlert("Fehler", "Fehler beim Lesen der PDF-Datei: " + e.getMessage());
		}
	}

	@FXML
	private void handlePrint()
	{
		try
		{
			Desktop desktop = Desktop.getDesktop();
			File file = new File(CSV_FILE_PATH);
			if (file.exists())
			{
				desktop.print(file);
			} else
			{
				showAlert("Fehler", "Die PDF-Datei existiert nicht.");
			}
		} catch (IOException e)
		{
			showAlert("Fehler", "Fehler beim Drucken der Datei: " + e.getMessage());
		}
	}

	@FXML
	private void handleDownload()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Speichern unter");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF-Dateien", "*.pdf"));
		File file = fileChooser.showSaveDialog(buttonDownload.getScene().getWindow());

		if (file != null)
		{
			saveAsPdf(file);
		}
	}

	private void saveAsPdf(File file)
	{
		try (PDDocument document = new PDDocument())
		{
			PDPage page = new PDPage();
			document.addPage(page);

			try (PDPageContentStream contentStream = new PDPageContentStream(document, page))
			{
				contentStream.beginText();
				contentStream.setFont(PDType1Font.HELVETICA, 12);
				contentStream.setLeading(14.5f);
				contentStream.newLineAtOffset(25, 725);

				for (String line : csvContentArea.getText().split("\n"))
				{
					contentStream.showText(line);
					contentStream.newLine();
				}
				contentStream.endText();
			}

			document.save(file);
			showAlert("Erfolg", "PDF erfolgreich gespeichert.");
		} catch (IOException e)
		{
			showAlert("Fehler", "Fehler beim Speichern des PDFs: " + e.getMessage());
		}
	}

	@FXML
	private void handleClose()
	{
		Stage stage = (Stage) buttonClose.getScene().getWindow();
		stage.close();
	}

	private void showAlert(String title, String message)
	{
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
