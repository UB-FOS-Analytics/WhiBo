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

import java.awt.Dialog;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import rs.fon.whibo.gui.MainPanel;
import rs.fon.whibo.problem.ComponentCompatibilityValidator;
import rs.fon.whibo.problem.Subproblem;
import rs.fon.whibo.problem.SubproblemData;
import rs.fon.whibo.problem.SubproblemParameter;
import rs.fon.whibo.problem.SubproblemParameterReader;

/**
 * 
 * @author Sasa
 */
public abstract class EditorPanel extends javax.swing.JPanel {

	/**
	 * 
	 * Definition of GUI's central panel structure and behvior. Concrete look of
	 * central panel is shown in run time from definitions of subproblems and
	 * components in code.
	 * 
	 */
	private static final long serialVersionUID = -8141066767385529563L;
	/**
	 * Variables used for labeling.
	 */
	private static final String NAME_LABEL = "Component name: ";
	private static final String DESCRIPTION_LABEL = "Component description: ";
	private static final String IS_MULTIPLE_LABEL = "Is the component multiple: ";
	private static final String SELECT_IMPLEMENTATION_LABEL = "Select component: ";
	private static final String PARAMETERS_LABEL = "Parameters: ";
	private static final String TOOLTIP_LIST = "Press 'F1' to see more information on selected components.";

	/** Step which information is drawn **/
	private Subproblem step;

	/** Owner dialog **/
	JDialog owner;

	/** Creates new form Editor */
	public EditorPanel() {
		initComponents();
	}

	public void setOwner(JDialog jOwnerDialog) {
		owner = jOwnerDialog;
	}

	protected abstract void performAction();

	public void load(Subproblem step) {
		this.step = step;
		drawAll();
		if (step.getMultipleStepData() != null
				|| step.getSubproblemData() != null)
			fireItemsSelected();
		else
			parameters1.preparePanel();
	}

	protected void drawAll() {
		setStepName();
		setDescription();
		setIsMultiple();
		setList();
		setSelectedListItem();
	}

	private void fireItemsSelected() {
		try {
			if (step.isMultiple()) {
				parameters1.load(getSelectedClasses(),
						step.getMultipleStepData());
			} else {
				parameters1
						.load(getSelectedClasses(), step.getSubproblemData());
			}
		} catch (Exception e) {
			// Do nothing. Thrown if new process selected
			System.err.println("fireItemsSelected: " + e);
		}
	}

	/**
	 * If F1 key is pressed show class summary dialog
	 */
	private void listKeyPressed(KeyEvent e) {
		if (KeyEvent.VK_F1 == e.getKeyCode()) {
			Class[] selectedClasses = getSelectedClasses();
			for (Class clazz : selectedClasses) {
				ClassSummaryDialog classSummaryDialog = new ClassSummaryDialog(
						clazz, owner);
				classSummaryDialog.setLocationRelativeTo(null);
				classSummaryDialog
						.setModalityType(Dialog.ModalityType.MODELESS);
				classSummaryDialog.setVisible(true);
			}
		}
	}

	private void setDescription() {
		if (step == null)
			this.description.setText("");
		else
			this.description.setText(step.getDescription());
	}

	private void setIsMultiple() {
		if (step == null) {
			this.isMultiple.setSelected(false);
		} else {

			this.isMultiple.setSelected(step.isMultiple());
			if (isMultiple.isSelected()) {
				list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			} else {
				list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}
		}
	}

	private void setSelectedListItem() {
		if (step == null)
			return;

		if (step.getMultipleStepData() == null
				&& step.getSubproblemData() == null) // Means
			// this
			// step
			// was
			// never
			// saved
			// before
			return;

		try {
			if (!isMultiple.isSelected()) {
				System.out.println("SAVED IMPL CLASS: "
						+ step.getSubproblemData()
								.getNameOfImplementationClass());
				list.setSelectedValue(step.getSubproblemData()
						.getNameOfImplementationClass(), true);
			} else {
				List<SubproblemData> multiStepData = step.getMultipleStepData();
				int numOfSelections = multiStepData.size();
				String[] classes = new String[numOfSelections];

				int i = 0;
				for (SubproblemData data : multiStepData) {
					classes[i++] = data.getNameOfImplementationClass();
				}

				int[] indices = new int[numOfSelections];

				i = 0;
				int j = 0;
				final String[] allowedClasses = ComponentCompatibilityValidator
						.getAllowedClasses(MainPanel.getProcess(), step);
				for (String s : allowedClasses) {

					for (String l : classes) {
						if (s.equals(l))
							indices[i++] = j;
					}

					j++;
				}

				list.setSelectedIndices(indices);

			}
		} catch (NullPointerException ne) {
			// If step shown but no implementation selected, doesn't cause
			// problems
		}
	}

