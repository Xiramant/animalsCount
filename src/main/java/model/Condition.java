package model;

import static model.HashCondition.replaceBracketsWithHash;
import static model.LogicalOperators.*;

//Класс для представления условия в форме двух подусловий и связывающего их логического оператора.
class Condition {

    private String condition1;

    private String condition2;

    private LogicalOperators conditionLogical;

    String getCondition1() {
        return condition1;
    }

    String getCondition2() {
        return condition2;
    }

    LogicalOperators getConditionLogical() {
        return conditionLogical;
    }


    private Condition(String condition1Arg, String condition2Arg, final LogicalOperators conditionLogicalArg) {
        this.condition1 = pretreatmentText(condition1Arg);
        this.condition2 = pretreatmentText(condition2Arg);
        this.conditionLogical = conditionLogicalArg;
    }


    //Разделить условие на два подусловия и логический оператор между ними.
    //Для оператора NOT первое подусловие получается пустым, а оператор применяется ко второму подусловию.
    //Операторы OR и AND ищутся в условии справа налево для правильного использования в рекурсии.
    //Оператор NOT ищется последним (при отсутствии обоих операторов OR и AND).
    static Condition split(final String conditionArg) {

        String condition = pretreatmentText(conditionArg);
        HashCondition hashCondition = replaceBracketsWithHash(condition);

        int andIndex = hashCondition.getConditionWithHash().lastIndexOf(AND.getText());
        int orIndex = hashCondition.getConditionWithHash().lastIndexOf(OR.getText());

        if (andIndex == -1 && orIndex == -1) {
            if (condition.contains(NOT.getText())) {
                return getCondition(hashCondition, NOT, condition.indexOf(NOT.getText()));
            }
            else {
                System.out.println("Не найдено логическое условие.");
                return null;
            }
        }

        if (andIndex != -1 && orIndex == -1) {
            return getCondition(hashCondition, AND, andIndex);
        }

        if (andIndex == -1 && orIndex != -1) {
            return getCondition(hashCondition, OR, orIndex);
        }

        if (andIndex > orIndex) {
            return getCondition(hashCondition, AND, andIndex);
        }
        else {
            return getCondition(hashCondition, OR, orIndex);
        }
    }

    //Получить класс Condition с установленными полями.
    private static Condition getCondition(final HashCondition hashConditionArg,
                                  final LogicalOperators logicalOperatorArg,
                                  final int logicalOperatorIndexArg) {
        String condition1 = hashConditionArg.getConditionWithHash().substring(0, logicalOperatorIndexArg);
        condition1 = hashConditionArg.returnBrackets(condition1);
        String condition2 = hashConditionArg.getConditionWithHash().substring(logicalOperatorIndexArg
                                                                            + logicalOperatorArg.getText().length());
        condition2 = hashConditionArg.returnBrackets(condition2);

        return new Condition(condition1, condition2, logicalOperatorArg);
    }

    //Провести предварительную обработку условия
    private static String pretreatmentText(final String conditionArg) {
        String condition = conditionArg.trim().toLowerCase();

        //Удаление излишних скобок
        while (isExcessiveBrackets(condition)) {
            condition = condition.substring(OPENING_BRACKET.getText().length(),
                                    condition.length() - CLOSING_BRACKET.getText().length()).trim();
        }

        return condition;
    }

    //Определить есть ли у переданного условия излишние скобки (по краям текста).
    private static boolean isExcessiveBrackets(final String conditionArg) {
        String condition = conditionArg;

        int firstOpeningBracketIndex = condition.indexOf(OPENING_BRACKET.getText());
        int lastOpeningBracketIndex = condition.lastIndexOf(OPENING_BRACKET.getText());

        while (firstOpeningBracketIndex != lastOpeningBracketIndex) {
            condition = removeTextFromInternalBrackets(condition);
            firstOpeningBracketIndex = condition.indexOf(OPENING_BRACKET.getText());
            lastOpeningBracketIndex = condition.lastIndexOf(OPENING_BRACKET.getText());
        }

        return condition.startsWith(OPENING_BRACKET.getText()) && condition.endsWith(CLOSING_BRACKET.getText());
    }

    //Удалить текст из внутренних скобок
    private static String removeTextFromInternalBrackets(final String conditionArg) {

        int lastOpeningBracketIndex = conditionArg.lastIndexOf(OPENING_BRACKET.getText());
        int firstClosingBracketAfterLastOpeningBracketIndex = conditionArg.indexOf(CLOSING_BRACKET.getText(),
                lastOpeningBracketIndex + OPENING_BRACKET.getText().length());

        return conditionArg.substring(0, lastOpeningBracketIndex)
                + conditionArg.substring(firstClosingBracketAfterLastOpeningBracketIndex + CLOSING_BRACKET.getText().length());
    }

}
