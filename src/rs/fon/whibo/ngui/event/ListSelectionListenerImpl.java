package rs.fon.whibo.ngui.event;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import rs.fon.whibo.ngui.problem.ProblemAdapter;
import rs.fon.whibo.ngui.swing.ParameterListPanel;

/**
 * @author s.velickovic@gmail.com
 */
public class ListSelectionListenerImpl implements ListSelectionListener {

	private ProblemAdapter problemAdapter;
	private JList list;
	private ParameterListPanel parameterListPanel;

	public ListSelectionListenerImpl(ProblemAdapter problemAdapter, JList list,
			ParameterListPanel parameterListPanel) {
		this.problemAdapter = problemAdapter;
		this.list = list;
		this.parameterListPanel = parameterListPanel;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

		try {
			parameterListPanel.setSelection(list.getSelectedIndices());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}
}
