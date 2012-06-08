package org.cytoscape.dyn.internal.loaddynnetwork;

import java.util.ArrayList;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.dyn.internal.event.Sink;
import org.cytoscape.dyn.internal.event.Source;
import org.cytoscape.dyn.internal.model.DynNetworkManager;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewFactory;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskMonitor;

/**
 * <code> LoadNetworkViewTask </code> implements {@link Task} and is responsible
 * for creating the network view {@link DynNetworkView} from a network {@link DynNetwork}.
 * 
 * @author sabina
 *
 */
public final class LoadNetworkViewTask<T> extends AbstractTask implements Source<T> 
{
	private final CyApplicationManager appManager;
	private final DynNetworkManager<T> dynNetworkManager;
	
	private ArrayList<Sink<T>> sinkList = new ArrayList<Sink<T>>(1);
	
	public LoadNetworkViewTask(
			final CyApplicationManager appManager,
			final DynNetworkManager<T> dynNetworkManager,
			final DynNetworkViewFactory<T> dynNetworkViewFactory)
	{
		this.appManager = appManager;
		this.dynNetworkManager = dynNetworkManager;
		this.addSink(dynNetworkViewFactory);
	}
	
	public void run(TaskMonitor tm) throws Exception
	{
		tm.setProgress(0.0);
		sinkList.get(0).createView(dynNetworkManager.getDynNetwork(appManager.getCurrentNetwork()));
		tm.setProgress(1.0);
	}

	@Override
	public void addSink(Sink<T> sink) 
	{
		sinkList.add(sink);
	}
	
	@Override
	public void removeSink(Sink<T> sink) 
	{
		if (sinkList.contains(sink))
			sinkList.remove(sink);
	}
	
}
