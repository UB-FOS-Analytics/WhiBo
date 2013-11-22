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
package rs.fon.whibo.GC.algorithm;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import rs.fon.whibo.GC.Tools.WhiBoTools;
import rs.fon.whibo.GC.clusterModel.WhiBoCentroid;
import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;
import rs.fon.whibo.GC.component.Evaluation.Evaluation;
import rs.fon.whibo.GC.component.Initialization.Initialization;
import rs.fon.whibo.GC.component.RecalculateRepresentatives.RecalculateRepresentatives;
import rs.fon.whibo.GC.component.SplitClusters.SplitClusters;
import rs.fon.whibo.GC.component.StopCriteria.StopCriteria;
import rs.fon.whibo.integration.adapters.interfaces.IGCAlgorithm;
import rs.fon.whibo.problem.Problem;
import rs.fon.whibo.problem.Subproblem;
import rs.fon.whibo.problem.SubproblemData;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.AttributeRole;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.Tools;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.tools.Ontology;

public class GCAlgorithm implements IGCAlgorithm {

	/**
	 * The state state member that keeps the information about optimization step
	 * number, cluster statistics, elapsed time... throughout the tree building
	 * process.
	 */
	private ClusterState clusterState;

	// COMPONENTS

	/** List of components selected to solve the Initialization subproblem. */
	private Initialization initialization;
	/** List of components selected to solve the Distance measure subproblem. */
	private DistanceMeasure distanceMeasure;
	/**
	 * List of components selected to solve the recalculate representatives
	 * subproblem.
	 */
	private RecalculateRepresentatives recalculateRepresentatives;
	/** List of components selected to solve the stop criteria subproblem. */
	private List<StopCriteria> stopCriteria;
	/** List of components selected to solve the evaluation subproblem. */
	private Evaluation evaluation;
	/** List of components selected to solve the split clusters subproblem. */
	private SplitClusters splitClusters;

	private WhiBoCentroidClusterModel centroidClusterModel;

	/**
	 * Instantiates a new GDT algorithm.
	 * 
	 * @param problem
	 *            file that holds all of the user defined information on
	 *            algorithm design.
	 * @throws Exception
	 *             with the description if something goes wrong.
	 */
	public GCAlgorithm(Problem problem) throws Exception {
		initializeComponents(problem);
		// log = new LogState("C:\\WhiboTesting\\ExperimentLog.csv");

	}

	/**
	 * Initializes the components selected by the user for execution. The
	 * implementation objects of the components are passed to the subproblem
	 * interfaces that are used by the generic algorithm.
	 * 
	 * @param problem
	 *            file that holds all of the user defined information on
	 *            algorithm design.
	 * @throws Exception
	 *             with the description if something goes wrong.
	 */
	@SuppressWarnings("unchecked")
	private void initializeComponents(Problem process) throws Exception {

		List<Subproblem> steps = process.getSubproblems();
		Subproblem step;

		// INITIALIZATION
		step = steps.get(0);

		{
			Constructor c = Class.forName(
					step.getSubproblemData().getNameOfImplementationClass())
					.getConstructor(new Class[] { List.class });
			initialization = ((Initialization) c
					.newInstance(new Object[] { step.getSubproblemData()
							.getListOfParameters() }));
		}

		// DISTANCE MEASURE AND ASIGN INSTANCES TO CENTROIDS
		step = steps.get(1);

		{
			Constructor c = Class.forName(
					step.getSubproblemData().getNameOfImplementationClass())
					.getConstructor(new Class[] { List.class });
			distanceMeasure = ((DistanceMeasure) c
					.newInstance(new Object[] { step.getSubproblemData()
							.getListOfParameters() }));
		}

		// RECALCULATE REPRESENTATIVES
		step = steps.get(2);

		{
			Constructor c = Class.forName(
					step.getSubproblemData().getNameOfImplementationClass())
					.getConstructor(new Class[] { List.class });
			recalculateRepresentatives = ((RecalculateRepresentatives) c
					.newInstance(new Object[] { step.getSubproblemData()
							.getListOfParameters() }));
		}

		// STOPING CRITERIA
		step = steps.get(3);
		stopCriteria = new LinkedList<StopCriteria>();

		if (step.getMultipleStepData() != null) {
			for (SubproblemData stepData : step.getMultipleStepData()) {
				Constructor c = Class.forName(
						stepData.getNameOfImplementationClass())
						.getConstructor(new Class[] { List.class });
				stopCriteria.add((StopCriteria) c
						.newInstance(new Object[] { stepData
								.getListOfParameters() }));
			}
		}

		// SPLIT CLUSTERS

		step = steps.get(4);

		if (step.getSubproblemData() != null) {
			Constructor c = Class.forName(
					step.getSubproblemData().getNameOfImplementationClass())
					.getConstructor(new Class[] { List.class });
			splitClusters = ((SplitClusters) c.newInstance(new Object[] { step
					.getSubproblemData().getListOfParameters() }));
		}

		// MODEL EVALUATION
		step = steps.get(5);

		{
			Constructor c = Class.forName(
					step.getSubproblemData().getNameOfImplementationClass())
					.getConstructor(new Class[] { List.class });
			evaluation = ((Evaluation) c.newInstance(new Object[] { step
					.getSubproblemData().getListOfParameters() }));
		}

	}

