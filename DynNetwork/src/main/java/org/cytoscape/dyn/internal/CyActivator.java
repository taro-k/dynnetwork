package org.cytoscape.dyn.internal;

import java.util.Properties;

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.dyn.internal.action.MenuActionLoadXGMML;
import org.cytoscape.dyn.internal.model.DynNetworkFactory;
import org.cytoscape.dyn.internal.model.DynNetworkFactoryImpl;
import org.cytoscape.dyn.internal.model.DynNetworkManager;
import org.cytoscape.dyn.internal.model.DynNetworkManagerImpl;
import org.cytoscape.dyn.internal.view.DynCytoPanel;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewFactoryImpl;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewManager;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewManagerImpl;
import org.cytoscape.group.CyGroupFactory;
import org.cytoscape.group.CyGroupManager;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.property.CyProperty;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
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
		CyNetworkManager cyNetworkManagerServiceRef = adapter.getCyNetworkManager();
    	CyNetworkViewManager cyNetworkViewManagerServiceRef = adapter.getCyNetworkViewManager();
    	CyNetworkViewFactory cyNetworkViewFactoryServiceRef = adapter.getCyNetworkViewFactory();
    	CyNetworkFactory cyNetworkFactoryServiceRef = adapter.getCyNetworkFactory();
    	CyRootNetworkManager cyRootNetworkManagerServiceRef = adapter.getCyRootNetworkFactory();
    	CyProperty<Properties> cyPropertyServiceRef = adapter.getCoreProperties();
    	CyNetworkNaming cyNetworkNamingServiceRef = adapter.getCyServiceRegistrar().getService(CyNetworkNaming.class);
    	CyGroupManager groupManagerServiceRef = adapter.getCyGroupManager();
    	CyGroupFactory groupFactoryServiceRef = adapter.getCyGroupFactory();
		
    	DynNetworkManagerImpl<T> dynNetManager = new DynNetworkManagerImpl<T>(cyNetworkManagerServiceRef);
		DynNetworkFactoryImpl<T> dynNetFactory = new DynNetworkFactoryImpl<T>(cyNetworkFactoryServiceRef,cyRootNetworkManagerServiceRef,groupManagerServiceRef,groupFactoryServiceRef,dynNetManager);
		DynNetworkViewManagerImpl<T> dynNetViewManager = new DynNetworkViewManagerImpl<T>(cyNetworkViewManagerServiceRef);
    	DynNetworkViewFactoryImpl<T> dynNetViewFactory = new DynNetworkViewFactoryImpl<T>(dynNetViewManager, cyNetworkViewFactoryServiceRef, cyNetworkViewManagerServiceRef);
		
    	DynCytoPanel<T> dyncytoPanel = new DynCytoPanel<T>(adapter.getTaskManager(),adapter.getCyApplicationManager(),dynNetManager,dynNetViewManager,dynNetViewFactory);
    	MenuActionLoadXGMML<T> action = new MenuActionLoadXGMML<T>(cytoscapeDesktopService,adapter,dyncytoPanel,dynNetFactory);

		registerService(context,dynNetManager,DynNetworkManager.class, new Properties());
		registerService(context,dynNetFactory,DynNetworkFactory.class, new Properties());
		registerService(context,dynNetViewManager,DynNetworkViewManager.class, new Properties());
		registerService(context,dynNetViewFactory,DynNetworkViewFactoryImpl.class, new Properties());
		registerService(context,dyncytoPanel,CytoPanelComponent.class, new Properties());
    	registerService(context,action,CyAction.class, new Properties());
	}
}

