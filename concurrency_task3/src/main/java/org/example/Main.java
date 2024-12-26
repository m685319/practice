package org.example;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ComplexTask {
    private final int taskId;

    public ComplexTask(int taskId) {
        this.taskId = taskId;
    }

    public int execute() {
        System.out.println("Task " + taskId + " is being executed by " + Thread.currentThread().getName());
        try {
            Thread.sleep(1000 + (int)(Math.random() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Task " + taskId + " completed by " + Thread.currentThread().getName());
        return taskId * 2;
    }
}

class ComplexTaskExecutor {
    private final int numberOfThreads;

    public ComplexTaskExecutor(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public void executeTasks(int numberOfTasks) {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CyclicBarrier barrier = new CyclicBarrier(numberOfTasks, () -> {
            System.out.println("All tasks are completed. Combining results...");
        });

        ConcurrentMap<Integer, Integer> results = new ConcurrentHashMap<>();

        for (int i = 0; i < numberOfTasks; i++) {
            final int taskId = i;
            executorService.submit(() -> {
                try {
                    ComplexTask task = new ComplexTask(taskId);
                    int result = task.execute();
                    results.put(taskId, result);
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Combined Results: " + results);
    }
}

public class Main {
    public static void main(String[] args) {
        ComplexTaskExecutor taskExecutor = new ComplexTaskExecutor(3);

        Runnable testRunnable = () -> {
            System.out.println(Thread.currentThread().getName() + " started the test.");

            taskExecutor.executeTasks(5);

            System.out.println(Thread.currentThread().getName() + " completed the test.");
        };

        Thread thread1 = new Thread(testRunnable, "TestThread-1");
        Thread thread2 = new Thread(testRunnable, "TestThread-2");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}