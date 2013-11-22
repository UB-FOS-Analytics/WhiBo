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
public class BinaryCategoricalTest {
	private GDTTestUtil testUtil = new GDTTestUtil();

	private BinaryCategorical binaryCategorical;

	@Before
	public void setUp() throws Exception {
		binaryCategorical = new BinaryCategorical(
				new LinkedList<SubproblemParameter>());
	}

	@Test
	public void testInit() throws Exception {
		binaryCategorical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
	}

	@Test(expected = NullPointerException.class)
	public void testHasNextSplitNoInit() throws Exception {
		// TODO custom exception?
		binaryCategorical.hasNextSplit();
	}

	@Test
	public void testHasNextSplit() throws Exception {
		binaryCategorical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		assertFalse(binaryCategorical.hasNextSplit());
	}

	@Test(expected = Exception.class)
	public void testNextSplit() throws Exception {
		// TODO custom exception - no more available splits
		binaryCategorical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		binaryCategorical.nextSplit();
	}

	@Test
	public void testGetAllCategories() throws Exception {
	}

	@Test
	public void testIsCategoricalSplit() throws Exception {
		binaryCategorical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		assertTrue(binaryCategorical.isCategoricalSplit());
	}

	@Test
	public void testIsNumericalSplit() throws Exception {
		binaryCategorical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		assertTrue(binaryCategorical.isCategoricalSplit());
	}
}
