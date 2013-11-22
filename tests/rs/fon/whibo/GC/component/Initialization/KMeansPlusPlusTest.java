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
public class KMeansPlusPlusTest {
	private GCTestUtil testUtil = new GCTestUtil();
	private KMeansPlusPlus kMeansPlusPlus;

	@Mock
	private DistanceMeasure distanceMeasure;

	@Mock
	private ExampleSet exampleSet;

	@Before
	public void setUp() throws Exception {
		LinkedList<SubproblemParameter> subproblemParameters = new LinkedList<SubproblemParameter>();
		SubproblemParameter subproblemParameter1 = new SubproblemParameter();
		// TODO Object or generics?
		subproblemParameter1.setParametertType(Integer.class);
		subproblemParameter1.setDefaultValue("3");
		subproblemParameter1.setMinValue("2");
		subproblemParameter1.setMaxValue("1000");
		subproblemParameter1.setXenteredValue("3");

		subproblemParameters.add(0, subproblemParameter1);

		SubproblemParameter subproblemParameter = new SubproblemParameter();
		// TODO Object or generics?
		subproblemParameter.setParametertType(Integer.class);
		subproblemParameter.setDefaultValue("5");
		subproblemParameter.setMinValue("1");
		subproblemParameter.setMaxValue("1000");
		subproblemParameter.setXenteredValue("5");

		subproblemParameters.add(1, subproblemParameter);

		distanceMeasure = new Euclidian(subproblemParameters);
		exampleSet = testUtil.createExampleSet();

		this.kMeansPlusPlus = new KMeansPlusPlus(subproblemParameters);
	}

	@Test
	public void testInitializeCentroids() throws Exception {
		WhiBoCentroidClusterModel clusterModel = kMeansPlusPlus
				.InitializeCentroids(exampleSet, distanceMeasure);
	}
}
