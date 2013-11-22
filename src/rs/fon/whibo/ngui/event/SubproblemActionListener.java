package rs.fon.whibo.ngui.event;

import rs.fon.whibo.ngui.problem.ProblemAdapter;
import rs.fon.whibo.problem.Subproblem;

public interface SubproblemActionListener {
	void actionPerformed(ProblemAdapter problem, Subproblem subproblem);
}
