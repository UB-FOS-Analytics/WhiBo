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

import java.util.Date;
import java.util.List;

import rs.fon.whibo.GDT.dataset.SplittedExampleSet;
import rs.fon.whibo.GDT.tools.LogTreeAnalysis;
import rs.fon.whibo.GDT.tools.TreeAnalysis;
import rs.fon.whibo.GDT.tools.TreeAnalysisAverages;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ValueDouble;
import com.rapidminer.operator.learner.tree.Tree;
import com.rapidminer.operator.learner.tree.TreeModel;
import com.rapidminer.operator.performance.PerformanceVector;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeCategory;
import com.rapidminer.parameter.ParameterTypeFile;
import com.rapidminer.parameter.ParameterTypeInt;
import com.rapidminer.parameter.ParameterTypeString;

public class WhiboXValidation extends WhiboXValidationChain {

	/** The name of the algorithm under test */
	public static final String PARAMETER_ALGORITHM_NAME = "algorithm_name";

	/** The name of the algorithm under test */
	public static final String PARAMETER_DATASET_NAME = "dataset_name";

	/**
	 * The parameter name for &quot;Number of subsets for the
	 * crossvalidation.&quot;
	 */
	public static final String PARAMETER_NUMBER_OF_FOLDS = "number_of_folds";

	/**
	 * The parameter name for &quot;Number of repetitions for the
	 * crossvalidation.&quot;
	 */
	public static final String PARAMETER_NUMBER_OF_REPETITIONS = "number_of_repetitions";

	/** The parameter name for &quot;Path to log file with details (csv).&quot; */
	public static final String PARAMETER_LOG_FILE_DETAILS = "log_file_details";

	/** The parameter name for &quot;Path to log file with averages (csv).&quot; */
	public static final String PARAMETER_LOG_FILE_AVERAGES = "log_file_averages";

	/**
	 * The parameter name for &quot;Defines the sampling type of the cross
	 * validation (linear = consecutive subsets, shuffled = random subsets,
	 * stratified = random subsets with class distribution kept constant)&quot;
	 */
	public static final String PARAMETER_SAMPLING_TYPE = "sampling_type";

	/**
	 * The parameter name for &quot;Indicates if only performance vectors should
	 * be averaged or all types of averagable result vectors&quot;
	 */
	public static final String PARAMETER_AVERAGE_PERFORMANCES_ONLY = "average_performances_only";

	/**
	 * The parameter name for &quot;Use the given random seed instead of global
	 * random numbers (-1: use global)&quot;
	 */
	public static final String PARAMETER_LOCAL_RANDOM_SEED = "local_random_seed";

	private int fold;
	private int repetition;

	public WhiboXValidation(OperatorDescription description) {
		super(description);
		addValue(new ValueDouble("fold", "The number of the current fold.") {
			public double getDoubleValue() {
				return fold;
			}
		});
		addValue(new ValueDouble("iteration",
				"The number of the current iteration.") {
			public double getDoubleValue() {
				return repetition;
			}
		});
	}

