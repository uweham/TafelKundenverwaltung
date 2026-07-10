package kundenverwaltung.service;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;

public class  TableView_to_PDF<T> {
  
  
  private TableView<T> queryResultTable;
  private File file=null;
  private String tabledescription="";
  private float fontSize = 12f;
  private PDType1Font font= PDType1Font.HELVETICA;
  private String resultmessage="";
  private int rows_per_page=20;
  
  // Getter / Setter

  public int getRows_per_page() {
    return rows_per_page;
  }

  public void setRows_per_page(int rows_per_page) {
    this.rows_per_page = rows_per_page;
  }
  
  public String getResultmessage() {
    return resultmessage;
  }

  public void setResultmessage(String resultmessage) {
    this.resultmessage = resultmessage;
  }

  public PDType1Font getFont() {
    return font;
  }

  public void setFont(PDType1Font font) {
    this.font = font;
  }

  public String getTabledescription() {
    return tabledescription;
  }

  public void setTabledescription(String tabledescription) {
    this.tabledescription = tabledescription;
  }
 
  public float getFontSize() {
    return fontSize;
  }

  public void setFontSize(float fontSize) {
    this.fontSize = fontSize;
  }
  
  // Init
  public TableView_to_PDF(TableView<T> queryResultTable)
  {
    this.queryResultTable=queryResultTable;
    
  }
  
  public TableView_to_PDF(TableView<T> queryResultTable,File file)
  {
    this.queryResultTable=queryResultTable;
    this.file=file;
  }
 
  public TableView_to_PDF(TableView<T> queryResultTable,File file,String tabledescription)
  {
    this.queryResultTable=queryResultTable;
    this.file=file;
    this.tabledescription=tabledescription;
  }
  
