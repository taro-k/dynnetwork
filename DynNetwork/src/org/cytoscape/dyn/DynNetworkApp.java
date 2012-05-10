package org.cytoscape.dyn;

import org.cytoscape.app.AbstractCyApp;
import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.dyn.internal.events.MenuAction;

/**
 * Dynamical Network App
 * @author sabina
 *
 * @param <T>
 */
public class DynNetworkApp<T> extends AbstractCyApp
{
	public DynNetworkApp(CyAppAdapter adapter)
	{
		super(adapter);
		adapter.getCySwingApplication().addAction(new MenuAction<T>(adapter));
	}
}
