package org.cytoscape.dyn.internal.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.dyn.internal.loaddynnetwork.LoadDynNetworkFileTaskFactoryImpl;
import org.cytoscape.dyn.internal.model.DynNetworkFactory;
import org.cytoscape.dyn.internal.read.xgmml.XGMMLDynFileFilter;
import org.cytoscape.dyn.internal.read.xgmml.XGMMLDynNetworkReaderFactory;
import org.cytoscape.dyn.internal.read.xgmml.XGMMLDynParser;
import org.cytoscape.dyn.internal.view.DynCytoPanel;
import org.cytoscape.dyn.internal.view.DynCytoPanelTask;
import org.cytoscape.io.DataCategory;
import org.cytoscape.io.util.StreamUtil;
import org.cytoscape.util.swing.FileChooserFilter;
import org.cytoscape.util.swing.FileUtil;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TunableSetter;

/**
 * Launch from the menu "File/Dynamic XGMML Loader"
 */
public class MenuActionLoadXGMML<T> extends AbstractCyAction
{
	private static final long serialVersionUID = 1L;
	
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
    	TunableSetter tunableSetterServiceRef = adapter.getCyServiceRegistrar().getService(TunableSetter.class);
    	XGMMLDynParser<T> xgmmlParser = new XGMMLDynParser<T>(dynNetworkFactory);
    	XGMMLDynFileFilter xgmmlFilter = new XGMMLDynFileFilter(new String[]{"xgmml","xml"}, new String[]{"text/xgmml","text/xgmml+xml"}, "XGMML files",DataCategory.NETWORK, streamUtil);
    	XGMMLDynNetworkReaderFactory xgmmlNetworkReaderFactory = new XGMMLDynNetworkReaderFactory(xgmmlFilter,xgmmlParser);
    	filters = new ArrayList<FileChooserFilter>();
    	filters.add(new FileChooserFilter("XGMML", "xgmml"));

    	// load file
    	LoadDynNetworkFileTaskFactoryImpl loadFactory = new LoadDynNetworkFileTaskFactoryImpl(xgmmlNetworkReaderFactory, tunableSetterServiceRef, streamUtil);
    	File file = fileUtil.getFile(adapter.getCySwingApplication().getJFrame(), "Load Dynamic Network", FileUtil.LOAD, filters);
    	TaskIterator iterator = new TaskIterator(loadFactory.creatTaskIterator(file).next(), new DynCytoPanelTask<T>(myDynPanel, cytoPanelWest));
    	adapter.getTaskManager().execute(iterator);
    }
}
