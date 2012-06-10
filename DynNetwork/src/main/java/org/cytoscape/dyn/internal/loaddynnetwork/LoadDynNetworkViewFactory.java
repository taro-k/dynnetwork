package org.cytoscape.dyn.internal.loaddynnetwork;

import org.cytoscape.work.TaskIterator;

/**
 * <code> LoadDynNetworkViewFactory </code> is interface that provides 
 * a task iterator for creating networks view from networks.
 * 
 * @author sabina
 *
 */
public interface LoadDynNetworkViewFactory<T>
{
	/**
	 * Create a task iterator for creating a network view from the current dynamic network.
	 * @param network
	 * @return a task iterator of type {@link TaskIterator}
	 */
	TaskIterator creatTaskIterator();
}
