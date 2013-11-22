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

package rs.fon.whibo.gui.editor;

import java.awt.FlowLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rs.fon.whibo.problem.SubproblemData;
import rs.fon.whibo.problem.SubproblemParameter;
import rs.fon.whibo.problem.SubproblemParameterReader;

/**
 * 
 * @author Sasa Mrkela
 */
public abstract class ParametersPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = -421225202319351182L;

	/** Parameters used for representation **/
	private List<SubproblemParameter> params = new LinkedList<SubproblemParameter>();

	/** TextFields containing parameters **/
	private List<JTextField> textFields = new LinkedList<JTextField>();

	/** Reference to the editor as a parent of this somponent **/
	@SuppressWarnings("unused")
	private EditorPanel editor;

	/** Creates new form Parameters */
	public ParametersPanel() {
		initComponents();
	}

	/**
	 * Sets the parameters for representation in case where multiple classes can
	 * be selected
	 * 
	 * @param classes
	 *            - classes for which the params will be shown
	 */

	@SuppressWarnings("unchecked")
	public void load(Class[] classes, List<SubproblemData> data) {

		System.out
				.println("GP:::Load method called for class ParametersPanel.");

		if (classes == null || classes.length == 0)
			return;

		params.removeAll(params); // Params list must be fresh each new time
		for (Class clazz : classes) {
			params.addAll(SubproblemParameterReader.readParameters(clazz));
		}
		updateParams(classes, data);
		setFields();
	}

	@SuppressWarnings("unchecked")
	public void load(Class[] classes, SubproblemData data) {
		if (data == null)
			load(classes, new LinkedList<SubproblemData>());
		else {
			List<SubproblemData> list = new LinkedList<SubproblemData>();
			list.add(data);
			load(classes, list);
		}
	}

	/**
	 * Checks whether some parameters have their values allready entered and if
	 * so assigns these values to them.
	 * 
	 * @param classes
	 *            classes which params are being checked
	 * @param data
	 *            data that contains entered values
	 */

	@SuppressWarnings("unchecked")
	private void updateParams(Class[] classes, List<SubproblemData> data) {

		if (data == null || data.size() == 0)
			return;

		System.out.println("GP:::Updating params for " + classes.length
				+ " classes. Provided " + data.size() + " StepData items.");

		int counter = 0; // count number of params of each class for skipping
		int lastPosition = 0; // remembers the last edited param index
		// Go through all selected classes, check number of params they have
		// Iterate through all step data and if certain step data belogs to the
		// mentioned class
		// iterate through all the params of that step data object and
		// delegate their values to the params of this class
		for (Class c : classes) {
			counter = SubproblemParameterReader.readParameters(c).size();
			for (SubproblemData step : data) {
				if (c.getName().equals(step.getNameOfImplementationClass())) {
					List<SubproblemParameter> stepParams = step
							.getListOfParameters();
					for (int i = 0; i < counter; i++) {
						String value = stepParams.get(i).getXenteredValue();
						System.out.println("GP:::Entered value is " + value);
						if (value == null)
							value = stepParams.get(i).getDefaultValue();
						System.out.println("GP:::Updated value is " + value);
						params.get(lastPosition + i).setXenteredValue(value);
					}
				}
			}
			lastPosition += counter;
		}
	}

	/**
	 * Removes all components from the panel. Used when steps are being changed
	 */

	public void preparePanel() {
		params.removeAll(params);
		cleanPanel();
	}

	protected void setFields() {
		cleanPanel();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// JScrollPane scroll=new JScrollPane();

		if (params.size() > 0) {

			for (SubproblemParameter param : params) {
				addTriplet(param);
			}

			revalidate();
			repaint();
		}
	}

	private void addTriplet(SubproblemParameter param) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

		addNameLabel(param);
		panel.add(addNameLabel(param));
		JTextField textField = addField(param);
		textFields.add(textField);
		panel.add(textField);
		JLabel l2 = addDescriptionLabel(param);
		panel.add(l2);

		add(panel);
	}

	/**
	 * Returns a list of parameters
	 * 
	 */
	public List<SubproblemParameter> getParams() {
		for (int i = 0; i < params.size(); i++) {
			params.get(i).setXenteredValue(textFields.get(i).getText());
		}
		return params;
	}

	/**
	 * Cleans the panel when step changed and no inmplementation class selected
	 * yet
	 */

	protected void cleanPanel() {
		removeAll();
		textFields.removeAll(textFields);
		revalidate();
		repaint();
	}

	// <editor-fold defaultstate="collapsed"
	// desc="Methods for adding components">
	private JLabel addDescriptionLabel(SubproblemParameter param) {
		String type = param.getParametertType().getSimpleName();
		String min = param.getMinValue();
		String max = param.getMaxValue();
		String def = param.getDefaultValue();

		String description = "(Type: " + type + ", Min: " + min + ", Max: "
				+ max + ", Default: " + def + " )";
		return new JLabel(description);
	}

	private JTextField addField(SubproblemParameter param) {
		String value = param.getXenteredValue();
		if (value == null)
			value = param.getDefaultValue();
		JTextField tf = new JTextField(value, 5);
		return tf;
	}

	private JLabel addNameLabel(SubproblemParameter param) {
		String text = param.getNameOfParameter() + ": ";
		return new JLabel(text);
	}// </editor-fold>

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 315,
				Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 201,
				Short.MAX_VALUE));
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	// End of variables declaration//GEN-END:variables

}
