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
package rs.fon.whibo.GDT.component.possibleSplits;

import java.util.List;

import rs.fon.whibo.GDT.dataset.SplittedExampleSet;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;

/**
 * 
 * Interface for solving possible split subproblem
 * 
 * @author Milan Vukicevic
 * 
 */
public interface PossibleSplit {

	/**
	 * Inits the possible split component.
	 * 
	 * @param exampleSet
	 *            - example set in current node
	 * @param attributesForSplitting
	 *            - the attributes that are available for creating splitting
	 *            candidates
	 */
	public void init(ExampleSet exampleSet,
			List<Attribute> attributesForSplitting);

	/**
	 * Next split.
	 * 
	 * @return the splitted example set
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public SplittedExampleSet nextSplit() throws Exception;

	/**
	 * Checks for next split.
	 * 
	 * @return true, if there are more created splits or there are more
	 *         attributes available for splitting
	 */
	public boolean hasNextSplit();

	/**
	 * Checks if created split is categorical.
	 * 
	 * @return true, if split is categorical
	 */
	public boolean isCategoricalSplit();

	/**
	 * Checks if split is numerical
	 * 
	 * @return true, if is split is numerical
	 */
	public boolean isNumericalSplit();

}
