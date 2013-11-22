package rs.fon.whibo.GC.component.DistanceMeasure;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.problem.SubproblemParameter;

/**
 * @author s.velickovic@gmail.com
 */
public class CamberraTest {
	private Camberra camberra;

	// TODO rename Camberra to Canberra

	@Before
	public void setUp() throws Exception {
		this.camberra = new Camberra(new LinkedList<SubproblemParameter>());
	}

	@Test
	public void testCalculateDistance() throws Exception {
		double distance = camberra.calculateDistance(
				new double[] { 0, 3, 4, 5 }, new double[] { 7, 6, 3, -1 });
		assertEquals(2.9761904761904763, distance, 0d);
	}

	@Test
	public void testCalculateSimilarity() throws Exception {
		double similiarity = camberra.calculateSimilarity(new double[] { 0, 3,
				4, 5 }, new double[] { 7, 6, 3, -1 });
		assertEquals(-2.9761904761904763, similiarity, 0d);
	}
	// TODO assignInstances, testInit, getNotCompatibleComponentNames,
	// getExclusiveComponentNames???
}
