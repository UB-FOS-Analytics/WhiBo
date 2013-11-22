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
package rs.fon.whibo.validation;

import java.awt.Color;
import java.util.List;

import rs.fon.whibo.GDT.dataset.SplittedExampleSet;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.gui.tools.SwingTools;
import com.rapidminer.operator.Model;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.performance.PerformanceVector;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeCategory;
import com.rapidminer.parameter.ParameterTypeDouble;
import com.rapidminer.parameter.ParameterTypeInt;
import com.rapidminer.tools.Tools;
import com.rapidminer.tools.math.FDistribution;
import com.rapidminer.tools.math.SignificanceTestResult;

/**
 * Applies the 5x2cv F-test Reference: xxx
 * 
 * @author Milos Jovanovic
 */

public class Ftest5x2cv extends Ftest5x2cvChain {

	public static final String PARAMETER_ALPHA = "alpha";
	public static final String PARAMETER_LOCAL_RANDOM_SEED = "local_random_seed";
	public static final String PARAMETER_SAMPLING_TYPE = "sampling_type";

	private String criterion; // description of the criterion used for
								// comparison of learners

	public List<ParameterType> getParameterTypes() {
		List<ParameterType> types = super.getParameterTypes();
		ParameterType type;
		type = new ParameterTypeDouble(
				PARAMETER_ALPHA,
				"The probability threshold which determines if differences are considered as significant.",
				0.0d, 1.0d, 0.05d);
		type.setExpert(false);
		types.add(type);
		type = new ParameterTypeInt(
				PARAMETER_LOCAL_RANDOM_SEED,
				"Use the given random seed instead of global random numbers (-1: use global)",
				-1, Integer.MAX_VALUE, -1);
		type.setExpert(false);
		types.add(type);
		type = new ParameterTypeCategory(
				PARAMETER_SAMPLING_TYPE,
				"Defines the sampling type of the cross validation (linear = consecutive subsets, shuffled = random subsets, stratified = random subsets with class distribution kept constant)",
				SplittedExampleSet.SAMPLING_NAMES,
				SplittedExampleSet.STRATIFIED_SAMPLING);
		type.setExpert(false);
		types.add(type);
		return types;
	}

	public Ftest5x2cv(OperatorDescription description) {
		super(description);
	}

	public SignificanceTestResult compareClassifiers(ExampleSet inputSet)
			throws OperatorException {

		double alpha = getParameterAsDouble(PARAMETER_ALPHA);
		int samplingType = getParameterAsInt(PARAMETER_SAMPLING_TYPE);
		int randomSeed = getParameterAsInt(PARAMETER_LOCAL_RANDOM_SEED);
		SplittedExampleSet splittedES;
		double[][] c = new double[5][2]; // comparisons (difference in
											// performance)
		double avgPerformance1 = 0; // average performanse of the first learner
		double avgPerformance2 = 0; // average performanse of the second learner

		// start test
		for (int i = 0; i < 5; i++) {
			// Split training / test set
			splittedES = new SplittedExampleSet(inputSet, 2, samplingType,
					randomSeed);

			for (int j = 0; j < 2; j++) {
				// train models
				splittedES.selectSingleSubset(0 + j);
				Model m1 = learn(1, splittedES);
				Model m2 = learn1(2, splittedES);

				// test models
				splittedES.selectSingleSubset(1 - j);
				PerformanceVector pv1 = evaluate(m1, splittedES);
				PerformanceVector pv2 = evaluate(m2, splittedES);
				double p1 = pv1.getMainCriterion().getAverage();
				double p2 = pv2.getMainCriterion().getAverage();
				avgPerformance1 += p1;
				avgPerformance2 += p2;

				// write result
				c[i][j] = p1 - p2;

				// set description of performance criterion
				criterion = pv1.getMainCriterion().getName();
			}
		}
		// end test

		// calculate statistic
		double p = 0;
		double s = 0;
		double m = 0;
		for (int i = 0; i < 5; i++) {
			m = (c[i][0] + c[i][1]) / 2; // calculate mean
			s += Math.pow((c[i][0] - m), 2) + Math.pow((c[i][1] - m), 2); // calculate
																			// variance
			p += Math.pow(c[i][0], 2) + Math.pow(c[i][1], 2); // calculate sumSq
																// of elements
		}

		double fStat = p / (2 * s);

		// calculate mean performanse of both learners
		avgPerformance1 = avgPerformance1 / 10;
		avgPerformance2 = avgPerformance2 / 10;

		/*
		 * double perf1 = 0; double perf2 = 0; for (int i=0;i<5;i++){ perf1 +=
		 * Math.abs((c[i][0])); perf2 += Math.abs((c[i][1])); } perf1 = perf1 /
		 * 5; perf2 = perf2 / 5;
		 */

		// return result class

		return new MySignificanceTestResult(avgPerformance1, avgPerformance2,
				fStat, alpha, criterion);

	}

