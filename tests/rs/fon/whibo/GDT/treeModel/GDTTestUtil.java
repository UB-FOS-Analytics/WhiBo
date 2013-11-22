package rs.fon.whibo.GDT.treeModel;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import rs.fon.whibo.GDT.tools.PruningTools;
import rs.fon.whibo.GDT.tools.TreeAnalysis;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.SimpleAttributes;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.example.table.DataRowFactory;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.example.table.PolynominalMapping;
import com.rapidminer.operator.learner.tree.Tree;
import com.rapidminer.tools.Ontology;

/**
 * @author s.velickovic@gmail.com
 */
public class GDTTestUtil {

	private Attribute outlookAttribute;
	private Attribute temperatureAttribute;
	private Attribute humidityAttribute;
	private Attribute windAttribute;
	private Attribute playAttribute;
	private SimpleAttributes attributes;

	private Object[][] exampleSetData = new Object[][] {
			{ "sunny", 85.0, 85.0, "false", "no" },
			{ "sunny", 80.0, 90.0, "true", "no" },
			{ "overcast", 83.0, 78.0, "false", "yes" },
			{ "rain", 70.0, 96.0, "false", "yes" },
			{ "rain", 68.0, 80.0, "false", "yes" },
			{ "rain", 65.0, 70.0, "true", "no" },
			{ "overcast", 64.0, 65.0, "true", "yes" },
			{ "sunny", 72.0, 95.0, "false", "no" },
			{ "sunny", 69.0, 70.0, "false", "yes" },
			{ "rain", 75.0, 80.0, "false", "yes" },
			{ "sunny", 75.0, 70.0, "true", "yes" },
			{ "overcast", 72.0, 90.0, "true", "yes" },
			{ "overcast", 81.0, 75.0, "false", "yes" },
			{ "rain", 71.0, 80.0, "true", "no" } };

	private MemoryExampleTable memoryExampleTable;
	private ArrayList<Example> examples;
	private ExampleSet exampleSet;

	public GDTTestUtil() {
		this.attributes = createAttributes();
		attributes.setLabel(playAttribute);
	}

	private SimpleAttributes createAttributes() {
		SimpleAttributes simpleAttributes = new SimpleAttributes();

		// outlook
		outlookAttribute = AttributeFactory.createAttribute("Outlook",
				Ontology.NOMINAL);
		HashMap<Integer, String> outlookMap = new HashMap<Integer, String>();
		outlookMap.put(0, "rain");
		outlookMap.put(1, "overcast");
		outlookMap.put(2, "sunny");
		outlookAttribute.setMapping(new PolynominalMapping(outlookMap));
		outlookAttribute.setTableIndex(0);
		simpleAttributes.addRegular(outlookAttribute);

		// temperature
		temperatureAttribute = AttributeFactory.createAttribute("Temperature",
				Ontology.NUMERICAL);
		temperatureAttribute.setTableIndex(1);
		simpleAttributes.addRegular(temperatureAttribute);

		// humidity
		humidityAttribute = AttributeFactory.createAttribute("Humidity",
				Ontology.NUMERICAL);
		humidityAttribute.setTableIndex(2);
		simpleAttributes.addRegular(humidityAttribute);

		// wind
		windAttribute = AttributeFactory.createAttribute("Wind",
				Ontology.NOMINAL);
		HashMap<Integer, String> windMap = new HashMap<Integer, String>();
		windMap.put(0, "true");
		windMap.put(1, "false");
		windAttribute.setMapping(new PolynominalMapping(windMap));
		windAttribute.setTableIndex(3);
		simpleAttributes.addRegular(windAttribute);

		// play
		playAttribute = AttributeFactory.createAttribute("Play",
				Ontology.NOMINAL);
		HashMap<Integer, String> playMap = new HashMap<Integer, String>();
		playMap.put(0, "no");
		playMap.put(1, "yes");
		playAttribute.setMapping(new PolynominalMapping(playMap));
		playAttribute.setTableIndex(4);
		simpleAttributes.addRegular(playAttribute);

		return simpleAttributes;

	}

