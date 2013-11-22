package rs.fon.whibo.ngui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author s.velickovic@gmail.com
 */
public class Util {
	public static JFileChooser createFileChooser(int dialogType,
			String dialogTitle, String description, String extension,
			File selectedFile) {
		JFileChooser jfc = new JFileChooser();
		if (selectedFile != null)
			jfc.setSelectedFile(selectedFile);

		jfc.setDialogType(dialogType);
		jfc.setDialogTitle(dialogTitle);
		FileFilter filter = new FileNameExtensionFilter(description, extension);
		jfc.addChoosableFileFilter(filter);

		return jfc;
	}

	public static Number getValue(double value, double maxValue,
			double minValue, double max, double min, Class<?> type) {
		/*
		 * not working for simple types???
		 * 
		 * Class<?> type = parameter.getParametertType(); double min =
		 * Double.parseDouble(parameter.getMinValue()); double max =
		 * Double.parseDouble(parameter.getMaxValue());
		 * 
		 * return (Number) type.cast(((max - min) / (maxValue - minValue) *
		 * value) + min);
		 */
		if (type.equals(Byte.class) || type.equals(byte.class)) {

			return (byte) (((max - min) / (maxValue - minValue) * (value - minValue)) + min);
		} else if (type.equals(Double.class) || type.equals(double.class)) {
			return (((max - min) / (maxValue - minValue) * (value - minValue)) + min);
		} else if (type.equals(Float.class) || type.equals(float.class)) {
			return (float) (((max - min) / (maxValue - minValue) * (value - minValue)) + min);
		} else if (type.equals(Integer.class) || type.equals(int.class)) {
			return (int) (((max - min) / (maxValue - minValue) * (value - minValue)) + min);
		} else if (type.equals(Long.class) || type.equals(long.class)) {
			return (long) (((max - min) / (maxValue - minValue) * (value - minValue)) + min);
		} else if (type.equals(Short.class) || type.equals(short.class)) {
			return (short) (((max - min) / (maxValue - minValue) * (value - minValue)) + min);
		}

		return null;

	}
}
