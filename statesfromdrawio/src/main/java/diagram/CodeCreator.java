package diagram;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
}
