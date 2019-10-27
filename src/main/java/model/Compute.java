package model;

import data.Data;

import java.util.stream.Stream;

import static data.Data.DATA_SEPARATOR;
import static model.LogicalOperators.*;
import static java.util.stream.Stream.concat;

public class Compute {

    private Data data;

    public Compute(final Data dataArg) {
        this.data = dataArg;
    }


    public long calculate() {
        String condition = data.getCondition();
        if (isLogicalOperatorPresent(condition)) {
            return getStream(Condition.split(condition)).count();
        }
        return getStream(condition).count();
    }


    private Stream<String> getStream(final String conditionArg) {
        return data.getData()
                .filter(line -> line.contains(DATA_SEPARATOR + conditionArg));
    }

    private Stream<String> getStream(final Condition conditionArg) {
        switch (conditionArg.getConditionLogical()) {
            case AND:
                return getStreamAnd(conditionArg);
            case OR:
                return getStreamOr(conditionArg);
            case NOT:
                return getStreamNot(conditionArg);
            default:
                System.out.println("Неизвестная логическая команда.");
                return null;
        }
    }

    private Stream<String> getStreamAnd(final Condition conditionArg) {
        return getStreamCondition(conditionArg.getCondition1())
                .filter(line -> getStreamCondition(conditionArg.getCondition2()).anyMatch(line::equals));
    }

    private Stream<String> getStreamOr(final Condition conditionArg) {
        return concat(getStreamCondition(conditionArg.getCondition1()), getStreamCondition(conditionArg.getCondition2()))
                .distinct();
    }

    private Stream<String> getStreamNot(final Condition conditionArg) {
        return data.getData()
                .filter(line -> getStreamCondition(conditionArg.getCondition2()).noneMatch(line::equals));
    }


    private Stream<String> getStreamCondition(final String conditionArg) {
        if (isLogicalOperatorPresent(conditionArg)) {
            return getStream(Condition.split(conditionArg));
        }
        else {
            return getStream(conditionArg);
        }
    }

    private boolean isLogicalOperatorPresent(final String conditionArg) {
        return conditionArg.contains(AND.getText())
                || conditionArg.contains(OR.getText())
                || conditionArg.contains(NOT.getText());
    }
}
