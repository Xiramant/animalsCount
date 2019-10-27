Программа для подсчета количества животных с определенными свойствами.
Данные по свойствам животных извлекаются из файла "src/main/resources/animals.txt". Свойства животных должны разделяться запятыми, без пробелов.
Правило для подсчета животных извлекается из файла "src/main/resources/condition.txt". Для записи условия используются ключевые слова "_и_", "_или_", "_не_". Порядок вычисления по умолчанию: слева направо. Порядок вычисления может быть изменен с помощью скобок, ключевые слова "(", ")".
Пример записи данных и условия можно посмотреть в представленных файлах animals.txt, condition.txt.
Результат подсчета записывается в файл "src/main/resources/result.txt".
При запуске программы в Linux можно воспользоваться командой "make".
