package edu.thecop.tools.statesfromdrawio.diagram;

import edu.thecop.tools.statesfromdrawio.diagram.elements.*;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ElementMap {
    private HashMap<String, LoopState> loopStates;
    private HashMap<String, LastingState> lastingStates;
    private Condition createdCondition;
    private List<Condition> conditions;
    private Element actor;

    {
        loopStates = new HashMap<>();
        lastingStates = new HashMap<>();
        conditions = new ArrayList<>();
    }

    public void add(Node node) {
        String[] styles = node.getAttributes().getNamedItem("style").getNodeValue().split(";");
        HashMap<String, String> styleMap = new HashMap<>(styles.length);
        Arrays.stream(styles).forEach((s -> {
            String[] split = s.split("=");
            styleMap.put(split[0], split[1]);
        }));
        String id = node.getAttributes().getNamedItem("id").getNodeValue();
        String[] values = node.getAttributes().getNamedItem("value").getNodeValue().split("<br>");
        if (styleMap.containsKey("startArrow")) {
            String sourceId = node.getAttributes().getNamedItem("source").getNodeValue();
            String targetId = node.getAttributes().getNamedItem("target").getNodeValue();
            switch (styleMap.get("startArrow")) {
                case "oval":
                    for (String v : values) {
                        conditions.add(new BreakCondition(v.split(" ")[1], sourceId, targetId, Integer.parseInt(v.split(" ")[0])));
                    }
                    break;
                case "ERmany":
                    for (String v : values) {
                        if (v.equals("default")) {
                            conditions.add(new DefaultCondition(v, sourceId, targetId));
                        } else {
                            conditions.add(new NextCondition(v.split(" ")[1], sourceId, targetId, Integer.parseInt(v.split(" ")[0])));
                        }
                    }
                    break;
                default:
                    if (createdCondition != null) {
                        throw new RuntimeException("have few created conditions");
                    }
                    createdCondition = new Condition(values[0], sourceId, targetId);
                    break;
            }
        } else if (!styleMap.containsKey("shape")) {
            if (actor != null) {
                throw new RuntimeException("have few actor");
            }
            actor = new Element(values[0]);
        } else if (styleMap.get("rounded").equals("0")) {
            lastingStates.put(id, new LastingState(values[0], values[1]));
        } else {
            loopStates.put(id, new LoopState(values[0]));
        }
    }

    public void calculate() {
        setTarget(createdCondition);
        for (Condition condition : conditions) {
            setTarget(condition);
        }
        lastingStates.forEach((id, state) -> {
            for (Condition condition : conditions) {
                if (id.equals(condition.getSourceId())) {
                    if (condition instanceof DefaultCondition) {
                        state.setDefaultCondition((DefaultCondition) condition);
                    } else if (condition instanceof NextCondition) {
                        state.addNextCondition((NextCondition) condition);
                    } else if (condition instanceof BreakCondition) {
                        state.addBreakCondition((BreakCondition) condition);
                    } else {
                        throw new RuntimeException("WTF");
                    }
                }
            }
        });
        loopStates.forEach((id, state) -> {
            for (Condition condition : conditions) {
                if (id.equals(condition.getSourceId())) {
                    if (condition instanceof BreakCondition) {
                        state.addBreakCondition((BreakCondition) condition);
                    } else {
                        throw new RuntimeException(String.format("state %s have a condition %s", state.getValue(), condition.getValue()));
                    }
                }
            }
        });
    }

    private void setTarget(Condition condition) {
        if (loopStates.containsKey(condition.getTargetId())) {
            condition.setTarget(loopStates.get(condition.getTargetId()));
        } else if (lastingStates.containsKey(condition.getTargetId())) {
            condition.setTarget(lastingStates.get(condition.getTargetId()));
        } else {
            throw new RuntimeException(String.format("Condition %s have not target state", condition.getValue()));
        }
    }
}

