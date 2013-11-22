/*
 *  WhiBo
 *
 *  Copyright (C) 2010- by WhiBo development team and the contributors
 *
 *  Complete list of developers available at our web site:
 *
 *       http://www.whibo.fon.bg.ac.rs
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package rs.fon.whibo.gui.panels.toolbar;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import rs.fon.whibo.GDT.problem.GenericTreeProblemBuilder;
import rs.fon.whibo.gui.MainPanel;
import rs.fon.whibo.problem.serialization.ProblemDecoder;
import rs.fon.whibo.problem.serialization.ProblemEncoder;

/**
 * The Class Toolbar.
 * 
 * @author Sasa Mrkela
 */
public class Toolbar extends ToolbarPanel {

	@Override
	protected void loadFile() {
		JFileChooser jfc = new JFileChooser();
		if (selectedFile != null)
			jfc.setSelectedFile(selectedFile);
		String message = "";
		jfc.setDialogTitle("Open WhiBo algorithm from a file");
		FileFilter filter = new FileNameExtensionFilter(
				"WhiBo Algorithm file (.wba)", "wba");
		jfc.addChoosableFileFilter(filter);
		int status = jfc.showOpenDialog(this);

		if (status == JFileChooser.APPROVE_OPTION) {
			selectedFile = jfc.getSelectedFile();
			try {
				MainPanel.setProcess(ProblemDecoder
						.decodeFromXMLToProces(selectedFile.getAbsolutePath()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			message = "WhiBo Algorithm loaded from " + selectedFile.getName();
		} else
			message = "No file selected";
		JOptionPane.showMessageDialog(this, message);
	}

	@Override
	public void loadFile(String filePath) {

		try {
			MainPanel
					.setProcess(ProblemDecoder.decodeFromXMLToProces(filePath));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		selectedFile = new File(filePath);

	}

	@Override
	protected void saveFile() {
		JFileChooser jfc = new JFileChooser();
		if (selectedFile != null)
			jfc.setSelectedFile(selectedFile);
		jfc.setDialogType(JFileChooser.SAVE_DIALOG);
		jfc.setDialogTitle("Save WhiBo Algorithm to a file");
		FileFilter filter = new FileNameExtensionFilter(
				"WhiBo Algorithm file (.wba)", "wba");
		jfc.addChoosableFileFilter(filter);
		final int showSaveDialog = jfc.showDialog(this, "Save WhiBo algorithm");

		if (showSaveDialog == JFileChooser.APPROVE_OPTION) {
			FileFilter fileFilter = jfc.getFileFilter();
			if (fileFilter.getDescription().equals(
					"WhiBo Algorithm file (.wba)")
					|| fileFilter.getDescription().equals("All Files")) {
				String file = jfc.getSelectedFile().getAbsolutePath();
				if (!file.endsWith(".wba"))
					file += ".wba";
				try {
					ProblemEncoder.encodeFormProcesToXML(
							MainPanel.getProcess(), file);
					JOptionPane.showMessageDialog(this,
							"WhiBo Algorithm saved.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void newFile() {
		String question = "Are you sure you want to start a new process? The current data will be lost.";
		int result = JOptionPane.showOptionDialog(this, question,
				"Confirmation", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE, null, null, null);

		if (result == JOptionPane.YES_OPTION)
			MainPanel
					.setProcess(new GenericTreeProblemBuilder().buildProblem());
	}

}
