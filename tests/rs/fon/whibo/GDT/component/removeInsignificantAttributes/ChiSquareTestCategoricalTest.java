package rs.fon.whibo.GDT.component.removeInsignificantAttributes;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.GDT.treeModel.GDTTestUtil;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.Attribute;

/**
 * @author s.velickovic@gmail.com
 */
public class ChiSquareTestCategoricalTest {
	private GDTTestUtil testUtil = new GDTTestUtil();

	private ChiSquareTestCategorical chiSquareTestCategorical;

	@Before
	public void setUp() throws Exception {
		LinkedList<SubproblemParameter> subproblemParameters = new LinkedList<SubproblemParameter>();
		SubproblemParameter subproblemParameter1 = new SubproblemParameter();
		// TODO Object or generics?
		subproblemParameter1.setParametertType(Double.class);
		subproblemParameter1.setDefaultValue("0.05");
		subproblemParameter1.setMinValue("0.0");
		subproblemParameter1.setMaxValue("0.5");
		subproblemParameter1.setXenteredValue("0.05");

		subproblemParameters.add(0, subproblemParameter1);

		SubproblemParameter subproblemParameter2 = new SubproblemParameter();
		// TODO Object or generics?
		subproblemParameter2.setParametertType(Integer.class);
		subproblemParameter2.setDefaultValue("0");
		subproblemParameter2.setMinValue("0");
		subproblemParameter2.setMaxValue("1");
		subproblemParameter2.setXenteredValue("0");

		subproblemParameters.add(0, subproblemParameter2);

		SubproblemParameter subproblemParameter3 = new SubproblemParameter();
		// TODO Object or generics?
		subproblemParameter3.setParametertType(Double.class);
		subproblemParameter3.setDefaultValue("0.3");
		subproblemParameter3.setMinValue("0.0");
		subproblemParameter3.setMaxValue("1.0");
		subproblemParameter3.setXenteredValue("0.3");

		subproblemParameters.add(0, subproblemParameter3);

		chiSquareTestCategorical = new ChiSquareTestCategorical(
				subproblemParameters);
	}

	@Test
	public void testRemoveAttributes() throws Exception {
		LinkedList<Attribute> r = chiSquareTestCategorical.removeAttributes(
				testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
	}

}
