package rs.fon.whibo.GC.component.DistanceMeasure;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.problem.SubproblemParameter;

/**
 * @author s.velickovic@gmail.com
 */
public class CorrelationNumericalTest {
	private CorrelationNumerical correlationNumerical;

	@Before
	public void setUp() throws Exception {
		correlationNumerical = new CorrelationNumerical(
				new LinkedList<SubproblemParameter>());
	}

	@Test
	public void testCalculateDistance() throws Exception {
		double distance = correlationNumerical.calculateDistance(new double[] {
				0, 3, 4, 5 }, new double[] { 7, 6, 3, -1 });
		assertEquals(0.8586775814821836, distance, 0d);
	}

	@Test
	public void testCalculateSimilarity() throws Exception {
		double similiarity = correlationNumerical.calculateSimilarity(
				new double[] { 0, 3, 4, 5 }, new double[] { 7, 6, 3, -1 });
		assertEquals(-0.8586775814821836, similiarity, 0d);
	}
}
