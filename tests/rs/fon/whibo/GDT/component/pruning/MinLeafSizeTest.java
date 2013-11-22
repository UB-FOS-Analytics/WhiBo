package rs.fon.whibo.GDT.component.pruning;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.GDT.component.prunning.MinLeafSize;
import rs.fon.whibo.GDT.dataset.SplittedExampleSet;
import rs.fon.whibo.GDT.treeModel.GDTTestUtil;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.Partition;
import com.rapidminer.operator.learner.tree.Tree;

/**
 * @author Milos Jovanovic
 */
public class MinLeafSizeTest {

	private GDTTestUtil testUtil;
	private Tree tree;
	private SplittedExampleSet filteredPruneSet;

	private MinLeafSize minLeafSize;

	@Before
	public void setUp() throws Exception {
		testUtil = new GDTTestUtil();
		tree = testUtil.createTreeModelForID3();
		ExampleSet pruneSet = testUtil.createGolfExampleSet();

		int[] mask = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 };
		filteredPruneSet = new SplittedExampleSet(pruneSet, new Partition(mask,
				2));
		filteredPruneSet.selectSingleSubset(1);

		LinkedList<SubproblemParameter> parameters3 = new LinkedList<SubproblemParameter>();
		SubproblemParameter parameter3 = mock(SubproblemParameter.class);
		when(parameter3.getXenteredValue()).thenReturn("3");
		parameters3.add(parameter3);
		minLeafSize = new MinLeafSize(parameters3);
	}

	@Test
	public void testPruneProcess() throws Exception {

		Tree result = minLeafSize.prune(tree, filteredPruneSet);
		System.out.println("------------------------------------");
		System.out.println("MinLeaf");
		System.out.println(result);

	}

}
