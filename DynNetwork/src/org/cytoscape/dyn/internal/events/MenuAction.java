package org.cytoscape.dyn.internal.events;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.dyn.internal.loaddynnetwork.LoadDynNetworkFileTaskFactoryImpl;
import org.cytoscape.dyn.internal.read.xgmml.XGMMLDynFileFilter;
import org.cytoscape.dyn.internal.read.xgmml.XGMMLDynNetworkReaderFactory;
import org.cytoscape.dyn.internal.read.xgmml.XGMMLDynParser;
import org.cytoscape.dyn.internal.view.DynControlPanelTask;
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


// TODO: make bundle

/**
 * Launch the App from the menu "File/Dynamic XGMML Loader"
 */
public class MenuAction<T> extends AbstractCyAction
{

	private final CyAppAdapter adapter;
    private List<FileChooserFilter> filters;

    public MenuAction(CyAppAdapter adapter)
    {
        super("Dynamic XGMML Loader");
        this.adapter = adapter;
        setPreferredMenu("File");
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
    	DynNetworkEventManagerImpl<T> manager = new DynNetworkEventManagerImpl<T>(cyNetworkFactoryServiceRef,cyRootNetworkManagerServiceRef);
    	XGMMLDynParser<T> xgmmlParser = new XGMMLDynParser<T>(manager);
    	XGMMLDynFileFilter xgmmlFilter = new XGMMLDynFileFilter(new String[]{"xgmml","xml"}, new String[]{"text/xgmml","text/xgmml+xml"}, "XGMML files",DataCategory.NETWORK, streamUtil);
    	XGMMLDynNetworkReaderFactory<T> xgmmlNetworkReaderFactory = new XGMMLDynNetworkReaderFactory<T>(xgmmlFilter,cyNetworkViewFactoryServiceRef,cyNetworkFactoryServiceRef,manager,xgmmlParser,renderingEngineMgr);
    	filters = new ArrayList<FileChooserFilter>();
    	filters.add(new FileChooserFilter("XGMML", "xgmml"));

    	// load file
    	LoadDynNetworkFileTaskFactoryImpl<T> loadFactory = new LoadDynNetworkFileTaskFactoryImpl<T>(xgmmlNetworkReaderFactory,cyNetworkManagerServiceRef,cyNetworkViewManagerServiceRef,manager,cyPropertyServiceRef,cyNetworkNamingServiceRef, tunableSetterServiceRef, streamUtil);
    	File file = fileUtil.getFile(adapter.getCySwingApplication().getJFrame(), "Load Dynamic Network", FileUtil.LOAD, filters);

    	// dynamic viewer
    	TaskIterator iterator = new TaskIterator(loadFactory.creatTaskIterator(file).next(), new DynControlPanelTask<T>(adapter, manager));
    	adapter.getTaskManager().execute(iterator);
    }
}
