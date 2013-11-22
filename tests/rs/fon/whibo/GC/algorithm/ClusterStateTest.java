package rs.fon.whibo.GC.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author s.velickovic@gmail.com
 */
public class ClusterStateTest {
	private ClusterState clusterState;

	@Before
	public void setUp() throws Exception {
		this.clusterState = new ClusterState();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNextRun() throws Exception {
		this.clusterState.nextRun();
	}

	@Test
	public void testRestartOptimizationStepNumber() throws Exception {
		this.clusterState.restartOptimizationStepNumber();
		assertEquals(0, this.clusterState.getOptimizationStepNumber());
	}

	@Test
	public void testNextOptimizationStep() throws Exception {
		this.clusterState.nextOptimizationStep();
		assertEquals(2, this.clusterState.getOptimizationStepNumber());
	}

	@Test
	public void testAlgorithmEnd() throws Exception {
		this.clusterState.algorithmEnd();
	}

	@Test
	public void testGetStartTime() throws Exception {
		Date startTime = this.clusterState.getStartTime();
		assertNotNull(startTime);
	}

	@Test
	public void testGetIntraClusterDistance() throws Exception {
		double intraClusterDistance = this.clusterState
				.getIntraClusterDistance();
	}

	@Test
	public void testSetIntraClusterDistance() throws Exception {
		// TODO Cluster.setIntraClusterDistance test parameters
		// this.clusterState.setIntraClusterDistance();
	}

	@Test
	public void testGetOptimizationStepNumber() throws Exception {
		assertEquals(1, this.clusterState.getOptimizationStepNumber());
	}

	@Test
	public void testGetRunNumber() throws Exception {
		assertEquals(1, this.clusterState.getRunNumber());
	}

	@Test
	public void testGetElapsedTime() throws Exception {
		this.clusterState.getElapsedTime();
	}

	@Test
	public void testGetMemory() throws Exception {
		this.clusterState.getMemory();
	}
}
