package com.suma.mid.test3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParseXML {
	private DocumentBuilderFactory builderFactory = DocumentBuilderFactory
			.newInstance(); // factory
	
	public void readXML(String filePath){   // Xpath方式查找节点
		try {
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
//			InputStream inputStream = ParseXML.class.getClassLoader().getResourceAsStream(filePath);
			Document dom = builder.parse(new File(filePath));
//			Document dom = builder.parse(inputStream);
			
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
//			XPathExpression xpaheExpression = xPath.compile("Autodiscover/Response/Account/Protocol[@name='pop']/Server");
			XPathExpression xpaheExpression = xPath.compile("Autodiscover/Response/Account/Protocol[2]/Server");
			NodeList nodeList = (NodeList) xpaheExpression.evaluate(dom, XPathConstants.NODESET);
			if(nodeList == null || nodeList.getLength() == 0) return;
			for(int i = 0; i < nodeList.getLength(); i++){
				Node node = nodeList.item(i);
				System.out.println(node.getNodeName()+": "+" "+ node.getTextContent());
			}
		
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
	}
	
	public void writeXML(){   //dom4j write xml
		org.dom4j.Document doc = DocumentHelper.createDocument();
		Element rootElement = doc.addElement("Root");
		rootElement.addComment("这是一个根节点");
		
		Element node1 = rootElement.addElement("Node1");
		node1.addAttribute("name", "nam1");
		node1.addAttribute("type", "str");
		node1.addComment("这是节点1");
		
		Element subNode1 = node1.addElement("SubNode1");
		subNode1.setText("subNode1");
		
		try {
			
			File xmlFile = new File("my.xml");
			if(xmlFile.exists()) xmlFile.delete();
			xmlFile.createNewFile();
			
			XMLWriter writer = new XMLWriter(new FileOutputStream(xmlFile));
			writer.write(doc);
			writer.flush();
			writer.close();
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
//		new ParseXML().readXML("YAHOO.SE.XML");
		new ParseXML().writeXML();
		
	}

}
