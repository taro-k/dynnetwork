package org.cytoscape.dyn.internal.read.xgmml.handler;

import org.cytoscape.dyn.internal.events.DynNetworkEventManagerImpl;
import org.cytoscape.dyn.internal.read.xgmml.ParseDynState;
import org.xml.sax.Attributes;

/**
 * Class to handle all the events coming from the parsing of the XGMML
 * @author sabina
 *
 * @param <T>
 */
public final class DynHandlerAll<T> extends AbstractDynHandler<T>{

	private DynNetworkEventManagerImpl<T> manager;
	
	String id;
	String label;
	String name;
	String source;
	String target;
	String type;
	String value;
	String start;
	String end;

	public DynHandlerAll(DynNetworkEventManagerImpl<T> manager) {
		super();
		this.manager = manager;
	}

	@Override
	public void handleStart(String tag, Attributes atts, ParseDynState current)
	{
		switch(current)
		{
		case NONE:
			break;
			
		case GRAPH:
			id = atts.getValue("id");
			label = atts.getValue("label");
			start = atts.getValue("start");
			end = atts.getValue("end");
			manager.addGraph(id==null?label:id, label, start, end);
			break;
			
		case NODE:
			id = atts.getValue("id");
			label = atts.getValue("label");
			start = atts.getValue("start");
			end = atts.getValue("end");
			manager.addNode(id==null?label:id, label, start, end);
			break;
			
		case EDGE:
			id = atts.getValue("id");
			label = atts.getValue("label");
			source = atts.getValue("source");
			target = atts.getValue("target");
			start = atts.getValue("start");
			end = atts.getValue("end");
			manager.addEdge(id==null?source+"-"+target:id, label, source, target, start, end);
			break;
			
		case NET_ATT:
			name = atts.getValue("name");
			value = atts.getValue("value");
			type = atts.getValue("type");
			start = atts.getValue("start");
			end = atts.getValue("end");
			manager.addGraphAttribute(name, value, type, start, end);
			break;
			
		case NODE_ATT:
			name = atts.getValue("name");
			value = atts.getValue("value");
			type = atts.getValue("type");
			start = atts.getValue("start");
			end = atts.getValue("end");
			manager.addNodeAttribute(name, value, type, start, end);
			break;
			
		case EDGE_ATT:
			name = atts.getValue("name");
			value = atts.getValue("value");
			type = atts.getValue("type");
			start = atts.getValue("start");
			end = atts.getValue("end");
			manager.addEdgeAttribute(name, value, type, start, end);
			break;
		}

	}

	@Override
	public void handleEnd(String tag, Attributes atts, ParseDynState current)
	{
		switch(current)
		{
		case NONE:
			break;
			
		case GRAPH:
			break;
		}

	}


	

}
