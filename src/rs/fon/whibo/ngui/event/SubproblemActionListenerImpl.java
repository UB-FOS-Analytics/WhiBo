package rs.fon.whibo.ngui.event;

import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import rs.fon.whibo.ngui.problem.ProblemAdapter;
import rs.fon.whibo.ngui.swing.ComponentListModel;
import rs.fon.whibo.ngui.swing.ParameterListPanel;
import rs.fon.whibo.problem.Subproblem;
import rs.fon.whibo.problem.SubproblemData;

/**
 * @author s.velickovic@gmail.com
 */
public class SubproblemActionListenerImpl implements SubproblemActionListener {
	private final JList list;
	private final ParameterListPanel parametersListPanel;
	private JLabel labelCurrentComponentName;
	private JLabel labelCurrentComponentDescription;

	public SubproblemActionListenerImpl(JList list,
			ParameterListPanel parametersListPanel,
			JLabel labelCurrentComponentName,
			JLabel labelCurrentComponentDescription) {
		this.list = list;
		this.parametersListPanel = parametersListPanel;
		this.labelCurrentComponentName = labelCurrentComponentName;
		this.labelCurrentComponentDescription = labelCurrentComponentDescription;
	}

	@Override
	public void actionPerformed(ProblemAdapter problemAdapter,
			Subproblem subproblem) {
		labelCurrentComponentName.setText(subproblem.getName());
		labelCurrentComponentDescription.setText(subproblem.getDescription());

		problemAdapter.setCurrentSubproblem(subproblem);
		list.setModel(new ComponentListModel(problemAdapter));
		parametersListPanel.removeParameters();
		setMultipleSelection(subproblem);
		loadSelection(problemAdapter);
	}

	public void setMultipleSelection(Subproblem subproblem) {
		list.setSelectionMode(subproblem.isMultiple() ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
				: ListSelectionModel.SINGLE_SELECTION);
	}

	private void loadSelection(ProblemAdapter problemAdapter) {
		HashMap<String, SubproblemData> subproblemData = problemAdapter
				.getSubproblemData();
		int[] indices = new int[subproblemData.size()];
		int index = 0;

		String[] items = ((ComponentListModel) list.getModel()).getItems();
		for (int i = 0; i < items.length; i++) {
			if (subproblemData.containsKey(items[i])) {
				indices[index++] = i;
			}
		}

		list.setSelectedIndices(indices);

	}
}
