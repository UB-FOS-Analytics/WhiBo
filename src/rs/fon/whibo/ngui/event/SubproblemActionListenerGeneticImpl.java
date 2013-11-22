package rs.fon.whibo.ngui.event;

import javax.swing.JLabel;
import javax.swing.JList;

import rs.fon.whibo.ngui.swing.ParameterListPanel;
import rs.fon.whibo.problem.Subproblem;

/**
 * Created by IntelliJ IDEA. User: sasa.velickovic Date: 4/12/11 Time: 12:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class SubproblemActionListenerGeneticImpl extends
		SubproblemActionListenerImpl {

	public SubproblemActionListenerGeneticImpl(JList list,
			ParameterListPanel parametersListPanel,
			JLabel labelCurrentComponentName,
			JLabel labelCurrentComponentDescription) {
		super(list, parametersListPanel, labelCurrentComponentName,
				labelCurrentComponentDescription);
	}

	@Override
	public void setMultipleSelection(Subproblem subproblem) {
		// TODO hack to allow genetic multiple choice
		subproblem.setMultiple(true);
	}
}
