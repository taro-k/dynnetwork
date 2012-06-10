package org.cytoscape.dyn.internal.loaddynnetwork;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.dyn.internal.model.DynNetworkManager;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewFactory;
import org.cytoscape.work.TaskIterator;

/**
 * <code> LoadDynNetworkViewFactoryImpl </code> implements the interface 
 * {@link LoadDynNetworkFileTaskFactory}.
 * 
 * @author sabina
 *
 */
public class LoadDynNetworkViewFactoryImpl<T> implements LoadDynNetworkViewFactory<T>
{
	private final CyApplicationManager appManager;
	private final DynNetworkManager<T> dynNetworkManager;
	private final DynNetworkViewFactory<T> dynNetworkViewFactory;

	public LoadDynNetworkViewFactoryImpl(
			CyApplicationManager appManager,
			DynNetworkManager<T> dynNetworkManager,
			DynNetworkViewFactory<T> dynNetworkViewFactory)
	{
		this.appManager = appManager;
		this.dynNetworkManager = dynNetworkManager;
		this.dynNetworkViewFactory = dynNetworkViewFactory;
	}

	@Override
	public TaskIterator creatTaskIterator()
	{
		return new TaskIterator(1, new LoadDynNetworkViewTask<T>(appManager, dynNetworkManager, dynNetworkViewFactory));
	}

}
