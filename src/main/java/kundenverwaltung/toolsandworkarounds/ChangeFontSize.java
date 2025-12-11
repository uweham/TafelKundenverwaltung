package kundenverwaltung.toolsandworkarounds;

import java.util.ArrayList;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.util.Callback;
import kundenverwaltung.model.Familienmitglied;

public class ChangeFontSize
{
  private static final Double DEFAULT_FONT_SIZE = Double.valueOf(13.0D);

  private static final Double DIFFERENCE_BETWEEN_PRIMARY_SECONDARY_FONTSIZE = Double.valueOf(2.0D);

  private static final Double DIFFERENCE_BETWEEN_DEFAULT_HEADER_FONTSIZE = Double.valueOf(1.0D);

  @SuppressWarnings("unused")
private static final String CSS_CODE_CHANGE_FONT_SIZE = "-fx-font-size: %.2fpx;";
  /**
   *.
   */
  public void changeFontSizeFromMenuArrayList(ArrayList<Menu> menuArrayList, DoubleProperty fontSize)
  {
    for (int i = 0; i < menuArrayList.size(); i++)
    {
      ((Menu) menuArrayList.get(i)).styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", new Object[] {fontSize }));
    }
  }
  /**
   *.
   */
  public void changeFontSizeFromMenuItemArrayList(ArrayList<MenuItem> menuItemArrayList, DoubleProperty fontSize)
  {
    for (int i = 0; i < menuItemArrayList.size(); i++)
    {
      ((MenuItem) menuItemArrayList.get(i)).styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", new Object[] {fontSize }));
    }
  }
  /**
   *.
   */
  public void changeFontSizeFromButtonArrayList(ArrayList<Button> buttonArrayList, DoubleProperty fontSize)
  {
    for (int i = 0; i < buttonArrayList.size(); i++)
    {
      ((Button) buttonArrayList.get(i)).styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", new Object[] {fontSize }));
    }
  }
  /**
   *.
   */
  public void changeFontSizeFromLabelArrayList(ArrayList<Label> labelArrayList, DoubleProperty fontSize)
  {
    for (int i = 0; i < labelArrayList.size(); i++)
    {
      ((Label) labelArrayList.get(i)).styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", new Object[] {fontSize }));
    }
  }
  /**
   *.
   */
  public void changeFontSizeFromTextFieldArrayList(ArrayList<TextField> textFieldArrayList, DoubleProperty fontSize)
  {
    for (int i = 0; i < textFieldArrayList.size(); i++)
    {
      ((TextField) textFieldArrayList.get(i)).styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", new Object[] {fontSize }));
    }
  }
  /**
   *.
   */
  public void changeFontSizeFromTextAreaArrayList(ArrayList<TextArea> textAreaArrayList, DoubleProperty fontSize)
  {
    for (int i = 0; i < textAreaArrayList.size(); i++)
    {
      ((TextArea) textAreaArrayList.get(i)).styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", new Object[] {fontSize }));
    }
  }
  /**
   *.
   */
  @SuppressWarnings("rawtypes")
public void changeFontSizeFromListViewArrayList(ArrayList<ListView> listViewArrayList, DoubleProperty fontSize)
  {
    for (int i = 0; i < listViewArrayList.size(); i++)
    {
      ((ListView) listViewArrayList.get(i)).styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", new Object[] {fontSize }));
    }
  }
  /**
   *.
   */
  public void changeFontSizeFromDatePickerArrayList(ArrayList<DatePicker> datePickerArrayList, DoubleProperty fontSize)
  {
    for (int i = 0; i < datePickerArrayList.size(); i++)
    {
      ((DatePicker) datePickerArrayList.get(i)).styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", new Object[] {fontSize }));
    }
  }
  /**
   *.
   */
  public void changeFontSizeFromCheckBoxArrayList(ArrayList<CheckBox> checkBoxArrayList, DoubleProperty fontSize)
  {
    for (int i = 0; i < checkBoxArrayList.size(); i++)
    {
      ((CheckBox) checkBoxArrayList.get(i)).styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", new Object[] {fontSize }));
    }
  }
  /**
   *.
   */
  public void changeFontSizeFromRadioButtonArrayList(ArrayList<RadioButton> radioButtonArrayList, DoubleProperty fontSize)
  {
    for (int i = 0; i < radioButtonArrayList.size(); i++)
    {
      ((RadioButton) radioButtonArrayList.get(i)).styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", new Object[] {fontSize }));
    }
  }
  /**
   *.
   */
  @SuppressWarnings("rawtypes")
public void changeFontSizeFromComboBoxArrayList(ArrayList<ComboBox> comboBoxArrayList, DoubleProperty fontSize)
  {
    for (int i = 0; i < comboBoxArrayList.size(); i++)
    {
      ((ComboBox) comboBoxArrayList.get(i)).styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", new Object[] {fontSize }));
    }
  }
  /**
   *.
   */
  @SuppressWarnings("rawtypes")
public void changeFontSizeFromTableColumnArrayList(ArrayList<TableColumn> tableColumnArrayList, DoubleProperty fontSize)
  {
    for (int i = 0; i < tableColumnArrayList.size(); i++)
    {
      ((TableColumn) tableColumnArrayList.get(i)).styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", new Object[] {fontSize }));
    }
  }
  /**
   *.
   */
  public void changeFontSizeFromTabArrayList(ArrayList<Tab> tabArrayList, DoubleProperty fontSize)
  {
    for (int i = 0; i < tabArrayList.size(); i++)
    {
      ((Tab) tabArrayList.get(i)).styleProperty().bind(Bindings.format("-fx-font-size: %.2fpx;", new Object[] {fontSize }));
    }
  }
  /**
   *.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
public void changeFontSizeFromTableColumn(TableColumn column, final Double fontSize)
  {
    column.setCellFactory(new Callback<TableColumn, TableCell>()
    {
          public TableCell call(TableColumn param)
          {
            return new TableCell<Familienmitglied, String>()
            {
                public void updateItem(String item, boolean empty)
                {
                  super.updateItem(item, empty);
                  if (isEmpty())
                  {
                    setText("");
                  } else
                  {
                    setFont(Font.font(fontSize.doubleValue()));
                    setText(item);
                  }
                }
              };
          }
        });
  }

  public static Double getDefaultFontSize()
  {
    return DEFAULT_FONT_SIZE;
  }

  public static Double getDifferenceBetweenPrimarySecondaryFontsize()
  {
    return DIFFERENCE_BETWEEN_PRIMARY_SECONDARY_FONTSIZE;
  }

  public static Double getDifferenceBetweenDefaultHeaderFontsize()
  {
    return DIFFERENCE_BETWEEN_DEFAULT_HEADER_FONTSIZE;
  }
}
