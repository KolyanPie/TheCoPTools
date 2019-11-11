package edu.thecop.tools.statesfromdrawio.diagram.elements;

abstract class Element {
    private String value;

    Element(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void fixFirstLetter(boolean isLow) {
        if (isLow) {
            value = value.replaceFirst(value.substring(0, 1), value.substring(0, 1).toLowerCase());
        } else {
            value = value.replaceFirst(value.substring(0, 1), value.substring(0, 1).toUpperCase());
        }
    }
}
