package rs.fon.whibo.GDT.component.pruning;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.GDT.component.prunning.CostComplexity;
import rs.fon.whibo.GDT.dataset.SplittedExampleSet;
import rs.fon.whibo.GDT.treeModel.GDTTestUtil;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.Partition;
import com.rapidminer.operator.learner.tree.Tree;

/**
 * @author Milos Jovanovic
 */
public class CostComplexityTest {

	private GDTTestUtil testUtil;
	private Tree tree;
	private SplittedExampleSet filteredPruneSet;

	private CostComplexity costComplexity;

	@Before
	public void setUp() throws Exception {
		testUtil = new GDTTestUtil();
		tree = testUtil.createTreeModelForID3();
		ExampleSet pruneSet = testUtil.createGolfExampleSet();

		int[] mask = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 };
		filteredPruneSet = new SplittedExampleSet(pruneSet, new Partition(mask,
				2));
		filteredPruneSet.selectSingleSubset(1);

		costComplexity = new CostComplexity(
				new LinkedList<SubproblemParameter>());
	}

	@Test
	public void testPruneProcess() throws Exception {

		// Tree result = costComplexity.prune(tree, filteredPruneSet);
		// System.out.println("------------------------------------");
		// System.out.println("CostComplexity");
		// System.out.println(result);
	}

}
