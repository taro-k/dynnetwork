package org.cytoscape.dyn.internal;

import java.util.Properties;

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.dyn.internal.events.MenuActionLoadXGMML;
import org.cytoscape.service.util.AbstractCyActivator;
import org.osgi.framework.BundleContext;

public class CyActivator<T> extends AbstractCyActivator
{
	public CyActivator()
	{
		super();
	}

	public void start(BundleContext context)
	{
		CySwingAppAdapter adapter = (CySwingAppAdapter) getService(context,CySwingAppAdapter.class);
		MenuActionLoadXGMML<T> action = new MenuActionLoadXGMML<T>(adapter);
		Properties properties = new Properties();
		registerAllServices(context, action, properties);
	}
}

