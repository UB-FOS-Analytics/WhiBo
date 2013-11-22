package rs.fon.whibo.GDT.component.splitEvaluation;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.GDT.component.possibleSplits.BinaryNumerical;
import rs.fon.whibo.GDT.dataset.SplittedExampleSet;
import rs.fon.whibo.GDT.treeModel.GDTTestUtil;
import rs.fon.whibo.problem.SubproblemParameter;

/**
 * @author s.velickovic@gmail.com
 */
public class InformationGainTest {
	private GDTTestUtil testUtil = new GDTTestUtil();

	private InformationGain informationGain;

	@Before
	public void setUp() throws Exception {
		informationGain = new InformationGain(
				new LinkedList<SubproblemParameter>());
	}

	@Test
	public void testEvaluate() throws Exception {
		BinaryNumerical binaryNumerical = new BinaryNumerical(
				new LinkedList<SubproblemParameter>());

		binaryNumerical.init(testUtil.createExampleSet(),
				testUtil.getAttributesForSplitting());
		SplittedExampleSet splittedExampleSet = binaryNumerical.nextSplit();
		double d = informationGain.evaluate(splittedExampleSet);
		assertEquals(0.047709111427960416, d, 0);
	}

}
