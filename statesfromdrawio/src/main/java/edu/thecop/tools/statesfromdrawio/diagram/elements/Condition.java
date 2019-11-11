package edu.thecop.tools.statesfromdrawio.diagram.elements;

public class Condition extends Element {
    private String sourceId;
    private String targetId;
    private LoopState target;

    public Condition(String value, String sourceId, String targetId) {
        super(value);
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public LoopState getTarget() {
        return target;
    }

    public void setTarget(LoopState target) {
        this.target = target;
    }
}
