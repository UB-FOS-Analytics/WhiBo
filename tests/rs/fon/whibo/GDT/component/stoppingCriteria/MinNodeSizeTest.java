package rs.fon.whibo.GDT.component.stoppingCriteria;

import static org.junit.Assert.assertFalse;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rs.fon.whibo.GDT.treeModel.GDTTestUtil;
import rs.fon.whibo.problem.SubproblemParameter;

/**
 * @author s.velickovic@gmail.com
 */
public class MinNodeSizeTest {
	private GDTTestUtil testUtil = new GDTTestUtil();

	private MinNodeSize minNodeSize;

	@Before
	public void setUp() throws Exception {
		List<SubproblemParameter> parameters = new LinkedList<SubproblemParameter>();
		SubproblemParameter subproblemParameter1 = new SubproblemParameter();
		// TODO Object or generics?
		subproblemParameter1.setParametertType(Integer.class);
		subproblemParameter1.setDefaultValue("1");
		subproblemParameter1.setMinValue("1");
		subproblemParameter1.setMaxValue("100000");
		subproblemParameter1.setXenteredValue("1");

		parameters.add(0, subproblemParameter1);

		minNodeSize = new MinNodeSize(parameters);

	}

	@Test
	public void testEvaluateStoppingCriteria() throws Exception {
		assertFalse(minNodeSize.evaluateStoppingCriteria(
				testUtil.createExampleSet(), 0, new Date()));
	}
}
