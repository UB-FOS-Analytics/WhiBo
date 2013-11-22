package rs.fon.whibo.GC.component.DistanceMeasure;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.problem.SubproblemParameter;

/**
 * @author s.velickovic@gmail.com
 */
public class CosineSimilarityTest {
	private CosineSimilarity cosineSimilarity;

	@Before
	public void setUp() throws Exception {
		cosineSimilarity = new CosineSimilarity(
				new LinkedList<SubproblemParameter>());
	}

	@Test
	public void testCalculateDistance() throws Exception {
		double distance = cosineSimilarity.calculateDistance(new double[] { 0,
				3, 4, 5 }, new double[] { 7, 6, 3, -1 });
		assertEquals(1.1995918617847487, distance, 0d);
	}

	@Test
	public void testCalculateSimilarity() throws Exception {
		double similiarity = cosineSimilarity.calculateSimilarity(new double[] {
				0, 3, 4, 5 }, new double[] { 7, 6, 3, -1 });
		assertEquals(0.36273812505500586, similiarity, 0d);
	}
}
