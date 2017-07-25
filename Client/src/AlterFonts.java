import java.awt.Font;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class AlterFonts {
	public static void alterFonts(){
		
		UIManager.put("Menu.font", new Font("SansSerif", Font.TRUETYPE_FONT, 12));
		UIManager.put("MenuItem.font", new Font("SansSerif", Font.TRUETYPE_FONT, 12));
		UIManager.put("Label.font", new Font("SansSerif", Font.TRUETYPE_FONT, 12));
		UIManager.put("Button.font", new Font("SansSerif", Font.TRUETYPE_FONT, 12));
		UIManager.put("RadioButton.font", new Font("SansSerif", Font.TRUETYPE_FONT, 12));
		UIManager.put("CheckBox.font", new Font("SansSerif", Font.TRUETYPE_FONT, 12));
		UIManager.put("TitledBorder.font", new Font("SansSerif", Font.TRUETYPE_FONT, 12));
		UIManager.put("ComboBox.font", new Font("SansSerif", Font.TRUETYPE_FONT, 12));
		UIManager.put("defaultFont", new Font("SansSerif", Font.TRUETYPE_FONT, 12));
	}
}