package rs.fon.whibo.GC.component.DistanceMeasure;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.problem.SubproblemParameter;

/**
 * @author s.velickovic@gmail.com
 */
public class OverlapNumericalTest {
	private OverlapNumerical overlapNumerical;

	@Before
	public void setUp() throws Exception {
		this.overlapNumerical = new OverlapNumerical(
				new LinkedList<SubproblemParameter>());
	}

	@Test
	public void testCalculateDistance() throws Exception {
		double distance = overlapNumerical.calculateDistance(new double[] { 0,
				3, 4, 5 }, new double[] { 7, 6, 3, -1 });
		assertEquals(-0.4166666666666667, distance, 0d);
	}

	@Test
	public void testCalculateSimilarity() throws Exception {
		double similiarity = overlapNumerical.calculateSimilarity(new double[] {
				0, 3, 4, 5 }, new double[] { 7, 6, 3, -1 });
		assertEquals(0.4166666666666667, similiarity, 0d);
	}
}
