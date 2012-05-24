package org.cytoscape.dyn.internal.events;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;

/**
 * <code> OrphanAttribute </code> is used to store attributes of 
 * <code> OrphanEdge </code>.
 * 
 * @author sabina
 *
 * @param <T>
 */
public final class OrphanAttribute<T>
{
	private final CyNetwork currentNetwork;
	private final String name;
	private final String value;
	private final String type;
	private final String start;
	private final String end;
	
	public OrphanAttribute(CyNetwork currentNetwork, String name, String value,
			String type, String start, String end)
	{
		super();
		this.currentNetwork = currentNetwork;
		this.name = name;
		this.value = value;
		this.type = type;
		this.start = start;
		this.end = end;
	}
	
	public void addToManager(AbstractDynNetworkEventManager<T> manager, CyEdge currentEdge)
	{
		if (currentEdge!= null && name!=null && value!=null && type!=null)
			manager.addEdgeAttribute(currentNetwork, currentEdge, name, value, type, start, end);
	}
	
}
