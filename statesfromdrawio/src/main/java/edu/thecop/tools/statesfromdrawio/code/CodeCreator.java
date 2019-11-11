package edu.thecop.tools.statesfromdrawio.code;

import edu.thecop.tools.statesfromdrawio.diagram.ElementMap;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

public class CodeCreator {
    public CodeCreator(Document document) {
        ElementMap elementMap = new ElementMap();
        Node root = document.getDocumentElement();
        NodeList list = root.getChildNodes();
        root = list.item(1);
        list = root.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node item = list.item(i);
            if (item.hasAttributes() && item.getAttributes().getNamedItem("style") != null) {
                elementMap.add(item);
            }
        }
        elementMap.calculate();
        elementMap.fixFirstLetter();
    }

    public void create(File directory) {
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                throw new RuntimeException("can't create directory " + directory.getAbsolutePath());
            }
        }

    }
}
