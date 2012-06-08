package org.cytoscape.dyn.internal.loaddynnetwork;

import java.io.File;

import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;

/**
 * <code> LoadDynNetworkFileTaskFactory </code> is interface that provides 
 * a task iterator for loading dynamic networks from files.
 * 
 * @author rozagh
 *
 */
public interface LoadDynNetworkFileTaskFactory extends TaskFactory
{
	/**
	 * Create a task iterator for loading a network from a file.
	 * The created task runs synchronously in the current thread and does not
	 * create a task monitor.
	 * @param file The file for loading into a network
	 * @return a task iterator of type {@link TaskIterator}
	 */
	TaskIterator creatTaskIterator(final File file);

}