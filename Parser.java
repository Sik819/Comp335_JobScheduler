
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser {
	AllServers listServers = new AllServers();
    static String[] args;

    public Parser() {
        args = null;
    }

    public Parser(String[] args2) {
        args = args2;
    }

    public void parse() throws ParserConfigurationException,
            SAXException, IOException {


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Load the input XML document, parse it and return an instance of the
        // Document class.
        Document document = builder.parse(new File(args[0]));



        NodeList systems = document.getElementsByTagName("system");
        for (int i = 0; i < systems.getLength(); i++) {
            NodeList servers= ((Element)systems.item(i)).getElementsByTagName("servers");
            NodeList ho= ((Element)systems.item(i)).getElementsByTagName("server");
            for(int  j = 0 ; j < ho.getLength(); j++) {
                Element server = (Element) ho.item(j);
                String type = server.getAttribute("type");
                String limit = server.getAttribute("limit");
                int l = Integer.parseInt(limit);
                String bootupTime = server.getAttribute("bootupTime");
                int b = Integer.parseInt(bootupTime);
                String rate = server.getAttribute("rate");
                float r = Float.parseFloat(rate);
                String coreCount = server.getAttribute("coreCount");
                int c = Integer.parseInt(coreCount);
                String memory = server.getAttribute("memory");
                int m = Integer.parseInt(memory);
                String disk = server.getAttribute("disk");
                int d = Integer.parseInt(disk);
                listServers.addIfNotExist(type, l, b, r, c, m, d);
                //System.out.println(" Server Type: " + type + " Limit: " + limit + " BootupTime: " + bootupTime + " rate: " + rate + " CoreCount: "+ coreCount + " Memory: "+ memory + " Disk: " + disk );
            }
        }
    }

}