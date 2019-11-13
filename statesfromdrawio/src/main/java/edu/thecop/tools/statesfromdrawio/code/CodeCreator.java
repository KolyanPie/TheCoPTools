package edu.thecop.tools.statesfromdrawio.code;

import edu.thecop.tools.statesfromdrawio.diagram.DiagramException;
import edu.thecop.tools.statesfromdrawio.diagram.ElementMap;
import edu.thecop.tools.statesfromdrawio.diagram.elements.LastingState;
import edu.thecop.tools.statesfromdrawio.diagram.elements.LoopState;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeCreator {

    private final ElementMap elementMap;
    private List<FileElement> fileElements;

    public CodeCreator(Document document) {
        elementMap = new ElementMap();
        Node root;
        NodeList list;
        try {
            root = document.getDocumentElement();
            list = root.getChildNodes();
            root = list.item(1);
        } catch (Exception e) {
            throw new DiagramException("Bad xml file organization");
        }
        list = root.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node item = list.item(i);
            if (item.hasAttributes() && item.getAttributes().getNamedItem("style") != null) {
                elementMap.add(item);
            }
        }
        elementMap.calculate();
    }

    public void create(String packageS) {
        String actor = Templates.fixFirstLetter(false, elementMap.getActorName());
        fileElements = new ArrayList<>();
        fileElements.add(new ActorInterface(packageS, actor, elementMap.getConditionSet()));
        fileElements.add(new ActorStatesEnum(packageS, actor, elementMap.getLoopStates(), elementMap.getLastingStates()));
        fileElements.add(new ActorLastingState(packageS, actor, elementMap.getLastingStates()));
        for (LoopState loopState : elementMap.getLoopStates()) {
            fileElements.add(new ActorStateLoopState(packageS, actor, loopState));
        }
        for (LastingState lastingState : elementMap.getLastingStates()) {
            fileElements.add(new ActorStateLastingState(packageS, actor, lastingState));
        }
    }

    public void write(File directory) throws IOException {
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                throw new DiagramException("can't create directory " + directory.getAbsolutePath());
            }
        }
        ClassWriter classWriter = new ClassWriter(directory);
        for (FileElement fileElement : fileElements) {
            classWriter.writeFile(fileElement);
        }
    }
}
