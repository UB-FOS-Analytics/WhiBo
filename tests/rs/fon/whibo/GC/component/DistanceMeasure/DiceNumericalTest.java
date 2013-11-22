package rs.fon.whibo.GC.component.DistanceMeasure;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.problem.SubproblemParameter;

/**
 * @author s.velickovic@gmail.com
 */
public class DiceNumericalTest {
	private DiceNumerical diceNumerical;

	@Before
	public void setUp() throws Exception {
		diceNumerical = new DiceNumerical(new LinkedList<SubproblemParameter>());
	}

	@Test
	public void testCalculateDistance() throws Exception {
		double distance = diceNumerical.calculateDistance(new double[] { 0, 3,
				4, 5 }, new double[] { 7, 6, 3, -1 });
		assertEquals(-1.8518518518518519, distance, 0d);
	}

	@Test
	public void testCalculateSimilarity() throws Exception {
		double similiarity = diceNumerical.calculateSimilarity(new double[] {
				0, 3, 4, 5 }, new double[] { 7, 6, 3, -1 });
		assertEquals(1.8518518518518519, similiarity, 0d);
	}
}
