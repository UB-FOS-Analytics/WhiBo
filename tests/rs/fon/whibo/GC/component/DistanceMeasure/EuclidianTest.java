package rs.fon.whibo.GC.component.DistanceMeasure;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.problem.SubproblemParameter;

/**
 * @author s.velickovic@gmail.com
 */
public class EuclidianTest {
	private Euclidian euclidian;

	@Before
	public void setUp() throws Exception {
		this.euclidian = new Euclidian(new LinkedList<SubproblemParameter>());
	}

	@Test
	public void testCalculateDistance() throws Exception {
		double distance = euclidian.calculateDistance(
				new double[] { 0, 3, 4, 5 }, new double[] { 7, 6, 3, -1 });
		assertEquals(9.746794344808963, distance, 0d);
	}

	@Test
	public void testCalculateSimilarity() throws Exception {
		double similiarity = euclidian.calculateSimilarity(new double[] { 0, 3,
				4, 5 }, new double[] { 7, 6, 3, -1 });
		assertEquals(-9.746794344808963, similiarity, 0d);
	}
}
