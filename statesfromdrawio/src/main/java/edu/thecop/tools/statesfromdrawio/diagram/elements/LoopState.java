package edu.thecop.tools.statesfromdrawio.diagram.elements;

import edu.thecop.tools.statesfromdrawio.diagram.DiagramException;

import java.util.*;
import java.util.stream.Collectors;

public class LoopState extends Element {
    private HashMap<Integer, BreakCondition> breakConditions;

    {
        breakConditions = new HashMap<>();
    }

    public LoopState(String value) {
        super(value);
    }

    public void addBreakCondition(BreakCondition condition) {
        if (breakConditions.containsKey(condition.getPriority())) {
            throw new DiagramException(String.format("State %s have condition %s and condition %s with equals priority", this.getValue(), breakConditions.get(condition.getPriority()).getValue(), condition.getValue()));
        }
        breakConditions.put(condition.getPriority(), condition);
    }

    public List<BreakCondition> getSortedBreakConditions() {
        List<Integer> list = new ArrayList<>(breakConditions.keySet());
        Collections.sort(list);
        return list.stream().map((key -> breakConditions.get(key))).collect(Collectors.toList());
    }
}
