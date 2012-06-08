package org.cytoscape.dyn.internal.model;

import org.cytoscape.dyn.internal.event.Sink;


/**
 * <code> DynIntervalTreeFactory </code> is a singleton factory object used for instantiating 
 * {@link DynNetwork} objects. The <code> DynIntervalTreeFactory </code> should be available 
 * as an OSGi service.
 */
public interface DynNetworkFactory<T> extends Sink<T>
{

}
