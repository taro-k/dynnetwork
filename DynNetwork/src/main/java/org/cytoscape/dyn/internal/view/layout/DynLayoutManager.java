package org.cytoscape.dyn.internal.view.layout;

import java.util.Collection;

import org.cytoscape.view.model.CyNetworkView;

public interface DynLayoutManager<T> 
{
	/**
	 * Add layout.
	 * @param dynLayout
	 */
	public void addDynLayout(DynLayout<T> dynLayout);

	/**
	 * Get network view.
	 * @param network
	 * @return network
	 */
	public DynLayout<T> getDynLayout(CyNetworkView view);
	
	/**
	 * Get all dynLayouts.
	 * @return networks
	 */
	public Collection<DynLayout<T>> getDynNetworks();
	
}
