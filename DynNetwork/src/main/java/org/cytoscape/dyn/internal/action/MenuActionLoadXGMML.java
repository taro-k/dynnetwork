package org.cytoscape.dyn.internal.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.dyn.internal.loaddynnetwork.LoadDynNetworkFileTaskFactoryImpl;
import org.cytoscape.dyn.internal.loaddynnetwork.LoadNetworkViewTask;
import org.cytoscape.dyn.internal.model.DynNetworkFactory;
import org.cytoscape.dyn.internal.model.DynNetworkManager;
import org.cytoscape.dyn.internal.read.xgmml.XGMMLDynFileFilter;
import org.cytoscape.dyn.internal.read.xgmml.XGMMLDynNetworkReaderFactory;
import org.cytoscape.dyn.internal.read.xgmml.XGMMLDynParser;
import org.cytoscape.dyn.internal.view.DynCytoPanel;
import org.cytoscape.dyn.internal.view.DynCytoPanelTask;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewFactory;
import org.cytoscape.io.DataCategory;
import org.cytoscape.io.util.StreamUtil;
import org.cytoscape.util.swing.FileChooserFilter;
import org.cytoscape.util.swing.FileUtil;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.TunableSetter;

/**
 * Launch from the menu "File/Dynamic XGMML Loader"
 */
public class MenuActionLoadXGMML<T,C> extends AbstractCyAction
{
	private static final long serialVersionUID = 1L;
	
	private final CySwingApplication desktopApp;
	private final CyApplicationManager appManager;
	
	private final CytoPanel cytoPanelWest;
	private final DynCytoPanel<T,C> myDynPanel;
	private final TaskManager<T,C> taskManager;
	private final DynNetworkManager<T> dynNetworkManager;
	private final DynNetworkFactory<T> dynNetworkFactory;
	private final DynNetworkViewFactory<T> dynNetworkViewFactory;
	private final FileUtil fileUtil;
	private final StreamUtil streamUtil;
	private final TunableSetter tunableSetterServiceRef;

    public MenuActionLoadXGMML(
    		final CySwingApplication desktopApp,
    		final CyApplicationManager appManager,
    		final DynCytoPanel<T,C> myDynPanel,
    		final TaskManager<T,C> taskManager,
    		final DynNetworkManager<T> dynNetworkManager,
    		final DynNetworkFactory<T> dynNetworkFactory,
    		final DynNetworkViewFactory<T> dynNetworkViewFactory,
    		final FileUtil fileUtil,
    		final StreamUtil streamUtil,
    		final TunableSetter tunableSetterServiceRef)
    {
        super("Dynamic XGMML Loader");
        setPreferredMenu("File");
        this.desktopApp = desktopApp;
        this.appManager = appManager;
        this.cytoPanelWest = desktopApp.getCytoPanel(CytoPanelName.WEST);
        this.myDynPanel = myDynPanel;
        this.taskManager = taskManager;
        this.dynNetworkManager = dynNetworkManager;
        this.dynNetworkFactory = dynNetworkFactory;
        this.dynNetworkViewFactory = dynNetworkViewFactory;
        this.fileUtil = fileUtil;
        this.streamUtil = streamUtil;
        this.tunableSetterServiceRef = tunableSetterServiceRef;
    }

    public void actionPerformed(ActionEvent e)
    {
    	XGMMLDynParser<T> xgmmlParser = new XGMMLDynParser<T>(dynNetworkFactory);
    	XGMMLDynFileFilter xgmmlFilter = new XGMMLDynFileFilter(new String[]{"xgmml","xml"}, new String[]{"text/xgmml","text/xgmml+xml"}, "XGMML files",DataCategory.NETWORK, streamUtil);
    	XGMMLDynNetworkReaderFactory xgmmlNetworkReaderFactory = new XGMMLDynNetworkReaderFactory(xgmmlFilter,xgmmlParser);
    	File file = fileUtil.getFile(desktopApp.getJFrame(), "Load Dynamic Network", FileUtil.LOAD, getFilters());
    	LoadDynNetworkFileTaskFactoryImpl loadFactory = new LoadDynNetworkFileTaskFactoryImpl(xgmmlNetworkReaderFactory, tunableSetterServiceRef, streamUtil);

    	Task loadTask = loadFactory.creatTaskIterator(file).next();
    	Task loadViewTask = new LoadNetworkViewTask<T>(appManager,dynNetworkManager,dynNetworkViewFactory);
    	Task loadPanelTask = new DynCytoPanelTask<T,C>(myDynPanel, cytoPanelWest);
    	TaskIterator iterator = new TaskIterator(loadTask,loadViewTask,loadPanelTask);
    	taskManager.execute(iterator);
    }
    
	private List<FileChooserFilter> getFilters()
	{
		List<FileChooserFilter> filters = new ArrayList<FileChooserFilter>();
    	filters.add(new FileChooserFilter("XGMML", "xgmml"));
    	return filters;
	}
}
