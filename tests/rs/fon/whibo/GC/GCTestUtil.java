package rs.fon.whibo.GC;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import rs.fon.whibo.GC.clusterModel.WhiBoCentroid;
import rs.fon.whibo.GC.clusterModel.WhiBoCentroidClusterModel;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.SimpleAttributes;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.example.table.DataRowFactory;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.example.table.PolynominalMapping;
import com.rapidminer.operator.clustering.Cluster;
import com.rapidminer.tools.Ontology;

/**
 * @author s.velickovic@gmail.com
 */
public class GCTestUtil {
	private SimpleAttributes attributes;
	private Attribute outlookAttribute;
	private Attribute temperatureAttribute;
	private Attribute humidityAttribute;
	private Attribute windAttribute;
	private Attribute playAttribute;
	private Attribute idAttribute;
	private Attribute clusterAttribute;
	private ExampleSet exampleSet;
	private MemoryExampleTable memoryExampleTable;
	private ArrayList<Example> examples;
	private ArrayList<WhiBoCentroid> centroids;

	private Object[][] exampleSetData = new Object[][] {
			{ "sunny", 85.0, 85.0, "false", "no", 1.0, "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0" },
			{ "sunny", 80.0, 90.0, "true", "no", 2.0, "cluster_1", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_1", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0" },
			{ "overcast", 83.0, 78.0, "false", "yes", 3.0, "cluster_1",
					"cluster_1", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_1", "cluster_1",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0" },
			{ "rain", 70.0, 96.0, "false", "yes", 4.0, "cluster_1",
					"cluster_1", "cluster_1", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_1", "cluster_1",
					"cluster_1", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0" },
			{ "rain", 68.0, 80.0, "false", "yes", 5.0, "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0" },
			{ "rain", 65.0, 70.0, "true", "no", 6.0, "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_0",
					"cluster_0", "cluster_0" },
			{ "overcast", 64.0, 65.0, "true", "yes", 7.0, "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_0", "cluster_0", "cluster_0" },
			{ "sunny", 72.0, 95.0, "false", "no", 8.0, "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0" },
			{ "sunny", 69.0, 70.0, "false", "yes", 9.0, "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_2",
					"cluster_2", "cluster_2", "cluster_2", "cluster_2",
					"cluster_2", "cluster_2", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_2", "cluster_2",
					"cluster_2", "cluster_2", "cluster_2", "cluster_2",
					"cluster_0", "cluster_0", "cluster_0" },
			{ "rain", 75.0, 80.0, "false", "yes", 10.0, "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0" },
			{ "sunny", 75.0, 70.0, "true", "yes", 11.0, "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_2", "cluster_2", "cluster_2",
					"cluster_2", "cluster_2", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_2", "cluster_2", "cluster_2", "cluster_2",
					"cluster_0", "cluster_0", "cluster_0" },
			{ "overcast", 72.0, 90.0, "true", "yes", 12.0, "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_0", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0", "cluster_0" },
			{ "overcast", 81.0, 75.0, "false", "yes", 13.0, "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_0",
					"cluster_0", "cluster_2", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_2", "cluster_2",
					"cluster_0", "cluster_0", "cluster_0" },
			{ "rain", 71.0, 80.0, "true", "no", 14.0, "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_0",
					"cluster_0", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_1", "cluster_1",
					"cluster_1", "cluster_1", "cluster_0", "cluster_0",
					"cluster_0", "cluster_0" } };

	private double[][] centroidData = new double[][] {
			{ 0.5, 64.5, 67.5, 0.0 }, { 0.9, 75.7, 84.9, 0.7 },
			{ 2.0, 72.0, 70.0, 0.5 } };

	private double[][] clusterData = new double[][] { { 6.0, 7.0 },
			{ 1.0, 2.0, 3.0, 4.0, 5.0, 8.0, 10.0, 12.0, 13.0, 14.0 },
			{ 9.0, 11.0 } };
	private ArrayList<Cluster> clusters;

	public GCTestUtil() {
		this.attributes = createAttributes();
		attributes.setLabel(playAttribute);
		attributes.setId(idAttribute);
		attributes.setCluster(clusterAttribute);
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

		// id
		idAttribute = AttributeFactory
				.createAttribute("id", Ontology.NUMERICAL);
		idAttribute.setTableIndex(5);
		simpleAttributes.addRegular(idAttribute);

		// cluster
		clusterAttribute = AttributeFactory.createAttribute("cluster",
				Ontology.NOMINAL);
		HashMap<Integer, String> clusterMap = new HashMap<Integer, String>();
		clusterMap.put(0, "cluster_0");
		clusterMap.put(1, "cluster_1");
		clusterMap.put(2, "cluster_2");
		clusterAttribute.setMapping(new PolynominalMapping(clusterMap));
		clusterAttribute.setTableIndex(6);
		simpleAttributes.addRegular(clusterAttribute);

		return simpleAttributes;

	}

	private void initExampleSetData() {

		SimpleAttributes attributes = createAttributes();
		for (int i = 7; i < 38; i++) {
			Attribute attribute = AttributeFactory
					.createAttribute(Ontology.NOMINAL);
			HashMap<Integer, String> clusterMap = new HashMap<Integer, String>();
			clusterMap.put(0, "cluster_0");
			clusterMap.put(1, "cluster_1");
			clusterMap.put(2, "cluster_2");
			attribute.setMapping(new PolynominalMapping(clusterMap));
			attribute.setTableIndex(i);
			attributes.addRegular(attribute);
		}

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
			example.setId((Double) exampleSetData[i][5]);
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

	public WhiBoCentroidClusterModel createWhiBoCentroidClusterModel() {
		initClusterModelData();
		WhiBoCentroidClusterModel model = mock(WhiBoCentroidClusterModel.class);
		when(model.getAttributeNames()).thenReturn(
				new String[] { "Outlook", "Temperature", "Humidity", "Wind" });
		when(model.getNumberOfClusters()).thenReturn(this.clusters.size());
		when(model.getClusters()).thenReturn(this.clusters);

		for (int i = 0; i < this.centroids.size(); i++) {
			when(model.getCentroid(eq(i))).thenReturn(this.centroids.get(i));
			when(model.getCluster(eq(i))).thenReturn(this.clusters.get(i));
			when(model.getCentroidCoordinates(eq(i))).thenReturn(
					this.centroids.get(i).getCentroid());
		}

		for (int i = 0; i < exampleSetData.length; i++) {
			Object id = exampleSetData[i][5];
			when(model.getClusterIndexOfId(eq(id))).thenReturn(
					getClusterIndexOfId(id));
		}

		return model;
	}

	private int getClusterIndexOfId(Object id) {
		int index = 0;
		for (Cluster cluster : clusters) {
			if (cluster.containsExampleId(id))
				return index;
			index++;
		}
		return -1;

	}

	private void initClusterModelData() {

		this.centroids = new ArrayList<WhiBoCentroid>(centroidData.length);
		this.clusters = new ArrayList<Cluster>();

		for (int i = 0; i < centroidData.length; i++) {
			WhiBoCentroid centroid = new WhiBoCentroid(4);
			centroid.setCentroid(centroidData[i]);
			this.centroids.add(i, centroid);

			Cluster cluster = new Cluster(i);
			for (int j = 0; j < clusterData[i].length; j++) {
				cluster.assignExample(clusterData[i][j]);
			}

			this.clusters.add(i, cluster);
		}
	}
}
