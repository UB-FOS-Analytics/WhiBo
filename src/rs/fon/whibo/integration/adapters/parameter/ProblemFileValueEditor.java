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
package rs.fon.whibo.integration.adapters.parameter;

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import rs.fon.whibo.integration.interfaces.IParameterTypeProcess;
import rs.fon.whibo.ngui.AlgorithmFrame;
import rs.fon.whibo.problem.ProblemBuilder;

import com.rapidminer.gui.properties.celleditors.value.FileValueCellEditor;
import com.rapidminer.operator.Operator;

/**
 * View of the {@link ParameterTypeProblemFile}
 * 
 * @author Nikola Nikolic
 * 
 */
public class ProblemFileValueEditor extends FileValueCellEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton processButton;

	private IParameterTypeProcess type;

	JDialog dialog = null;

	public ProblemFileValueEditor(ParameterTypeProblemFile type) {
		super(type);
		this.type = type;

		processButton = new JButton("Design Algorithm");
		addAction();
		addButton(createFileChooserButton(), GridBagConstraints.WEST);
		addButton(processButton, GridBagConstraints.REMAINDER);

		// Obavezna ipmlementacija definisana u nadklasi
	}

	public void setOperator(Operator operator) {
		// Obavezna ipmlementacija definisana u nadklasi

	}

	private void addAction() {
		processButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProblemBuilder pb = null;
				try {
					pb = type.getProcessBuilder().newInstance();
					if (dialog == null) {
						dialog = new /* MainPanel */AlgorithmFrame(pb,
								(String) getCellEditorValue(), "wba");
						dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
					}
					dialog.setVisible(true);
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}

			}
		});
	}

	@Override
	public boolean rendersLabel() {
		// TODO Auto-generated method stub
		return true;
	}

}
