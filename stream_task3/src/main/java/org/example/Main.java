package org.example;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Main {
    public static void main(String[] args) {
        int n = 10;
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        FactorialTask factorialTask = new FactorialTask(n);
        long result = forkJoinPool.invoke(factorialTask);
        System.out.println("Факториал " + n + "! = " + result);
    }
}

class FactorialTask extends RecursiveTask<Long> {

    private final int n;

    public FactorialTask(int n) {
        this.n = n;
    }

    @Override
    protected Long compute() {
        if (n <= 1) {
            return 1L;
        }
        int mid = n / 2;
        FactorialTask lowerTask = new FactorialTask(mid);
        lowerTask.fork();
        long upperResult = calculateRight(mid + 1, n);
        long lowerResult = lowerTask.join();
        return lowerResult * upperResult;
    }

    private long calculateRight(int start, int end) {
        long result = 1L;
        for (int i = start; i <= end; i++) {
            result *= i;
        }
        return result;
    }
}