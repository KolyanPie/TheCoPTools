package edu.thecop.tools.statesfromdrawio;

import edu.thecop.tools.statesfromdrawio.code.CodeCreator;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Start {
    private final static String help;

    static {
        help = "arg filename(file from text in drawio Extras/Edit diagram...)";
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(help);
            System.exit(0);
        }
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(args[0]);
            CodeCreator codeCreator = new CodeCreator(document);
            codeCreator.create(new File(args[0].substring(0, args[0].length() - 4)));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

    }
}
