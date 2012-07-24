/*
 * DynNetwork plugin for Cytoscape 3.0 (http://www.cytoscape.org/).
 * Copyright (C) 2012 Sabina Sara Pfister
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

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
import org.cytoscape.dyn.internal.view.gui.AdvancedDynCytoPanel;
import org.cytoscape.dyn.internal.view.layout.DynLayoutFactoryImpl;
import org.cytoscape.dyn.internal.view.layout.DynLayoutManager;
import org.cytoscape.dyn.internal.view.layout.DynLayoutManagerImpl;
import org.cytoscape.dyn.internal.view.layout.algorithm.DynForceLayoutAlgorithm;
import org.cytoscape.dyn.internal.view.layout.algorithm.DynRandomLayoutAlgorithm;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewFactoryImpl;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewManager;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewManagerImpl;
import org.cytoscape.dyn.internal.view.vizmap.mapping.DynContinousMappingFactory;
import org.cytoscape.dyn.internal.view.vizmap.mapping.DynDiscreteMappingFactory;
import org.cytoscape.dyn.internal.view.vizmap.mapping.DynPassthroughMappingFactory;
import org.cytoscape.group.CyGroupFactory;
import org.cytoscape.group.CyGroupManager;
import org.cytoscape.group.events.GroupCollapsedListener;
import org.cytoscape.io.util.StreamUtil;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.util.swing.FileUtil;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.TunableSetter;
import org.cytoscape.work.undo.UndoSupport;
import org.osgi.framework.BundleContext;

/**
 * <code> CyActivator </code> for DynNetwork plugin.
 * 
 * @author sabina
 *
 * @param <T>
 * @param <C>
 */
public class CyActivator<T,C> extends AbstractCyActivator
{
	/**
	 * <code> CyActivator </code> Constructor
	 */
	public CyActivator()
	{
		super();
	}

	/**
	 * Start bundle.
	 */
	@SuppressWarnings("unchecked")
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
    	UndoSupport undo = getService(context,UndoSupport.class);
    	
    	DynNetworkManagerImpl<T> dynNetManager = new DynNetworkManagerImpl<T>(cyNetworkManagerServiceRef);
		DynNetworkFactoryImpl<T> dynNetworkFactory = new DynNetworkFactoryImpl<T>(cyNetworkFactoryServiceRef,cyRootNetworkManagerServiceRef,groupManagerServiceRef,groupFactoryServiceRef,dynNetManager,cyNetworkNamingServiceRef);
		DynNetworkViewManagerImpl<T> dynNetViewManager = new DynNetworkViewManagerImpl<T>(cyNetworkViewManagerServiceRef);
    	DynNetworkViewFactoryImpl<T> dynNetworkViewFactory = new DynNetworkViewFactoryImpl<T>(dynNetViewManager, cyNetworkViewFactoryServiceRef, cyNetworkViewManagerServiceRef,visualMappingServiceRef);
    	
    	DynLayoutManagerImpl dynLayoutManager = new DynLayoutManagerImpl();
    	DynLayoutFactoryImpl dynLayoutFactory = new DynLayoutFactoryImpl(dynLayoutManager);
    	AdvancedDynCytoPanel<T,C> dynCytoPanel = new AdvancedDynCytoPanel<T,C>(taskManager,cyApplicationManagerServiceRef,dynNetViewManager,dynLayoutManager);
    	CyLayoutAlgorithm dynRandomLayout = new DynRandomLayoutAlgorithm<T,C>("Dynamic Layouts", "Random Dynamic",undo,dynCytoPanel,dynLayoutFactory);
    	CyLayoutAlgorithm dynForceLayout = new DynForceLayoutAlgorithm<T,C>("Dynamic Layouts", "Force Dynamic",undo,dynCytoPanel,dynLayoutFactory);
    	
    	MenuActionLoadXGMML<T,C> action = new MenuActionLoadXGMML<T,C>(cytoscapeDesktopService,cyApplicationManagerServiceRef,dynCytoPanel,taskManager,dynNetManager,dynNetworkFactory,dynNetworkViewFactory,fileUtil,streamUtil,tunableSetterServiceRef);

    	Properties myLayoutProps = new Properties();
        myLayoutProps.setProperty("preferredMenu","Dynamic Layouts");

		registerService(context,dynNetManager,DynNetworkManager.class, new Properties());
		registerService(context,dynNetworkFactory,DynNetworkFactory.class, new Properties());
		registerService(context,dynNetViewManager,DynNetworkViewManager.class, new Properties());
		registerService(context,dynNetworkViewFactory,DynNetworkViewFactoryImpl.class, new Properties());
		registerService(context,dynCytoPanel,CytoPanelComponent.class, new Properties());
    	registerService(context,action,CyAction.class, new Properties());   	
    	registerService(context,dynCytoPanel,SetCurrentNetworkViewListener.class, new Properties());
    	registerService(context,dynCytoPanel,GroupCollapsedListener.class, new Properties());
    	registerService(context,dynRandomLayout,CyLayoutAlgorithm.class, myLayoutProps);
    	registerService(context,dynForceLayout,CyLayoutAlgorithm.class, myLayoutProps);
    	registerService(context,dynLayoutManager,DynLayoutManager.class, new Properties());

	}
	
}

