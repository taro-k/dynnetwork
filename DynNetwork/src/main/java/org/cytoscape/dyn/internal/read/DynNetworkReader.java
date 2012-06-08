package org.cytoscape.dyn.internal.read;

import org.cytoscape.work.Task;
import org.cytoscape.work.TaskMonitor;

/**
 *  <code> DynNetworkReader </code> interface of network reader task.
 * 
 * @author sabina
 *
 */
public interface DynNetworkReader extends Task 
{
	/**
	 * @see org.cytoscape.work.Task#run(org.cytoscape.work.TaskMonitor)
	 */
	@Override
	public void run(TaskMonitor tm) throws Exception;

}