	// //////////////////////////////////////////////////////////////
	// INNER CLASS FOR HOLDING THE RESULTS

	public static class MySignificanceTestResult extends SignificanceTestResult {

		private static final long serialVersionUID = 9007616378489018565L;

		private int df1 = 5;
		private int df2 = 10;
		private double alpha = 0.05;

		private double fValue = 0.0d;
		private double prob = 0.0d;

		// performances of the two learners
		private double perf1 = 0.0d;
		private double perf2 = 0.0d;

		private String criterion; // description on the performanse criterion

		public MySignificanceTestResult(double perf1, double perf2,
				double fValue, double alpha, String criterion) {

			this.perf1 = perf1;
			this.perf2 = perf2;
			this.criterion = criterion;

			this.fValue = fValue;
			this.alpha = alpha;

			FDistribution fDist = new FDistribution(df1, df2);
			this.prob = fDist.getProbabilityForValue(this.fValue);
			if (this.prob < 0)
				this.prob = 1.0d;
			else
				this.prob = 1.0d - this.prob;
		}

		public String getName() {
			return "5x2cv F-Test";
		}

		// public String toString() {
		// return "5x2cv F-Test result (f=" + Tools.formatNumber(fValue) +
		// ", prob=" + Tools.formatNumber(prob) + ", alpha=" +
		// Tools.formatNumber(alpha) + ")";
		// }

		public double getProbability() {
			return prob;
		}

