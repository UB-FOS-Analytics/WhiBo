package rs.fon.whibo.ngui.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import rs.fon.whibo.ngui.event.SubproblemActionListener;
import rs.fon.whibo.ngui.problem.ProblemAdapter;
import rs.fon.whibo.problem.Subproblem;

public class ButtonPanel extends JPanel {
	private ProblemAdapter problemAdapter;

	private ArrayList<SubproblemActionListener> actionListeners = new ArrayList<SubproblemActionListener>();

	public ButtonPanel(ProblemAdapter problemAdapter) {
		this.problemAdapter = problemAdapter;

		setPanelLayout();
		createComponents();

	}

	public void reset() {
		removeAll();
		createComponents();
		revalidate();
		repaint();
	}

	public void addActionListener(SubproblemActionListener actionListener) {
		this.actionListeners.add(actionListener);
	}

	public void removeActionListener(SubproblemActionListener actionListener) {
		actionListeners.remove(actionListener);
	}

	private void createComponents() {
		for (int i = 0; i < problemAdapter.subproblemsCount(); i++) {
			final Subproblem subproblem = this.problemAdapter.getProblem()
					.getSubproblems().get(i);
			JButton button = addComponent(i, subproblem.getName());
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for (SubproblemActionListener actionListener : actionListeners) {
						actionListener.actionPerformed(problemAdapter,
								subproblem);
					}
				}
			});
		}
	}

	private void setPanelLayout() {
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 0, 0 };

		// create rows for gridbag layout
		int rowNum = this.problemAdapter.subproblemsCount() + 1;
		gbl.rowHeights = new int[rowNum];
		gbl.rowWeights = new double[rowNum];

		for (int i = 0; i < rowNum; i++) {
			gbl.rowHeights[i] = 60;
			gbl.rowWeights[i] = (i == rowNum - 1 ? Double.MIN_VALUE : 0);
		}

		gbl.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		this.setLayout(gbl);
	}

	private JButton addComponent(int index, String name) {
		GridBagConstraints glb = new GridBagConstraints();
		glb.fill = GridBagConstraints.BOTH;
		glb.insets = new Insets(0, 0, 5, 0);
		glb.gridx = 0;
		glb.gridy = index;

		JButton button = new JButton(name);
		this.add(button, glb);

		return button;
	}

}
