package edu.thecop.tools.statesfromdrawio.elements;

import java.util.HashMap;

public class LastingState extends LoopState {
    private String durationVar;
    private HashMap<Integer, NextCondition> nextConditions;
    private DefaultCondition defaultCondition;

    {
        nextConditions = new HashMap<>();
    }

    public LastingState(String value, String durationVar) {
        super(value);
        this.durationVar = durationVar;
    }

    public void addNextCondition(NextCondition condition) {
        if (nextConditions.containsKey(condition.getPriority())) {
            throw new RuntimeException(String.format("State %s have condition %s and condition %s with equals priority", this.getValue(), nextConditions.get(condition.getPriority()).getValue(), condition.getValue()));
        }
        nextConditions.put(condition.getPriority(), condition);
    }

    public void setDefaultCondition(DefaultCondition Condition) {
        if (defaultCondition != null) {
            throw new RuntimeException(String.format("State %s have few default condition", this.getValue()));
        }
    }
}
