package org.cytoscape.dyn.internal.read.xgmml.handler;

import org.cytoscape.dyn.internal.events.DynNetworkEventManagerImpl;
import org.cytoscape.dyn.internal.read.xgmml.ParseDynState;
import org.xml.sax.Attributes;

public interface DynHandler<T> {
	
	public void handleStart(String tag, Attributes atts, ParseDynState current);
	
	public void handleEnd(String tag, Attributes atts, ParseDynState current);
	
	public void setManager(DynNetworkEventManagerImpl<T> manager);

}
