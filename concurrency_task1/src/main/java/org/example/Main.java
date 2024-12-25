package org.example;

import java.util.LinkedList;
import java.util.Queue;

class BlockingQueue<T> {
    private final Queue<T> queue;
    private final int capacity;

    public BlockingQueue(int capacity) {
        this.queue = new LinkedList<>();
        this.capacity = capacity;
    }

    public synchronized void enqueue(T element) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.add(element);
        notifyAll();
    }

    public synchronized T dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T element = queue.poll();
        notifyAll();
        return element;
    }

    public synchronized int size() {
        return queue.size();
    }
}

public class Main {

    public static void main(String[] args) throws Exception {
        BlockingQueue<Integer> blockingQueue = new BlockingQueue<>(5);

        Thread producerThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    blockingQueue.enqueue(i);
                    System.out.println("Producer Thread: " + i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
        });

        Thread consumerThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    int element = blockingQueue.dequeue();
                    System.out.println("Consumer Thread: " + element);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
        });
        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();
    }
}