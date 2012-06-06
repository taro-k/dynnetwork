package org.cytoscape.dyn.internal.read;

import java.io.InputStream;

import org.cytoscape.io.read.CyNetworkReader;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.AbstractTask;

public abstract class AbstractDynNetworkReader extends AbstractTask implements CyNetworkReader
{
	protected InputStream inputStream;

	public AbstractDynNetworkReader(InputStream inputStream) 
	{
		this.inputStream = inputStream;
	}
	
	// this methods are not used anymore
	
	@Override
	public CyNetworkView buildCyNetworkView(CyNetwork network) 
	{
		return null;
	}

	@Override
	public CyNetwork[] getNetworks() 
	{
		return null;
	}
}
