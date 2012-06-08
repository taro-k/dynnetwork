package org.cytoscape.dyn.internal.read.xgmml.handler;

import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.EDGE;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.EDGE_ATT;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.EDGE_GRAPHICS;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.GRAPH;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.NET_ATT;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.NET_GRAPHICS;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.NODE;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.NODE_ATT;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.NODE_GRAPH;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.NODE_GRAPHICS;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.NONE;

import java.util.HashMap;
import java.util.Map;

import org.cytoscape.dyn.internal.model.DynNetworkFactory;
import org.cytoscape.dyn.internal.read.xgmml.ParseDynState;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <code> DynHandlerXGMMLFactory </code> is the factory for the event Handler. 
 * Implements the finite state machine states and transition rules
 * for start elements and end elements from the SAX Parser.
 * @author sabina
 *
 * @param <T>
 */
public final class DynHandlerXGMMLFactory<T>
{
	
	private Map<ParseDynState, Map<String, ParseDynState>> startParseMap;
	
	private DynHandlerXGMML<T> handler;
	
	public DynHandlerXGMMLFactory(DynNetworkFactory<T> sink)
	{
		handler = new DynHandlerXGMML<T>(sink);
		startParseMap = new HashMap<ParseDynState, Map<String, ParseDynState>>();
		buildMap(createStartParseTable(), startParseMap);
	}
	
	public ParseDynState handleStartState(ParseDynState current, String tag, Attributes atts) throws SAXException
	{
		current = startParseMap.get(current).get(tag);
		handler.handleStart(atts, current);
		return current;
	}
	
	public ParseDynState handleEndState(ParseDynState current, String tag, Attributes atts) throws SAXException
	{
		handler.handleEnd(atts, current);
		return current;
	}

	private Object[][] createStartParseTable()
	{

		final Object[][] tbl =
		{
				
				// Handle graphs
				{ NONE, "graph", GRAPH, null },
				{ GRAPH, "att", NET_ATT, null },
				{ GRAPH, "node", NODE, null },
				{ GRAPH, "edge", EDGE, null },
				{ GRAPH, "graphics", NET_GRAPHICS, null },
				{ NET_GRAPHICS, "att", NET_GRAPHICS, null },
				
				// Handle subgraphs
				{ NODE_ATT, "graph", NODE_GRAPH, null },
				{ NODE_GRAPH, "att", NET_ATT, null },
				{ NODE_GRAPH, "node", NODE, null },
				{ NODE_GRAPH, "edge", EDGE, null },
				{ NODE_GRAPH, "graphics", NET_GRAPHICS, null },
				
				// Handle nodes
				{ NODE, "att", NODE_ATT, null },
				{ NODE, "graphics", NODE_GRAPHICS, null },
				{ NODE_GRAPHICS, "att", NODE_GRAPHICS, null },
				
				// Handle edges
				{ EDGE, "att", EDGE_ATT, null },
				{ EDGE, "graphics", EDGE_GRAPHICS, null },
				{ EDGE_GRAPHICS, "att", EDGE_GRAPHICS, null } };
		
		return tbl;
	}

	private void buildMap(Object[][] table, Map<ParseDynState, Map<String, ParseDynState>> map)
	{
		int size = table.length;
		Map<String, ParseDynState> internalMap = null;

		for (int i = 0; i < size; i++)
		{
			internalMap = map.get((ParseDynState) table[i][0]);
			if (internalMap == null)
			{
				internalMap = new HashMap<String, ParseDynState>();
			}
			internalMap.put((String) table[i][1], (ParseDynState) table[i][2]);
			map.put((ParseDynState) table[i][0], internalMap);	
		}

	}
}
