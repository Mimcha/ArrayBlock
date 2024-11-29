package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    private static final ArrayBlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    private static final ArrayBlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    private static final ArrayBlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {
        // Поток генерации для строк
        Thread generatorTheread = new Thread(() -> {
            Random random = new Random();
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    // Добавляем строки в каждую очередь
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            // Добавляем "конец" для каждого потока
            try {
                queueA.put("END");
                queueB.put("END");
                queueC.put("END");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        // Поток для подсчета символов
        Thread couuterA = new Thread(new CharacterCounter(queueA, 'a'));
        Thread couuterB = new Thread(new CharacterCounter(queueB, 'b'));
        Thread couuterC = new Thread(new CharacterCounter(queueC, 'c'));

        // Запуск потоков
        generatorTheread.start();
        couuterA.start();
        couuterB.start();
        couuterC.start();

        // Ожидание завершения потоков
        generatorTheread.join();
        couuterA.join();
        couuterB.join();
        couuterC.join();
    }

    // Метод генерации текста
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}