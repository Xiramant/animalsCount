package model;

public enum LogicalOperators {

    AND("_и_"),
    OR("_или_"),
    NOT("_не_"),
    OPENING_BRACKET("("),
    CLOSING_BRACKET(")");

    private String TEXT;

    LogicalOperators(String text) {
        this.TEXT = text;
    }

    String getText() {
        return TEXT;
    }

}
