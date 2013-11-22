package rs.fon.whibo.GC.component.DistanceMeasure;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.problem.SubproblemParameter;

/**
 * @author s.velickovic@gmail.com
 */
public class JaccardNumericalTest {
	private JaccardNumerical jaccardNumerical;

	@Before
	public void setUp() throws Exception {
		this.jaccardNumerical = new JaccardNumerical(
				new LinkedList<SubproblemParameter>());
	}

	@Test
	public void testCalculateDistance() throws Exception {
		double distance = jaccardNumerical.calculateDistance(new double[] { 0,
				3, 4, 5 }, new double[] { 7, 6, 3, -1 });
		assertEquals(-12.5, distance, 0d);
	}

	@Test
	public void testCalculateSimilarity() throws Exception {
		double similiarity = jaccardNumerical.calculateSimilarity(new double[] {
				0, 3, 4, 5 }, new double[] { 7, 6, 3, -1 });
		assertEquals(12.5, similiarity, 0d);
	}
}
