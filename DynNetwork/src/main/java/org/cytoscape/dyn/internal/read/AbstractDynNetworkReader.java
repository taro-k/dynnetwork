package org.cytoscape.dyn.internal.read;

import java.io.InputStream;

import org.cytoscape.work.AbstractTask;

/**
 * <code> AbstractDynNetworkReader </code> abstract class for the implementation of a 
 * the reader.
 * 
 * @author sabina
 *
 */
public abstract class AbstractDynNetworkReader extends AbstractTask implements DynNetworkReader
{
	protected InputStream inputStream;

	public AbstractDynNetworkReader(InputStream inputStream) 
	{
		this.inputStream = inputStream;
	}

}
