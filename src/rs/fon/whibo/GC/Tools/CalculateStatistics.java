package rs.fon.whibo.GC.Tools;

import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;

public class CalculateStatistics {

	public double getIntraClusterDistance(DistanceMeasure measure,
			WhiBoCentroidClusterModel model, ExampleSet exampleSet) {

		double distanceSum = 0;
		int i = 0;
		for (Example example : exampleSet) {
			double distance = measure.calculateDistance(
					model.getCentroidCoordinates(i),
					getAsDoubleArray(example, exampleSet.getAttributes()));

			// da li se ovako radi za svaku meru?
			distanceSum += distance * distance;
			i++;
		}

		return distanceSum;
	}

	public double getMaxDistanceIndex(DistanceMeasure measure,
			WhiBoCentroidClusterModel model, ExampleSet exampleSet) {

		double maxDistanceSum = 0;
		int maxDistanceIndex = 0;

		int k = model.getNumberOfClusters();
		int exampleIndex = 0;
		for (Example example : exampleSet) {
			double distanceSum = 0;
			for (int l = 0; l < k; l++) {
				distanceSum += measure.calculateDistance(
						model.getCentroidCoordinates(l),
						getAsDoubleArray(example, exampleSet.getAttributes()));

			}
			if (distanceSum > maxDistanceSum) {
				maxDistanceSum = distanceSum;
				maxDistanceIndex = exampleIndex;
			}
			exampleIndex++;

		}
		return maxDistanceIndex;
	}

	// prebaciti u neke toolsove budzevina
	private double[] getAsDoubleArray(Example example, Attributes attributes) {
		double[] values = new double[attributes.size()];
		int i = 0;
		for (Attribute attribute : attributes) {
			values[i] = example.getValue(attribute);
			i++;
		}
		return values;
	}
}
