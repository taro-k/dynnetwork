package org.cytoscape.dyn.internal.read.xgmml.handler;

import java.util.Stack;

import org.cytoscape.dyn.internal.events.DynNetworkEventManagerImpl;
import org.cytoscape.dyn.internal.read.xgmml.ParseDynState;
import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.xml.sax.Attributes;

/**
 * Class to handle all the events coming from the parsing of the XGMML.
 * @author sabina
 *
 * @param <T>
 */
public final class DynHandlerAll<T> extends AbstractDynHandler<T>{

	private DynNetworkEventManagerImpl<T> manager;
	
	private CyNetwork currentNetwork;
	private CyGroup currentGroup;
	private CyNode currentNode;
	private CyEdge currentEdge;
	
	private final Stack<CyGroup> groupStack;
	
	private String directed;
	private String id;
	private String label;
	private String name;
	private String source;
	private String target;
	private String type;
	private String value;
	private String start;
	private String end;

	public DynHandlerAll(DynNetworkEventManagerImpl<T> manager)
	{
		super();
		this.manager = manager;
		groupStack = new Stack<CyGroup>();
	}

	@Override
	public void handleStart(Attributes atts, ParseDynState current)
	{
//		System.out.println("START: " + current);
		switch(current)
		{
		case NONE:
			break;
			
		case GRAPH:
			id = atts.getValue("id");
			label = atts.getValue("label");
			start = atts.getValue("start");
			end = atts.getValue("end");
			directed = atts.getValue("directed");
			currentNetwork = manager.addGraph(id==null?label:id, label, start, end, directed==null?"1":directed);
			groupStack.push(null);
			break;
			
		case NODE_GRAPH:
			id = atts.getValue("id");
			label = atts.getValue("label");
			start = atts.getValue("start");
			end = atts.getValue("end");
			currentGroup = manager.addGroup(currentNetwork, currentNode);
			groupStack.push(currentGroup);
			break;
			
		case NODE:
			id = atts.getValue("id");
			label = atts.getValue("label");
			start = atts.getValue("start");
			end = atts.getValue("end");
			currentNode = manager.addNode(currentNetwork, currentGroup, id==null?label:id, label, start, end);
			break;
			
		case EDGE:
			id = atts.getValue("id");
			label = atts.getValue("label");
			source = atts.getValue("source");
			target = atts.getValue("target");
			start = atts.getValue("start");
			end = atts.getValue("end");
			currentEdge = manager.addEdge(currentNetwork, id==null?source+"-"+target:id, label, source, target, start, end);
			break;
			
		case NET_ATT:
			name = atts.getValue("name");
			value = atts.getValue("value");
			type = atts.getValue("type");
			start = atts.getValue("start");
			end = atts.getValue("end");
			if (name!=null && value!=null && type!=null)
				manager.addGraphAttribute(currentNetwork, name, value, type, start, end);
			break;
			
		case NODE_ATT:
			name = atts.getValue("name");
			value = atts.getValue("value");
			type = atts.getValue("type");
			start = atts.getValue("start");
			end = atts.getValue("end");
			if (name!=null && value!=null && type!=null)
				manager.addNodeAttribute(currentNetwork, currentNode, name, value, type, start, end);
			break;
			
		case EDGE_ATT:
			name = atts.getValue("name");
			value = atts.getValue("value");
			type = atts.getValue("type");
			start = atts.getValue("start");
			end = atts.getValue("end");
			if (name!=null && value!=null && type!=null)
				manager.addEdgeAttribute(currentNetwork, currentEdge, name, value, type, start, end);
			break;
		}

	}

	@Override
	public void handleEnd(Attributes atts, ParseDynState current)
	{
//		System.out.println("END: " + current);
		switch(current)
		{
		case GRAPH:
			break;
			
		case NODE_GRAPH:
			currentNode = groupStack.pop().getGroupNode();
			currentGroup = groupStack.peek();
			break;

		}
	}

}
