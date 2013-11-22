package rs.fon.whibo.optimization.ga;

import java.util.ArrayList;
import java.util.List;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.Population;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.MutationOperator;
import org.jgap.impl.UniformCrossover;

import rs.fon.whibo.optimization.ga.genes.CreateSplitGene;
import rs.fon.whibo.optimization.ga.genes.EvaluateSplitGene;
import rs.fon.whibo.optimization.ga.genes.PruneGene;
import rs.fon.whibo.optimization.ga.genes.RIAGene;
import rs.fon.whibo.optimization.ga.genes.StoppingGene;
import rs.fon.whibo.optimization.ga.genes.SubproblemGene;
import rs.fon.whibo.optimization.ga.rapidminer.EAOperator;
import rs.fon.whibo.problem.Problem;

public class WhiBoTreeGaOpt {

	private EAOperator eaOperator;
	private Genotype population;
	private Configuration conf;
	private GeneralizationErrorFitnessFunction myFunc;

	// PARAMETERS
	private int MAX_ALLOWED_EVOLUTIONS = 50;
	private int POPULATION_SIZE = 30;
	private int MUTATION_RATE = 6;
	private double CROSSOVER_RATE = 0.35;
	private double SWITCH_FROM_SURROGATE_PERCENTAGE_EVOLUTIONS = 0.4;
	private double SURROGATE_PERCENTAGE = 0.3;
	private boolean MUTATE_PARAMETERS = false;

	public WhiBoTreeGaOpt(EAOperator eaOperator) throws Exception {
		this.eaOperator = eaOperator;
		setParameters(this.eaOperator.getEAParameters());
	}

	private void setParameters(List<String[]> paramerets) throws Exception {
		try {
			MAX_ALLOWED_EVOLUTIONS = Integer.parseInt(paramerets.get(0)[1]);
			POPULATION_SIZE = Integer.parseInt(paramerets.get(1)[1]);
			MUTATION_RATE = Integer.parseInt(paramerets.get(2)[1]);
			CROSSOVER_RATE = Double.parseDouble(paramerets.get(3)[1]);
			SWITCH_FROM_SURROGATE_PERCENTAGE_EVOLUTIONS = Double
					.parseDouble(paramerets.get(4)[1]);
			SURROGATE_PERCENTAGE = Double.parseDouble(paramerets.get(5)[1]);
			MUTATE_PARAMETERS = Boolean.parseBoolean(paramerets.get(7)[1]);
			SubproblemGene.shouldMutateParameters(Boolean
					.parseBoolean(paramerets.get(7)[1]));
			SubproblemGene.shouldMutateComponents(Boolean
					.parseBoolean(paramerets.get(6)[1]));
			SubproblemGene.setComponentMutationRate(Double
					.parseDouble(paramerets.get(8)[1]));
			SubproblemGene.setParameterMutationRate(Double
					.parseDouble(paramerets.get(9)[1]));
		} catch (Exception e) {
			throw new Exception("Please insert correct parameter values");
		}
	}

	public void initEA() throws Exception {
		try {
			conf.reset();
			LogProgress.getInstance().reset();

			conf = new DefaultConfiguration();
			conf.setPreservFittestIndividual(true); // elitism
			conf.getGeneticOperators().remove(0); // remove default Crossover
			// operator
			conf.getGeneticOperators().remove(0); // remove default Mutation
			// operator
			conf.addGeneticOperator(new UniformCrossover(conf, CROSSOVER_RATE)); // add
			// custom
			// Crossover
			// operator
			conf.addGeneticOperator(new MutationOperator(conf, MUTATION_RATE)); // add
			// custom
			// Mutation
			// operator
			myFunc = new GeneralizationErrorFitnessFunction(eaOperator);
			conf.setFitnessFunction(myFunc);

			eaOperator.setSamplePercentage(SURROGATE_PERCENTAGE);

			// Chromosomes setup

			ArrayList<Gene> genes = new ArrayList<Gene>();

			genes.add(new RIAGene(conf));
			genes.add(new CreateSplitGene(conf));
			genes.add(new EvaluateSplitGene(conf));
			genes.add(new StoppingGene(conf));
			genes.add(new PruneGene(conf));

			IChromosome sampleChromosome = new Chromosome(conf,
					(Gene[]) genes.toArray(new Gene[genes.size()]));
			conf.setSampleChromosome(sampleChromosome);

			conf.setPopulationSize(POPULATION_SIZE);

			this.population = Genotype.randomInitialGenotype(conf);
		} catch (Exception exception) {
			throw new Exception(
					"Please select at least one valid component for create split and evaluate split");
		}
	}

