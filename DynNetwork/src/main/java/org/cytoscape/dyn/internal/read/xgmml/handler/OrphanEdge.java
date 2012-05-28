package org.cytoscape.dyn.internal.read.xgmml.handler;

import java.util.ArrayList;

import org.cytoscape.dyn.internal.events.DynNetworkEventManager;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;

/**
 * <code> OrphanEdge </code> is used to store edges connected to nodes that 
 * have not been yet defined. We will try to insert them once the parser reaches
 * the last command.
 * 
 * @author sabina
 *
 * @param <T>
 */
public final class OrphanEdge<T>
{
	private final CyNetwork currentNetwork;
	private final String id;
	private final String label;
	private final String source;
	private final String target;
	private final String start;
	private final String end;
	
	private final ArrayList<OrphanAttribute<T>> attributes;
	
	public OrphanEdge(CyNetwork currentNetwork, String id, String label,
			String source, String target, String start, String end)
	{
		this.currentNetwork = currentNetwork;
		this.id = id;
		this.label = label;
		this.source = source;
		this.target = target;
		this.start = start;
		this.end = end;
		attributes = new ArrayList<OrphanAttribute<T>>();
	}
	
	public void addToManager(DynNetworkEventManager manager)
	{
		CyEdge currentEdge = manager.addEdge(currentNetwork, id, label, source, target, start,  end);
		for (OrphanAttribute<T> attr : attributes)
			attr.addToManager(manager, currentEdge);
	}
	
	public void addAttribute(CyNetwork currentNetwork, String name, String value, String type, String start, String end)
	{
		attributes.add(new OrphanAttribute<T>(currentNetwork, name, value, type, start, end));
	}

}
