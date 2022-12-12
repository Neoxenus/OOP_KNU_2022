package org.example;

import java.util.concurrent.atomic.AtomicReference;

public class NonBlockingQueue<T> {
    private final AtomicReference<Node<T>> head;
    private final AtomicReference<Node<T>> tail;

    public NonBlockingQueue() {
        Node<T> tNode = new Node<>();
        this.head = new AtomicReference<>(tNode);
        this.tail = new AtomicReference<>(tNode);
    }

    private static class Node<T> {
        T item;
        AtomicReference<Node<T>> next;

        Node() {
            this.next = new AtomicReference<>();
        }
        Node(T item) {
            this.item = item;
            this.next = new AtomicReference<>();
        }
    }

    public void add(T value) {
        Node<T> toPush = new Node<>(value);
        Node<T> currentTailNode;
        Node<T> currentNextNode;

        while (true) {
            currentTailNode = tail.get();
            currentNextNode = currentTailNode.next.get();

            if (currentTailNode == tail.get()) {
                if (currentNextNode == null) {
                    if (currentTailNode.next.compareAndSet(null, toPush)) {
                        break;
                    }
                } else {
                    tail.compareAndSet(currentTailNode, currentNextNode);
                }
            }
        }
        tail.compareAndSet(currentTailNode, toPush);
    }

    public T poll() {
        T result;
        Node<T> currentHeadNode;
        Node<T> currentTailNode;
        Node<T> currentNextNode;

        while (true) {
            currentHeadNode = head.get();
            currentTailNode = tail.get();
            currentNextNode = currentHeadNode.next.get();

            if (currentHeadNode == head.get()) {
                if (currentHeadNode == currentTailNode) {
                    if (currentNextNode == null) {
                        return null;
                    }

                    tail.compareAndSet(currentTailNode, currentNextNode);
                } else {
                    result = currentNextNode.item;

                    if (head.compareAndSet(currentHeadNode, currentNextNode)) {
                        break;
                    }
                }
            }
        }

        return result;
    }
    public T peak() {
        return head.get().item;
    }
}