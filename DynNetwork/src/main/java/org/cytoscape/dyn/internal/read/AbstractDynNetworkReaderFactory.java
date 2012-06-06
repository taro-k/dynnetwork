package org.cytoscape.dyn.internal.read;

import org.cytoscape.io.CyFileFilter;
import org.cytoscape.io.read.AbstractInputStreamTaskFactory;

public abstract class AbstractDynNetworkReaderFactory extends AbstractInputStreamTaskFactory
{
	public AbstractDynNetworkReaderFactory(CyFileFilter filter)
	{
		super(filter);
	}
}