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
public class BinaryNumericalTest {
	private GDTTestUtil testUtil = new GDTTestUtil();

	private BinaryNumerical binaryNumerical;

	@Before
	public void setUp() throws Exception {
		binaryNumerical = new BinaryNumerical(
				new LinkedList<SubproblemParameter>());
	}

	@Test
	public void testInit() throws Exception {
		binaryNumerical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
	}

	@Test(expected = NullPointerException.class)
	public void testHasNextSplitNoInit() throws Exception {
		// TODO custom exception?
		binaryNumerical.hasNextSplit();
	}

	@Test
	public void testHasNextSplit() throws Exception {
		binaryNumerical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		assertTrue(binaryNumerical.hasNextSplit());
	}

	@Test
	public void testNextSplit() throws Exception {
		binaryNumerical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		binaryNumerical.nextSplit();
	}

	@Test
	public void testGetAllCategories() throws Exception {
	}

	@Test
	public void testIsCategoricalSplit() throws Exception {
		binaryNumerical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		assertFalse(binaryNumerical.isCategoricalSplit());
	}

	@Test
	public void testIsNumericalSplit() throws Exception {
		binaryNumerical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		assertFalse(binaryNumerical.isCategoricalSplit());
	}
}
