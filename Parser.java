package vanilla_client;

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
    static String[] args;

    public Parser() {
        args = null;
    }

    public Parser(String[] args2) {
        args = args2;
    }

    public static void main() throws ParserConfigurationException,
            SAXException, IOException {

        if(args.length != 1)
            throw new RuntimeException("The name of the XML file is required!");
        else
            System.out.println("Running " + args[0]);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Load the input XML document, parse it and return an instance of the
        // Document class.
        Document document = builder.parse(new File(args[0]));



        NodeList systems = document.getElementsByTagName("system");
        for (int i = 0; i < systems.getLength(); i++) {
            NodeList servers= ((Element)systems.item(i)).getElementsByTagName("servers");
            NodeList ho= ((Element)systems.item(i)).getElementsByTagName("server");
            System.out.println(ho.getLength());
            for(int  j =Parser 0 ; j < ho.getLength(); j++) {
                Element server = (Element) ho.item(j);
                String type = server.getAttribute("type");
                String limit = server.getAttribute("limit");
                String bootupTime = server.getAttribute("bootupTime");
                String rate = server.getAttribute("rate");
                String coreCount = server.getAttribute("coreCount");
                String memory = server.getAttribute("memory");
                String disk = server.getAttribute("disk");
                System.out.println("Fuck 335");
                System.out.println("Type: " + type + " Limit: " + limit + " BootupTime: " + bootupTime + " rate: " + rate + " CoreCount: "+ coreCount + " Memory: "+ memory + " Disk: " + disk );
            }
        }
    }

}