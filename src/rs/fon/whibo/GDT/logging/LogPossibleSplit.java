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

package rs.fon.whibo.GDT.logging;

import com.rapidminer.example.Attribute;
import com.rapidminer.operator.learner.tree.AbstractSplitCondition;

/**
 * Possible split log class
 * 
 * @author Nenad Zdravkovic
 * 
 */
public class LogPossibleSplit extends Log {

	private static final long serialVersionUID = 300026917171004572L;

	public enum Category {
		EVALUATION, BEST_POSSIBLE_SPLIT,
	}

	private Category category;

	private double currentSplitEvaluation;

	private double bestSplitEvaluation;

	private boolean splitIsBetter;

	private int subsetIndex;

	private int size;

	private String bestAttribute;

	private String conditionValue;

	private LogPossibleSplit(Category category) {
		super();
		this.category = category;
	}

	@Override
	protected String getOutput() {
		String output = getOutputHeader(category.toString());
		switch (category) {

		case EVALUATION:
			output += "Current split evaluation: " + currentSplitEvaluation
					+ DELIMITER + "Best split evaluation: "
					+ bestSplitEvaluation + DELIMITER + "Split is better: "
					+ splitIsBetter + DELIMITER;
			break;

		case BEST_POSSIBLE_SPLIT:
			output += "Subset index: " + subsetIndex + DELIMITER + "Size: "
					+ size + DELIMITER + "Best attribute: " + bestAttribute
					+ DELIMITER + "Condition value: " + conditionValue
					+ DELIMITER;
			break;

		default:
			break;
		}
		return output;
	}

	public static LogPossibleSplit logEvaluation(double currentSplitEvaluation,
			double bestSplitEvaluation, boolean splitIsBetter) {
		LogPossibleSplit log = new LogPossibleSplit(Category.EVALUATION);
		log.currentSplitEvaluation = currentSplitEvaluation;
		log.bestSplitEvaluation = bestSplitEvaluation;
		log.splitIsBetter = splitIsBetter;
		return log;
	}

	public static LogPossibleSplit logBestPossibleSplit(int subsetIndex,
			int size, Attribute bestAttribute, AbstractSplitCondition condition) {
		LogPossibleSplit log = new LogPossibleSplit(
				Category.BEST_POSSIBLE_SPLIT);
		log.subsetIndex = subsetIndex;
		log.size = size;
		log.bestAttribute = bestAttribute.getName();
		log.conditionValue = condition.getValueString();
		return log;
	}
}
