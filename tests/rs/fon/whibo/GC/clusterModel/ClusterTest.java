package rs.fon.whibo.GC.clusterModel;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import com.rapidminer.operator.clustering.Cluster;

/**
 * @author s.velickovic@gmail.com
 */
public class ClusterTest {

	// TODO Cluster class is not used
	private Cluster cluster;

	@Before
	public void setUp() throws Exception {
		this.cluster = new Cluster(0);
	}

	@Test
	public void testGetExampleIds() throws Exception {
		this.cluster.getExampleIds();
	}

	@Test
	public void testContainsExampleId() throws Exception {
		// TODO Cluster uses ArrayList
		assertFalse(this.cluster.containsExampleId(0));
	}

	@Test
	public void testGetClusterId() throws Exception {
		int clusterId = this.cluster.getClusterId();
	}

	@Test
	public void testGetNumberOfExamples() throws Exception {
		int numberOfExamples = this.cluster.getNumberOfExamples();
	}

	@Test
	public void testAssignExample() throws Exception {
		this.cluster.assignExample(0);
	}

	@Test
	public void testRemoveExamples() throws Exception {
		// this.cluster.removeExamples();
	}
}
