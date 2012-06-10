package org.cytoscape.dyn.internal.view.model;

import java.util.Collection;

import org.cytoscape.view.model.CyNetworkView;

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
	 * @param view
	 * @param dynNetworkView
	 */
	public void addDynNetworkView(CyNetworkView view, DynNetworkView<T> dynNetworkView);

	/**
	 * Get network view.
	 * @param view
	 * @return network view
	 */
	public DynNetworkView<T> getDynNetworkView(CyNetworkView view);
	
	/**
	 * Get all network views.
	 * @param dynNetwork
	 * @return network views
	 */
	public Collection<DynNetworkView<T>> getDynNetworkViews();
}
