package rs.fon.whibo.GDT.component.pruning;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.GDT.component.prunning.ReducedError;
import rs.fon.whibo.GDT.dataset.SplittedExampleSet;
import rs.fon.whibo.GDT.treeModel.GDTTestUtil;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.Partition;
import com.rapidminer.operator.learner.tree.Tree;

/**
 * @author Milos Jovanovic
 */
public class ReducedErrorTest {

	private GDTTestUtil testUtil;
	private Tree tree;
	private SplittedExampleSet filteredPruneSet;

	private ReducedError reducedError;

	@Before
	public void setUp() throws Exception {
		testUtil = new GDTTestUtil();
		tree = testUtil.createTreeModelForID3();
		ExampleSet pruneSet = testUtil.createGolfExampleSet();

		int[] mask = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 };
		filteredPruneSet = new SplittedExampleSet(pruneSet, new Partition(mask,
				2));
		filteredPruneSet.selectSingleSubset(1);

		reducedError = new ReducedError(new LinkedList<SubproblemParameter>());
	}

	@Test
	public void testPruneProcess() throws Exception {

		Tree result = reducedError.prune(tree, filteredPruneSet);
		System.out.println("------------------------------------");
		System.out.println("Reduced");
		System.out.println(result);
	}

}
