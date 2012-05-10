package org.cytoscape.dyn.internal.view;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue {

	private final Lock lock;

    public BlockingQueue() {
    	lock = new ReentrantLock();
    }

    public void lock() {
    	lock.lock();
    }

    public void unlock() {
    	lock.unlock();
    }

}