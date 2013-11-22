package rs.fon.whibo.ngui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "rs.fon.whibo.resources.gui"; //$NON-NLS-1$
	// private static final String BUNDLE_NAME =
	// "C:\\Documents and Settings\\Silvio\\workspace\\fondex\\resources\\rs\\fon\\whibo\\resources\\gui.properties";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
