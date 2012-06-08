package org.cytoscape.dyn.internal.view.model;

import java.util.Collection;

import org.cytoscape.dyn.internal.model.DynNetwork;

/**
 * <code> DynNetworkViewManager </code> is the interface of the 
 * {@link DynNetworkView}s manager.
 * 
 * @author sabina
 *
 * @param <T>
 */
public interface DynNetworkViewManager<T>
{
	/**
	 * Add network view
	 * @param dynNetwork
	 * @param dynNetworkView
	 */
	public void addDynNetworkView(DynNetwork<T> dynNetwork, DynNetworkView<T> dynNetworkView);

	/**
	 * Get network view.
	 * @param dynNetwork
	 * @return network view
	 */
	public DynNetworkView<T> getDynNetworkView(DynNetwork<T> dynNetwork);
	
	/**
	 * Get all network views.
	 * @param dynNetwork
	 * @return network views
	 */
	public Collection<DynNetworkView<T>> getDynNetworkViews();
}
