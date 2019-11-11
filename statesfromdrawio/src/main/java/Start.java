import diagram.CodeCreator;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
            new CodeCreator(document);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

    }
}
