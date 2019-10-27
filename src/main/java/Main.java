import model.Compute;
import data.Data;

import static data.Data.printResult;

public class Main {

    private static final String ANIMALS_DATA_PATH = "src/main/resources/animals.txt";

    private static final String ANIMALS_CONDITION_PATH = "src/main/resources/condition.txt";

    private static final String ANIMALS_RESULT_PATH = "src/main/resources/result.txt";


    public static void main(String[] args) {
        Data animalsData = new Data(ANIMALS_DATA_PATH, ANIMALS_CONDITION_PATH);
        Compute animalsCompute = new Compute(animalsData);
        long count = animalsCompute.calculate();
        printResult(ANIMALS_RESULT_PATH, String.valueOf(count));
    }
}
