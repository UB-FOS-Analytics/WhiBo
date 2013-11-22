/*
 *  WhiBo
 *
 *  Copyright (C) 2010- by WhiBo development team and the contributors
 *
 *  Complete list of developers available at our web site:
 *
 *       http://www.whibo.fon.bg.ac.rs
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package rs.fon.whibo.integration.adapters.parameter;

import javax.swing.JDialog;

import rs.fon.whibo.integration.interfaces.IParameterTypeProcess;
import rs.fon.whibo.problem.ProblemBuilder;

import com.rapidminer.parameter.ParameterTypeFile;

/**
 * Parameter type that configures process of file creation and loading
 * 
 * @author Nikola Nikolic
 */
public class ParameterTypeProblemFile extends ParameterTypeFile implements
		IParameterTypeProcess {

	private Class<? extends ProblemBuilder> processBuilder;

	private IParameterTypeProcess type;

	JDialog dialog = null;

	public ParameterTypeProblemFile(String key, String description,
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
