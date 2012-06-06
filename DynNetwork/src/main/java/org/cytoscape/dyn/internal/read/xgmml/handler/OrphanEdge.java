package org.cytoscape.dyn.internal.read.xgmml.handler;

import java.util.ArrayList;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.model.CyEdge;

/**
 * <code> OrphanEdge </code> is used to store edges connected to nodes that 
 * have not been yet defined. We will try to insert them once the parser reaches
 * the last command.
 * 
 * @author sabina
 *
 */
public final class OrphanEdge<T>
{
	private final DynNetwork<T> currentNetwork;
	private final String id;
	private final String label;
	private final String source;
	private final String target;
	private final String start;
	private final String end;
	
	private final ArrayList<OrphanAttribute<T>> attributes;
	
	public OrphanEdge(DynNetwork<T> currentNetwork, String id, String label,
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
	
	public void add(DynHandlerXGMML<T> handler)
	{
		CyEdge currentEdge = handler.addEdge(currentNetwork, id, label, source, target, start,  end);
		for (OrphanAttribute<T> attr : attributes)
			attr.add(handler, currentEdge);
		
		if (currentEdge==null)
			System.out.println("\nXGMML Parser Warning: skipped edge id=" + id + 
					" label=" + label + " source=" + source + " target=" + target + " (missing nodes)");
	}
	
	public void addAttribute(DynNetwork<T> currentNetwork, String name, String value, String type, String start, String end)
	{
		attributes.add(new OrphanAttribute<T>(currentNetwork, name, value, type, start, end));
	}

}
