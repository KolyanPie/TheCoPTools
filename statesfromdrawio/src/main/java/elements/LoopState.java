package elements;

import java.util.HashMap;

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
            throw new RuntimeException(String.format("State %s have condition %s and condition %s with equals priority", this.getValue(), breakConditions.get(condition.getPriority()).getValue(), condition.getValue()));
        }
        breakConditions.put(condition.getPriority(), condition);
    }
}
