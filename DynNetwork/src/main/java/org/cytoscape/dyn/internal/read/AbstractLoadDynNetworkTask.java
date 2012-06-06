package org.cytoscape.dyn.internal.read;

import java.net.URI;

import org.cytoscape.io.read.CyNetworkReader;
import org.cytoscape.io.read.InputStreamTaskFactory;
import org.cytoscape.io.util.StreamUtil;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;

public abstract class AbstractLoadDynNetworkTask extends AbstractTask
{	
	protected CyNetworkReader reader;
	protected InputStreamTaskFactory factory;
	protected URI uri;
	protected TaskMonitor taskMonitor;
	protected String name;
	protected boolean interrupted = false;
	protected StreamUtil streamUtil;

	public AbstractLoadDynNetworkTask(
			final InputStreamTaskFactory factory,
			StreamUtil streamUtil)
	{
		this.factory = factory;
		this.streamUtil = streamUtil;
	}
	
	@ProvidesTitle
	public String getTitle()
	{
		return "Import Network";
	}
	
}
