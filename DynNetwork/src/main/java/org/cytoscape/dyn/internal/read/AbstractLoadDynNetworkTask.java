package org.cytoscape.dyn.internal.read;

import java.net.URI;
import java.util.Properties;

import org.cytoscape.io.read.CyNetworkReader;
import org.cytoscape.io.read.InputStreamTaskFactory;
import org.cytoscape.io.util.StreamUtil;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;

public abstract class AbstractLoadDynNetworkTask extends AbstractTask {
	
	@ProvidesTitle
	public String getTitle() {
		return "Import Network";
	}
	
	private final String VIEW_THRESHOLD = "viewThreshold";
	private static final int DEF_VIEW_THRESHOLD = 3000;
	
	protected int viewThreshold;
	
	protected CyNetworkReader reader;
	protected InputStreamTaskFactory factory;
	protected URI uri;
	protected TaskMonitor taskMonitor;
	protected String name;
	protected boolean interrupted = false;
	protected CyNetworkManager networkManager;
	protected CyNetworkViewManager networkViewManager;
	protected Properties props;
	protected CyNetworkNaming namingUtil;
	protected StreamUtil streamUtil;

	public AbstractLoadDynNetworkTask(
			final InputStreamTaskFactory factory, 
			final CyNetworkManager networkManager,
			final CyNetworkViewManager networkViewManager, 
			final Properties props, 
			final CyNetworkNaming namingUtil,
			StreamUtil streamUtil) {
		this.factory = factory;
		this.networkManager = networkManager;
		this.networkViewManager = networkViewManager;
		this.props = props;
		this.namingUtil = namingUtil;
		this.streamUtil = streamUtil;
		
		this.viewThreshold = getViewThreshold();
	}

	private int getViewThreshold() {
		final String vts = props.getProperty(VIEW_THRESHOLD);
		int threshold;
		try {
			threshold = Integer.parseInt(vts);
		} catch (Exception e) {
			threshold = DEF_VIEW_THRESHOLD;
		}

		return threshold;
	}

}
