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
package rs.fon.whibo.problem;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class which filters available components based on their configuration
 * 
 * @author Nenad Zdravkovic
 * 
 */
public class ComponentCompatibilityValidator {

	/**
	 * Filter components for current step based on compatibility configuration
	 * of components already in algorithm and components in current step
	 * 
	 * @param problem
	 *            Current algorithm
	 * @param subproblem
	 *            Current step
	 * @return applicable components
	 */
	public static String[] getAllowedClasses(Problem problem,
			Subproblem subproblem) {
		ArrayList<String> availableClasses = new ArrayList<String>();

		// add all classes as available initially
		for (String availableClass : subproblem
				.getAvailableImplementationClassNames()) {
			availableClasses.add(availableClass);
		}

		ArrayList<String> classesInAlgorithm = getExistingSubproblemNames(problem
				.getSubproblems());
		filterClassesByCurrentAlghorithmComponentsConfiguration(
				availableClasses, classesInAlgorithm, subproblem);
		filterNonCompatibleClassesByCurentStep(availableClasses,
				classesInAlgorithm);
		filterNonExclusiveClassesByCurentStep(availableClasses, problem);

		String[] array = new String[availableClasses.size()];
		availableClasses.toArray(array);
		return array;
	}

	/**
	 * Filters available classes by checking if classes from current step
	 * nonexclusive classes appears in current algorithm
	 */
	private static void filterNonExclusiveClassesByCurentStep(
			ArrayList<String> availableClasses, Problem problem) {

		ArrayList<String> nonApplicableClasses = new ArrayList<String>();
		for (String availableClass : availableClasses) {
			// For each available class make list of exclusive components.
			// If there is in current algorithm any component that is in step
			// for which is required
			// exclusive component, and that step contain non exclusive
			// component, remove available class
			// from list
			ArrayList<String> exclusiveClasses = getExclusiveClassesNames(availableClass);
			List<Subproblem> subproblems = problem.getSubproblems();

			if (subproblems != null && subproblems.size() > 0) {
				for (Subproblem step : subproblems) {
					// make list of exclusive classes that are in the subproblem
					ArrayList<String> exclussiveClassesFromSubproblem = new ArrayList<String>();
					ArrayList<String> classesFormSubproblem = convertToArrayList(step
							.getAvailableImplementationClassNames());
					for (String exclusiveClass : exclusiveClasses) {
						if (classesFormSubproblem.contains(exclusiveClass)) {
							exclussiveClassesFromSubproblem.add(exclusiveClass);
						}
					}
					if (exclussiveClassesFromSubproblem.size() > 0) {
						if (step.isMultiple()) {
							List<SubproblemData> multipleSubproblemData = step
									.getMultipleStepData();
							if (multipleSubproblemData != null) {
								for (SubproblemData stepData : multipleSubproblemData) {
									// if there is a class which is nonexclusive
									// for current
									// available class, remove available class
									if (!exclussiveClassesFromSubproblem
											.contains(stepData
													.getNameOfImplementationClass())) {
										addToList(nonApplicableClasses,
												availableClass);
										System.out
												.println("Class "
														+ availableClass
														+ " prepared for removal because there "
														+ "is non exclusive class in algorithm");
									}
								}
							}
						} else { /* if step is not multiple */
							SubproblemData subproblemData = step
									.getSubproblemData();
							if (subproblemData != null) {
								// if there is a class which is nonexclusive for
								// current
								// available class, remove available class
								if (!exclussiveClassesFromSubproblem
										.contains(subproblemData
												.getNameOfImplementationClass())) {
									addToList(nonApplicableClasses,
											availableClass);
									System.out
											.println("Class "
													+ availableClass
													+ " prepared for removal because there "
													+ "is non exclusive class in algorithm");
								}
							}
						}
					}
				}
			}
		}
		for (String nonApplicableClass : nonApplicableClasses) {
			availableClasses.remove(nonApplicableClass);
		}
	}

	/**
	 * Filters current step classes if there are non compatible classes in
	 * current algorithm
	 */
	private static void filterNonCompatibleClassesByCurentStep(
			ArrayList<String> availableClasses,
			ArrayList<String> classesInAlgorithm) {
		// get noncompatibleclasses for class from current step
		// and check if there is such noncompatible classes in current algorithm
		// if that is the case, remove curent class from available classes
		ArrayList<String> nonApplicableClasses = new ArrayList<String>();
		for (String availableClass : availableClasses) {
			ArrayList<String> nonCompatibleClasses = getNotCompatibleClassesNames(availableClass);
			for (String nonCompatibleClass : nonCompatibleClasses) {
				if (classesInAlgorithm.contains(nonCompatibleClass)) {
					nonApplicableClasses.add(availableClass);
					System.out.println("Class " + availableClass
							+ " prepared for removal as non compatible");
					break;
				}
			}
		}

		for (String nonApplicableClass : nonApplicableClasses) {
			availableClasses.remove(nonApplicableClass);
		}
	}

