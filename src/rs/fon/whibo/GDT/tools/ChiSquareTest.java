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
package rs.fon.whibo.GDT.tools;

import java.util.LinkedList;
import java.util.List;

import rs.fon.whibo.GDT.dataset.SplittedExampleSet;
import JSci.maths.statistics.ChiSqrDistribution;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;

/**
 * This class implements chi square test based on example set, input attributes
 * and label attribute (class).
 */
public class ChiSquareTest {

	/** The contigency table. */
	long[][] contigencyTable;

	/** The contigency calculated. */
	double[][] contigencyCalculated;

	/** The num of columns. */
	int numOfColumns;

	/** The num of rows. */
	int numOfRows;

	/** The row sum. */
	long[] rowSum;

	/** The column sum. */
	long[] columnSum;

	/** The all sum. */
	long allSum;

	/** The chi square. */
	double chiSquare;

	/** The prob. */
	double prob;

	/**
	 * Instantiates a new chi square test.
	 * 
	 * @param exampleSet
	 *            - the example set
	 */
	public ChiSquareTest(SplittedExampleSet exampleSet) {
		Attribute label = exampleSet.getAttributes().getLabel();
		List<String> labelCategories = null;

		labelCategories = exampleSet.getAllCategories(label);

		int numberOfLabelCategories = labelCategories.size();

		numOfRows = exampleSet.getNumberOfSubsets();

		numOfColumns = numberOfLabelCategories;

		contigencyTable = new long[numOfRows][numOfColumns];
		try {
			for (int i = 0; i < numOfRows; i++) {
				exampleSet.selectSingleSubset(i);
				for (int j = 0; j < numOfColumns; j++) {

					contigencyTable[i][j] = 0;

					for (Example e : exampleSet) {

						if (labelCategories.get(j).equals(
								e.getNominalValue(label)))
							contigencyTable[i][j] += 1;
					}

				}
				exampleSet.selectAllSubsets();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		calculateRowSum();
		calculateColumnSum();
		calculateAllSum();
		contigencyCalculated = calculateContigency(contigencyTable);
		calculateChiSquare();

	}

	/**
	 * Instantiates a new chi square test.
	 * 
	 * @param exampleSet
	 *            the example set
	 * @param categories
	 *            the categories
	 * @param attribute
	 *            the attribute
	 */
	public ChiSquareTest(SplittedExampleSet exampleSet,
			LinkedList<String> categories, Attribute attribute) {
		Attribute label = exampleSet.getAttributes().getLabel();
		List<String> labelCategories = null;

		labelCategories = exampleSet.getAllCategories(label);

		int numberOfLabelCategories = labelCategories.size();

		numOfRows = categories.size();

		numOfColumns = numberOfLabelCategories;
		contigencyTable = new long[numOfRows][numOfColumns];
		try {
			for (int i = 0; i < numOfRows; i++) {

				for (int j = 0; j < numOfColumns; j++) {

					contigencyTable[i][j] = 0;

					for (Example e : exampleSet) {

						if (categories.get(i).contains(
								e.getNominalValue(attribute))
								&& labelCategories.get(j).equals(
										e.getNominalValue(label)))
							contigencyTable[i][j] += 1;
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		calculateRowSum();
		calculateColumnSum();
		calculateAllSum();
		contigencyCalculated = calculateContigency(contigencyTable);
		calculateChiSquare();

	}

	/**
	 * Gets the chi square value.
	 * 
	 * @return chi squre value
	 */
	public double getChiSquare() {
		return chiSquare;
	}

	/**
	 * Gets the probability.
	 * 
	 * @return the probability
	 */
	public double getProbability() {
		return prob;
	}

	/**
	 * Calculate column sum.
	 */
	private void calculateColumnSum() {

		columnSum = new long[numOfRows];
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				columnSum[i] += contigencyTable[i][j];
			}
		}

	}

	/**
	 * Calculate row sum.
	 */
	private void calculateRowSum() {
		rowSum = new long[numOfColumns];
		for (int j = 0; j < numOfColumns; j++) {
			for (int i = 0; i < numOfRows; i++) {
				rowSum[j] += contigencyTable[i][j];
			}
		}
	}

	/**
	 * Calculate all sum.
	 */
	private void calculateAllSum() {

		for (int j = 0; j < columnSum.length; j++) {
			allSum += columnSum[j];
		}

	}

	/**
	 * Calculate contigency.
	 * 
	 * @param contigencyTable
	 *            for pair of input attributes
	 * 
	 * @return the double[][]
	 */
	private double[][] calculateContigency(long[][] contigencyTable) {
		contigencyCalculated = new double[numOfRows][numOfColumns];
		for (int j = 0; j < numOfColumns; j++)

		{
			for (int i = 0; i < numOfRows; i++) {
				double rs = rowSum[j];
				double cs = columnSum[i];
				double calc = rs * cs / allSum;
				contigencyCalculated[i][j] = calc;
			}
		}

		return contigencyCalculated;
	}

	/**
	 * Calculate chi square.
	 */
	private void calculateChiSquare() {
		chiSquare = 0;

		for (int i = 0; i < numOfRows; i++)

		{
			for (int j = 0; j < numOfColumns; j++) {
				double c1 = contigencyTable[i][j];
				double c2 = contigencyCalculated[i][j];
				double c3 = c1 - c2;

				if (c2 != 0)
					chiSquare += Math.pow(c3, 2) / c2;
			}
		}

		ChiSqrDistribution dist = new ChiSqrDistribution((numOfColumns - 1)
				* (numOfRows - 1));

		prob = 1 - dist.cumulative(chiSquare);

	}

	/**
	 * Gets the observed matrix.
	 * 
	 * @return the observed matrix
	 */
	public long[][] getObserved() {
		return this.contigencyTable;
	}

	/**
	 * Gets the expected matrix.
	 * 
	 * @return the expected matrix
	 */
	public double[][] getExpected() {
		return this.contigencyCalculated;
	}

}
