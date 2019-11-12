package edu.thecop.tools.statesfromdrawio.diagram;

import edu.thecop.tools.statesfromdrawio.diagram.elements.*;
import org.w3c.dom.Node;

import java.util.*;
import java.util.stream.Collectors;

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
        String id = node.getAttributes().getNamedItem("id").getNodeValue();
        Node value = node.getAttributes().getNamedItem("value");
        value.setNodeValue(delDiv(value.getNodeValue()));
        String[] values = value.getNodeValue().split("\n");
        String[] styles = node.getAttributes().getNamedItem("style").getNodeValue().split(";");
        HashMap<String, String> styleMap = new HashMap<>(styles.length);
        Arrays.stream(styles).forEach((s -> {
            String[] split = s.split("=");
            if (split.length == 2) {
                styleMap.put(split[0], split[1]);
            }
        }));
        if (styleMap.containsKey("startArrow")) {
            String sourceId;
            String targetId;
            Node source = node.getAttributes().getNamedItem("source");
            Node target = node.getAttributes().getNamedItem("target");
            if (source == null) {
                if (target == null) {
                    throw new RuntimeException("Condition " + value.getNodeValue() + " have not source and target");
                }
                throw new RuntimeException("Condition " + value.getNodeValue() + " to " + target.getNodeValue() + " have not source");
            }
            if (target == null) {
                throw new RuntimeException("Condition " + value.getNodeValue() + " from " + source.getNodeValue() + " have not target");
            }
            sourceId = source.getNodeValue();
            targetId = target.getNodeValue();
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
        } else if (styleMap.containsKey("shape")) {
            if (actor != null) {
                throw new RuntimeException("have few actor");
            }
            actor = new Element(values[0]);
        } else if (styleMap.get("rounded").equals("0")) {
            lastingStates.put(id, new LastingState(values[0]));
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

    public String getActorName() {
        return actor.getValue();
    }

    public Set<String> getConditionSet() {
        return conditions.stream().map(Element::getValue).filter((s) -> !s.equals("default")).map((s) -> {
            if (s.charAt(0) == '!') {
                return s.substring(1);
            }
            return s;
        }).collect(Collectors.toSet());
    }

    public Collection<LoopState> getLoopStates() {
        return loopStates.values();
    }

    public Collection<LastingState> getLastingStates() {
        return lastingStates.values();
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

    private String delDiv(String value) {
        value = value.replace("<br>", "\n");
        value = value.replaceAll("<[^<>]*>", "");
        return value;
    }
}

