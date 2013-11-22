package rs.fon.whibo.GC.clusterModel;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

/**
 * @author s.velickovic@gmail.com
 */
public class WhiBoCentroidTest {
	private WhiBoCentroid centroid;

	@Before
	public void setUp() throws Exception {
		centroid = new WhiBoCentroid(4);
	}

	@Test
	public void testGetCentroid() throws Exception {
		this.centroid.getCentroid();
	}

	@Test
	public void testSetCentroid() throws Exception {
		// TODO Centroid.setCentroid exceptions and test values
		this.centroid.setCentroid(new double[] { 1, 2, 3, 4 });
	}

	@Test
	public void testSetNumberOfAssigned() throws Exception {
		// TODO Centroid.setNumberOfAssigned exceptions and test values
		this.centroid.setNumberOfAssigned(4);
	}

	@Test
	public void testAssignExample() throws Exception {
		// TODO Centroid.assignExample exceptions and test values
		this.centroid.assignExample(new double[] { 1, 2, 3, 4 });
	}

	@Test
	public void testRestartCentroidSum() throws Exception {
		// TODO Centroid.restartCentroidSum sets to array of zeros?
		this.centroid.restartCentroidSum();

	}

	@Test
	public void testSetCentroidSum() throws Exception {
		// TODO Centroid.setCentroidSum exceptions and test values
		this.centroid.setCentroidSum(new double[] { 1, 2, 3, 4 });
	}

	@Test
	public void testFinishAssign() throws Exception {
		// TODO Centroid.finishAssign exceptions and test values
		assertFalse(this.centroid.finishAssign());
	}

	@Test
	public void testGetCentroidSum() throws Exception {
		this.centroid.getCentroidSum();
	}

	@Test
	public void testGetNumberOfAssigned() throws Exception {
		this.centroid.getNumberOfAssigned();
	}
}
