package org.cytoscape.dyn.internal.view.layout;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

import org.cytoscape.view.model.CyNetworkView;

public final class DynLayoutManagerImpl<T> implements DynLayoutManager<T>
{
	private final Map<CyNetworkView, DynLayout<T>> dynLayoutMap;
	
	public DynLayoutManagerImpl()
	{
		this.dynLayoutMap = new WeakHashMap<CyNetworkView, DynLayout<T>>();
	}

	@Override
	public void addDynLayout(DynLayout<T> dynLayout)
	{
		this.dynLayoutMap.put(dynLayout.getNetworkView(), dynLayout);
	}

	@Override
	public DynLayout<T> getDynLayout(CyNetworkView view)
	{
		return dynLayoutMap.get(view);
	}
	
	@Override
	public Collection<DynLayout<T>> getDynNetworks() 
	{
		return dynLayoutMap.values();
	}
	
}

