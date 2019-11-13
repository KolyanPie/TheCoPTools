package edu.thecop.tools.statesfromdrawio.diagram.elements;

import edu.thecop.tools.statesfromdrawio.diagram.DiagramException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class LastingState extends LoopState {
    private HashMap<Integer, NextCondition> nextConditions;
    private DefaultCondition defaultCondition;

    {
        nextConditions = new HashMap<>();
    }

    public LastingState(String value) {
        super(value);
    }

    public void addNextCondition(NextCondition condition) {
        if (nextConditions.containsKey(condition.getPriority())) {
            throw new DiagramException(String.format("State %s have condition %s and condition %s with equals priority", this.getValue(), nextConditions.get(condition.getPriority()).getValue(), condition.getValue()));
        }
        nextConditions.put(condition.getPriority(), condition);
    }

    public void setDefaultCondition(DefaultCondition condition) {
        if (defaultCondition != null) {
            throw new DiagramException(String.format("State %s have few default condition", this.getValue()));
        }
        defaultCondition = condition;
    }

    public List<NextCondition> getReverseSortedNextConditions() {
        List<Integer> list = new ArrayList<>(nextConditions.keySet());
        Collections.sort(list);
        Collections.reverse(list);
        return list.stream().map((key -> nextConditions.get(key))).collect(Collectors.toList());
    }

    public DefaultCondition getDefaultCondition() {
        return defaultCondition;
    }
}