	/**
	 * Implements generic clusterer algorithm that uses components defined in
	 * problem file
	 * 
	 * @param ExampleSet
	 *            - the example set that will be clustered.
	 */
	public WhiBoCentroidClusterModel learnClusterModel(ExampleSet exampleSet) {

		clusterState = new ClusterState();
		boolean shouldStop = false;
		WhiBoCentroidClusterModel bestCentroidClusterModel = null;
		int restartNumber = 0;
		double bestEvaluation = evaluation.getWorstValue();
		double currentEvaluation = 0;
		int[] centroidAssignments = new int[exampleSet.size()];
		WhiBoTools whiboTools = new WhiBoTools();

		// gets only regular attributes (non-id, non-label, etc.)

		try {
			Tools.checkAndCreateIds(exampleSet);
		} catch (OperatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ExampleSet trainingSet = (ExampleSet) exampleSet.clone();

		Iterator<AttributeRole> it = trainingSet.getAttributes()
				.specialAttributes();
		while (it.hasNext()) {
			AttributeRole ar = it.next();
			trainingSet.getAttributes().remove(ar);
		}

		// restarts algorithm as many times as defined in Initialization
		// sub-problem

		while (restartNumber < initialization.getNumberOfRestarts()) {
			shouldStop = false;
			clusterState.restartOptimizationStepNumber();

			centroidClusterModel = initialization.InitializeCentroids(
					trainingSet, distanceMeasure);

			// Iterates until some stop criteria is reached
			while (!shouldStop) {
				clusterState.nextOptimizationStep();

				centroidAssignments = distanceMeasure.assignInstances(
						trainingSet, centroidClusterModel);

				centroidClusterModel.setClusterAssignments(centroidAssignments,
						trainingSet);
				// implicit stop criteria stability - if centroids remain the
				// same in
				// two optimization steps, algorithm stops
				shouldStop = recalculateRepresentatives.Recalculate(
						centroidAssignments, centroidClusterModel, trainingSet,
						distanceMeasure);
				if (!shouldStop) {
					for (StopCriteria sc : stopCriteria) {
						shouldStop = sc.shouldStop(clusterState);
					}
				}

				currentEvaluation = evaluation.Evaluate(distanceMeasure,
						centroidClusterModel, trainingSet);
				// if (bestEvaluation<currentEvaluation)
				if (evaluation.isBetter(currentEvaluation, bestEvaluation)) {
					bestCentroidClusterModel = centroidClusterModel;
					bestEvaluation = currentEvaluation;
				}

				// new cluster model with centroids (examples are removed)
				if (!shouldStop) {
					ArrayList<WhiBoCentroid> centroids = new ArrayList<WhiBoCentroid>();
					for (int i = 0; i < centroidClusterModel
							.getNumberOfClusters(); i++) {
						WhiBoCentroid centroid = centroidClusterModel
								.getCentroid(i);
						centroids.add(centroid);
					}
					centroidClusterModel = new WhiBoCentroidClusterModel(
							trainingSet,
							centroidClusterModel.getNumberOfClusters(),
							exampleSet.getAttributes());
					centroidClusterModel.setCentroids(centroids);
				}
			}

			// COMPONENT: SplitClusters
			if (!(splitClusters == null))
				centroidClusterModel = splitClusters
						.split(centroidClusterModel);

			restartNumber++;
		}

		Attribute cluster = AttributeFactory.createAttribute("cluster",
				Ontology.INTEGER);
		exampleSet.getExampleTable().addAttribute(cluster);
		exampleSet.getAttributes().setCluster(cluster);
		int i1 = 0;
		for (Example example1 : exampleSet) {
			// example1.setValue(cluster, "cluster_" + centroidAssignments[i1]);
			example1.setValue(cluster, centroidAssignments[i1]);

			i1++;
		}
		// bestCentroidClusterModel.setClusterAssignments(centroidAssignments,
		// exampleSet);
		return bestCentroidClusterModel;
	}

}
