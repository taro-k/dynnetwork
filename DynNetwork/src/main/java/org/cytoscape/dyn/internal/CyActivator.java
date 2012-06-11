package org.cytoscape.dyn.internal;

import java.util.Properties;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.events.SetCurrentNetworkViewListener;
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
import org.cytoscape.io.util.StreamUtil;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.util.swing.FileUtil;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.TunableSetter;
import org.osgi.framework.BundleContext;

public class CyActivator<T,C> extends AbstractCyActivator
{
	public CyActivator()
	{
		super();
	}

	public void start(BundleContext context)
	{
		CySwingApplication cytoscapeDesktopService = getService(context,CySwingApplication.class);
		CyApplicationManager cyApplicationManagerServiceRef = getService(context,CyApplicationManager.class);
		CyNetworkManager cyNetworkManagerServiceRef = getService(context,CyNetworkManager.class);
    	CyNetworkViewManager cyNetworkViewManagerServiceRef = getService(context,CyNetworkViewManager.class);
    	CyNetworkViewFactory cyNetworkViewFactoryServiceRef = getService(context,CyNetworkViewFactory.class);
    	CyNetworkFactory cyNetworkFactoryServiceRef = getService(context,CyNetworkFactory.class);
    	CyRootNetworkManager cyRootNetworkManagerServiceRef = getService(context,CyRootNetworkManager.class);
    	CyNetworkNaming cyNetworkNamingServiceRef = getService(context,CyNetworkNaming.class);
    	CyGroupManager groupManagerServiceRef = getService(context,CyGroupManager.class);
    	CyGroupFactory groupFactoryServiceRef = getService(context,CyGroupFactory.class);
    	TaskManager<T,C> taskManager = getService(context,TaskManager.class);
    	VisualMappingManager visualMappingServiceRef = getService(context,VisualMappingManager.class);
    	FileUtil fileUtil = getService(context,FileUtil.class);
    	StreamUtil streamUtil = getService(context,StreamUtil.class);
    	TunableSetter tunableSetterServiceRef = getService(context,TunableSetter.class);
    	
    	DynNetworkManagerImpl<T> dynNetManager = new DynNetworkManagerImpl<T>(cyNetworkManagerServiceRef);
		DynNetworkFactoryImpl<T> dynNetworkFactory = new DynNetworkFactoryImpl<T>(cyNetworkFactoryServiceRef,cyRootNetworkManagerServiceRef,groupManagerServiceRef,groupFactoryServiceRef,dynNetManager,cyNetworkNamingServiceRef);
		DynNetworkViewManagerImpl<T> dynNetViewManager = new DynNetworkViewManagerImpl<T>(cyNetworkViewManagerServiceRef);
    	DynNetworkViewFactoryImpl<T> dynNetworkViewFactory = new DynNetworkViewFactoryImpl<T>(dynNetViewManager, cyNetworkViewFactoryServiceRef, cyNetworkViewManagerServiceRef,visualMappingServiceRef);

    	DynCytoPanel<T,C> dynCytoPanel = new DynCytoPanel<T,C>(taskManager,cyApplicationManagerServiceRef,dynNetViewManager);
    	MenuActionLoadXGMML<T,C> action = new MenuActionLoadXGMML<T,C>(cytoscapeDesktopService,cyApplicationManagerServiceRef,dynCytoPanel,taskManager,dynNetManager,dynNetworkFactory,dynNetworkViewFactory,fileUtil,streamUtil,tunableSetterServiceRef);

		registerService(context,dynNetManager,DynNetworkManager.class, new Properties());
		registerService(context,dynNetworkFactory,DynNetworkFactory.class, new Properties());
		registerService(context,dynNetViewManager,DynNetworkViewManager.class, new Properties());
		registerService(context,dynNetworkViewFactory,DynNetworkViewFactoryImpl.class, new Properties());
		registerService(context,dynCytoPanel,CytoPanelComponent.class, new Properties());
    	registerService(context,action,CyAction.class, new Properties());
    	registerService(context,dynCytoPanel, SetCurrentNetworkViewListener.class, new Properties());
	}
	
}

