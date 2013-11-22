package rs.fon.whibo.GC.component.Initialization;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import rs.fon.whibo.GC.GCTestUtil;
import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;
import rs.fon.whibo.GC.component.DistanceMeasure.Euclidian;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.ExampleSet;

/**
 * @author s.velickovic@gmail.com
 */
public class GMeansTest {

	private GCTestUtil testUtil = new GCTestUtil();
	private GMeans gMeans;

	@Mock
	private DistanceMeasure distanceMeasure;

	@Mock
	private ExampleSet exampleSet;

	@Before
	public void setUp() throws Exception {
		LinkedList<SubproblemParameter> subproblemParameters = new LinkedList<SubproblemParameter>();
		SubproblemParameter subproblemParameter = new SubproblemParameter();
		// TODO Object or generics?
		subproblemParameter.setParametertType(Integer.class);
		subproblemParameter.setDefaultValue("5");
		subproblemParameter.setMinValue("1");
		subproblemParameter.setMaxValue("1000");
		subproblemParameter.setXenteredValue("5");

		subproblemParameters.add(0, subproblemParameter);

		distanceMeasure = new Euclidian(subproblemParameters);
		exampleSet = testUtil.createExampleSet();

		this.gMeans = new GMeans(subproblemParameters);
	}

	@Test
	public void testInitializeCentroids() throws Exception {
		WhiBoCentroidClusterModel clusterModel = gMeans.InitializeCentroids(
				exampleSet, distanceMeasure);
	}
}
