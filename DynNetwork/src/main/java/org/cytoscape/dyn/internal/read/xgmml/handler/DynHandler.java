package org.cytoscape.dyn.internal.read.xgmml.handler;

import org.cytoscape.dyn.internal.read.xgmml.ParseDynState;
import org.xml.sax.Attributes;

public interface DynHandler
{	
	public void handleStart(Attributes atts, ParseDynState current);
	
	public void handleEnd(Attributes atts, ParseDynState current);
	
}
