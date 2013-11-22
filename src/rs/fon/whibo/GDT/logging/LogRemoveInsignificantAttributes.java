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

import java.util.Iterator;
import java.util.LinkedList;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Statistics;

/**
 * Remove insignificant attributes log class
 * 
 * @author Nenad Zdravkovic
 * 
 */
public class LogRemoveInsignificantAttributes extends Log {

	private static final long serialVersionUID = -462424935242804014L;

	public enum Category {
		REMOVE_LABEL_ATTRIBUTE, REMOVES_ATTRIBUTES_WITH_ONLY_SINGLE_CATEGORY, REMOVE_NUMERIC_CATEGORICAL_ATTRIBUTES, REMOVE_INSIGNIFICANT_ATTRIBUTES, ATTRIBUTES_BEFORE_REMOVAL, ATTRIBUTES_AFTER_REMOVAL,
	}

	private Category category;

	private String removedLabel;

	LinkedList<Attribute> attributesBeforeRemoval;

	LinkedList<Attribute> attributesAfterRemoval;

	private LogRemoveInsignificantAttributes(Category category) {
		super();
		this.category = category;
	}

	public static LogRemoveInsignificantAttributes logRemoveLabelAttribute(
			Attribute attributeLabel) {

		LogRemoveInsignificantAttributes log = new LogRemoveInsignificantAttributes(
				Category.REMOVE_LABEL_ATTRIBUTE);
		log.removedLabel = attributeLabel.getName();
		Iterator<Statistics> s = attributeLabel.getAllStatistics();
		while (s.hasNext()) {

			log.removedLabel += DELIMITER + s.next().toString();

		}
		return log;
	}

	public static LogRemoveInsignificantAttributes logRemoveOnlySingleCategoryAttribute(
			Attribute a) {
		LogRemoveInsignificantAttributes log = new LogRemoveInsignificantAttributes(
				Category.REMOVES_ATTRIBUTES_WITH_ONLY_SINGLE_CATEGORY);
		log.removedLabel = a.getName();
		return log;
	}

	public static LogRemoveInsignificantAttributes logNumercialCategorical(
			Attribute a) {
		LogRemoveInsignificantAttributes log = new LogRemoveInsignificantAttributes(
				Category.REMOVE_NUMERIC_CATEGORICAL_ATTRIBUTES);
		log.removedLabel = a.getName();
		return log;
	}

	public static LogRemoveInsignificantAttributes logAttributesBeforeRemoval(
			LinkedList<Attribute> attributesForSplit) {
		LogRemoveInsignificantAttributes log = new LogRemoveInsignificantAttributes(
				Category.REMOVE_NUMERIC_CATEGORICAL_ATTRIBUTES);
		log.attributesBeforeRemoval = attributesForSplit;
		return log;
	}

	public static LogRemoveInsignificantAttributes logAttributesAfterRemoval(
			LinkedList<Attribute> attributesForSplit) {
		LogRemoveInsignificantAttributes log = new LogRemoveInsignificantAttributes(
				Category.REMOVE_NUMERIC_CATEGORICAL_ATTRIBUTES);
		log.attributesAfterRemoval = attributesForSplit;
		return log;
	}

	@Override
	protected String getOutput() {
		String output = getOutputHeader(category.toString());
		switch (category) {
		case REMOVE_LABEL_ATTRIBUTE:
		case REMOVES_ATTRIBUTES_WITH_ONLY_SINGLE_CATEGORY:
		case REMOVE_NUMERIC_CATEGORICAL_ATTRIBUTES:
		case REMOVE_INSIGNIFICANT_ATTRIBUTES:
			output += removedLabel + DELIMITER;
			break;
		case ATTRIBUTES_BEFORE_REMOVAL:
			for (Attribute a : attributesBeforeRemoval) {
				output += a.getName() + DELIMITER;
			}
			break;
		case ATTRIBUTES_AFTER_REMOVAL:
			for (Attribute a : attributesAfterRemoval) {
				output += a.getName() + DELIMITER;
			}
			break;

		default:
			break;
		}
		return output;
	}
}
