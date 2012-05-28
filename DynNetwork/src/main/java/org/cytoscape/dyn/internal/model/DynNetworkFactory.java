package org.cytoscape.dyn.internal.model;

import org.cytoscape.model.CyNetwork;


/**
 * <code> DynIntervalTreeFactory </code> is a singleton factory object used for instantiating 
 * {@link DynNetwork} objects. The <code> DynIntervalTreeFactory </code> should be available 
 * as an OSGi service.
 */
public interface DynNetworkFactory<T>
{
	/**
	 * Returns a new, empty {@link DynNetwork} object. 
	 * @return A new, empty {@link DynNetwork} object. 
	 */
	DynNetwork<T> createDynNetwork(CyNetwork network);

}
