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
 * The Class BIC.
 */
public class BIC { // extends AbstractEvaluation {

	// /** The tools. */
	// WhiBoTools tools=new WhiBoTools();
	//
	// /**
	// * Instantiates a new bIC.
	// *
	// * @param parameters the parameters
	// */
	// public BIC(List<SubproblemParameter> parameters) {
	// super(parameters);
	//
	// }
	//
	//
	// /* (non-Javadoc)
	// * @see
	// rs.fon.whibo.GC.component.Evaluation.AbstractEvaluation#Evaluate(rs.fon.whibo.GC.component.DistanceMeasure.DistanceMeasure,
	// rs.fon.whibo.GC.clusterModel.CentroidClusterModel,
	// com.rapidminer.example.ExampleSet)
	// */
	//
	// @Override
	// public double Evaluate(DistanceMeasure measure, WhiBoCentroidClusterModel
	// model, ExampleSet exampleSet)
	// {
	// WhiBoTools tools = new WhiBoTools();
	// double bic=0;
	// double Ejk=0;
	// double sigmeRacun=0;
	// double eta=0;
	// double mj=0;
	//
	//
	//
	// Attributes at=exampleSet.getAttributes();
	// Attribute []nizAtr=at.createRegularAttributeArray();
	//
	//
	// ArrayList<Attribute> nizNominal=new ArrayList<Attribute>();
	// ArrayList<Attribute> nizNumerical=new ArrayList<Attribute>();
	//
	// for(int i=0;i<nizAtr.length;i++)
	// {
	// if(nizAtr[i].isNominal())
	// {
	// nizNominal.add(nizAtr[i]);
	// }
	// else
	// {
	// nizNumerical.add(nizAtr[i]);
	// }
	// }
	// Object[][] nizNumerical2Ceo=new
	// Object[exampleSet.size()][nizNumerical.size()+1];
	//
	// int brojac1=0;
	// for(Example ex1:exampleSet)
	// {
	// nizNumerical2Ceo[brojac1][0]=ex1.getId();
	// for(int i=0;i<nizNumerical.size();i++)
	// {
	// nizNumerical2Ceo[brojac1][i+1]=ex1.getNumericalValue(nizNumerical.get(i));
	// }
	// brojac1++;
	// }
	//
	// Double[] nizSigme=new Double[nizNumerical.size()];
	// Variance v=new Variance();
	//
	// for(int i=0;i<nizNumerical.size();i++)
	// {
	// double[] pomocniDouble=new double[exampleSet.size()];
	//
	// for(int j=0;j<exampleSet.size();j++)
	// {
	// pomocniDouble[j]=(Double)nizNumerical2Ceo[j][i+1];
	// }
	//
	// nizSigme[i]=v.evaluate(pomocniDouble);
	// }
	//
	// for(Cluster c1:model.getClusters())
	// {
	// int brojacNom=0;
	// int brojacNum=0;
	// Object[][] nizNumerical2=new
	// Object[c1.getNumberOfExamples()][nizAtr.length+1];
	// Object[][] nizNominal2=new
	// Object[c1.getNumberOfExamples()][nizAtr.length+1];
	// for(Example ex1:exampleSet)
	// {
	//
	// if(c1.getClusterId()==model.getClusterIndexOfId(ex1.getId()))
	// {
	// nizNominal2[brojacNom][0]=ex1.getId();
	// nizNumerical2[brojacNum][0]=ex1.getId();
	//
	// for(int i=0;i<nizNominal.size();i++)
	// {
	// nizNominal2[brojacNom][i+1]=ex1.getNominalValue(nizNominal.get(i));
	//
	// if(i==nizNominal.size()-1)
	// {
	// brojacNom++;
	// }
	// }
	// for(int i=0;i<nizNumerical.size();i++)
	// {
	// nizNumerical2[brojacNum][i+1]=ex1.getNumericalValue(nizNumerical.get(i));
	//
	// if(i==nizNumerical.size()-1)
	// {
	// brojacNum++;
	// }
	// }
	//
	//
	// }
	// }
	//
	// for(int i=0;i<nizNominal.size();i++)
	// {
	// LinkedList<String> ll=WhiBoTools.getAllCategories(exampleSet,
	// nizNominal.get(i));
	//
	// for(int i1=0;i1<ll.size();i1++)
	// {
	// int counterPoKategoriji=0;
	// for(int j1=0;j1<nizNominal2.length;j1++)
	// {
	// if(ll.get(i1)==nizNominal2[j1][i+1])
	// {
	// counterPoKategoriji++;
	// }
	// }
	// Ejk+=-(counterPoKategoriji/nizNominal2.length*Math.log(counterPoKategoriji/nizNominal2.length));
	// }
	// }
	//
	//
	// for(int i=0;i<nizNumerical.size();i++)
	// {
	// double[] pomocniDouble=new double[exampleSet.size()];
	// for(int j=0;j<c1.getNumberOfExamples()-1;j++)
	// {
	//
	// pomocniDouble[j]=(Double)nizNumerical2[j][i+1];
	//
	// }
	// double sigma=v.evaluate(pomocniDouble);
	//
	// sigmeRacun+=1/2*Math.log(sigma*sigma+nizSigme[i]*nizSigme[i]);
	// }
	//
	// eta=-nizNominal2.length*(sigmeRacun+Ejk);
	//
	//
	// for(int i=0;i<nizNominal.size();i++)
	// {
	// mj+=WhiBoTools.getAllCategories(exampleSet, nizNominal.get(i)).size()-1;
	// }
	// mj=(mj+2*nizNumerical.size())*model.getNumberOfClusters();
	//
	//
	// bic=bic+sigmeRacun+mj*Math.log(exampleSet.size());
	// }
	// bic=-2*bic;
	//
	// return bic;
	// }
	//
	// /* (non-Javadoc)
	// * @see
	// rs.fon.whibo.GC.component.Evaluation.AbstractEvaluation#isBetter(double,
	// double)
	// */
	//
	// @Override
	// public double Evaluate(DistanceMeasure measure, CentroidClusterModel
	// model, ExampleSet exampleSet)
	// {
	// WhiBoTools tools = new WhiBoTools();
	// double bic=0;
	// double Ejk=0;
	// double sigmeRacun=0;
	// double eta=0;
	// double mj=0;
	//
	//
	//
	// Attributes at=exampleSet.getAttributes();
	// Attribute []nizAtr=at.createRegularAttributeArray();
	//
	//
	// ArrayList<Attribute> nizNominal=new ArrayList<Attribute>();
	// ArrayList<Attribute> nizNumerical=new ArrayList<Attribute>();
	//
	// for(int i=0;i<nizAtr.length;i++)
	// {
	// if(nizAtr[i].isNominal())
	// {
	// nizNominal.add(nizAtr[i]);
	// }
	// else
	// {
	// nizNumerical.add(nizAtr[i]);
	// }
	// }
	// Object[][] nizNumerical2Ceo=new
	// Object[exampleSet.size()][nizNumerical.size()+1];
	//
	// int brojac1=0;
	// for(Example ex1:exampleSet)
	// {
	// nizNumerical2Ceo[brojac1][0]=ex1.getId();
	// for(int i=0;i<nizNumerical.size();i++)
	// {
	// nizNumerical2Ceo[brojac1][i+1]=ex1.getNumericalValue(nizNumerical.get(i));
	// }
	// brojac1++;
	// }
	//
	// Double[] nizSigme=new Double[nizNumerical.size()];
	// Variance v=new Variance();
	//
	// for(int i=0;i<nizNumerical.size();i++)
	// {
	// double[] pomocniDouble=new double[exampleSet.size()];
	//
	// for(int j=0;j<exampleSet.size();j++)
	// {
	// pomocniDouble[j]=(Double)nizNumerical2Ceo[j][i+1];
	// }
	//
	// nizSigme[i]=v.evaluate(pomocniDouble);
	// }
	//
	// for(Cluster c1:model.getClusters())
	// {
	// int brojacNom=0;
	// int brojacNum=0;
	// Object[][] nizNumerical2=new
	// Object[c1.getNumberOfExamples()][nizAtr.length+1];
	// Object[][] nizNominal2=new
	// Object[c1.getNumberOfExamples()][nizAtr.length+1];
	// for(Example ex1:exampleSet)
	// {
	//
	// String clustId = "cluster_"+c1.getClusterId();
	//
	// if(clustId.equals(ex1.getNominalValue(ex1.getAttributes().get("cluster"))))
	// {
	// nizNominal2[brojacNom][0]=ex1.getId();
	// nizNumerical2[brojacNum][0]=ex1.getId();
	//
	// for(int i=0;i<nizNominal.size();i++)
	// {
	// nizNominal2[brojacNom][i+1]=ex1.getNominalValue(nizNominal.get(i));
	//
	// if(i==nizNominal.size()-1)
	// {
	// brojacNom++;
	// }
	// }
	// for(int i=0;i<nizNumerical.size();i++)
	// {
	// nizNumerical2[brojacNum][i+1]=ex1.getNumericalValue(nizNumerical.get(i));
	//
	// if(i==nizNumerical.size()-1)
	// {
	// brojacNum++;
	// }
	// }
	//
	//
	// }
	// }
	//
	// for(int i=0;i<nizNominal.size();i++)
	// {
	// LinkedList<String> ll=WhiBoTools.getAllCategories(exampleSet,
	// nizNominal.get(i));
	//
	// for(int i1=0;i1<ll.size();i1++)
	// {
	// int counterPoKategoriji=0;
	// for(int j1=0;j1<nizNominal2.length;j1++)
	// {
	// if(ll.get(i1)==nizNominal2[j1][i+1])
	// {
	// counterPoKategoriji++;
	// }
	// }
	// Ejk+=-(counterPoKategoriji/nizNominal2.length*Math.log(counterPoKategoriji/nizNominal2.length));
	// }
	// }
	//
	//
	// for(int i=0;i<nizNumerical.size();i++)
	// {
	// double[] pomocniDouble=new double[exampleSet.size()];
	// for(int j=0;j<c1.getNumberOfExamples()-1;j++)
	// {
	//
	// pomocniDouble[j]=(Double)nizNumerical2[j][i+1];
	//
	// }
	// double sigma=v.evaluate(pomocniDouble);
	//
	// sigmeRacun+=1/2*Math.log(sigma*sigma+nizSigme[i]*nizSigme[i]);
	// }
	//
	// eta=-nizNominal2.length*(sigmeRacun+Ejk);
	//
	//
	// for(int i=0;i<nizNominal.size();i++)
	// {
	// mj+=WhiBoTools.getAllCategories(exampleSet, nizNominal.get(i)).size()-1;
	// }
	// mj=(mj+2*nizNumerical.size())*model.getNumberOfClusters();
	//
	//
	// bic=bic+sigmeRacun+mj*Math.log(exampleSet.size());
	// }
	// bic=-2*bic;
	//
	// return bic;
	// }
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
	// return Double.MAX_VALUE;
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