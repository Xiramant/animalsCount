package model;

import data.Data;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static data.Data.printResult;
import static model.LogicalOperators.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnimalModelTest {

    private final String TEST_ANIMALS_DATA_PATH = "src/main/resources/animalsTest.txt";

    private final String TEST_ANIMALS_CONDITION_PATH = "src/main/resources/conditionTest.txt";

    private void createTestData(final String dataArg, final String conditionArg) {

        File animalsTest = new File(TEST_ANIMALS_DATA_PATH);
        File conditionTest = new File(TEST_ANIMALS_CONDITION_PATH);

        try {
            if (animalsTest.createNewFile() && conditionTest.createNewFile()) {
                printResult(TEST_ANIMALS_DATA_PATH, dataArg);
                printResult(TEST_ANIMALS_CONDITION_PATH, conditionArg);
            }
        } catch (IOException e) {
            System.out.println("Не удалось создать тестовый файл(ы)");
            e.printStackTrace();
        }
    }

    private void removeTestData() {
        File animalsTest = new File(TEST_ANIMALS_DATA_PATH);
        if (!animalsTest.delete()) {
            System.out.println("Не удалось удалить файл " + TEST_ANIMALS_DATA_PATH);
        }

        File conditionTest = new File(TEST_ANIMALS_CONDITION_PATH);
        if (!conditionTest.delete()) {
            System.out.println("Не удалось удалить файл " + TEST_ANIMALS_CONDITION_PATH);
        }
    }

    private String getTestData() {
        return "  легкое,маленькое,всеядное" + "\n" +
                "тяжелое,маленькое,травоядное" + "\n" +
                "тяжелое,невысокое,травоядное" + "\n" +
                "среднее,невысокое,плотоядное" + "\n" +
                "легкое,невысокое,травоядное" + "\n" +
                "тяжелое,высокое,всеядное" + "\n" +
                "тяжелое,высокое,плотоядное" + "\n" +
                "  легкое,маленькое,всеядное" + "\n" +
                "тяжелое,маленькое,травоядное" + "\n" +
                "тяжелое,невысокое,травоядное" + "\n" +
                "среднее,невысокое,плотоядное" + "\n" +
                "легкое,невысокое,травоядное" + "\n" +
                "тяжелое,высокое,всеядное" + "\n" +
                "тяжелое,высокое,плотоядное" + "\n";
    }

    @Test
    void testSimpleCount() {
        String condition = "легкое";
        createTestData(getTestData(), condition);
        Data data = new Data(TEST_ANIMALS_DATA_PATH, TEST_ANIMALS_CONDITION_PATH);
        Compute compute = new Compute(data);
        long count = compute.calculate();
        assertEquals(4, count);
        removeTestData();

        condition = "высокое";
        createTestData(getTestData(), condition);
        data = new Data(TEST_ANIMALS_DATA_PATH, TEST_ANIMALS_CONDITION_PATH);
        compute = new Compute(data);
        count = compute.calculate();
        assertEquals(4, count);
        removeTestData();
    }

    @Test
    void testLogicalConditionSplit() {
        Condition condition = Condition.split("тяжелое _и_ высокое");
        assertEquals("тяжелое", condition.getCondition1());
        assertEquals("высокое", condition.getCondition2());
        assertEquals(AND, condition.getConditionLogical());

        condition = Condition.split("тяжелое _или_ высокое");
        assertEquals("тяжелое", condition.getCondition1());
        assertEquals("высокое", condition.getCondition2());
        assertEquals(OR, condition.getConditionLogical());

        condition = Condition.split("_не_ тяжелое");
        assertEquals("", condition.getCondition1());
        assertEquals("тяжелое", condition.getCondition2());
        assertEquals(NOT, condition.getConditionLogical());
    }

    @Test
    void testLogicalCompoundConditionSplit() {
        Condition condition = Condition.split("тяжелое _или_ маленькое _и_ всеядное");
        assertEquals("тяжелое _или_ маленькое", condition.getCondition1());
        assertEquals("всеядное", condition.getCondition2());
        assertEquals(AND, condition.getConditionLogical());

        condition = Condition.split("тяжелое _или_ маленькое _или_ всеядное");
        assertEquals("тяжелое _или_ маленькое", condition.getCondition1());
        assertEquals("всеядное", condition.getCondition2());
        assertEquals(OR, condition.getConditionLogical());

        condition = Condition.split("травоядное _или_ _не_ невысокое");
        assertEquals("травоядное", condition.getCondition1());
        assertEquals("_не_ невысокое", condition.getCondition2());
        assertEquals(OR, condition.getConditionLogical());

         condition = Condition.split(" (  (тяжелое _и_ маленькое )) ");
        assertEquals("тяжелое", condition.getCondition1());
        assertEquals("маленькое", condition.getCondition2());
        assertEquals(AND, condition.getConditionLogical());

        condition = Condition.split("тяжелое _или_ ((маленькое _и_ всеядное))");
        assertEquals("тяжелое", condition.getCondition1());
        assertEquals("маленькое _и_ всеядное", condition.getCondition2());
        assertEquals(OR, condition.getConditionLogical());

        condition = Condition.split("(легкое _и_ невысокое) _или_ (маленькое _и_ всеядное)");
        assertEquals("легкое _и_ невысокое", condition.getCondition1());
        assertEquals("маленькое _и_ всеядное", condition.getCondition2());
        assertEquals(OR, condition.getConditionLogical());

        condition = Condition.split("легкое _и_ (невысокое _или_ (маленькое _и_ всеядное))");
        assertEquals("легкое", condition.getCondition1());
        assertEquals("невысокое _или_ (маленькое _и_ всеядное)", condition.getCondition2());
        assertEquals(AND, condition.getConditionLogical());
    }

    @Test
    void testConditionAnd(){
        String condition = "тяжелое _и_ высокое";
        createTestData(getTestData(), condition);
        Data data = new Data(TEST_ANIMALS_DATA_PATH, TEST_ANIMALS_CONDITION_PATH);
        Compute compute = new Compute(data);
        long count = compute.calculate();
        assertEquals(4, count);
        removeTestData();
    }

    @Test
    void testConditionOr(){
        String condition = "тяжелое _или_ маленькое";
        createTestData(getTestData(), condition);
        Data data = new Data(TEST_ANIMALS_DATA_PATH, TEST_ANIMALS_CONDITION_PATH);
        Compute compute = new Compute(data);
        long count = compute.calculate();
        assertEquals(10, count);
        removeTestData();
    }

    @Test
    void testConditionNot(){
        String condition = "_не_ высокое";
        createTestData(getTestData(), condition);
        Data data = new Data(TEST_ANIMALS_DATA_PATH, TEST_ANIMALS_CONDITION_PATH);
        Compute compute = new Compute(data);
        long count = compute.calculate();
        assertEquals(10, count);
        removeTestData();
    }

    @Test
    void testCompoundCondition() {
        String condition = "тяжелое _или_ маленькое _и_ всеядное";
        createTestData(getTestData(), condition);
        Data data = new Data(TEST_ANIMALS_DATA_PATH, TEST_ANIMALS_CONDITION_PATH);
        Compute compute = new Compute(data);
        long count = compute.calculate();
        assertEquals(4, count);
        removeTestData();

        condition = "тяжелое _или_ маленькое _и_ _не_ всеядное";
        createTestData(getTestData(), condition);
        data = new Data(TEST_ANIMALS_DATA_PATH, TEST_ANIMALS_CONDITION_PATH);
        compute = new Compute(data);
        count = compute.calculate();
        assertEquals(6, count);
        removeTestData();

        condition = "_не_ невысокое _и_ _не_ травоядное";
        createTestData(getTestData(), condition);
        data = new Data(TEST_ANIMALS_DATA_PATH, TEST_ANIMALS_CONDITION_PATH);
        compute = new Compute(data);
        count = compute.calculate();
        assertEquals(6, count);
        removeTestData();

        condition = "тяжелое _или_ (маленькое _и_ всеядное)";
        createTestData(getTestData(), condition);
        data = new Data(TEST_ANIMALS_DATA_PATH, TEST_ANIMALS_CONDITION_PATH);
        compute = new Compute(data);
        count = compute.calculate();
        assertEquals(10, count);
        removeTestData();

        condition = "(легкое _и_ невысокое) _или_ (маленькое _и_ всеядное)";
        createTestData(getTestData(), condition);
        data = new Data(TEST_ANIMALS_DATA_PATH, TEST_ANIMALS_CONDITION_PATH);
        compute = new Compute(data);
        count = compute.calculate();
        assertEquals(4, count);
        removeTestData();

        condition = "легкое _и_ (невысокое _или_ (маленькое _и_ всеядное))";
        createTestData(getTestData(), condition);
        data = new Data(TEST_ANIMALS_DATA_PATH, TEST_ANIMALS_CONDITION_PATH);
        compute = new Compute(data);
        count = compute.calculate();
        assertEquals(4, count);
        removeTestData();

        condition = "легкое _и_ _не_ (невысокое _или_ (маленькое _и_ всеядное))";
        createTestData(getTestData(), condition);
        data = new Data(TEST_ANIMALS_DATA_PATH, TEST_ANIMALS_CONDITION_PATH);
        compute = new Compute(data);
        count = compute.calculate();
        assertEquals(0, count);
        removeTestData();

        condition = "легкое _и_ (высокое _или_ _не_ (невысокое _и_ травоядное))";
        createTestData(getTestData(), condition);
        data = new Data(TEST_ANIMALS_DATA_PATH, TEST_ANIMALS_CONDITION_PATH);
        compute = new Compute(data);
        count = compute.calculate();
        assertEquals(2, count);
        removeTestData();
    }

}