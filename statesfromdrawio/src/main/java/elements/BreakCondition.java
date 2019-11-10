package elements;

public class BreakCondition extends Condition {
    private int priority;

    public BreakCondition(String value, String sourceId, String targetId, int priority) {
        super(value, sourceId, targetId);
        this.priority = priority;
    }

    int getPriority() {
        return priority;
    }
}