	public Problem createOptimalWBAlgorithmTweak() throws Exception {

		LogProgress.getInstance().startStopWatch();
		SubproblemGene.shouldMutateParameters(MUTATE_PARAMETERS);

		// ---------------------------------------------------------------
		myFunc.switchToSurrogateFunction();
		for (int iteration = 0; iteration < MAX_ALLOWED_EVOLUTIONS; iteration++) {
			if (!uniqueChromosomes(population.getPopulation())) {
				throw new RuntimeException("Invalid state in generation "
						+ iteration);
			}
			if (iteration == Math.round(MAX_ALLOWED_EVOLUTIONS
					* SWITCH_FROM_SURROGATE_PERCENTAGE_EVOLUTIONS)) {
				myFunc.switchToFullFitnessFunction();
				((GeneralizationErrorFitnessFunction) conf.getFitnessFunction())
						.clearCache();
				Tools.clearChromosomFitnessValues(population.getPopulation());

				LogProgress.getInstance().log("Cache cleared--------------");
				LogProgress.getInstance().recordStatistics();
				LogProgress.getInstance().clearStatistic();
				LogProgress.getInstance().splitTimeStopWatch();
				LogProgress.getInstance().log(
						"Execution time: "
								+ LogProgress.getInstance().elapsedTime());
				LogProgress.getInstance().log("Cache cleared--------------");
			}

			if (true && iteration == 30) {
				SubproblemGene.shouldMutateParameters(MUTATE_PARAMETERS);
			}

			population.evolve();

		}
		SubproblemGene.shouldMutateParameters(MUTATE_PARAMETERS);

		// ---------------------------------------------------------------------
		LogProgress.getInstance().splitTimeStopWatch();

		// HACK: uses setChromosemes() to set the m_changed for population to
		// TRUE,
		// in order to calculate fittest chromosome from scratch
		population.getPopulation().setChromosomes(
				population.getPopulation().getChromosomes());

		IChromosome bestSolutionSoFar = population.getFittestChromosome();
		System.out.println("The best solution has a fitness value of "
				+ bestSolutionSoFar.getFitnessValue());
		LogProgress.getInstance().log(
				"The best solution fitness value:,"
						+ bestSolutionSoFar.getFitnessValue());
		LogProgress.getInstance().log("Best Solution:");
		LogProgress.getInstance().log(bestSolutionSoFar.getGenes());
		// LogProgress.getInstance().log("Execution time:,"+LogProgress.getInstance().elapsedTime());

		LogProgress.getInstance().recordStatistics();

		LogProgress.getInstance().closeLog();

		Problem bestWhiboAlgorithm = myFunc.createProblem(bestSolutionSoFar);

		return bestWhiboAlgorithm;
	}

	public Problem createOptimalWBAlgorithm() throws Exception {

		LogProgress.getInstance().startStopWatch();

		// ---------------------------------------------------------------
		myFunc.switchToSurrogateFunction();
		for (int iteration = 0; iteration < MAX_ALLOWED_EVOLUTIONS; iteration++) {
			if (!uniqueChromosomes(population.getPopulation())) {
				throw new RuntimeException("Invalid state in generation "
						+ iteration);
			}
			if (iteration == Math.round(MAX_ALLOWED_EVOLUTIONS
					* SWITCH_FROM_SURROGATE_PERCENTAGE_EVOLUTIONS)) {
				myFunc.switchToFullFitnessFunction();
				((GeneralizationErrorFitnessFunction) conf.getFitnessFunction())
						.clearCache();
				Tools.clearChromosomFitnessValues(population.getPopulation());

				LogProgress.getInstance().log("Cache cleared--------------");
				LogProgress.getInstance().recordStatistics();
				LogProgress.getInstance().clearStatistic();
				LogProgress.getInstance().splitTimeStopWatch();
				LogProgress.getInstance().log(
						"Execution time: "
								+ LogProgress.getInstance().elapsedTime());
				LogProgress.getInstance().log("Cache cleared--------------");
			}
			population.evolve();

			/*
			 * //=================================================
			 * LogProgress.getInstance().splitTimeStopWatch(); String
			 * elapsedTime = LogProgress.getInstance().elapsedTime();
			 * System.out.println(elapsedTime);
			 * 
			 * //SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
			 * 
			 * //Date d = formatter.parse(elapsedTime); //if(d.getMinutes()>2)
			 * //break;
			 */}

		// ---------------------------------------------------------------------
		LogProgress.getInstance().splitTimeStopWatch();

		// HACK: uses setChromosemes() to set the m_changed for population to
		// TRUE,
		// in order to calculate fittest chromosome from scratch
		population.getPopulation().setChromosomes(
				population.getPopulation().getChromosomes());

		IChromosome bestSolutionSoFar = population.getFittestChromosome();
		System.out.println("The best solution has a fitness value of "
				+ bestSolutionSoFar.getFitnessValue());
		LogProgress.getInstance().log(
				"The best solution fitness value:,"
						+ bestSolutionSoFar.getFitnessValue());
		LogProgress.getInstance().log("Best Solution:");
		LogProgress.getInstance().log(bestSolutionSoFar.getGenes());
		// LogProgress.getInstance().log("Execution time:,"+LogProgress.getInstance().elapsedTime());

		LogProgress.getInstance().recordStatistics();

		LogProgress.getInstance().closeLog();

		Problem bestWhiboAlgorithm = myFunc.createProblem(bestSolutionSoFar);

		/*
		 * String algorithmPath = "D:\\ea_"; Date now = new Date();
		 * algorithmPath = algorithmPath + String.valueOf(now.getMinutes())+
		 * "-"+String.valueOf(now.getSeconds()); algorithmPath = algorithmPath +
		 * ".wba"; ProblemEncoder.encodeFormProcesToXML(bestWhiboAlgorithm,
		 * algorithmPath);
		 */

		return bestWhiboAlgorithm;
	}

	public static boolean uniqueChromosomes(Population a_pop) {
		// Check that all chromosomes are unique
		for (int i = 0; i < a_pop.size() - 1; i++) {
			IChromosome c = a_pop.getChromosome(i);
			for (int j = i + 1; j < a_pop.size(); j++) {
				IChromosome c2 = a_pop.getChromosome(j);
				if (c == c2) {
					return false;
				}
			}
		}
		return true;
	}
}
