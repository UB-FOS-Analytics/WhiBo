package rs.fon.whibo.integration.adapters.parameter;

import javax.swing.JDialog;

import rs.fon.whibo.integration.interfaces.IParameterTypeProcess;
import rs.fon.whibo.problem.ProblemBuilder;

import com.rapidminer.parameter.ParameterTypeFile;

public class ParameterTypeProblemFileEA extends ParameterTypeFile implements
		IParameterTypeProcess {

	private Class<? extends ProblemBuilder> processBuilder;

	private IParameterTypeProcess type;

	JDialog dialog = null;

	public ParameterTypeProblemFileEA(String key, String description,
			String extension, boolean optional,
			Class<? extends ProblemBuilder> processBuilder) {
		super(key, description, extension, optional);

		this.processBuilder = processBuilder;

	}

	public Class<? extends ProblemBuilder> getProcessBuilder() {
		return processBuilder;
	}

	public void setProcessBuilder(Class<? extends ProblemBuilder> processBuilder) {
		this.processBuilder = processBuilder;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
