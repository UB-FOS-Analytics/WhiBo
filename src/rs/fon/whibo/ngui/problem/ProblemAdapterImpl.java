package rs.fon.whibo.ngui.problem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import rs.fon.whibo.ngui.swing.ParameterPanel;
import rs.fon.whibo.ngui.swing.ParameterPanelImpl;
import rs.fon.whibo.problem.ComponentCompatibilityValidator;
import rs.fon.whibo.problem.Problem;
import rs.fon.whibo.problem.ProblemBuilder;
import rs.fon.whibo.problem.Subproblem;
import rs.fon.whibo.problem.SubproblemData;
import rs.fon.whibo.problem.SubproblemParameterReader;
import rs.fon.whibo.problem.serialization.ProblemDecoder;

/**
 * @author s.velickovic@gmail.com
 */
public class ProblemAdapterImpl implements ProblemAdapter {
	private Problem problem;
	private Subproblem currentSubproblem;
	private int[] selectedIndices;
	private LinkedList<SubproblemData> selection = new LinkedList<SubproblemData>();
	private ProblemBuilder problemBuilder;
	private String filePath;

	public ProblemAdapterImpl(ProblemBuilder problemBuilder, String filePath) {
		this.problemBuilder = problemBuilder;
		this.filePath = filePath;
		this.problem = buildProblem();
	}

	private Problem buildProblem() {
		if (filePath != null) {
			return ProblemDecoder.decodeFromXMLToProces(filePath);
		}

		return problemBuilder.buildProblem();
	}

	@Override
	public Problem getProblem() {
		return problem;
	}

	@Override
	public int subproblemsCount() {
		return problem != null ? problem.getSubproblems().size() : 0;
	}

	@Override
	public Subproblem getCurrentSubproblem() {
		return currentSubproblem;
	}

	@Override
	public void setCurrentSubproblem(Subproblem currentSubproblem) {
		this.currentSubproblem = currentSubproblem;
	}

	@Override
	public String[] getAllowedClasses() {
		return ComponentCompatibilityValidator.getAllowedClasses(
				this.getProblem(), this.getCurrentSubproblem());
	}

	@Override
	public void setSelection(int[] selectedIndices) {
		this.selectedIndices = selectedIndices;
		selection.clear();
	}

	@Override
	public ParameterPanel[] createPanels() throws ClassNotFoundException {
		ParameterPanel[] parameterPanels = new ParameterPanel[selectedIndices.length];
		String[] classes = getAllowedClasses();
		HashMap<String, SubproblemData> previousSelection = getSubproblemData();

		for (int i = 0, selectedIndicesLength = selectedIndices.length; i < selectedIndicesLength; i++) {
			SubproblemData d;
			int selectedIndice = selectedIndices[i];

			if (previousSelection.containsKey(classes[selectedIndice])) {
				d = previousSelection.get(classes[selectedIndice]);
			} else {
				d = createSubproblemData(classes[selectedIndice]);
			}

			selection.add(d);
			parameterPanels[i] = createParameterPanel(d);
		}

		return parameterPanels;
	}

	public ParameterPanel createParameterPanel(SubproblemData d) {
		return new ParameterPanelImpl(d);
	}

	public HashMap<String, SubproblemData> getSubproblemData() {
		HashMap<String, SubproblemData> result = new HashMap<String, SubproblemData>();

		// generic solution for is multiple data resolving
		if (currentSubproblem.isMultiple()) {
			List<SubproblemData> dataList = currentSubproblem
					.getMultipleStepData();
			if (dataList != null) {
				for (SubproblemData data : dataList) {
					result.put(data.getNameOfImplementationClass(), data);
				}
			}
		} else {
			SubproblemData subproblemData = currentSubproblem
					.getSubproblemData();
			if (subproblemData != null) {
				result.put(subproblemData.getNameOfImplementationClass(),
						subproblemData);
			}
		}

		return result;
	}

	@Override
	public void resetProblem() {
		setProblem(problemBuilder.buildProblem());
	}

	@Override
	public void setProblem(Problem problem) {
		selectedIndices = new int[0];
		selection.clear();
		currentSubproblem = null;
		this.problem = problem;
	}

	@Override
	public void disableSubproblem() {
		if (currentSubproblem.isMultiple()) {
			currentSubproblem.setMultipleSubproblemData(null);
		} else {
			currentSubproblem.setSubproblemData(null);
		}
	}

	private SubproblemData createSubproblemData(String aClass)
			throws ClassNotFoundException {
		SubproblemData subproblemData = new SubproblemData();
		subproblemData.setNameOfImplementationClass(aClass);
		subproblemData.setListOfParameters(SubproblemParameterReader
				.readParameters(Class.forName(aClass)));
		return subproblemData;

	}

	public void save() {
		LinkedList<SubproblemData> c = (LinkedList<SubproblemData>) selection
				.clone();
		if (currentSubproblem.isMultiple()) {
			currentSubproblem.setMultipleSubproblemData(c);
		} else {
			currentSubproblem.setSubproblemData(c.getFirst());
		}
	}
}
