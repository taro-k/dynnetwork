package org.cytoscape.dyn.internal.view.model;

import java.util.Collection;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.model.View;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;

public final class DynNetworkViewImpl<T> implements DynNetworkView<T>
{
	private final DynNetwork<T> dynNetwork;
	private final CyNetworkView view;
	private final VisualStyle style;
	
	private int currentTime;

	public DynNetworkViewImpl(
			DynNetwork<T> dynNetwork,
			final CyNetworkViewManager networkViewManager,
			final CyNetworkViewFactory cyNetworkViewFactory,
			final VisualMappingManager vmm)
	{
		this.currentTime = 0;
		this.dynNetwork = dynNetwork;
		this.style = vmm.getCurrentVisualStyle();
		this.view = cyNetworkViewFactory.createNetworkView(this.dynNetwork.getNetwork());
		networkViewManager.addNetworkView(view);
		vmm.setVisualStyle(style, view);
		style.apply(view);
//		this.dynNetwork.collapseAllGroups();
		init();
//		view.updateView();
//		view.fitContent();
	}

	@Override
	public CyNetworkView getNetworkView() 
	{
		return this.view;
	}

	public boolean readVisualProperty(CyNode node, VisualProperty<Boolean> vp) 
	{
		return view.getNodeView(node).getVisualProperty(vp).booleanValue();
	}

	public void writeVisualProperty(CyNode node, VisualProperty<Boolean> vp, boolean value) 
	{
		view.getNodeView(node).setVisualProperty(vp,value);
	}

	public boolean readVisualProperty(CyEdge edge, VisualProperty<Boolean> vp) 
	{
		return view.getEdgeView(edge).getVisualProperty(vp).booleanValue();
	}

	public void writeVisualProperty(CyEdge edge, VisualProperty<Boolean> vp, boolean value) 
	{
		view.getEdgeView(edge).setVisualProperty(vp,value);
	}

	public void updateView() 
	{
		view.updateView();
	}

	public DynNetwork<T> getNetwork() 
	{
		return this.dynNetwork;
	}
	
	private void init()
	{
		if (view.getModel().getNodeCount() > 0) 
		{
			final Collection<View<CyNode>> nodes = view.getNodeViews();
			final Collection<View<CyEdge>> edges = view.getEdgeViews();
			
			for (final View<CyNode> nodeView : nodes)
				nodeView.setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, false);
			for (final View<CyEdge> edgeView : edges)
				edgeView.setVisualProperty(BasicVisualLexicon.EDGE_VISIBLE, false);
		}
	}

	public int getCurrentTime() 
	{
		return currentTime;
	}

	public void setCurrentTime(int currentTime) 
	{
		this.currentTime = currentTime;
	}
}
