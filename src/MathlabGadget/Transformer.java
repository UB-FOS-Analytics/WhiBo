package MathlabGadget;

import java.util.Iterator;

import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.ExampleSet;

public class Transformer {

	public double[][] prepakujExample(ExampleSet es) {
		int numberOfExample = es.size();
		int numberOfAttribute = es.getExample(0).size();
		double[][] doubleNiz = new double[numberOfExample][numberOfAttribute];
		Attributes nizAttribute = es.getAttributes();

		for (int i = 0; i < numberOfExample; i++) {
			Iterator<Attribute> la = nizAttribute.allAttributes();
			for (int j = 0; j < numberOfAttribute; j++) {
				doubleNiz[i][j] = es.getExample(i).getNumericalValue(la.next());
			}
		}
		return doubleNiz;
	}

	public WhiBoCentroidClusterModel prepakujCentre(ExampleSet exampleSet,
			double[][] niz) {
		int brKlastera = niz.length;
		int brAtributa = niz[0].length;
		WhiBoCentroidClusterModel wcc = new WhiBoCentroidClusterModel(
				exampleSet, brKlastera, exampleSet.getAttributes());

		for (int i = 0; i < brKlastera; i++) {
			wcc.assignExample(i, niz[i]);
		}
		return wcc;
	}

}
