package rs.fon.whibo.GC.component.Initialization;

import org.junit.Test;
import org.mockito.Mock;

import rs.fon.whibo.GC.GCTestUtil;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;

import com.rapidminer.example.ExampleSet;

/**
 * @author s.velickovic@gmail.com
 */
public class DianaTest {
	private GCTestUtil testUtil = new GCTestUtil();
	private Diana diana;

	@Mock
	private DistanceMeasure distanceMeasure;

	@Mock
	private ExampleSet exampleSet;

	// TODO Diana not working
	// @Before
	// public void setUp() throws Exception {
	// LinkedList<SubproblemParameter> subproblemParameters = new
	// LinkedList<SubproblemParameter>();
	// SubproblemParameter subproblemParameter = new SubproblemParameter();
	//
	// subproblemParameter.setParametertType(Integer.class);
	// subproblemParameter.setDefaultValue("5");
	// subproblemParameter.setMinValue("1");
	// subproblemParameter.setMaxValue("1000");
	// subproblemParameter.setXenteredValue("5");
	//
	//
	// subproblemParameters.add(0, subproblemParameter);
	//
	// distanceMeasure = new Euclidian(subproblemParameters);
	// exampleSet = testUtil.createExampleSet();
	//
	// this.diana = new Diana(subproblemParameters);
	// }
	//
	//
	@Test
	public void testInitializeCentroids() throws Exception {
		// WhiBoCentroidClusterModel clusterModel =
		// diana.InitializeCentroids(exampleSet, distanceMeasure);
	}
}
