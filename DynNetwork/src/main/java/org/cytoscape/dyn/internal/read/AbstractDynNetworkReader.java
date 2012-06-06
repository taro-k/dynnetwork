package org.cytoscape.dyn.internal.read;

import java.io.InputStream;

import org.cytoscape.work.AbstractTask;

public abstract class AbstractDynNetworkReader extends AbstractTask
{
	protected InputStream inputStream;

	public AbstractDynNetworkReader(InputStream inputStream) 
	{
		this.inputStream = inputStream;
	}
}
