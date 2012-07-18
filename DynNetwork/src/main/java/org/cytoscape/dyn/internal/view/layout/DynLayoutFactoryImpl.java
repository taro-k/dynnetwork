package org.cytoscape.dyn.internal.view.layout;

import org.cytoscape.view.model.CyNetworkView;

public class DynLayoutFactoryImpl <T> implements DynLayoutFactory<T> 
{
	private final DynLayoutManager<T> layoutManager;
	
	public DynLayoutFactoryImpl(DynLayoutManager<T> layoutManager)
	{
		this.layoutManager = layoutManager;
	}
	
	@Override
	public DynLayoutImpl<T> createLayout(CyNetworkView view)
	{
		DynLayoutImpl<T> layout = new DynLayoutImpl<T>(view);
		layoutManager.addDynLayout(layout);
		return layout;
	}

}