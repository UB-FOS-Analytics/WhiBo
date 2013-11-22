package rs.fon.whibo.GDT.component.possibleSplits;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.GDT.treeModel.GDTTestUtil;
import rs.fon.whibo.problem.SubproblemParameter;

/**
 * @author s.velickovic@gmail.com
 */
public class SignificantCategoricalTest {
	private GDTTestUtil testUtil = new GDTTestUtil();

	private SignificantCategorical significantCategorical;

	@Before
	public void setUp() throws Exception {

		LinkedList<SubproblemParameter> subproblemParameters = new LinkedList<SubproblemParameter>();
		SubproblemParameter subproblemParameter1 = new SubproblemParameter();
		// TODO Object or generics?
		subproblemParameter1.setParametertType(Double.class);
		subproblemParameter1.setDefaultValue("0.05");
		subproblemParameter1.setMinValue("0.0");
		subproblemParameter1.setMaxValue("1.0");
		subproblemParameter1.setXenteredValue("0.05");

		subproblemParameters.add(0, subproblemParameter1);

		SubproblemParameter subproblemParameter2 = new SubproblemParameter();
		// TODO Object or generics?
		subproblemParameter2.setParametertType(Double.class);
		subproblemParameter2.setDefaultValue("0.05");
		subproblemParameter2.setMinValue("0.0");
		subproblemParameter2.setMaxValue("1.0");
		subproblemParameter2.setXenteredValue("0.05");

		subproblemParameters.add(0, subproblemParameter2);

		significantCategorical = new SignificantCategorical(
				subproblemParameters);
	}

	@Test
	public void testInit() throws Exception {
		significantCategorical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
	}

	@Test(expected = NullPointerException.class)
	public void testHasNextSplitNoInit() throws Exception {
		// TODO custom exception?
		significantCategorical.hasNextSplit();
	}

	@Test
	public void testHasNextSplit() throws Exception {
		significantCategorical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		assertFalse(significantCategorical.hasNextSplit());
	}

	@Test
	public void testNextSplit() throws Exception {
		significantCategorical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		significantCategorical.nextSplit();
	}

	@Test
	public void testGetAllCategories() throws Exception {
	}

	@Test
	public void testIsCategoricalSplit() throws Exception {
		significantCategorical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		assertTrue(significantCategorical.isCategoricalSplit());
	}

	@Test
	public void testIsNumericalSplit() throws Exception {
		significantCategorical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		assertTrue(significantCategorical.isCategoricalSplit());
	}
}
