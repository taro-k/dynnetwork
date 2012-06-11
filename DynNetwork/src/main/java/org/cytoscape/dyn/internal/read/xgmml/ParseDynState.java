package org.cytoscape.dyn.internal.read.xgmml;

public enum ParseDynState 
{
	
	// Graph elements
    NONE("none"),
    GRAPH("Graph Element"),
    NODE_GRAPH("Node Graph"),
    NODE("Node Element"),
    EDGE("Edge Element"),
    
    // Graph attributes
    NET_ATT("Network Attribute"),
    NODE_ATT("Node Attribute"),
    EDGE_ATT("Edge Attribute"),

    // Graphical attribute
    NET_GRAPHICS("Network Graphics"),
    NODE_GRAPHICS("Node Graphics"),
    EDGE_GRAPHICS("Edge Graphics"),
    LOCKED_VISUAL_PROP_ATT("Bypass Attribute"),
    
    // Others (not implemented yet)
    EDGE_BEND("Edge Bend"),
    EDGE_HANDLE("Edge Handle"),
    EDGE_HANDLE_ATT("Edge Handle Attribute"),
    RDF("RDF"),
    RDF_DESC("RDF Description"),
    ANY("any");

    private String name;

    private ParseDynState(String str) 
    {
        name = str;
    }

    public String toString() 
    {
        return name;
    }
}
