package org.cytoscape.dyn.internal.read.xgmml.handler;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.model.CyEdge;

/**
 * <code> OrphanAttribute </code> is used to store attributes of 
 * {@link OrphanEdge}.
 * 
 * @author sabina
 * 
 */
public final class OrphanAttribute<T>
{
	private final DynNetwork<T> currentNetwork;
	private final String name;
	private final String value;
	private final String type;
	private final String start;
	private final String end;
	
	public OrphanAttribute(DynNetwork<T> currentNetwork, String name, String value,
			String type, String start, String end)
	{
		this.currentNetwork = currentNetwork;
		this.name = name;
		this.value = value;
		this.type = type;
		this.start = start;
		this.end = end;
	}
	
	public void add(DynHandlerXGMML<T> handler, CyEdge currentEdge)
	{
		if (currentEdge!= null && name!=null && value!=null && type!=null)
			handler.addEdgeAttribute(currentNetwork, currentEdge, name, value, type, start, end);
	}
	
}
