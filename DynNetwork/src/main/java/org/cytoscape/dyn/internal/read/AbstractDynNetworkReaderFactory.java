package org.cytoscape.dyn.internal.read;

import org.cytoscape.io.CyFileFilter;
import org.cytoscape.io.read.AbstractInputStreamTaskFactory;

/**
 * <code> AbstractDynNetworkReaderFactory </code> abstract class for the implementation of a 
 * the reader factory.
 *  
 * @author sabina
 *
 */
public abstract class AbstractDynNetworkReaderFactory extends AbstractInputStreamTaskFactory
{
	public AbstractDynNetworkReaderFactory(CyFileFilter filter)
	{
		super(filter);
	}
}