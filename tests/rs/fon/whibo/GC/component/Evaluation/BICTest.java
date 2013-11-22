package rs.fon.whibo.GC.component.Evaluation;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import rs.fon.whibo.GC.GCTestUtil;
import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;
import rs.fon.whibo.GC.component.DistanceMeasure.Euclidian;
import rs.fon.whibo.GC.component.Evaluation.RCsInDevelopement.BIC;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.ExampleSet;

/**
 * @author s.velickovic@gmail.com
 */
public class BICTest {

	private GCTestUtil testUtil = new GCTestUtil();
	private BIC bic;

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

		// this.bic = new BIC(subproblemParameters);//TODO: Fix - doesn't work
	}

	@Test
	public void testEvaluate() throws Exception {
		// double result = this.bic.Evaluate(this.distanceMeasure,
		// this.whiBoCentroidClusterModel, this.exampleSet);
		// assertEquals(-1995.1273411891352,result, 0d);//TODO: Fix - doesn't
		// work
	}
}
