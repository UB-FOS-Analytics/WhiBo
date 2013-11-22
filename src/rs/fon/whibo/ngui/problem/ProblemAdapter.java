package rs.fon.whibo.ngui.problem;

import java.util.HashMap;

import rs.fon.whibo.ngui.swing.ParameterPanel;
import rs.fon.whibo.problem.Problem;
import rs.fon.whibo.problem.Subproblem;
import rs.fon.whibo.problem.SubproblemData;

/**
 * @author s.velickovic@gmail.com
 */
public interface ProblemAdapter {
	Problem getProblem();

	int subproblemsCount();

	Subproblem getCurrentSubproblem();

	void setCurrentSubproblem(Subproblem currentSubproblem);

	String[] getAllowedClasses();

	void setSelection(int[] selectedIndices);

	ParameterPanel[] createPanels() throws ClassNotFoundException;

	void save();

	public HashMap<String, SubproblemData> getSubproblemData();

	void resetProblem();

	void setProblem(Problem problem);

	void disableSubproblem();
}
