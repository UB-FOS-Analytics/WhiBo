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
package rs.fon.whibo.GC.Tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import rs.fon.whibo.GC.clusterModel.WhiBoCentroid;
import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;
import rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.clustering.CentroidClusterModel;
import com.rapidminer.operator.clustering.Cluster;

//import com.rapidminer.operator.clustering.Centroid;

/**
 * The Class Tools.
 */
public class WhiBoTools {

	public WhiBoTools() {

	}

	/**
	 * Gets the cluster number of the example.
	 * 
	 * @param example
	 *            the example
	 * 
	 * @return the cluster number
	 */
	public int getClusterNumber(Example example) {

		return Integer.parseInt(example.getNominalValue(
				example.getAttributes().getCluster()).substring(8, 9));
	}

	/**
	 * Equal coordinates.
	 * 
	 * @param c1
	 *            the c1
	 * @param c2
	 *            the c2
	 * 
	 * @return true, if successful
	 */
	public boolean equalCoordinates(WhiBoCentroid c1, WhiBoCentroid c2) {
		double[] c1Coordinates = c1.getCentroid();
		double[] c2Coordinates = c2.getCentroid();
		int l = c1Coordinates.length;
		for (int i = 0; i < l; i++) {
			if (c1Coordinates[i] == c2Coordinates[i])
				return false;
		}

		return true;
	}

	/**
	 * Gets the as double array.
	 * 
	 * @param example
	 *            the example
	 * @param attributes
	 *            the attributes
	 * 
	 * @return the as double array
	 */
	public double[] getAsDoubleArray(Example example, Attributes attributes) {
		double[] values = new double[attributes.size()];
		int i = 0;
		for (Attribute attribute : attributes) {
			values[i] = example.getValue(attribute);
			i++;
		}
		return values;
	}

	// dodato milos
	// public JTable getDistanceTable(DistanceMeasure measure,
	// CentroidClusterModel model, ExampleSet exampleSet,int numberOfNearest)
	// {
	// JTable table=new JTable();
	//
	// for(int i=0;i<exampleSet.size()+1;i++)
	// {
	// table.addColumn(new TableColumn());
	// }
	// for (int i=0;i<exampleSet.size();i++)
	// {
	// Example ex=exampleSet.getExample(i);
	// //dodati red
	// table.setValueAt(ex.getId(), i, 0);
	// for(int j=0;j<exampleSet.size();j++)
	// {
	//
	// double distance = measure.calculateDistance(getAsDoubleArray(ex,
	// exampleSet.getAttributes()), getAsDoubleArray(ex,
	// exampleSet.getAttributes()));
	// table.setValueAt(distance, i, j+1);
	//
	// }
	// }
	// return table;
	// }
	// dodato sa gdta
	public static LinkedList<String> getAllCategories(ExampleSet exampleSet,
			Attribute attribute) {
		LinkedList<String> allCategoryList = new LinkedList<String>();

		Iterator<Example> reader = exampleSet.iterator();

		while (reader.hasNext()) {
			Example example = reader.next();
			String currentValue = example.getValueAsString(attribute);
			if (!allCategoryList.contains(currentValue))
				allCategoryList.add(currentValue);
		}

		return allCategoryList;
	}

	//
	public Object[][] getDistanceTable(DistanceMeasure measure,
			ExampleSet exampleSet, int numberOfNearest) {
		// pravim 2d niz
		Object[][] niz2d = new Object[exampleSet.size()][numberOfNearest * 2 + 1];

		for (int i = 0; i < exampleSet.size(); i++) {

			Example ex = exampleSet.getExample(i);
			// odredjujem id za svaki record
			niz2d[i][0] = ex.getId();

			for (int b = 1; b < numberOfNearest * 2; b = b + 2) {
				// inicijalizujem pocetni 2d niz
				niz2d[i][b] = Double.MAX_VALUE;
				niz2d[i][b + 1] = "aaa";
			}
			// pocetne vrednosti
			int trenutniMaxPolozaj = 1;
			double trenutniMaxVrednost = Double.MAX_VALUE;
			double maxZaPosleIspitivanje = 0;

			// fali jos sortiranjeeeeee
			for (int j = 0; j < exampleSet.size(); j++) {
				if (j != i) {
					double vr = Math.abs(measure.calculateDistance(
							getAsDoubleArray(ex, exampleSet.getAttributes()),
							getAsDoubleArray(exampleSet.getExample(j),
									exampleSet.getAttributes())));
					if (vr < trenutniMaxVrednost) {
						niz2d[i][trenutniMaxPolozaj] = vr;
						niz2d[i][trenutniMaxPolozaj + 1] = exampleSet
								.getExample(j).getId();
						// trenutniMaxPolozaj=trenutniMaxPolozaj+2;
						trenutniMaxVrednost = Double.MIN_VALUE;
						for (int a = 1; a < numberOfNearest * 2; a = a + 2) {

							if (Double.parseDouble(niz2d[i][a].toString()) > trenutniMaxVrednost) {

								trenutniMaxVrednost = Double
										.parseDouble(niz2d[i][a].toString());

								trenutniMaxPolozaj = a;

							}
						}
					}
				}
			}

			ArrayList<Double> a1 = new ArrayList<Double>();
			ArrayList a2 = new ArrayList();

			for (int pp = 1; pp < numberOfNearest * 2 + 1; pp = pp + 2) {
				a1.add(Double.parseDouble(niz2d[i][pp].toString()));
				a2.add(niz2d[i][pp + 1]);
			}

			int a = 0;
			// double pomP=Double.MAX_VALUE;
			String pomNaziv = "";
			int pomPromnljiva = 1;
			for (int r = 0; r < a1.size(); r++) {
				double pomP = Double.MAX_VALUE;

				for (int t = 0; t < a1.size(); t++) {
					if (!Double.isNaN((a1.get(t)))) {
						if (pomP > Double.parseDouble(a1.get(t).toString())) {
							pomP = Double.parseDouble(a1.get(t).toString());
							pomNaziv = a2.get(t).toString();
							a = t;
						}
					}
				}

				a1.set(a, Double.NaN);
				a2.set(a, "");
				niz2d[i][pomPromnljiva] = pomP;
				niz2d[i][pomPromnljiva + 1] = pomNaziv;
				pomPromnljiva = pomPromnljiva + 2;

			}
		}
		return niz2d;
	}

