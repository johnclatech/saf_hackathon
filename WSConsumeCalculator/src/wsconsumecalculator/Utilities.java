/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wsconsumecalculator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author jkaru
 */
public class Utilities {

    public HashMap ParseXml(String xmlString) {
        HashMap<String, String> hm = new HashMap();
        try {

            hm.clear();
            InputStream file = new ByteArrayInputStream(xmlString.getBytes(Charset.defaultCharset()));
            if (!xmlString.contains("ERR@")) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();
                NodeList nodes = doc.getElementsByTagName("*");
                Node node;
                int nodeCount = nodes.getLength();
                for (int i = 0; i < nodeCount; i++) {
                    node = nodes.item(i);
                    if (!"message".equals(node.getNodeName()) && node.getFirstChild() != null) {
                        hm.put(node.getNodeName().replace("field", ""), node.getFirstChild().getNodeValue());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return hm;
    }

}