	/**
	 * Filter classes in current step if classes in algorithm refers to them as
	 * incompatible
	 */
	private static void filterClassesByCurrentAlghorithmComponentsConfiguration(
			ArrayList<String> availableClasses,
			ArrayList<String> classesInAlgorithm, Subproblem subproblem) {

		ArrayList<String> nonApplicableClasses = new ArrayList<String>();
		for (String classInAlgorithm : classesInAlgorithm) {
			ArrayList<String> nonCompatibleClasses = getNotCompatibleClassesNames(classInAlgorithm);
			addToList(nonApplicableClasses, nonCompatibleClasses);
			for (String nonCompatibleClass : nonCompatibleClasses) {
				System.out.println("Class " + nonCompatibleClass
						+ " prepared for removal as non compatible");
			}

			ArrayList<String> exclusiveClasses = getExclusiveClassesNames(classInAlgorithm);
			ArrayList<String> subproblemClasses = convertToArrayList(subproblem
					.getAvailableImplementationClassNames());
			if (collectionContainAnyElement(subproblemClasses, exclusiveClasses)) {
				for (String subproblemClass : subproblemClasses) {
					if (!exclusiveClasses.contains(subproblemClass)) {
						addToList(nonApplicableClasses, subproblemClass);
						System.out.println("Class " + subproblemClass
								+ " prepared for removal as non exclusive");
					}
				}
			}
		}

		for (String nonApplicableClass : nonApplicableClasses) {
			availableClasses.remove(nonApplicableClass);
		}
	}

	private static void addToList(ArrayList<String> list,
			ArrayList<String> elementsToAdd) {
		for (String element : elementsToAdd) {
			addToList(list, element);
		}
	}

	private static void addToList(ArrayList<String> list, String element) {
		if (!list.contains(element)) {
			list.add(element);
		}
	}

	private static ArrayList<String> convertToArrayList(String[] array) {
		ArrayList<String> arrayList = new ArrayList<String>();
		if (array != null) {
			Collections.addAll(arrayList, array);
		}
		return arrayList;
	}

	/**
	 * Returns list of subproblem class names
	 */
	private static ArrayList<String> getExistingSubproblemNames(
			List<Subproblem> subproblems) {
		ArrayList<String> existingSubproblemNames = new ArrayList<String>();
		if (subproblems != null) {
			for (Subproblem subproblem : subproblems) {
				if (subproblem.isMultiple()) {
					List<SubproblemData> multipleStepData = subproblem
							.getMultipleStepData();
					if (multipleStepData != null) {
						for (SubproblemData stepData : multipleStepData) {
							existingSubproblemNames.add(stepData
									.getNameOfImplementationClass());
						}
					}
				} else { /* if step is not multiple */
					SubproblemData subproblemData = subproblem
							.getSubproblemData();
					if (subproblemData != null) {
						existingSubproblemNames.add(subproblemData
								.getNameOfImplementationClass());
					}
				}
			}
		}
		return existingSubproblemNames;
	}

	private static Boolean collectionContainAnyElement(
			ArrayList<String> collection, ArrayList<String> elements) {
		for (String element : elements) {
			if (collection.contains(element)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates instance of referent component with default parameter values
	 * 
	 * @param referentComponent
	 *            name of component class
	 * @return instance of component or null in case of exception
	 */
	private static AbstractComponent getComponentInstance(
			String referentComponent) {
		try {
			Class<?> selectedClass = Class.forName(referentComponent);
			List<SubproblemParameter> parameters = SubproblemParameterReader
					.readParameters(selectedClass);
			for (SubproblemParameter subproblemParameter : parameters) {
				// set default values to parameters so we can instantiate
				// component
				subproblemParameter.setXenteredValue(subproblemParameter
						.getDefaultValue());
			}

			Constructor<?> constructor = selectedClass
					.getConstructor(new Class[] { List.class });
			AbstractComponent instance = (AbstractComponent) constructor
					.newInstance(new Object[] { parameters });
			return instance;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static ArrayList<String> getExclusiveClassesNames(
			String referentComponent) {
		AbstractComponent componentInstance = getComponentInstance(referentComponent);
		String[] classNames = componentInstance.getExclusiveClassNames();
		return convertToArrayList(classNames);
	}

	private static ArrayList<String> getNotCompatibleClassesNames(
			String referentComponent) {
		AbstractComponent componentInstance = getComponentInstance(referentComponent);
		String[] classNames = componentInstance.getNotCompatibleClassNames();
		return convertToArrayList(classNames);
	}
}
