package org.cytoscape.dyn.internal.view;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.VisualProperty;

//TODO: full implementation of a synchronized CyNetwork and CyNetworkView....?
public class DynNetworkViewSync<T> {

	private final ReentrantReadWriteLock tableLock;
	private final ReentrantReadWriteLock viewLock;
	private final Lock readView;
	private final Lock writeView;
	private final Lock readTable;
	private final Lock writeTable;
	
	private final CyNetworkView view;
	private final CyNetwork network;

	public DynNetworkViewSync(
			final CyNetworkView view,
			final CyNetwork network) {
		this.view = view;
		this.network = network;
		this.tableLock = new ReentrantReadWriteLock();
		this.readTable  = tableLock.readLock();
		this.writeTable = tableLock.writeLock();
		this.viewLock = new ReentrantReadWriteLock();
		this.readView  = viewLock.readLock();
		this.writeView = viewLock.writeLock();
	}
	
	public List<CyNode> getNodes() {
		readTable.lock();
		try{
			return network.getNodeList();
		} finally {
			readTable.unlock();
		}
	}
	
	public List<CyEdge> getEdges() {
		readTable.lock();
		try{
			return network.getEdgeList( );
		} finally {
			readTable.unlock();
		}
	}
	
	public Iterator<String> getNodeAttributes(CyNode node) {
		readTable.lock();
		try{
			return network.getRow(node).getAllValues().keySet().iterator();
		} finally {
			readTable.unlock();
		}
	}
	
	public Iterator<String> getEdgeAttributes(CyEdge edge) {
		readTable.lock();
		try{
			return network.getRow(edge).getAllValues().keySet().iterator();
		} finally {
			readTable.unlock();
		}
	}
	
	public Object readEdgeAttr(CyEdge edge, String columnName) {
		readTable.lock();
		try{
			return network.getRow(edge).getRaw(columnName);
		} finally {
			readTable.unlock();
		}
	}
	
	public CyNode readNodeTable(long key) {
		readTable.lock();
		try{
			return network.getNode(key);
		} finally {
			readTable.unlock();
		}
	}
	
	public void writeNodeTable(CyNode node, String name, T value) {
		writeTable.lock();
		try {
			network.getRow(node).set(name, value);
		} finally {
			writeTable.unlock();
		}
	}
	
	public CyEdge readEdgeTable(long key) {
		readTable.lock();
		try{
			return network.getEdge(key);
		} finally {
			readTable.unlock();
		}
	}
	
	public void writeEdgeTable(CyEdge edge, String name, T value) {
		writeTable.lock();
		try {
			network.getRow(edge).set(name, value);
		} finally {
			writeTable.unlock();
		}
	}
	
	public boolean readVisualProperty(CyNode node, VisualProperty<Boolean> vp) {
		readView.lock();
		try{
			return view.getNodeView(node).getVisualProperty(vp).booleanValue();
		} finally {
			readView.unlock();
		}
	}

	public void writeVisualProperty(CyNode node, VisualProperty<Boolean> vp, boolean value) {
		writeView.lock();
		try {
			view.getNodeView(node).setVisualProperty(vp,value);
		} finally {
			writeView.unlock();
		}
	}

	public boolean readVisualProperty(CyEdge edge, VisualProperty<Boolean> vp) {
		readView.lock();
		try{
			return view.getEdgeView(edge).getVisualProperty(vp).booleanValue();
		} finally {
			readView.unlock();
		}
	}
	
	public void writeVisualProperty(CyEdge edge, VisualProperty<Boolean> vp, boolean value) {
		writeView.lock();
		try {
			view.getEdgeView(edge).setVisualProperty(vp,value);
		} finally {
			writeView.unlock();
		}
	}
	
	public void updateView() {
		readView.lock();
		try{
			view.updateView();
		} finally {
			readView.unlock();
		}
	}

	public CyNetwork getNetwork() {
		return network;
	}

}
