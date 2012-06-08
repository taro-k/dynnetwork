package org.cytoscape.dyn.internal.event;

import java.util.concurrent.Callable;


/**
 * <code> DynEvent </code> is the interface for the Callable event 
 * that is used to generate graph events between a {@link Source}
 * and a {@link Sink}.
 * 
 * @author sabina
 *
 */
public interface DynEvent extends Callable<Object>
{
	/**
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Object call() throws Exception;
}
