package org.cytoscape.dyn.internal.view.model;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.VisualProperty;

public interface DynNetworkView<T>
{
	public boolean readVisualProperty(CyNode node, VisualProperty<Boolean> vp);

	public void writeVisualProperty(CyNode node, VisualProperty<Boolean> vp, boolean value);

	public boolean readVisualProperty(CyEdge edge, VisualProperty<Boolean> vp);
	
	public void writeVisualProperty(CyEdge edge, VisualProperty<Boolean> vp, boolean value);
	
	public CyNetworkView getNetworkView();
	
	public void updateView();

	public DynNetwork<T> getNetwork();

}
