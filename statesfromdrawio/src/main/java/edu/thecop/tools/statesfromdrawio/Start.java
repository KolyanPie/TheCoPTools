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

    private static CodeCreator codeCreator;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(help);
            System.exit(0);
        }
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(args[0]);
            codeCreator = new CodeCreator(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String dir = args[0].substring(0, args[0].length() - 4);
        try {
            String[] split = dir.split("/");
            codeCreator.create("ru.edu.cop.model." + split[split.length - 1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            codeCreator.write(new File(dir));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
