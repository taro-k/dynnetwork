package org.cytoscape.dyn.internal.loaddynnetwork;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.cytoscape.io.read.InputStreamTaskFactory;
import org.cytoscape.io.util.StreamUtil;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TunableSetter;

/**
 * <code> LoadDynNetworkFileTaskFactoryImpl </code> implements the interface 
 * {@link LoadDynNetworkFileTaskFactory}.
 * 
 * @author sabina
 *
 */
public final class LoadDynNetworkFileTaskFactoryImpl extends AbstractTaskFactory implements LoadDynNetworkFileTaskFactory {

	private InputStreamTaskFactory factory;
	private final TunableSetter tunableSetter;
	private final StreamUtil streamUtil;

	public LoadDynNetworkFileTaskFactoryImpl(
			InputStreamTaskFactory factory, 
			TunableSetter tunableSetter,
			StreamUtil streamUtil)
	{	
		this.factory = factory;
		this.tunableSetter = tunableSetter;
		this.streamUtil = streamUtil;
	}
	
	public TaskIterator createTaskIterator()
	{
		return new TaskIterator(1, new LoadDynNetworkFileTask(factory, streamUtil));
	}

	@Override
	public TaskIterator creatTaskIterator(File file)
	{
		final Map<String, Object> m = new HashMap<String, Object>();
		m.put("file", file);
		return tunableSetter.createTaskIterator(this.createTaskIterator(), m); 
	}
}