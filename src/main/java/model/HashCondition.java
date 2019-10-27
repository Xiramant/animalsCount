package model;

import java.util.HashMap;
import java.util.Set;

import static model.LogicalOperators.*;

//Вспомогательный класс для удаления всех скобок в условии
// для определения правильного порядка действия логических операторов.
public class HashCondition {

    private String conditionWithHash;

    private HashMap<String, String> bracketsHashMap;

    String getConditionWithHash() {
        return conditionWithHash;
    }


    private HashCondition(final String conditionWithHash, final HashMap<String, String> bracketsHashMap) {
        this.conditionWithHash = conditionWithHash;
        this.bracketsHashMap = bracketsHashMap;
    }


    //В условии заменить скобки с их содержимым на хэш.
    //Скобки заменяются по направлению справа налево начиная с вложенных к внешним
    // до тех пор, пока скобки не уйдут из условия.
    static HashCondition replaceBracketsWithHash(final String conditionArg) {
        String condition = conditionArg;
        HashMap<String, String> bracketsHashMap = new HashMap<>();
        
        int lastOpeningBracketIndex = condition.lastIndexOf(OPENING_BRACKET.getText());
        int firstClosingBracketAfterLastOpeningBracketIndex = condition.indexOf(CLOSING_BRACKET.getText(),
                                                                                lastOpeningBracketIndex);
        
        while (lastOpeningBracketIndex != -1) {

            String innerBracketsCondition = condition.substring(lastOpeningBracketIndex,
                    firstClosingBracketAfterLastOpeningBracketIndex + CLOSING_BRACKET.getText().length());
            String hash = String.valueOf(innerBracketsCondition.hashCode());
            
            bracketsHashMap.put(hash, innerBracketsCondition);
            condition = condition.replace(innerBracketsCondition, hash);

            lastOpeningBracketIndex = condition.lastIndexOf(OPENING_BRACKET.getText());
            firstClosingBracketAfterLastOpeningBracketIndex = condition.indexOf(CLOSING_BRACKET.getText(),
                                                                                lastOpeningBracketIndex);
        }

        return new HashCondition(condition, bracketsHashMap);
    }


    //В условии заменить хэш обратно на скобки.
    String returnBrackets(final String conditionArg) {
        if (bracketsHashMap.size() == 0) {
            return conditionArg;
        }

        String condition = conditionArg;
        boolean replacement = true;
        Set<String> keySet = bracketsHashMap.keySet();

        while (replacement) {
            for (String key: keySet) {
                if (condition.contains(key)) {
                    condition = condition.replace(key, bracketsHashMap.get(key));
                    replacement = true;
                    break;
                }
                replacement = false;
            }
        }

        return condition;
    }

    @Override
    public String toString() {
        return "conditionWithHash = " + conditionWithHash + ";" + System.lineSeparator()
                + "bracketsHashMap = " + bracketsHashMap.toString() + ".";
    }

}
