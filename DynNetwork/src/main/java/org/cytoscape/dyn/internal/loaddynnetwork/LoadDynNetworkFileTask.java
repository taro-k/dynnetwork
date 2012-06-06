package org.cytoscape.dyn.internal.loaddynnetwork;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

import org.cytoscape.dyn.internal.read.AbstractLoadDynNetworkTask;
import org.cytoscape.io.read.CyNetworkReader;
import org.cytoscape.io.read.InputStreamTaskFactory;
import org.cytoscape.io.util.StreamUtil;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

public final class LoadDynNetworkFileTask extends AbstractLoadDynNetworkTask 
{
	@Tunable(description = "Network file to load", params = "fileCategory=network;input=true")
	public File file;
	
	@ProvidesTitle
	public String getTitle() 
	{
		return "Load Network from File";
	}
	
	public LoadDynNetworkFileTask(
			final InputStreamTaskFactory factory, 
			StreamUtil streamUtil)
	{
		super(factory, streamUtil);
	}
	
	public void run(TaskMonitor taskMonitor) throws Exception
	{
		this.taskMonitor = taskMonitor;
		
		if (file == null)
			throw new NullPointerException("No file specified!");
		
		InputStream stream = streamUtil.getInputStream(file.toURI().toURL());
		if (!stream.markSupported())
			stream = new BufferedInputStream(stream);

		reader = (CyNetworkReader) factory.createTaskIterator(stream, file.getName()).next();

		if (cancelled)
			return;

		if (factory == null)
			throw new NullPointerException("Failed to find appropriate reader for file: " + file);
		
		insertTasksAfterCurrentTask(reader);

	}
	
}
