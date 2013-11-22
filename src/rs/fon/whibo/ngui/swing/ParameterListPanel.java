package rs.fon.whibo.ngui.swing;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import rs.fon.whibo.ngui.problem.ProblemAdapter;

public class ParameterListPanel extends JPanel {
	private ProblemAdapter problemAdapter;

	public ParameterListPanel(ProblemAdapter problemAdapter) {
		this.problemAdapter = problemAdapter;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	public void setSelection(int[] selectedIndices)
			throws ClassNotFoundException {
		this.removeAll();

		problemAdapter.setSelection(selectedIndices);

		ParameterPanel[] panels = problemAdapter.createPanels();
		for (ParameterPanel panel : panels) {
			addParameterPanel(panel);
		}

		revalidate();
		this.repaint();
	}

	public void removeParameters() {
		this.removeAll();
		this.repaint();
	}

	private void addParameterPanel(final ParameterPanel panel) {
		if (panel == null) {
			return;
		}
		add(panel);

	}
}
