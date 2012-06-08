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
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;

/**
 * <code> DynNetworkViewImpl </code> is the interface for the visualization of 
 * dynamic networks {@link DynNetworkView}.
 * 
 * @author sabina
 *
 * @param <T>
 */
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
		this.style = vmm.getDefaultVisualStyle();
		this.view = cyNetworkViewFactory.createNetworkView(this.dynNetwork.getNetwork());
		networkViewManager.addNetworkView(view);
		vmm.setVisualStyle(style, view);
		style.apply(view);
		
		// TODO: FIXME
		
		this.dynNetwork.expandAllGroups();
		this.dynNetwork.collapseAllGroups();
		initNodes();
		initEdges();
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

	private void initNodes()
	{
		for (final View<CyNode> nodeView : view.getNodeViews())
			nodeView.setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, false);
	}

	private void initEdges()
	{
		for (final View<CyEdge> edgeView : view.getEdgeViews())
			edgeView.setVisualProperty(BasicVisualLexicon.EDGE_VISIBLE, false);
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
