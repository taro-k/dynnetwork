package org.cytoscape.dyn.internal.view.model;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.model.View;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

public class DynNetworkViewImpl<T> implements DynNetworkView<T>
{
	private final DynNetwork<T> dynNetwork;
	private final CyNetworkView view;

	public DynNetworkViewImpl(
			DynNetwork<T> dynNetwork,
			final CyNetworkViewManager networkViewManager,
			final CyNetworkViewFactory cyNetworkViewFactory)
	{
		this.dynNetwork = dynNetwork;
		this.view = cyNetworkViewFactory.createNetworkView(this.dynNetwork.getNetwork());
		networkViewManager.addNetworkView(view);
		this.dynNetwork.collapseAllGroups();
		setVisibility();
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

	private void setVisibility()
	{
		for (View<CyNode> v : this.view.getNodeViews())
			v.setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, false);
		for (View<CyEdge> v : this.view.getEdgeViews())
			v.setVisualProperty(BasicVisualLexicon.EDGE_VISIBLE, false);
	}

}
