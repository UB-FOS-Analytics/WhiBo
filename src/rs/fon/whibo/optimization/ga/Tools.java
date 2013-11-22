package rs.fon.whibo.optimization.ga;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.Population;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Tools {

	public static void setXMLAttributeValue(String xmlFilePath, String xQuery,
			String value) throws Exception {

		File file = new File(xmlFilePath);

		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder.parse(file);

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expr = xpath.compile(xQuery);

		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		Node n = nodes.item(0);
		n.setNodeValue(value);

		// set up a transformer
		TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = transfac.newTransformer();

		// create string from xml tree
		StringWriter sw = new StringWriter();
		StreamResult streamResult = new StreamResult(sw);
		DOMSource source = new DOMSource(doc);
		trans.transform(source, streamResult);
		String xmlString = sw.toString();

		OutputStream f0;
		byte buf[] = xmlString.getBytes();
		f0 = new FileOutputStream(xmlFilePath);
		for (int i = 0; i < buf.length; i++) {
			f0.write(buf[i]);
		}
		f0.close();
		buf = null;

	}

	public static String chromosomeToString(IChromosome chromosome) {
		String result = "";
		for (Gene g : chromosome.getGenes()) {
			result = result + g.toString();
		}

		return result;
	}

	public static void clearChromosomFitnessValues(Population pop) {

		for (int i = 0; i < pop.size(); i++) {
			IChromosome chrom = pop.getChromosome(i);
			chrom.setFitnessValueDirectly(FitnessFunction.NO_FITNESS_VALUE);
		}
	}
}
