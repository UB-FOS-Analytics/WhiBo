package rs.fon.whibo.GDT.algorithm;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.GDT.treeModel.GDTTestUtil;
import rs.fon.whibo.problem.Problem;
import rs.fon.whibo.problem.serialization.ProblemDecoder;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.learner.tree.Tree;

;

public class GDTAlgorithmTest {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testID3() throws Exception {
		GDTTestUtil testUtil = new GDTTestUtil();
		ExampleSet dataset = testUtil.createGolfExampleSet();
		Tree expectedResult = testUtil.createTreeModelForID3();

		File algorithmFile = new File(".\\resources\\tests\\ID3.wba");
		Problem algorithm = ProblemDecoder.decodeFromXMLToProces(algorithmFile
				.getAbsolutePath());

		GDTAlgorithm gdt = new GDTAlgorithm(algorithm);
		Tree result = gdt.learnTree(dataset);

		assertTrue(testUtil.equalTrees(result, expectedResult, dataset));
	}

	@Test
	public void testC45() throws Exception {
		GDTTestUtil testUtil = new GDTTestUtil();
		ExampleSet dataset = testUtil.createGolfExampleSet();
		Tree expectedResult = testUtil.createTreeModelForC45();

		File algorithmFile = new File(".\\resources\\tests\\C4.5.wba");
		Problem algorithm = ProblemDecoder.decodeFromXMLToProces(algorithmFile
				.getAbsolutePath());

		GDTAlgorithm gdt = new GDTAlgorithm(algorithm);
		Tree result = gdt.learnTree(dataset);

		assertTrue(testUtil.equalTrees(result, expectedResult, dataset));
	}

}
