package rs.fon.whibo.GC.component.Evaluation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import rs.fon.whibo.GC.GCTestUtil;
import rs.fon.whibo.GC.ExternalValidation.AdjustedRandIndex;
import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;
import rs.fon.whibo.GC.component.DistanceMeasure.Euclidian;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.ExampleSet;

/**
 * @author s.velickovic@gmail.com
 */

public class AdjustedRandIndexTest {
	// TODO bug in ComponentCompatibilityValidator getExclusiveComponentsName,
	// getNotCompatibleComponentsName

	private GCTestUtil testUtil = new GCTestUtil();
	private AdjustedRandIndex adjustedRandIndex;

	@Mock
	private DistanceMeasure distanceMeasure;
	@Mock
	private WhiBoCentroidClusterModel whiBoCentroidClusterModel;
	@Mock
	private ExampleSet exampleSet;

	@Before
	public void setUp() throws Exception {
		LinkedList<SubproblemParameter> subproblemParameters = new LinkedList<SubproblemParameter>();

		distanceMeasure = new Euclidian(subproblemParameters);
		whiBoCentroidClusterModel = testUtil.createWhiBoCentroidClusterModel();
		exampleSet = testUtil.createExampleSet();

		this.adjustedRandIndex = new AdjustedRandIndex(subproblemParameters);
	}

	@Test
	public void testEvaluate() throws Exception {
		double result = this.adjustedRandIndex.Evaluate(this.distanceMeasure,
				this.whiBoCentroidClusterModel, this.exampleSet);
		assertEquals(-0.5224665391969407, result, 0d);
	}

	@Test
	public void testEvaluate1() throws Exception {
	}

	@Test
	public void testIsBetter() throws Exception {
		assertTrue(adjustedRandIndex.isBetter(1, 0));
	}

	@Test
	public void testGetWorstValue() throws Exception {
		assertEquals(Double.NEGATIVE_INFINITY,
				adjustedRandIndex.getWorstValue());
	}
}
