package org.cytoscape.dyn.internal.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.dyn.internal.loaddynnetwork.LoadDynNetworkFileTaskFactoryImpl;
import org.cytoscape.dyn.internal.model.DynNetworkFactory;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.read.xgmml.XGMMLDynFileFilter;
import org.cytoscape.dyn.internal.read.xgmml.XGMMLDynNetworkReaderFactory;
import org.cytoscape.dyn.internal.read.xgmml.XGMMLDynParser;
import org.cytoscape.dyn.internal.view.DynCytoPanel;
import org.cytoscape.dyn.internal.view.DynCytoPanelTask;
import org.cytoscape.group.CyGroupFactory;
import org.cytoscape.group.CyGroupManager;
import org.cytoscape.io.DataCategory;
import org.cytoscape.io.util.StreamUtil;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.property.CyProperty;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.util.swing.FileChooserFilter;
import org.cytoscape.util.swing.FileUtil;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TunableSetter;

/**
 * Launch from the menu "File/Dynamic XGMML Loader"
 */
public class MenuActionLoadXGMML<T> extends AbstractCyAction
{
	private CySwingApplication desktopApp;
	private final CytoPanel cytoPanelWest;
	private final CySwingAppAdapter adapter;
	private final DynCytoPanel<T> myDynPanel;
	private final DynNetworkFactory<T> dynNetworkFactory;
	
    private List<FileChooserFilter> filters;

    public MenuActionLoadXGMML(
    		CySwingApplication desktopApp,
    		CySwingAppAdapter adapter,
    		DynCytoPanel<T> myDynPanel,
    		DynNetworkFactory<T> dynNetworkFactory)
    {
        super("Dynamic XGMML Loader");
        setPreferredMenu("File");
        this.desktopApp = desktopApp;
        this.adapter = adapter;
        this.cytoPanelWest = this.desktopApp.getCytoPanel(CytoPanelName.WEST);
        this.myDynPanel = myDynPanel;
        this.dynNetworkFactory = dynNetworkFactory;
    }

    public void actionPerformed(ActionEvent e)
    {
    	// set up some variables
    	FileUtil fileUtil = adapter.getCyServiceRegistrar().getService(FileUtil.class);
    	StreamUtil streamUtil = adapter.getCyServiceRegistrar().getService(StreamUtil.class);
    	CyNetworkManager cyNetworkManagerServiceRef = adapter.getCyNetworkManager();
    	CyNetworkViewManager cyNetworkViewManagerServiceRef = adapter.getCyNetworkViewManager();
    	CyNetworkViewFactory cyNetworkViewFactoryServiceRef = adapter.getCyNetworkViewFactory();
    	CyNetworkFactory cyNetworkFactoryServiceRef = adapter.getCyNetworkFactory();
    	CyRootNetworkManager cyRootNetworkManagerServiceRef = adapter.getCyRootNetworkFactory();
    	CyProperty<Properties> cyPropertyServiceRef = adapter.getCoreProperties();
    	CyNetworkNaming cyNetworkNamingServiceRef = adapter.getCyServiceRegistrar().getService(CyNetworkNaming.class);
    	TunableSetter tunableSetterServiceRef = adapter.getCyServiceRegistrar().getService(TunableSetter.class);
    	RenderingEngineManager renderingEngineMgr = adapter.getRenderingEngineManager();
    	CyGroupManager groupManagerServiceRef = adapter.getCyGroupManager();
    	CyGroupFactory groupFactoryServiceRef = adapter.getCyGroupFactory();
    	XGMMLDynParser xgmmlParser = new XGMMLDynParser(dynNetworkFactory);
    	XGMMLDynFileFilter xgmmlFilter = new XGMMLDynFileFilter(new String[]{"xgmml","xml"}, new String[]{"text/xgmml","text/xgmml+xml"}, "XGMML files",DataCategory.NETWORK, streamUtil);
    	XGMMLDynNetworkReaderFactory xgmmlNetworkReaderFactory = new XGMMLDynNetworkReaderFactory(xgmmlFilter,xgmmlParser);
    	filters = new ArrayList<FileChooserFilter>();
    	filters.add(new FileChooserFilter("XGMML", "xgmml"));
    	
    	// TODO: fix static variables
    	DynInterval.minStartTime = Double.POSITIVE_INFINITY;
    	DynInterval.maxStartTime = Double.NEGATIVE_INFINITY;
    	DynInterval.minEndTime = Double.POSITIVE_INFINITY;
    	DynInterval.maxEndTime = Double.NEGATIVE_INFINITY;

    	// load file
    	LoadDynNetworkFileTaskFactoryImpl loadFactory = new LoadDynNetworkFileTaskFactoryImpl(xgmmlNetworkReaderFactory, tunableSetterServiceRef, streamUtil);
    	File file = fileUtil.getFile(adapter.getCySwingApplication().getJFrame(), "Load Dynamic Network", FileUtil.LOAD, filters);
    	TaskIterator iterator = new TaskIterator(loadFactory.creatTaskIterator(file).next(), new DynCytoPanelTask<T>(myDynPanel, cytoPanelWest));
    	adapter.getTaskManager().execute(iterator);
    }
}
