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
package rs.fon.whibo.GDT.component.removeInsignificantAttributes;

import java.util.List;

import rs.fon.whibo.problem.AbstractComponent;
import rs.fon.whibo.problem.SubproblemParameter;

/**
 * This abstract component is decreasing the number of candidate attributes in
 * order to improve computational efficiency of an algorithm. The idea for this
 * sub-problem was found in (Loh and Shih 1997) where chi-square test and ANOVA
 * F test were used for categorical and numerical attributes respectively to
 * find the most significant attribute which will be split further. This way
 * there is no need to evaluate split candidates, but only to find the split.
 * 
 * We modified this idea and used “ANOVA F-test” and “Chi-square test” to opt
 * out attributes that don’t satisfy a predefined significance threshold. These
 * RCs are merely heuristics (Wu and Flach 2002), because they don’t evaluate
 * the significance of splits, but the significance of attributes that could
 * produce the splits. These RCs suppose that significant attributes contain the
 * most significant split.
 * 
 * As an input it takes input dataset (with attributs and label) for an
 * algorithm As an output it provides reduced dataset (without "insignificant"
 * attributes).
 * 
 * 
 * Loh WY, Shih YS (1997) Split Selection Methods for Classification Trees, Stat
 * Sin 7: 815-840 Wu S, Flach PA (2002) Feature selection with labeled and
 * unlabeled data. In Proceedings of ECML/PKDD Workshop on Integration and
 * Collaboration Aspects of Data Mining, Decision Support and Meta-Learning,
 * IDDM 2002, Helsinki, Finland.
 */
public abstract class AbstractRemoveInsignificantAtributes extends
		AbstractComponent implements RemoveInsignificantAtributes {

	/** The parameters. */
	List<SubproblemParameter> parameters;

	/**
	 * Instantiates a new abstract remove insignificant attributes.
	 * 
	 * @param parameters
	 *            - list of parameters that can be used by inherited classes
	 *            (components).
	 */
	public AbstractRemoveInsignificantAtributes(
			List<SubproblemParameter> parameters) {
		this.parameters = parameters;
	}
}