package rs.fon.whibo.GC.component.RecalculateRepresentatives;

import static org.junit.Assert.assertFalse;

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
public class ArithmeticMeanTest {

	private GCTestUtil testUtil = new GCTestUtil();

	@Mock
	private DistanceMeasure distanceMeasure;
	@Mock
	private WhiBoCentroidClusterModel whiBoCentroidClusterModel;
	@Mock
	private ExampleSet exampleSet;

	private ArithmeticMean arithmeticMean;

	@Before
	public void setUp() throws Exception {
		LinkedList<SubproblemParameter> subproblemParameters = new LinkedList<SubproblemParameter>();

		distanceMeasure = new Euclidian(subproblemParameters);
		whiBoCentroidClusterModel = testUtil.createWhiBoCentroidClusterModel();
		exampleSet = testUtil.createExampleSet();

		this.arithmeticMean = new ArithmeticMean(subproblemParameters);
	}

	@Test
	public void testRecalculate() throws Exception {
		boolean result = arithmeticMean.Recalculate(whiBoCentroidClusterModel,
				exampleSet, distanceMeasure);
		assertFalse(result);
	}
}
