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
package rs.fon.whibo.ngui.extended;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import rs.fon.whibo.problem.Subproblem;
import rs.fon.whibo.problem.SubproblemData;
import rs.fon.whibo.problem.SubproblemParameter;

/**
 * This class representing extending JPanel class and contains JTree component
 * which represents Problem.
 * 
 * @author Nikola Nikolic
 */
public class ProblemPreviewPanel extends javax.swing.JPanel {

	/**
     * 
     */
	private static final long serialVersionUID = 1032090920935913360L;
	private final ImageIcon PROCESS_ICON = new ImageIcon("");
	private final ImageIcon STEP_ICON = new ImageIcon("");
	private final ImageIcon CLASS_ICON = new ImageIcon("");
	private final ImageIcon PARAMETER_ICON = new ImageIcon("");
	private final ImageIcon VALUE_ICON = new ImageIcon("");

	/** Creates new form ProcessPreviewPanel */
	public ProblemPreviewPanel() {
		initComponents();
	}

	/**
	 * This method creates JTree component form given Process
	 * 
	 * @param process
	 *            {@link Process}
	 */
	public void refreshTreePreview(rs.fon.whibo.problem.Problem process) {

		DefaultMutableTreeNode rootProcess = new DefaultMutableTreeNode(
				new rs.fon.whibo.ngui.extended.IconData(PROCESS_ICON,
						process.getName()));

		List<Subproblem> processSteps = process.getSubproblems();

		if (processSteps != null && processSteps.size() > 0) {
			for (Subproblem step : processSteps) {

				DefaultMutableTreeNode stepNode = new DefaultMutableTreeNode(
						new rs.fon.whibo.ngui.extended.IconData(STEP_ICON,
								step.getName()));

				if (step.isMultiple()) {
					List<SubproblemData> multipleStepData = step
							.getMultipleStepData();
					if (multipleStepData != null && multipleStepData.size() > 0) {

						for (SubproblemData stepData : multipleStepData) {

							DefaultMutableTreeNode stepDataNode = null;
							try {
								stepDataNode = new DefaultMutableTreeNode(
										new rs.fon.whibo.ngui.extended.IconData(
												STEP_ICON,
												Class.forName(
														stepData.getNameOfImplementationClass())
														.getSimpleName()));

								List<SubproblemParameter> params = stepData
										.getListOfParameters();

								if (params != null && params.size() > 0) {
									for (SubproblemParameter parameter : params) {
										DefaultMutableTreeNode paramNode = new DefaultMutableTreeNode(
												new rs.fon.whibo.ngui.extended.IconData(
														PARAMETER_ICON,
														parameter
																.getNameOfParameter()));
										DefaultMutableTreeNode valueNode = new DefaultMutableTreeNode(
												new rs.fon.whibo.ngui.extended.IconData(
														VALUE_ICON,
														getEnteredValue(parameter)));
										paramNode.add(valueNode);
										stepDataNode.add(paramNode);

									}
								}
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
							stepNode.add(stepDataNode);
						}
					}

				} else {
					if (step.getSubproblemData() != null) {
						DefaultMutableTreeNode stepDataNode = null;
						try {
							stepDataNode = new DefaultMutableTreeNode(
									new rs.fon.whibo.ngui.extended.IconData(
											STEP_ICON,
											Class.forName(
													step.getSubproblemData()
															.getNameOfImplementationClass())
													.getSimpleName()));

							List<SubproblemParameter> params = step
									.getSubproblemData().getListOfParameters();
							if (params != null && params.size() > 0) {
								for (SubproblemParameter parameter : params) {
									DefaultMutableTreeNode paramNode = new DefaultMutableTreeNode(
											new rs.fon.whibo.ngui.extended.IconData(
													PARAMETER_ICON,
													parameter
															.getNameOfParameter()));
									DefaultMutableTreeNode valueNode = new DefaultMutableTreeNode(
											new rs.fon.whibo.ngui.extended.IconData(
													VALUE_ICON,
													getEnteredValue(parameter)));
									paramNode.add(valueNode);
									stepDataNode.add(paramNode);
								}
							}
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						stepNode.add(stepDataNode);
					}
				}
				rootProcess.add(stepNode);

			}

		}
		DefaultTreeModel model = new DefaultTreeModel(rootProcess);

		jTreeProcessPreview.setModel(model);

		try {
			// Expand all the tree rows...
			int rc = 0;
			do {
				rc = this.getTree().getRowCount();
				for (int x = rc; x >= 0; x--) {
					this.getTree().expandRow(x);
				}
			} while (rc != this.getTree().getRowCount());
		} catch (Exception e) {
			// Throw when new process selected. Cause not clear yet.
		}

	}

	private String getEnteredValue(SubproblemParameter parameter) {
		String xenteredValue = parameter.getXenteredValue();

		if (parameter.getXenteredUpperValue() != null) {
			xenteredValue += " - " + parameter.getXenteredUpperValue();
		}
		return xenteredValue;
	}

	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		jTreeProcessPreview = new JTree();

		jScrollPane1.setViewportView(jTreeProcessPreview);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 220,
				Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 220,
				Short.MAX_VALUE));

	}// </editor-fold>

	private JTree getTree() {
		return jTreeProcessPreview;
	}

	// Variables declaration - do not modify
	private javax.swing.JScrollPane jScrollPane1;
	private JTree jTreeProcessPreview;
	// End of variables declaration
}
