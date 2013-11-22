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

import rs.fon.whibo.GDT.algorithm.TreeState;
import rs.fon.whibo.GDT.component.stoppingCriteria.StoppingCriteria;
import rs.fon.whibo.gui.util.Helper;

/**
 * Stopping criteria log class
 * 
 * @author Nenad Zdravkovic
 * 
 */
public class LogStoppingCriteria extends Log {

	private static final long serialVersionUID = -3392485425363443801L;

	public enum Category {
		EVALUATION, PURE_NODE, NO_MORE_ATTRIBUTES, NO_MORE_EXAMPLES,
	}

	private String critName;

	private Category category;

	private int treeDepth;

	private boolean result;

	private boolean splitNode;

	private LogStoppingCriteria(Category category) {
		super();
		this.category = category;
	}

	@Override
	protected String getOutput() {
		String output = getOutputHeader(category.toString());
		switch (category) {
		case EVALUATION:
			output += "Criteria Name: " + critName + DELIMITER
					+ "Three Depth: " + treeDepth + DELIMITER + "Result: "
					+ result + DELIMITER + "SplitNode: " + splitNode
					+ DELIMITER;
			break;

		case NO_MORE_ATTRIBUTES:
			output += "SplitNode: " + splitNode + DELIMITER;
			break;

		case PURE_NODE:
			output += "SplitNode: " + splitNode + DELIMITER;
			break;

		case NO_MORE_EXAMPLES:
			output += "SplitNode: " + splitNode + DELIMITER;
			break;

		default:
			break;
		}
		return output;
	}

	public static Log logEvaluation(StoppingCriteria crit, TreeState treeState,
			boolean result, boolean splitNode) {
		LogStoppingCriteria log = new LogStoppingCriteria(Category.EVALUATION);
		log.critName = Helper.stripPackageName(crit.getClass().getName());
		log.treeDepth = treeState.getTreeDepth();
		log.result = result;
		log.splitNode = splitNode;
		return log;
	}

	public static Log logPureNode(boolean splitNode) {
		LogStoppingCriteria log = new LogStoppingCriteria(Category.PURE_NODE);
		log.splitNode = splitNode;
		return log;
	}

	public static Log logNoMoreAttributes(boolean splitNode) {
		LogStoppingCriteria log = new LogStoppingCriteria(
				Category.NO_MORE_ATTRIBUTES);
		log.splitNode = splitNode;
		return log;
	}

	public static Log logNoMoreExamples(boolean splitNode) {
		LogStoppingCriteria log = new LogStoppingCriteria(
				Category.NO_MORE_EXAMPLES);
		log.splitNode = splitNode;
		return log;
	}
}
