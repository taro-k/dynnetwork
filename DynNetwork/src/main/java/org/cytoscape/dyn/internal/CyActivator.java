package org.cytoscape.dyn.internal;

import java.util.Properties;

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.dyn.internal.events.MenuActionLoadXGMML;
import org.cytoscape.dyn.internal.view.DynCytoPanel;
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
		CySwingApplication cytoscapeDesktopService = getService(context,CySwingApplication.class);
		
		DynCytoPanel<T> panel = new DynCytoPanel<T>(adapter.getTaskManager());
		MenuActionLoadXGMML<T> action = new MenuActionLoadXGMML<T>(cytoscapeDesktopService,adapter,panel);

		registerService(context,panel,CytoPanelComponent.class, new Properties());
    	registerService(context,action,CyAction.class, new Properties());
	}
}

