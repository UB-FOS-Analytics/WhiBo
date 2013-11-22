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
package rs.fon.whibo.GC.component.Evaluation.RCsInDevelopement;

// TODO: Auto-generated Javadoc
/**
 * The Class Symmetry.
 */
public class Symmetry { // extends AbstractEvaluation {

	// /** Parameter No_of nearest defines number nearest for check. */
	// @Parameter(defaultValue = "5", minValue = "1", maxValue = "1000")
	// private int No_Of_Nearest;
	//
	//
	//
	// /**
	// * Instantiates a new symmetry.
	// *
	// * @param parameters the parameters
	// */
	// public Symmetry(List<SubproblemParameter> parameters) {
	// super(parameters);
	//
	// No_Of_Nearest = Integer.parseInt(parameters.get(0).getXenteredValue());
	//
	// }
	//
	// /* (non-Javadoc)
	// * @see
	// rs.fon.whibo.GC.component.Evaluation.AbstractEvaluation#Evaluate(rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure,
	// rs.fon.whibo.GC.clusterModel.CentroidClusterModel,
	// com.rapidminer.example.ExampleSet)
	// */
	// @Override
	// public double Evaluate(DistanceMeasure measure, WhiBoCentroidClusterModel
	// model, ExampleSet exampleSet)
	// {
	// //
	// WhiBoTools tools = new WhiBoTools();
	// double sym=0;
	// double dps=0;
	// double Ei = 0;
	// double Ek = 0;
	// double dk=Double.MIN_VALUE;
	//
	// ExampleSet kontraExampleSet=tools.getExampleSetKontra(measure,
	// exampleSet, model);
	//
	// Object[][] niz2d=tools.getDistanceTableZaSil(measure,
	// exampleSet,kontraExampleSet, model, No_Of_Nearest);
	//
	// for(Cluster c:model.getClusters())
	// {
	// Collection<Object> niz1=c.getExampleIds();
	// Object[]niz =niz1.toArray();
	// for(int i=0;i<niz.length;i++)
	// {
	// Example ex1=kontraExampleSet.getExample(i);
	// double dpsPomocni=0;
	// for(int j=1;j<niz2d[0].length;j=j+2)
	// {
	// Example ex2=exampleSet.getExample(j);
	// dpsPomocni=dpsPomocni+measure.calculateDistance(tools.getAsDoubleArray(ex1,
	// ex1.getAttributes()),tools.getAsDoubleArray(ex2, ex2.getAttributes()));
	// }
	//
	// Ei=Ei+(dpsPomocni/(niz2d[0].length-1)/2*measure.calculateDistance(tools.getAsDoubleArray(ex1,
	// ex1.getAttributes()), model.getCentroidCoordinates(c.getClusterId())));
	// }
	// Ek=Ek+Ei;
	// }
	//
	//
	//
	// for(Cluster c1:model.getClusters())
	// {
	// for(Cluster c2:model.getClusters())
	// {
	// if(dk<measure.calculateDistance(model.getCentroidCoordinates(c1.getClusterId()),
	// model.getCentroidCoordinates(c2.getClusterId())))
	// {
	//
	// dk=measure.calculateDistance(model.getCentroidCoordinates(c1.getClusterId()),
	// model.getCentroidCoordinates(c2.getClusterId()));
	// }
	// }
	// }
	// sym=1/model.getNumberOfClusters()*1/Ek*dk;
	//
	//
	// return sym;
	// }
	//
	// @Override
	// public double Evaluate(DistanceMeasure measure, CentroidClusterModel
	// model, ExampleSet exampleSet)
	// {
	// //
	// WhiBoTools tools = new WhiBoTools();
	// double sym=0;
	// double dps=0;
	// double Ei = 0;
	// double Ek = 0;
	// double dk=Double.MIN_VALUE;
	//
	// ExampleSet kontraExampleSet=tools.getExampleSetKontra(measure,
	// exampleSet, model);
	//
	// Object[][] niz2d=tools.getDistanceTableZaSil(measure,
	// exampleSet,kontraExampleSet, model, No_Of_Nearest);
	//
	// for(Cluster c:model.getClusters())
	// {
	// Collection<Object> niz1=c.getExampleIds();
	// Object[]niz =niz1.toArray();
	// for(int i=0;i<niz.length;i++)
	// {
	// Example ex1=kontraExampleSet.getExample(i);
	// double dpsPomocni=0;
	// for(int j=1;j<niz2d[0].length;j=j+2)
	// {
	// Example ex2=exampleSet.getExample(j);
	// dpsPomocni=dpsPomocni+measure.calculateDistance(tools.getAsDoubleArray(ex1,
	// ex1.getAttributes()),tools.getAsDoubleArray(ex2, ex2.getAttributes()));
	// }
	//
	// Ei=Ei+(dpsPomocni/(niz2d[0].length-1)/2*measure.calculateDistance(tools.getAsDoubleArray(ex1,
	// ex1.getAttributes()), model.getCentroidCoordinates(c.getClusterId())));
	// }
	// Ek=Ek+Ei;
	// }
	//
	//
	//
	// for(Cluster c1:model.getClusters())
	// {
	// for(Cluster c2:model.getClusters())
	// {
	// if(dk<measure.calculateDistance(model.getCentroidCoordinates(c1.getClusterId()),
	// model.getCentroidCoordinates(c2.getClusterId())))
	// {
	//
	// dk=measure.calculateDistance(model.getCentroidCoordinates(c1.getClusterId()),
	// model.getCentroidCoordinates(c2.getClusterId()));
	// }
	// }
	// }
	// sym=1/model.getNumberOfClusters()*1/Ek*dk;
	//
	// return sym;
	// }
	//
	//
	//
	//
	// /* (non-Javadoc)
	// * @see
	// rs.fon.whibo.GC.component.Evaluation.AbstractEvaluation#isBetter(double,
	// double)
	// */
	// @Override
	// public boolean isBetter(double eval1, double eval2){
	// if (eval1>eval2)
	// return true;
	// else return false;
	// }
	//
	// /* (non-Javadoc)
	// * @see
	// rs.fon.whibo.GC.component.Evaluation.AbstractEvaluation#getWorstValue()
	// */
	// @Override
	// public double getWorstValue(){
	// return Double.MIN_VALUE;
	// }
	//
	// /**
	// * Gets collection of class names of components that are not compatible
	// with this component
	// *
	// * @return collection of class names of not compatible components
	// */
	// public String[] getNotCompatibleClassNames() {
	// return null;
	// }
	//
	// /**
	// * Gets collection of class names of components which are exclusive to
	// this component
	// *
	// * @return collection of class names of exclusive components
	// */
	// public String[] getExclusiveClassNames() {
	// return null;
	// }
}