	@Override
	public void estimatePerformance(ExampleSet inputSet)
			throws OperatorException {
		{

			String algorithmName = getParameterAsString(PARAMETER_ALGORITHM_NAME);
			String datasetName = getParameterAsString(PARAMETER_DATASET_NAME);

			int folds = getParameterAsInt(PARAMETER_NUMBER_OF_FOLDS);
			int repetitions = getParameterAsInt(PARAMETER_NUMBER_OF_REPETITIONS);

			log("Starting " + folds + "-fold cross validation, in "
					+ repetition + ". iteration.");

			int samplingType = getParameterAsInt(PARAMETER_SAMPLING_TYPE);
			int randomSeed = getParameterAsInt(PARAMETER_LOCAL_RANDOM_SEED);
			String filePathDetails = getParameterAsString(PARAMETER_LOG_FILE_DETAILS);
			String filePathAvg = getParameterAsString(PARAMETER_LOG_FILE_AVERAGES);

			TreeAnalysis analysis = null;
			TreeAnalysisAverages averages = new TreeAnalysisAverages();
			LogTreeAnalysis logDetails = null;
			LogTreeAnalysis logAverages = null;
			try {
				logDetails = new LogTreeAnalysis(filePathDetails);
				logAverages = new LogTreeAnalysis(filePathAvg);
			} catch (Exception e) {
				throw new OperatorException("failed to open log files");
			}

			Date startTime = new Date();

			// begin crossvalidation
			for (repetition = 0; repetition < repetitions; repetition++) {

				SplittedExampleSet splittedES = new SplittedExampleSet(
						inputSet, folds, samplingType, randomSeed + repetition
								* 10);

				for (fold = 0; fold < folds; fold++) {

					analysis = new TreeAnalysis();

					// learn model on train data
					splittedES.selectAllSubsetsBut(fold);
					// IOContainer learnResult = learn(splittedES);
					// Tree tree = learnResult.get(TreeModel.class).getRoot();
					Tree tree = ((TreeModel) learn(splittedES)).getRoot();

					// test model on test data
					splittedES.selectSingleSubset(fold);
					// IOContainer evalOutput = evaluate(splittedES);
					PerformanceVector pv = evaluate(splittedES);
					// double performance =
					// evalOutput.get(PerformanceVector.class).getMainCriterion().getMikroAverage();
					double performance = pv.getMainCriterion()
							.getMikroAverage();
					// calculate model properties & log to file
					analysis.measureTime();
					analysis.analyseTree(tree, performance);
					analysis.setRunDescription("R" + (repetition + 1) + " - F"
							+ (fold + 1));
					analysis.setAlgorithmDescription(algorithmName);
					analysis.setDatasetDescription(datasetName);
					logDetails.logPerformance(analysis);

					averages.addAnalysis(analysis);

					inApplyLoop();
				}

			}
			logAverages.logPerformance(averages);

			logDetails.closeLog();
			logAverages.closeLog();
		}

	}

	public List<ParameterType> getParameterTypes() {
		List<ParameterType> types = super.getParameterTypes();

		types.add(new ParameterTypeBoolean(
				PARAMETER_AVERAGE_PERFORMANCES_ONLY,
				"Indicates if only performance vectors should be averaged or all types of averagable result vectors",
				true));

		ParameterType type;

		type = new ParameterTypeString(PARAMETER_ALGORITHM_NAME,
				"Name of the algorithm under test.");
		type.setExpert(false);
		types.add(type);

		type = new ParameterTypeString(PARAMETER_DATASET_NAME,
				"Name of the dataset under test.");
		type.setExpert(false);
		types.add(type);

		type = new ParameterTypeInt(PARAMETER_NUMBER_OF_FOLDS,
				"Number of subsets for the crossvalidation.", 2,
				Integer.MAX_VALUE, 2);
		type.setExpert(false);
		types.add(type);

		type = new ParameterTypeInt(PARAMETER_NUMBER_OF_REPETITIONS,
				"Number of repetitions of the cross validation.", 1,
				Integer.MAX_VALUE, 5);
		type.setExpert(false);
		types.add(type);

		type = new ParameterTypeCategory(
				PARAMETER_SAMPLING_TYPE,
				"Defines the sampling type of the cross validation (linear = consecutive subsets, shuffled = random subsets, stratified = random subsets with class distribution kept constant)",
				SplittedExampleSet.SAMPLING_NAMES,
				SplittedExampleSet.STRATIFIED_SAMPLING);
		type.setExpert(true);
		types.add(type);

		type = new ParameterTypeInt(PARAMETER_LOCAL_RANDOM_SEED,
				"Use the given random seed", 1, Integer.MAX_VALUE, 1);
		// type.registerDependencyCondition(new EqualTypeCondition(this,
		// PARAMETER_SAMPLING_TYPE, false, SplittedExampleSet.SHUFFLED_SAMPLING,
		// SplittedExampleSet.STRATIFIED_SAMPLING));
		types.add(type);

		type = new ParameterTypeFile(
				PARAMETER_LOG_FILE_DETAILS,
				"Define a path to a log file with details of experiments in csv format",
				"csv", "");
		type.setExpert(false);
		types.add(type);

		type = new ParameterTypeFile(PARAMETER_LOG_FILE_AVERAGES,
				"Define a path to a log file with averages in csv format",
				"csv", "");
		type.setExpert(false);
		types.add(type);

		return types;
	}

}
