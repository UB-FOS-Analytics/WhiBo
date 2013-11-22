package rs.fon.whibo.GC.component.SplitClusters;

import java.util.List;

import rs.fon.whibo.problem.AbstractComponent;
import rs.fon.whibo.problem.SubproblemParameter;

public abstract class AbstractSplitClusters extends AbstractComponent implements
		SplitClusters {

	/** The parameters. */
	List<SubproblemParameter> parameters;

	/**
	 * Instantiates a new abstract initialization.
	 * 
	 * @param parameters
	 *            the parameters
	 */
	public AbstractSplitClusters(List<SubproblemParameter> parameters) {
		this.parameters = parameters;
	}

}
