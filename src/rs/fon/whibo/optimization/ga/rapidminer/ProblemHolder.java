package rs.fon.whibo.optimization.ga.rapidminer;

import rs.fon.whibo.problem.Problem;

public class ProblemHolder {

	private static ProblemHolder instance;
	private Problem problem;

	private ProblemHolder() {

	}

	public static ProblemHolder getInstance() {
		if (instance == null) {
			instance = new ProblemHolder();
		}
		return instance;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public Problem getProblem() {
		return problem;
	}
}
