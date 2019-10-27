package data;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Data {

    public static final String DATA_SEPARATOR = ",";

    private long id = 0;

    private final Collection<String> data;

    private final String condition;

    public Stream<String> getData() {
        return data.stream();
    }

    public String getCondition() {
        return condition;
    }


    public Data(final String dataPathArg, final String conditionPathArg) {


        this.data = fileToString(dataPathArg)
                .lines()
                .map(line -> line.trim())
                //Добавление DATA_SEPARATOR в начало строки требуется для корректного поиска свойства,
                // например, в случае поиска свойства "высокое" при возможных значениях "невысокое" или "очень высокое"
                //В дальнейшем поиск производится по "DATA_SEPARATOR + искомое значение свойства".
                .map(line -> DATA_SEPARATOR + line)
                //Добавление уникального id требуется при полностью совпавшем комплекте свойств для некоторых животных
                // для правильного слияния потоков при логическом операторе OR.
                //Текущая реализация основана на конкантенации результатов двух подусловий оператора OR
                // (что может привести к дублированию строк) и удалению дублирования.
                // Если существуют животные с одинаковым набором свойств, то в результате удаления дублирования
                // могут быть убраны нужные строки, поэтому для каждого набора свойств создается уникальный идентификатор.
                .map(line -> id++ + line)
                .collect(Collectors.toList());

        this.condition = fileToString(conditionPathArg).trim();
    }

    private static String fileToString(final String filePathArg) {
        String stringFromFile = "";

        try {
            stringFromFile = Files.readString(Path.of(filePathArg));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringFromFile.toLowerCase().trim();
    }

    public static void printResult(final String resultPathArg, final String result) {
        try(FileWriter writer = new FileWriter(resultPathArg, false)) {
            writer.write(result);
            writer.flush();
        }
        catch(IOException e){
            System.out.println("Не удалось записать результат в файл.");
            e.printStackTrace();
        }
    }

}