		/**
		 * Returns a label that displays the {@link #toResultString()} result
		 * encoded as html.
		 */
		// public java.awt.Component getVisualizationComponent(IOContainer
		// container) {
		// StringBuffer buffer = new StringBuffer();
		// Color bgColor = SwingTools.LIGHTEST_YELLOW;
		// String bgColorString = "#" + Integer.toHexString(bgColor.getRed()) +
		// Integer.toHexString(bgColor.getGreen()) +
		// Integer.toHexString(bgColor.getBlue());
		// Color headerColor = SwingTools.LIGHTEST_BLUE;
		// String headerColorString = "#" +
		// Integer.toHexString(headerColor.getRed()) +
		// Integer.toHexString(headerColor.getGreen()) +
		// Integer.toHexString(headerColor.getBlue());
		//
		// //table with performance results
		// buffer.append("<table bgcolor=\""+bgColorString+"\" border=\"1\">");
		// buffer.append("<tr bgcolor=\""+headerColorString+"\"><th>Learner</th><th>Performance</th></tr>");
		// buffer.append("<tr><td>Learner 1</td><td>" +
		// Tools.formatNumber(perf1) + "</td></tr>");
		// buffer.append("<tr><td>Learner 2</td><td>" +
		// Tools.formatNumber(perf2) + "</td></tr>");
		// buffer.append("</table>");
		// buffer.append("<p>Performance criterion is: " + criterion + "</p>");
		// buffer.append("<br/>");
		//
		// //table with significance results
		// buffer.append("<table bgcolor=\""+bgColorString+"\" border=\"1\">");
		// buffer.append("<tr bgcolor=\""+headerColorString+"\"><th>Hypothesis</th><th>F value</th><th>df1</th><th>df2</th><th>Prob</th><th>Alpha</th></tr>");
		// buffer.append("<tr><td>H0: learners perform equal</td><td>" +
		// Tools.formatNumber(fValue) + "</td><td>" + df1 + "</td><td>" + df2 +
		// "</td><td>" + Tools.formatNumber(prob) + "</td><td>" +
		// Tools.formatNumber(alpha) + "</td></tr>");
		// buffer.append("</table>");
		//
		// buffer.append("<br>Probability for random values with the same result: "
		// + Tools.formatNumber(prob) + "<br>");
		// if (prob < alpha)
		// buffer.append("Difference between algorithms performance is probably <b>significant</b>, since "
		// + Tools.formatNumber(prob) + " &lt; alpha = " +
		// Tools.formatNumber(alpha) + "!");
		// else
		// buffer.append("Difference between algorithms performance is probably <b>not significant</b>, since "
		// + Tools.formatNumber(prob) + " &gt; alpha = " +
		// Tools.formatNumber(alpha) + "!");
		//
		// JEditorPane textPane = new JEditorPane("text/html", "<html><h1>" +
		// getName() + "</h1>" + buffer.toString() + "</html>");
		// textPane.setBackground((new JLabel()).getBackground());
		// textPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(11,
		// 11, 11, 11));
		// return new ExtendedJScrollPane(textPane);
		// }

		@Override
		public String toString() {
			// return "5x2cv F-Test result (f=" + Tools.formatNumber(fValue) +
			// ", prob=" + Tools.formatNumber(prob) + ", alpha=" +
			// Tools.formatNumber(alpha) + ")";
			StringBuffer buffer = new StringBuffer();
			Color bgColor = SwingTools.LIGHTEST_YELLOW;
			String bgColorString = "#" + Integer.toHexString(bgColor.getRed())
					+ Integer.toHexString(bgColor.getGreen())
					+ Integer.toHexString(bgColor.getBlue());
			Color headerColor = SwingTools.LIGHTEST_BLUE;
			String headerColorString = "#"
					+ Integer.toHexString(headerColor.getRed())
					+ Integer.toHexString(headerColor.getGreen())
					+ Integer.toHexString(headerColor.getBlue());

			// table with performance results

			buffer.append("Learner 1:" + Tools.formatNumber(perf1) + "\n");
			buffer.append("Learner 2:" + Tools.formatNumber(perf2) + "\n");

			buffer.append("Performance criterion is: " + criterion + "\n");

			// table with significance results

			buffer.append("Hypothesis H0: learners perform equal \nF value: "
					+ Tools.formatNumber(fValue) + " df1:" + df1 + " df2:"
					+ df2 + " Prob:" + Tools.formatNumber(prob) + " Alpha:"
					+ Tools.formatNumber(alpha) + "\n");
			// buffer.append("<tr><td>H0: learners perform equal</td><td>" +
			// Tools.formatNumber(fValue) + "</td><td>" + df1 + "</td><td>" +
			// df2 + "</td><td>" + Tools.formatNumber(prob) + "</td><td>" +
			// Tools.formatNumber(alpha) + "</td></tr>");

			buffer.append("Probability for random values with the same result: "
					+ Tools.formatNumber(prob) + "\n");
			if (prob < alpha)
				buffer.append("Difference between algorithms performance is probably significant, since "
						+ Tools.formatNumber(prob)
						+ " <alpha = "
						+ Tools.formatNumber(alpha) + "!");
			else
				buffer.append("Difference between algorithms performance is probably not significant, since "
						+ Tools.formatNumber(prob)
						+ " >alpha = "
						+ Tools.formatNumber(alpha) + "!");

			return buffer.toString();
		}
	}
}
