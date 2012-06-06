package org.cytoscape.dyn.internal.read.xgmml.handler;

import java.util.Stack;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.read.xgmml.ParseDynState;
import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.xml.sax.Attributes;

public final class DynHandlerXGMML<T> extends AbstractXGMMLSource<T> implements DynHandler 
{
	private final Stack<CyGroup> groupStack;
	private final Stack<OrphanEdge<T>> orphanEdgeList;
	
	private DynNetwork<T> currentNetwork;
	private CyGroup currentGroup;
	private CyNode currentNode;
	private CyEdge currentEdge;
	
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
	
	private int ID = 0;
	
	private String spaces = " ";
	private int line = 0;

	public DynHandlerXGMML()
	{
		super();
		groupStack = new Stack<CyGroup>();
		orphanEdgeList = new Stack<OrphanEdge<T>>();
		System.out.println("");
	}

	@Override
	public void handleStart(Attributes atts, ParseDynState current)
	{
		line++;
		System.out.println(spaces + "<" + current + "> (Line " + line + ")");
		spaces = spaces + " ";
		
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
			label = label==null?"dynamic network ("+(ID++)+")":label;
			id = id==null?label:id;
			directed = directed==null?"1":directed;
			currentNetwork = this.addGraph(id, label, start, end, directed);
			groupStack.push(null);
			break;
			
		case NODE_GRAPH:
			id = atts.getValue("id");
			label = atts.getValue("label");
			start = atts.getValue("start");
			end = atts.getValue("end");
			currentGroup = this.addGroup(currentNetwork, currentNode);
			groupStack.push(currentGroup);
			break;
			
		case NODE:
			id = atts.getValue("id");
			label = atts.getValue("label");
			start = atts.getValue("start");
			end = atts.getValue("end");
			id = id==null?label:id;
			currentNode = this.addNode(currentNetwork, currentGroup, id, label, start, end);
			break;
			
		case EDGE:
			id = atts.getValue("id");
			label = atts.getValue("label");
			source = atts.getValue("source");
			target = atts.getValue("target");
			start = atts.getValue("start");
			end = atts.getValue("end");
			label = label==null?source+"-"+target:label;
			id = id==null?label:id;
			currentEdge = this.addEdge(currentNetwork, id, label, source, target, start, end);
			if (currentEdge==null)
				orphanEdgeList.push(new OrphanEdge<T>(currentNetwork, id, label, source, target, start, end));
			break;
			
		case NET_ATT:
			name = atts.getValue("name");
			value = atts.getValue("value");
			type = atts.getValue("type");
			start = atts.getValue("start");
			end = atts.getValue("end");
			if (currentNetwork!= null && name!=null && value!=null && type!=null)
				this.addGraphAttribute(currentNetwork, name, value, type, start, end);
			break;
			
		case NODE_ATT:
			name = atts.getValue("name");
			value = atts.getValue("value");
			type = atts.getValue("type");
			start = atts.getValue("start");
			end = atts.getValue("end");
			if (currentNode!= null && name!=null && value!=null && type!=null)
				this.addNodeAttribute(currentNetwork, currentNode, name, value, type, start, end);
			break;
			
		case EDGE_ATT:
			name = atts.getValue("name");
			value = atts.getValue("value");
			type = atts.getValue("type");
			start = atts.getValue("start");
			end = atts.getValue("end");
			if (currentEdge!= null && name!=null && value!=null && type!=null)
				this.addEdgeAttribute(currentNetwork, currentEdge, name, value, type, start, end);
			else
				orphanEdgeList.peek().addAttribute(currentNetwork, name, value, type, start, end);
			break;
		}

	}

	@Override
	public void handleEnd(Attributes atts, ParseDynState current)
	{
		line++;
		spaces = spaces.substring(1);
		System.out.println(spaces + "<" + current + "/> (Line " + line + ")");
		
		switch(current)
		{
		case GRAPH:
			while (!orphanEdgeList.isEmpty())
				orphanEdgeList.pop().add(this);
			this.finalize(currentNetwork);
			break;
			
		case NODE_GRAPH:
			currentNode = groupStack.pop().getGroupNode();
            currentGroup = groupStack.peek();
			break;
		}
	}

	@Override
	protected CyEdge addEdge(DynNetwork<T> currentNetwork, String id, String label,
			String source, String target, String start, String end)
	{
		return sinkList.get(0).addedEdge(currentNetwork, id, label, source, target, start, end);
	}

	@Override
	protected void addEdgeAttribute(DynNetwork<T> network, CyEdge currentEdge,
			String name, String value, String Type, String start, String end)
	{
		sinkList.get(0).addedEdgeAttribute(network, currentEdge, name, value, Type, start, end);
	}
	

}