	public ExampleSet getExampleSetKontra(DistanceMeasure measure,
			ExampleSet exampleSet1, WhiBoCentroidClusterModel model) {
		// praviExample2
		ExampleSet exampleSet2 = (ExampleSet) exampleSet1.clone();

		Attributes nizA = exampleSet2.getAttributes();
		Attribute[] niz = nizA.createRegularAttributeArray();

		for (Example ex : exampleSet2) {
			for (Cluster c : model.getClusters()) {
				if (c.getClusterId() == model.getClusterIndexOfId(ex.getId())) {
					double[] nizKordinata = model.getCentroidCoordinates(c
							.getClusterId());

					for (int i = 0; i < niz.length; i++) {
						double vr = 2 * nizKordinata[i] - ex.getValue(niz[i]);
						ex.setValue(niz[i], vr);
					}
				}
			}
		}
		return exampleSet2;
	}

	public ExampleSet getExampleSetKontra(DistanceMeasure measure,
			ExampleSet exampleSet1, CentroidClusterModel model) {
		// praviExample2
		ExampleSet exampleSet2 = (ExampleSet) exampleSet1.clone();

		Attributes nizA = exampleSet2.getAttributes();
		Attribute[] niz = nizA.createRegularAttributeArray();

		for (Example ex : exampleSet2) {
			for (Cluster c : model.getClusters()) {
				if (c.getClusterId() == model.getClusterIndexOfId(ex.getId())) {
					double[] nizKordinata = model.getCentroidCoordinates(c
							.getClusterId());

					for (int i = 0; i < niz.length; i++) {
						double vr = 2 * nizKordinata[i] - ex.getValue(niz[i]);
						ex.setValue(niz[i], vr);
					}
				}
			}
		}
		return exampleSet2;
	}

	/*
	 * public double[] getMean(ExampleSet exampleSet) { int i =
	 * exampleSet.size(); }
	 */
	public Object[][] getDistanceTableZaSil(DistanceMeasure measure,
			ExampleSet exampleSetNormalan, ExampleSet exampleSetKontra,
			WhiBoCentroidClusterModel model, int numberOfNearest) {
		// praviExample2

		// pravim 2d niz
		Object[][] niz2d = new Object[exampleSetKontra.size()][numberOfNearest * 2 + 1];

		for (int i = 0; i < exampleSetKontra.size(); i++) {

			Example ex = exampleSetKontra.getExample(i);
			// odredjujem id za svaki record
			niz2d[i][0] = ex.getId();

			for (int b = 1; b < numberOfNearest * 2; b = b + 2) {
				// inicijalizujem pocetni 2d niz
				niz2d[i][b] = Double.MAX_VALUE;
				niz2d[i][b + 1] = "aaa";
			}
			// pocetne vrednosti
			int trenutniMaxPolozaj = 1;
			double trenutniMaxVrednost = Double.MAX_VALUE;
			double maxZaPosleIspitivanje = 0;

			// fali jos sortiranjeeeeee
			for (int j = 0; j < exampleSetNormalan.size(); j++) {
				if (j != i) {
					double vr = Math.abs(measure.calculateDistance(
							getAsDoubleArray(ex,
									exampleSetKontra.getAttributes()),
							getAsDoubleArray(exampleSetNormalan.getExample(j),
									exampleSetNormalan.getAttributes())));
					if (vr < trenutniMaxVrednost) {
						niz2d[i][trenutniMaxPolozaj] = vr;
						niz2d[i][trenutniMaxPolozaj + 1] = exampleSetNormalan
								.getExample(j).getId();
						// trenutniMaxPolozaj=trenutniMaxPolozaj+2;
						trenutniMaxVrednost = Double.MIN_VALUE;
						for (int a = 1; a < numberOfNearest * 2; a = a + 2) {

							if (Double.parseDouble(niz2d[i][a].toString()) > trenutniMaxVrednost) {

								trenutniMaxVrednost = Double
										.parseDouble(niz2d[i][a].toString());

								trenutniMaxPolozaj = a;

							}
						}
					}
				}
			}

			ArrayList<Double> a1 = new ArrayList<Double>();
			ArrayList a2 = new ArrayList();

			for (int pp = 1; pp < numberOfNearest * 2 + 1; pp = pp + 2) {
				a1.add(Double.parseDouble(niz2d[i][pp].toString()));
				a2.add(niz2d[i][pp + 1]);
			}

			int a = 0;
			// double pomP=Double.MAX_VALUE;
			String pomNaziv = "";
			int pomPromnljiva = 1;
			for (int r = 0; r < a1.size(); r++) {
				double pomP = Double.MAX_VALUE;

				for (int t = 0; t < a1.size(); t++) {
					if (!Double.isNaN((a1.get(t)))) {
						if (pomP > Double.parseDouble(a1.get(t).toString())) {
							pomP = Double.parseDouble(a1.get(t).toString());
							pomNaziv = a2.get(t).toString();
							a = t;
						}
					}
				}

				a1.set(a, Double.NaN);
				a2.set(a, "");
				niz2d[i][pomPromnljiva] = pomP;
				niz2d[i][pomPromnljiva + 1] = pomNaziv;
				pomPromnljiva = pomPromnljiva + 2;

			}
		}
		return niz2d;
	}

