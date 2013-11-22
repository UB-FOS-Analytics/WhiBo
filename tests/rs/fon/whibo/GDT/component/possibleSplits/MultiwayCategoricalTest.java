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
public class MultiwayCategoricalTest {
	private GDTTestUtil testUtil = new GDTTestUtil();

	private MultiwayCategorical multiwayCategorical;

	@Before
	public void setUp() throws Exception {
		multiwayCategorical = new MultiwayCategorical(
				new LinkedList<SubproblemParameter>());
	}

	@Test
	public void testInit() throws Exception {
		multiwayCategorical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
	}

	@Test(expected = NullPointerException.class)
	public void testHasNextSplitNoInit() throws Exception {
		// TODO custom exception?
		multiwayCategorical.hasNextSplit();
	}

	@Test
	public void testHasNextSplit() throws Exception {
		multiwayCategorical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		assertFalse(multiwayCategorical.hasNextSplit());
	}

	@Test
	public void testNextSplit() throws Exception {
		multiwayCategorical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		multiwayCategorical.nextSplit();
	}

	@Test
	public void testGetAllCategories() throws Exception {
	}

	@Test
	public void testIsCategoricalSplit() throws Exception {
		multiwayCategorical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		assertTrue(multiwayCategorical.isCategoricalSplit());
	}

	@Test
	public void testIsNumericalSplit() throws Exception {
		multiwayCategorical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		assertTrue(multiwayCategorical.isCategoricalSplit());
	}
}
