package org.cytoscape.dyn.internal.view.model;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.model.VisualProperty;

public class DynNetworkViewImpl<T> implements DynNetworkView<T>
{
	private final ReentrantReadWriteLock lock;
	private final Lock read;
	private final Lock write;
	
	private final DynNetwork<T> dynNetwork;
	private final CyNetworkView view;
	
	public DynNetworkViewImpl(
			DynNetwork<T> dynNetwork,
			final CyNetworkViewManager networkViewManager,
			final CyNetworkViewFactory cyNetworkViewFactory)
	{
		this.lock = new ReentrantReadWriteLock();
		this.read  = lock.readLock();
		this.write = lock.writeLock();
		
		this.dynNetwork = dynNetwork;
		this.view = cyNetworkViewFactory.createNetworkView(this.dynNetwork.getNetwork());
		networkViewManager.addNetworkView(view);
		
		this.dynNetwork.collapseAllGroups();
		view.fitContent();
	}
	
	@Override
	public CyNetworkView getNetworkView() 
	{
		read.lock();
		try{
			return this.view;
		} finally {
			read.unlock();
		}
	}
	
	public boolean readVisualProperty(CyNode node, VisualProperty<Boolean> vp) 
	{
		read.lock();
		try{
			return view.getNodeView(node).getVisualProperty(vp).booleanValue();
		} finally {
			read.unlock();
		}
	}

	public void writeVisualProperty(CyNode node, VisualProperty<Boolean> vp, boolean value) 
	{
		write.lock();
		try {
			view.getNodeView(node).setVisualProperty(vp,value);
		} finally {
			write.unlock();
		}
	}

	public boolean readVisualProperty(CyEdge edge, VisualProperty<Boolean> vp) 
	{
		read.lock();
		try{
			return view.getEdgeView(edge).getVisualProperty(vp).booleanValue();
		} finally {
			read.unlock();
		}
	}
	
	public void writeVisualProperty(CyEdge edge, VisualProperty<Boolean> vp, boolean value) 
	{
		write.lock();
		try {
			view.getEdgeView(edge).setVisualProperty(vp,value);
		} finally {
			write.unlock();
		}
	}
	
	public void updateView() 
	{
		write.lock();
		try{
			view.updateView();
		} finally {
			write.unlock();
		}
	}

	public DynNetwork<T> getNetwork() 
	{
		read.lock();
		try{
			return this.dynNetwork;
		} finally {
			read.unlock();
		}
	}
	
}