	public Object[][] getDistanceTableZaSil(DistanceMeasure measure,
			ExampleSet exampleSetNormalan, ExampleSet exampleSetKontra,
			CentroidClusterModel model, int numberOfNearest) {
		// praviExample2

		// pravim 2d niz
		Object[][] niz2d = new Object[exampleSetKontra.size()][numberOfNearest * 2 + 1];

		for (int i = 0; i < exampleSetKontra.size(); i++) {

			Example ex = exampleSetKontra.getExample(i);
			// odredjujem id za svaki record
			niz2d[i][0] = ex.getId();

			for (int b = 1; b < numberOfNearest * 2; b = b + 2) {
				// inicijalizujem pocetni 2d niz
				niz2d[i][b] = Double.MAX_VALUE;
				niz2d[i][b + 1] = "aaa";
			}
			// pocetne vrednosti
			int trenutniMaxPolozaj = 1;
			double trenutniMaxVrednost = Double.MAX_VALUE;
			double maxZaPosleIspitivanje = 0;

			// fali jos sortiranjeeeeee
			for (int j = 0; j < exampleSetNormalan.size(); j++) {
				if (j != i) {
					double vr = Math.abs(measure.calculateDistance(
							getAsDoubleArray(ex,
									exampleSetKontra.getAttributes()),
							getAsDoubleArray(exampleSetNormalan.getExample(j),
									exampleSetNormalan.getAttributes())));
					if (vr < trenutniMaxVrednost) {
						niz2d[i][trenutniMaxPolozaj] = vr;
						niz2d[i][trenutniMaxPolozaj + 1] = exampleSetNormalan
								.getExample(j).getId();
						// trenutniMaxPolozaj=trenutniMaxPolozaj+2;
						trenutniMaxVrednost = Double.MIN_VALUE;
						for (int a = 1; a < numberOfNearest * 2; a = a + 2) {

							if (Double.parseDouble(niz2d[i][a].toString()) > trenutniMaxVrednost) {

								trenutniMaxVrednost = Double
										.parseDouble(niz2d[i][a].toString());

								trenutniMaxPolozaj = a;

							}
						}
					}
				}
			}

			ArrayList<Double> a1 = new ArrayList<Double>();
			ArrayList a2 = new ArrayList();

			for (int pp = 1; pp < numberOfNearest * 2 + 1; pp = pp + 2) {
				a1.add(Double.parseDouble(niz2d[i][pp].toString()));
				a2.add(niz2d[i][pp + 1]);
			}

			int a = 0;
			// double pomP=Double.MAX_VALUE;
			String pomNaziv = "";
			int pomPromnljiva = 1;
			for (int r = 0; r < a1.size(); r++) {
				double pomP = Double.MAX_VALUE;

				for (int t = 0; t < a1.size(); t++) {
					if (!Double.isNaN((a1.get(t)))) {
						if (pomP > Double.parseDouble(a1.get(t).toString())) {
							pomP = Double.parseDouble(a1.get(t).toString());
							pomNaziv = a2.get(t).toString();
							a = t;
						}
					}
				}

				a1.set(a, Double.NaN);
				a2.set(a, "");
				niz2d[i][pomPromnljiva] = pomP;
				niz2d[i][pomPromnljiva + 1] = pomNaziv;
				pomPromnljiva = pomPromnljiva + 2;

			}
		}
		return niz2d;
	}
}
