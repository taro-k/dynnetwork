package org.cytoscape.dyn.internal.read.xgmml.handler;

import org.cytoscape.dyn.internal.action.DynNetworkEventManagerImpl;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.read.xgmml.ParseDynState;
import org.xml.sax.Attributes;

public abstract class AbstractDynHandler<T> implements DynHandler<T> {

	protected DynNetworkEventManagerImpl<T> manager;
	protected DynInterval<T> attributeValueUtil;

	public AbstractDynHandler() {
	}

	@Override
	abstract public void handleStart(Attributes atts, ParseDynState current);
	
	@Override
	abstract public void handleEnd(Attributes atts, ParseDynState current);

	@Override
	public void setManager(DynNetworkEventManagerImpl<T> manager) {
		this.manager = manager;
	}

}