	private void initExampleSetData() {

		SimpleAttributes attributes = createAttributes();

		memoryExampleTable = new MemoryExampleTable(
				attributes.createRegularAttributeArray());

		this.examples = new ArrayList<Example>(exampleSetData.length);
		DataRowFactory dataRowFactory = new DataRowFactory(
				DataRowFactory.TYPE_DOUBLE_ARRAY, '.');
		for (int i = 0; i < exampleSetData.length; i++) {
			DataRow dataRow = dataRowFactory.create(exampleSetData[i],
					attributes.createRegularAttributeArray());
			this.memoryExampleTable.addDataRow(dataRow);
			Example example = new Example(dataRow, this.exampleSet);
			// example.setId((Double) exampleSetData[i][5]);
			examples.add(i, example);

		}

	}

	public ExampleSet createExampleSet() {

		exampleSet = mock(ExampleSet.class);
		when(exampleSet.clone()).thenReturn(exampleSet);
		when(exampleSet.getAttributes()).thenReturn(this.attributes);
		initExampleSetData();
		when(exampleSet.size()).thenReturn(examples.size());
		when(exampleSet.iterator()).thenAnswer(new Answer<Iterator<Example>>() {
			@Override
			public Iterator<Example> answer(InvocationOnMock invocationOnMock)
					throws Throwable {
				return examples.iterator();
			}
		});
		when(exampleSet.getExampleTable()).thenReturn(this.memoryExampleTable);

		for (int i = 0; i < this.examples.size(); i++) {
			when(exampleSet.getExample(eq(i))).thenReturn(this.examples.get(i));
		}

		exampleSet.getExampleTable();
		return exampleSet;
	}

	public LinkedList<Attribute> getAttributesForSplitting() {
		LinkedList<Attribute> attributesForSplitting = new LinkedList<Attribute>();
		// temperature
		Attribute temperatureAttribute = AttributeFactory.createAttribute(
				"Temperature", Ontology.NUMERICAL);
		temperatureAttribute.setTableIndex(1);
		attributesForSplitting.add(temperatureAttribute);

		// humidity
		Attribute humidityAttribute = AttributeFactory.createAttribute(
				"Humidity", Ontology.NUMERICAL);
		humidityAttribute.setTableIndex(2);
		attributesForSplitting.add(humidityAttribute);
		return attributesForSplitting;
	}

	public Tree createTreeModelForC45() {
		File file;
		ObjectInputStream in;
		try {
			file = new File(".\\resources\\tests\\C4.5 result.data");
			in = new ObjectInputStream(new FileInputStream(file));

			Tree result = (Tree) in.readObject();

			return result;
		} catch (Exception e) {
			return null;
		} finally {
			// in.close();
		}
	}

	public Tree createTreeModelForID3() {
		File file;
		ObjectInputStream in;
		try {
			file = new File(".\\resources\\tests\\ID3 result.data");
			in = new ObjectInputStream(new FileInputStream(file));

			Tree result = (Tree) in.readObject();

			return result;
		} catch (Exception e) {
			return null;
		} finally {
			// in.close();
		}
	}

	public ExampleSet createGolfExampleSet() {
		File file;
		ObjectInputStream in;
		try {
			file = new File(".\\resources\\tests\\Golf dataset.data");
			in = new ObjectInputStream(new FileInputStream(file));

			ExampleSet result = (ExampleSet) in.readObject();

			return result;
		} catch (Exception e) {
			return null;
		} finally {
			// in.close();
		}
	}

	public boolean equalTrees(Tree tree1, Tree tree2, ExampleSet dataset) {

		TreeAnalysis ta1 = new TreeAnalysis();
		TreeAnalysis ta2 = new TreeAnalysis();

		PruningTools.recalculateTreeNodesStatistics(tree1, dataset);
		PruningTools.recalculateTreeNodesStatistics(tree2, dataset);

		ta1.analyseTree(tree1, 0);
		ta2.analyseTree(tree2, 0);

		if (ta1.getMaxTreeDepth() != ta2.getMaxTreeDepth())
			return false;
		if (ta1.getNumberOfLeaves() != ta2.getNumberOfLeaves())
			return false;
		if (ta1.getNumberOfNodes() != ta2.getNumberOfNodes())
			return false;
		if (ta1.getWeightedAverageTreeDepth() != ta2
				.getWeightedAverageTreeDepth())
			return false;

		return true;

	}
}
