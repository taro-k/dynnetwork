package org.cytoscape.dyn.internal.view.layout;

import org.cytoscape.view.model.CyNetworkView;

public interface DynLayoutFactory<T>
{
	/**
	 * Create layout for view.
	 * @param view
	 * @return
	 */
	public DynLayoutImpl<T> createLayout(CyNetworkView view);
}
