package com.litefix.commons.collections;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class LockFreeStack<T> {

	private static class StackNode<T> {
		T value;
		StackNode<T> next;

		StackNode(T value) {
			this.value = value;
		}
	}

	// Defining the stack nodes as Atomic Reference
	private AtomicReference<StackNode<T>> headNode = new AtomicReference<StackNode<T>>();
	private AtomicInteger noOfOperations = new AtomicInteger(0);

	public int getNoOfOperations() {
		return noOfOperations.get();
	}

	// Push operation
	public void push(T value) {
		StackNode<T> newHead = new StackNode<T>(value);

		// CAS loop defined
		while (true) {
			StackNode<T> currentHeadNode = headNode.get();
			newHead.next = currentHeadNode;

			// perform CAS operation before setting new
			// value
			if (headNode.compareAndSet(currentHeadNode, newHead)) {
				break;
			} else {
				// waiting for a nanosecond
				LockSupport.parkNanos(1);
			}
		}

		// getting the value atomically
		noOfOperations.incrementAndGet();
	}

	// Pop function
	public T pop() {
		StackNode<T> currentHeadNode = headNode.get();

		// CAS loop defined
		while (currentHeadNode != null) {
			StackNode<T> newHead = currentHeadNode.next;
			if (headNode.compareAndSet(currentHeadNode, newHead)) {
				break;
			} else {
				// waiting for a nanosecond
				LockSupport.parkNanos(1);
				currentHeadNode = headNode.get();
			}
		}
		noOfOperations.incrementAndGet();
		return currentHeadNode != null ? currentHeadNode.value : null;
	}
}
