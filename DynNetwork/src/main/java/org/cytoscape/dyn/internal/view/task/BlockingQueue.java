package org.cytoscape.dyn.internal.view.task;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <code> BlockingQueue </code> implements a simple blocking queue used to 
 * process visualization updates sequentially.
 * 
 * @author sabina
 *
 */
public class BlockingQueue
{

	private final Lock lock;

    public BlockingQueue()
    {
    	lock = new ReentrantLock();
    }

    public void lock()
    {
    	lock.lock();
    }

    public void unlock()
    {
    	lock.unlock();
    }

}