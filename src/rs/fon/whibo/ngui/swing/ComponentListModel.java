package rs.fon.whibo.ngui.swing;

import javax.swing.AbstractListModel;

import rs.fon.whibo.ngui.problem.ProblemAdapter;

public class ComponentListModel extends AbstractListModel {

	// private Subproblem subproblem;
	private ProblemAdapter problemAdapter;

	private String[] items = new String[0];

	public ComponentListModel(ProblemAdapter problemAdapter) {
		// this.subproblem = subproblem;
		this.problemAdapter = problemAdapter;

		if (this.problemAdapter.getCurrentSubproblem() != null) {
			this.items = problemAdapter.getAllowedClasses();
		}
	}

	public String[] getItems() {
		return items;
	}

	public void clear() {
		items = new String[0];
	}

	@Override
	public int getSize() {
		return items.length;
	}

	@Override
	public Object getElementAt(int index) {
		if ((index < 0) || (index > items.length - 1))
			return null;

		String item = items[index];
		return item.substring(item.lastIndexOf('.') + 1);
	}
}
