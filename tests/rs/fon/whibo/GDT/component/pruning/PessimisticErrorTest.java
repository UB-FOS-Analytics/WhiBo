package rs.fon.whibo.GDT.component.pruning;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.GDT.component.prunning.PessimisticError;
import rs.fon.whibo.GDT.dataset.SplittedExampleSet;
import rs.fon.whibo.GDT.treeModel.GDTTestUtil;
import rs.fon.whibo.problem.SubproblemParameter;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.Partition;
import com.rapidminer.operator.learner.tree.Tree;

/**
 * @author Milos Jovanovic
 */
public class PessimisticErrorTest {

	private GDTTestUtil testUtil;
	private Tree tree;
	private SplittedExampleSet filteredPruneSet;

	private PessimisticError pessimisticError;

	@Before
	public void setUp() throws Exception {
		testUtil = new GDTTestUtil();
		tree = testUtil.createTreeModelForID3();
		ExampleSet pruneSet = testUtil.createGolfExampleSet();

		int[] mask = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 };
		filteredPruneSet = new SplittedExampleSet(pruneSet, new Partition(mask,
				2));
		filteredPruneSet.selectSingleSubset(1);

		LinkedList<SubproblemParameter> parameters2 = new LinkedList<SubproblemParameter>();
		SubproblemParameter parameter2 = mock(SubproblemParameter.class);
		when(parameter2.getXenteredValue()).thenReturn("0.1");
		parameters2.add(parameter2);
		pessimisticError = new PessimisticError(parameters2);
	}

	@Test
	public void testPruneProcess() throws Exception {

		Tree result = pessimisticError.prune(tree, filteredPruneSet);
		System.out.println("------------------------------------");
		System.out.println("Pessimistic");
		System.out.println(result);

	}

}
