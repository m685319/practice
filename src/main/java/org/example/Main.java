package org.example;

import java.util.Arrays;
import java.util.Stack;

class MyStringBuilder {
    private char[] buffer;
    private int size;
    private final Stack<Snapshot> history;

    public MyStringBuilder() {
        this.buffer = new char[16];
        this.size = 0;
        this.history = new Stack<>();
    }

    public MyStringBuilder append(String str) {
        saveState();

        if (str == null) {
            str = "null";
        }

        if (size + str.length() > buffer.length) {
            resize(buffer.length * 2);
        }

        for (int i = 0; i < str.length(); i++) {
            buffer[size++] = str.charAt(i);
        }

        return this;
    }

    public MyStringBuilder append(char c) {
        saveState();
        if (size + 1 > buffer.length) {
            resize(size + 1);
        }
        buffer[size++] = c;
        return this;
    }

    private void resize(int newSize) {
        char[] newBuffer = new char[newSize];
        if (size >= 0) {
            System.arraycopy(buffer, 0, newBuffer, 0, size);
        }
        buffer = newBuffer;
    }

    public MyStringBuilder reverse() {
        char[] reversed = new char[size];
        for (int i = 0; i < size; i++) {
            reversed[i] = buffer[size - i - 1];
        }
        buffer = reversed;
        return this;
    }

    public void clear() {
        saveState();
        size = 0;
    }

    public void undo() {
        if(!history.isEmpty()) {
            Snapshot snapshot = history.pop();
            this.buffer = snapshot.state();
            this.size = snapshot.size();
        } else {
            System.out.println("Nothing to undo.");
        }
    }

    private void saveState() {
        history.push(new Snapshot(buffer, size));
    }

    @Override
    public String toString() {
        return new String(buffer, 0, size);
    }
}

record Snapshot(char[] state, int size) {
    Snapshot(char[] state, int size) {
        this.state = Arrays.copyOf(state, size);
        this.size = size;
    }
}
