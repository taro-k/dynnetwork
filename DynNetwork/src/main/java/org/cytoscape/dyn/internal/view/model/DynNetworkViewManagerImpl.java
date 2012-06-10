package org.cytoscape.dyn.internal.view.model;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewManager;

/**
 * <code> DynNetworkManagerImpl </code> implements the interface
 * {@link DynNetworkViewManager} for managing multiple {@link DynNetworkView}s.
 *  
 * @author sabina
 * 
 */
public final class DynNetworkViewManagerImpl<T> implements DynNetworkViewManager<T>
{
	private final CyNetworkViewManager cyNetworkViewManager;
	private final Map<CyNetworkView, DynNetworkView<T>> dynNetworkViewMap;

	public DynNetworkViewManagerImpl(final CyNetworkViewManager cyNetworkViewManager) 
	{
		this.cyNetworkViewManager = cyNetworkViewManager;
		this.dynNetworkViewMap = new WeakHashMap<CyNetworkView, DynNetworkView<T>>();
	}
	
	@Override
	public void addDynNetworkView(CyNetworkView view, DynNetworkView<T> dynNetworkView)
	{
		this.cyNetworkViewManager.addNetworkView(dynNetworkView.getNetworkView());
		this.dynNetworkViewMap.put(view, dynNetworkView);
	}

	@Override
	public DynNetworkView<T> getDynNetworkView(CyNetworkView view)
	{
		return dynNetworkViewMap.get(view);
	}

	@Override
	public Collection<DynNetworkView<T>> getDynNetworkViews() 
	{
		return dynNetworkViewMap.values();
	}	
	
}
