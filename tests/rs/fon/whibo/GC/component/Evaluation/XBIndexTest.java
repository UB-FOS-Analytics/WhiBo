package rs.fon.whibo.GC.component.Evaluation;

import static org.junit.Assert.assertEquals;

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
public class XBIndexTest {
	private GCTestUtil testUtil = new GCTestUtil();
	private XBIndex xbIndex;

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

		this.xbIndex = new XBIndex(subproblemParameters);
	}

	@Test
	public void testEvaluate() throws Exception {
		double result = this.xbIndex.Evaluate(this.distanceMeasure,
				this.whiBoCentroidClusterModel, this.exampleSet);
		assertEquals(12.341648018304054, result, 0d);
	}
}
