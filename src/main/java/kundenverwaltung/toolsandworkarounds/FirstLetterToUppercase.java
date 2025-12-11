package kundenverwaltung.toolsandworkarounds;


import javafx.scene.control.TextField;

/**
 * This class changes the first Letter of the String to Uppercase.
 * @Author Adam Starobrzanski
 * @Date 03.08.2018
 */

public class FirstLetterToUppercase
{

	/**
	 * On key released change first letter to uppercase.
	 * @param textField a text field where te first letter should be changed.
	 */

	public void firstLetterUppercase(TextField textField)
	{
		textField.setOnKeyReleased(event ->
		{
			String input = textField.getText();
			String firstLetter;

			if (input.length() > 0)
			{
				firstLetter = input.substring(0, 1).toUpperCase();
				textField.setText(firstLetter + input.substring(1));
				textField.positionCaret(input.length());
			}
		});
	}
}
