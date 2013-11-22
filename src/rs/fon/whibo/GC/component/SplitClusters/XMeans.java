package rs.fon.whibo.GC.component.SplitClusters;

import java.util.List;

import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.problem.SubproblemParameter;

public class XMeans extends AbstractSplitClusters {

	public XMeans(List<SubproblemParameter> parameters) {
		super(parameters);
	}

	@Override
	public WhiBoCentroidClusterModel split(WhiBoCentroidClusterModel clusters) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets collection of class names of components that are not compatible with
	 * this component
	 * 
	 * @return collection of class names of not compatible components
	 */
	public String[] getNotCompatibleClassNames() {
		return null;
	}

	/**
	 * Gets collection of class names of components which are exclusive to this
	 * component
	 * 
	 * @return collection of class names of exclusive components
	 */
	public String[] getExclusiveClassNames() {
		return null;
	}

}