	private void setStepName() {
		if (step == null)
			this.name.setText("");
		else
			this.name.setText(step.getName());
	}

	private void setList() {
		if (step == null) {
			this.list.setModel(new DefaultListModel());
		} else {

			final String[] allowedClasses = ComponentCompatibilityValidator
					.getAllowedClasses(MainPanel.getProcess(), step);

			for (int i = 0; i < allowedClasses.length; i++) { // remove package
																// names
				String[] parts = allowedClasses[i].split("\\.");
				allowedClasses[i] = parts[parts.length - 1];
			}

			DefaultListModel listModel = new DefaultListModel();
			for (String s : allowedClasses) {
				listModel.addElement(s);
			}
			list.setModel(listModel);

		}
	}

	@SuppressWarnings("unchecked")
	protected Class[] getSelectedClasses() {
		try {
			int[] indices = list.getSelectedIndices();
			Class[] selectedClasses = new Class[indices.length];

			final String[] allowedClasses = ComponentCompatibilityValidator
					.getAllowedClasses(MainPanel.getProcess(), step);

			for (int i = 0; i < indices.length; i++) {
				selectedClasses[i] = Class.forName(allowedClasses[indices[i]]);
			}

			return selectedClasses;
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(EditorPanel.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void saveStep() {

		List<SubproblemParameter> oldParams = null;
		try {
			oldParams = parameters1.getParams();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			return;
		}

		// Clone the list of params
		List<SubproblemParameter> params = new LinkedList<SubproblemParameter>();
		for (int i = 0; i < oldParams.size(); i++) {
			try {
				System.out.println("VALUE AND TYPE: "
						+ oldParams.get(i).getXenteredValue()
						+ oldParams.get(i).getParametertType());
				params.add((SubproblemParameter) oldParams.get(i).clone());
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(EditorPanel.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}

		System.out.println("GP:::Saving component " + step.getName()
				+ " which is "
				+ (step.isMultiple() ? "multiple" : "not multiple") + ".");
		System.out.print("GP:::Params entered values to be saved are: ");
		for (SubproblemParameter p : params)
			System.out.print(p.getXenteredValue() + " ");
		System.out.println(".");

		if (!step.isMultiple()) {
			String selectedClassName = getSelectedClasses()[0].getName();
			SubproblemData sd = new SubproblemData();
			sd.setListOfParameters(params);
			sd.setNameOfImplementationClass(selectedClassName);
			System.out.println("GP:::Saving single component data: "
					+ sd.getNameOfImplementationClass() + " with "
					+ sd.getListOfParameters().size() + " params.");
			step.setSubproblemData(sd);
		} else {
			List<SubproblemData> stepData = new LinkedList<SubproblemData>();
			Class[] selectedClases = getSelectedClasses();
			List<SubproblemParameter> helperList;
			int counter = 0; // count number of params of each class for
			// skipping
			int lastPosition = 0; // remembers the last edited param index
			for (Class c : selectedClases) {
				counter = SubproblemParameterReader.readParameters(c).size();
				SubproblemData sd = new SubproblemData();
				helperList = new LinkedList<SubproblemParameter>();
				for (int i = lastPosition; i < lastPosition + counter; i++) {
					helperList.add(params.get(i));
				}
				sd.setListOfParameters(helperList);
				sd.setNameOfImplementationClass(c.getName());
				stepData.add(sd);
				lastPosition += counter;
			}
			step.setMultipleSubproblemData(stepData);
		}

		performAction();
	}

	private void disableStep() {

		System.out.println("GP:::Disabling component " + step.getName()
				+ " which is "
				+ (step.isMultiple() ? "multiple" : "not multiple") + ".");

		if (!step.isMultiple()) {
			step.setSubproblemData(null);
		} else {
			step.setMultipleSubproblemData(null);
		}

		load(step);
		performAction();
	}

	public void restart() {
		step = null;
		drawAll();
		parameters1.preparePanel();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		name = new javax.swing.JLabel();
		description = new javax.swing.JLabel();
		isMultiple = new javax.swing.JCheckBox();
		jLabel4 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		list = new javax.swing.JList();
		jLabel5 = new javax.swing.JLabel();
		parameters1 = new rs.fon.whibo.gui.editor.Parameters();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();

		jLabel1.setText(NAME_LABEL);

		jLabel2.setText(DESCRIPTION_LABEL);

		jLabel3.setText(IS_MULTIPLE_LABEL);

		name.setText(" ");

		description.setText(" ");

		isMultiple.setEnabled(false);

		jLabel4.setText(SELECT_IMPLEMENTATION_LABEL);

		list.setToolTipText(TOOLTIP_LIST);
		list.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				listValueChanged(evt);
			}
		});
		list.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// do nothing
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// do nothing
			}

			/*
			 * if F1 key is pressed show summary frame (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyPressed(KeyEvent e) {
				listKeyPressed(e);
			}
		});
		jScrollPane1.setViewportView(list);

		jLabel5.setText(PARAMETERS_LABEL);

		parameters1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		javax.swing.GroupLayout parameters1Layout = new javax.swing.GroupLayout(
				parameters1);
		parameters1.setLayout(parameters1Layout);
		parameters1Layout.setHorizontalGroup(parameters1Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 396, Short.MAX_VALUE));
		parameters1Layout.setVerticalGroup(parameters1Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 91, Short.MAX_VALUE));

		jButton1.setText("Save component");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jButton2.setText("Disable component");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														parameters1,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(
																						jLabel1)
																				.addComponent(
																						jLabel2)
																				.addComponent(
																						jLabel3)
																				.addComponent(
																						jLabel4))
																.addGap(28, 28,
																		28)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.TRAILING)
																				.addComponent(
																						jScrollPane1,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						306,
																						Short.MAX_VALUE)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.LEADING)
																												.addGroup(
																														layout.createSequentialGroup()
																																.addGroup(
																																		layout.createParallelGroup(
																																				javax.swing.GroupLayout.Alignment.TRAILING)
																																				.addComponent(
																																						description,
																																						javax.swing.GroupLayout.DEFAULT_SIZE,
																																						183,
																																						Short.MAX_VALUE)
																																				.addComponent(
																																						name,
																																						javax.swing.GroupLayout.DEFAULT_SIZE,
																																						183,
																																						Short.MAX_VALUE))
																																.addPreferredGap(
																																		javax.swing.LayoutStyle.ComponentPlacement.RELATED))
																												.addGroup(
																														layout.createSequentialGroup()
																																.addComponent(
																																		isMultiple)
																																.addGap(166,
																																		166,
																																		166)))
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.LEADING,
																												false)
																												.addComponent(
																														jButton2,
																														javax.swing.GroupLayout.DEFAULT_SIZE,
																														javax.swing.GroupLayout.DEFAULT_SIZE,
																														Short.MAX_VALUE)
																												.addComponent(
																														jButton1,
																														javax.swing.GroupLayout.DEFAULT_SIZE,
																														119,
																														Short.MAX_VALUE)))))
												.addComponent(jLabel5))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addContainerGap()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										jLabel1)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.BASELINE)
																												.addComponent(
																														jLabel2)
																												.addComponent(
																														description)))
																				.addGroup(
																						layout.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.BASELINE)
																								.addComponent(
																										name)
																								.addComponent(
																										jButton1,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										33,
																										javax.swing.GroupLayout.PREFERRED_SIZE)))
																.addGap(24, 24,
																		24)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										jLabel3)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addComponent(
																										jLabel4))
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										isMultiple)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addComponent(
																										jScrollPane1,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										107,
																										javax.swing.GroupLayout.PREFERRED_SIZE)))
																.addGap(10, 10,
																		10)
																.addComponent(
																		jLabel5)
																.addGap(18, 18,
																		18)
																.addComponent(
																		parameters1,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE))
												.addGroup(
														layout.createSequentialGroup()
																.addGap(52, 52,
																		52)
																.addComponent(
																		jButton2,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		31,
																		javax.swing.GroupLayout.PREFERRED_SIZE)))
								.addContainerGap()));
	}// </editor-fold>//GEN-END:initComponents

	private void listValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN
		// -
		// FIRST
		// :
		// event_listValueChanged
		if (!evt.getValueIsAdjusting())
			fireItemsSelected();
	}// GEN-LAST:event_listValueChanged

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-
		// FIRST
		// :
		// event_jButton1ActionPerformed
		if (step == null || step.getName().equals(""))
			JOptionPane.showMessageDialog(this,
					"No component defined. There is nothing to save.");
		else if (getSelectedClasses().length == 0)
			JOptionPane
					.showMessageDialog(
							this,
							"No class selected so there is nothing to save. \nIf you disabled this component there is no need to save it, it is saved automaticaly.");
		else
			saveStep();
	}// GEN-LAST:event_jButton1ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-
		// FIRST
		// :
		// event_jButton2ActionPerformed
		disableStep();
	}// GEN-LAST:event_jButton2ActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	protected javax.swing.JLabel description;
	protected javax.swing.JCheckBox isMultiple;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JScrollPane jScrollPane1;
	protected javax.swing.JList list;
	protected javax.swing.JLabel name;
	private rs.fon.whibo.gui.editor.Parameters parameters1;
	// End of variables declaration//GEN-END:variables

}
