package org.cytoscape.dyn.internal.loaddynnetwork;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.cytoscape.dyn.internal.events.DynNetworkEventManagerImpl;
import org.cytoscape.io.read.InputStreamTaskFactory;
import org.cytoscape.io.util.StreamUtil;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.property.CyProperty;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TunableSetter;

public final class LoadDynNetworkFileTaskFactoryImpl<T> extends AbstractTaskFactory implements LoadDynNetworkFileTaskFactory {

	private InputStreamTaskFactory factory;
	private CyNetworkManager netmgr;
	private final CyNetworkViewManager networkViewManager;
	private Properties props;
	
	private CyNetworkNaming cyNetworkNaming;
	private final TunableSetter tunableSetter;
	private final StreamUtil streamUtil;
	private final DynNetworkEventManagerImpl<T> manager;

	public LoadDynNetworkFileTaskFactoryImpl(
			InputStreamTaskFactory factory, 
			CyNetworkManager netmgr,
			final CyNetworkViewManager networkViewManager, 
			final DynNetworkEventManagerImpl<T> manager,
			CyProperty<Properties> cyProp,
			CyNetworkNaming cyNetworkNaming, 
			TunableSetter tunableSetter,
			StreamUtil streamUtil) {
		
		this.factory = factory;
		this.netmgr = netmgr;
		this.networkViewManager = networkViewManager;
		this.props = cyProp.getProperties();
		this.cyNetworkNaming = cyNetworkNaming;
		this.tunableSetter = tunableSetter;
		this.streamUtil = streamUtil;
		this.manager = manager;
	}
	
	public TaskIterator createTaskIterator() {
		return new TaskIterator(1, new LoadDynNetworkFileTask<T>(factory, netmgr, networkViewManager, manager, props, cyNetworkNaming, streamUtil));
	}

	@Override
	public TaskIterator creatTaskIterator(File file) {
		final Map<String, Object> m = new HashMap<String, Object>();
		m.put("file", file);
		return tunableSetter.createTaskIterator(this.createTaskIterator(), m); 
	}
}