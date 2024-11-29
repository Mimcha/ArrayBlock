package org.example;

import java.util.concurrent.ArrayBlockingQueue;

public class CharacterCounter implements Runnable {
    private final ArrayBlockingQueue<String> queue;
    private final char character;
    private int maxCount = 0;

    public CharacterCounter(ArrayBlockingQueue<String> queue, char character) {
        this.queue = queue;
        this.character = character;
    }

    @Override
    public void run() {
        try {
            String text;
            while (!(text = queue.take()).equals("END")) {
                int count = countCharacter(text, character);
                if (count > maxCount) {
                    maxCount = count;
                }
            }
            System.out.println("Максимальное количество символов '" + character + "': " + maxCount);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Метод подсчета символов в строкe
    private int countCharacter(String text, char character) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c == character) {
                count++;
            }
        }
        return count;
    }
}
