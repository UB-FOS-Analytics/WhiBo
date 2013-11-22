package rs.fon.whibo.GC.component.DistanceMeasure;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.problem.SubproblemParameter;

/**
 * @author s.velickovic@gmail.com
 */
public class DTWNumericalTest {
	private DTWNumerical dtwNumerical;

	@Before
	public void setUp() throws Exception {
		this.dtwNumerical = new DTWNumerical(
				new LinkedList<SubproblemParameter>());
	}

	@Test
	public void testCalculateDistance() throws Exception {
		double distance = dtwNumerical.calculateDistance(new double[] { 0, 3,
				4, 5 }, new double[] { 7, 6, 3, -1 });
		assertEquals(4.853406592853679, distance, 0d);
	}

	@Test
	public void testCalculateSimilarity() throws Exception {
		double similiarity = dtwNumerical.calculateSimilarity(new double[] { 0,
				3, 4, 5 }, new double[] { 7, 6, 3, -1 });
		assertEquals(0.17084068638267547, similiarity, 0d);
	}
}
