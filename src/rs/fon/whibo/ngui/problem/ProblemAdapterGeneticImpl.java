package rs.fon.whibo.ngui.problem;

import rs.fon.whibo.ngui.swing.ParameterPanel;
import rs.fon.whibo.ngui.swing.ParameterPanelGeneticImpl;
import rs.fon.whibo.problem.ProblemBuilder;
import rs.fon.whibo.problem.SubproblemData;

/**
 * Created by IntelliJ IDEA. User: sasa.velickovic Date: 4/12/11 Time: 12:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProblemAdapterGeneticImpl extends ProblemAdapterImpl {

	public ProblemAdapterGeneticImpl(ProblemBuilder problemBuilder,
			String filePath) {
		super(problemBuilder, filePath);
	}

	@Override
	public ParameterPanel createParameterPanel(SubproblemData d) {
		return new ParameterPanelGeneticImpl(d);
	}
}
