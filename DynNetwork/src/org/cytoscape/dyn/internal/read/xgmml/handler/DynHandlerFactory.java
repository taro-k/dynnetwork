package org.cytoscape.dyn.internal.read.xgmml.handler;

import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.EDGE;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.EDGE_ATT;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.EDGE_BEND;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.EDGE_GRAPHICS;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.EDGE_HANDLE;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.GRAPH;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.LIST_ATT;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.LIST_ELEMENT;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.NET_ATT;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.NODE;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.NODE_ATT;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.NODE_GRAPH;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.NODE_GRAPHICS;
import static org.cytoscape.dyn.internal.read.xgmml.ParseDynState.NONE;

import java.util.HashMap;
import java.util.Map;

import org.cytoscape.dyn.internal.events.DynNetworkEventManagerImpl;
import org.cytoscape.dyn.internal.read.xgmml.ParseDynState;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Factory for event Handler
 * @author sabina
 *
 * @param <T>
 */
public final class DynHandlerFactory<T> {
	
	private Map<ParseDynState, Map<String, ParseDynState>> startParseMap;
	
	private DynHandlerAll<T> handler;
	
	public DynHandlerFactory(DynNetworkEventManagerImpl<T> manager)
	{
		handler = new DynHandlerAll<T>(manager);
		startParseMap = new HashMap<ParseDynState, Map<String, ParseDynState>>();
		buildMap(createStartParseTable(), startParseMap);
	}
	
	public ParseDynState handleStartState(ParseDynState current, String tag, Attributes atts) throws SAXException {
		current = startParseMap.get(current).get(tag);
		handler.handleStart(tag, atts, current);
		return current;
	}
	
	public ParseDynState handleEndState(ParseDynState current, String tag, Attributes atts) throws SAXException {
		handler.handleEnd(tag, atts, current);
		return current;
	}

	private Object[][] createStartParseTable() {

		final Object[][] tbl = {
				// Graph
				{ NONE, "graph", GRAPH, null },
				{ GRAPH, "att", NET_ATT, null },
				// Sub-graphs
				{ NET_ATT, "graph", GRAPH, null },
				// Handle nodes
				{ GRAPH, "node", NODE, null },
				{ NODE_GRAPH, "node", NODE, null },
				{ NODE, "graphics", NODE_GRAPHICS, null },
				{ NODE, "att", NODE_ATT, null },
				{ NODE_ATT, "graph", NODE_GRAPH, null },
				{ NODE_GRAPH, "att", NET_ATT, null },
				{ NODE_GRAPHICS, "att", NODE_GRAPHICS, null },
				// Handle edges
				{ GRAPH, "edge", EDGE, null },
				{ NODE_GRAPH, "edge", EDGE, null },
				{ EDGE, "att", EDGE_ATT, null },
				{ EDGE, "graphics", EDGE_GRAPHICS, null },
				{ EDGE_GRAPHICS, "att", EDGE_GRAPHICS, null },
				{ EDGE_BEND, "att", EDGE_HANDLE, null },
				{ EDGE_HANDLE, "att", EDGE_HANDLE, null },
				{ LIST_ATT, "att", LIST_ELEMENT, null },
				{ LIST_ELEMENT, "att", LIST_ELEMENT, null } };
		return tbl;
	}

	private void buildMap(Object[][] table, Map<ParseDynState, Map<String, ParseDynState>> map) {
		int size = table.length;
		Map<String, ParseDynState> internalMap = null;

		for (int i = 0; i < size; i++) {
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
