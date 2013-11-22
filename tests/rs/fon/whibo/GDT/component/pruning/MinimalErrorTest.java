package rs.fon.whibo.GDT.component.pruning;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.GDT.component.prunning.MinimalError;
import rs.fon.whibo.GDT.dataset.SplittedExampleSet;
import rs.fon.whibo.GDT.treeModel.GDTTestUtil;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.Partition;
import com.rapidminer.operator.learner.tree.Tree;

/**
 * @author Milos Jovanovic
 */
public class MinimalErrorTest {

	private GDTTestUtil testUtil;
	private Tree tree;
	private SplittedExampleSet filteredPruneSet;
	private ExampleSet pruneSet;

	private MinimalError minimalError;

	@Before
	public void setUp() throws Exception {
		testUtil = new GDTTestUtil();
		tree = testUtil.createTreeModelForID3();
		pruneSet = testUtil.createGolfExampleSet();

		int[] mask = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 };
		filteredPruneSet = new SplittedExampleSet(pruneSet, new Partition(mask,
				2));
		filteredPruneSet.selectSingleSubset(1);

		LinkedList<SubproblemParameter> parameters1 = new LinkedList<SubproblemParameter>();
		SubproblemParameter parameter1 = mock(SubproblemParameter.class);
		when(parameter1.getXenteredValue()).thenReturn("2");
		parameters1.add(parameter1);
		minimalError = new MinimalError(parameters1);
	}

	@Test
	public void testPruneProcess() throws Exception {

		Tree result = minimalError.prune(tree, pruneSet);
		System.out.println("------------------------------------");
		System.out.println("Minimal:");
		System.out.println(result);
	}

}