  public int to_PDF()
  {
      int returncode=Constants.NO_ERROR;
      try (PDDocument document = new PDDocument())
      {
          
          PDPage page = new PDPage();
          document.addPage(page);
          
          // Core layout variables
          float margin = 50;
          float yStart = page.getMediaBox().getHeight() - margin;
          float yStartHeader = yStart;
          float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
          int rows=queryResultTable.getItems().size();  
          int cols=queryResultTable.getColumns().size();
          float rowHeight = 30f;
          float colWidth = tableWidth / (float) cols;
          float cellPadding = 5f;
          String pageprint="";
          float pageprintwidth=0;
          
          try 
          {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            String[][] tableData=new String[rows][cols];
            String[] headerData=new String[cols];
            
            int i_col=0;
            
            // get columns + header
            for (TableColumn<T, ?> column : queryResultTable.getColumns()) {
              String headerText = column.getText();
              System.out.println("Found column: " + headerText);
              headerData[i_col]=headerText;
              i_col++;
            }  
            int i_row=0;
            //for (ObservableList<String> row : queryResultTable.getItems())
            for (T row : queryResultTable.getItems())
            {
              for (int i = 0; i < cols; i++) 
              {
                String value = (String) ((List<T>) row).get(i);
   //uncomment if debug             System.out.println("Found Row: " +i+ " Value "+ value);
                tableData[i_row][i]=(value == null)?"null": value;
              }
              i_row ++;
            }
              
              int pageNo=1;
              contentStream.beginText();
              contentStream.setFont(this.font, this.fontSize);
              contentStream.setLeading(14.5f);
              contentStream.newLineAtOffset(margin, yStartHeader);
              contentStream.showText(tabledescription);
              contentStream.endText();
              pageprint="Page: "+pageNo;
              pageprintwidth= this.font.getStringWidth(pageprint)*this.fontSize / 1000f;
              contentStream.beginText();
              contentStream.newLineAtOffset(margin + tableWidth-pageprintwidth,yStartHeader);
              contentStream.showText(pageprint);
              contentStream.newLine();
              contentStream.newLine();
              contentStream.endText();
              float textHeight = this.font.getFontDescriptor().getFontBoundingBox().getHeight() * this.fontSize / 1000f;
              yStart = yStart - textHeight - margin;
              
              
              // 1. Draw Table Structure (Grid Borders)
              contentStream.setStrokingColor(Color.DARK_GRAY);
              contentStream.setLineWidth(1f);

              
              int row_begin=0;
              int row_end=(rows>this.rows_per_page)?this.rows_per_page:rows;
              do 
              { // generate one page 
                float currentY = yStart;
                for (int i = 0; i <= row_end-row_begin+1; i++) {
                    // Draw horizontal lines
                    contentStream.moveTo(margin, currentY);
                    contentStream.lineTo(margin + tableWidth, currentY);
                    contentStream.stroke();
                    currentY -= rowHeight;
                }

                for (int i = 0; i <= cols; i++) {
                    // Draw vertical lines
                    float currentX = margin + (i * colWidth);
                    contentStream.moveTo(currentX, yStart);
                    contentStream.lineTo(currentX, yStart - ((row_end-row_begin+1) * rowHeight));
                    contentStream.stroke();
                }
                // 2. Populate Text Elements
                
                // print header
                for (int col = 0; col < cols; col++) {
                  String text = headerData[col];
                  // Calculate pinpoint layout offsets
                  float textX = margin + (col * colWidth) + cellPadding;
                  // Center text vertically relative to the cell container line boundary
                  float textY = yStart - (rowHeight) + cellPadding + 5f; 
                  contentStream.beginText();
                  //contentStream.setFont(font, fontSize);
                  contentStream.newLineAtOffset(textX, textY);
                  contentStream.showText(text);
                  contentStream.endText();
                }
              
                for (int row = row_begin; row < row_end; row++) {
                    for (int col = 0; col < cols; col++) {
                        String text = tableData[row][col];
                        // Calculate pinpoint layout offsets
                        float textX = margin + (col * colWidth) + cellPadding;
                        // Center text vertically relative to the cell container line boundary
                        float textY = yStart - rowHeight - ((row-row_begin + 1) * rowHeight) + cellPadding + 5f ; 
  
                        contentStream.beginText();
                        //contentStream.setFont(font, fontSize);
                        contentStream.newLineAtOffset(textX, textY);
                        contentStream.showText(text);
                        contentStream.endText();
                    }
                  }
                System.out.println("row_begin: "+row_begin+" row_end: " +row_end);
                if (row_end<rows)
                 {
                   //new page
                   contentStream.close();
                   PDPage newPage = new PDPage();
                   document.addPage(newPage);
                   pageNo++;
                   
                   contentStream = new PDPageContentStream(document, newPage);
                   contentStream.setFont(this.font, this.fontSize);
                   contentStream.beginText();
                   contentStream.setFont(this.font, this.fontSize);
                   contentStream.setLeading(14.5f);
                   contentStream.newLineAtOffset(margin, yStartHeader);
                   contentStream.showText(tabledescription);
                   contentStream.endText();
                   pageprint="Page: "+pageNo;
                   pageprintwidth= this.font.getStringWidth(pageprint)*this.fontSize / 1000f;
                   contentStream.beginText();
                   contentStream.newLineAtOffset(margin + tableWidth-pageprintwidth,yStartHeader);
                   contentStream.showText(pageprint);
                   contentStream.newLine();
                   contentStream.newLine();
                   contentStream.endText();
                   
                   row_begin=row_begin+this.rows_per_page;
                   row_end =row_end+this.rows_per_page; 
                   row_end=(rows>row_end)?row_end:rows;
                 }
                 else 
                 {
                   break;
                 }
                } while (true);
               contentStream.close();
              
          } catch (IOException e) 
          {
            e.printStackTrace();
          };
          document.save(file);
          this.resultmessage = "PDF erfolgreich gespeichert.";
          returncode=Constants.NO_ERROR;
      } catch (IOException e)
      {
          this.resultmessage="Fehler beim Speichern des PDFs: " + e.getMessage();
          returncode=Constants.FILE_PDF_GEN_ERROR;
      }
      return returncode;
  }
}
  
  
  

