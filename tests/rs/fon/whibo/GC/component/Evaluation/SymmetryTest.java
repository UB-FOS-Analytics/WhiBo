package rs.fon.whibo.GC.component.Evaluation;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import rs.fon.whibo.GC.GCTestUtil;
import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;
import rs.fon.whibo.GC.component.DistanceMeasure.Euclidian;
import rs.fon.whibo.GC.component.Evaluation.RCsInDevelopement.Symmetry;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.ExampleSet;

/**
 * @author s.velickovic@gmail.com
 */
public class SymmetryTest {
	private GCTestUtil testUtil = new GCTestUtil();
	private Symmetry symmetry;

	@Mock
	private DistanceMeasure distanceMeasure;
	@Mock
	private WhiBoCentroidClusterModel whiBoCentroidClusterModel;
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

		subproblemParameters.add(subproblemParameter);

		distanceMeasure = new Euclidian(subproblemParameters);
		whiBoCentroidClusterModel = testUtil.createWhiBoCentroidClusterModel();
		exampleSet = testUtil.createExampleSet();

		// this.symmetry = new Symmetry(subproblemParameters);//TODO: Fix -
		// doesn't work
	}

	@Test
	public void testEvaluate() throws Exception {
		// TODO test not woking correctly, usage of ExampleSet.clone()?
		// double result = this.symmetry.Evaluate(this.distanceMeasure,
		// this.whiBoCentroidClusterModel, this.exampleSet);//TODO: Fix -
		// doesn't work
		// assertEquals(0, result, 0d);
	}
}
