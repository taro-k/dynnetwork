package org.cytoscape.dyn.internal.view.task;

import java.util.List;

import org.cytoscape.dyn.internal.tree.DynInterval;
import org.cytoscape.dyn.internal.view.gui.DynCytoPanel;
import org.cytoscape.dyn.internal.view.layout.DynLayout;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

public class AbstractCyNetworkViewTask<T,C>  implements Runnable
{
	protected final DynCytoPanel<T,C> panel;
	protected final CyNetworkView view;
	protected final CyNetwork network;
	protected final DynLayout<T> layout;
	protected final BlockingQueue queue;
	protected final double low;
	protected final double high;
	
	protected double timeStart;
	protected double timeEnd;
	
	protected DynInterval<T> timeInterval;
	
	protected boolean cancelled = false;
	
	public AbstractCyNetworkViewTask(
			final DynCytoPanel<T, C> panel,
			final CyNetworkView view,
			final DynLayout<T> layout,
			final BlockingQueue queue, 
			final double low, 
			final double high) 
	{
		this.panel = panel;
		this.view = view;
		this.network = view.getModel();
		this.layout = layout;
		this.queue = queue;
		this.low = low;
		this.high = high;
	}

	public void cancel() 
	{
		this.cancelled = true;
	}

	@Override
	public void run()
	{
		
	}
	
	protected void updatePosition(List<DynInterval<T>> intervalList, double alpha, int n)
	{
		for (int i=0;i<n;i++)
		{
			timeStart = System.currentTimeMillis();

			for (DynInterval<T> interval : intervalList)
			{
				CyNode node = network.getNode(interval.getAttribute().getKey().getRow());
				if (node!=null)
					if (interval.getAttribute().getColumn().equals("node_X_Pos"))
						view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, 
								((1-alpha)*view.getNodeView(node).getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION)+alpha*(Double)interval.getValue()));
					else if (interval.getAttribute().getColumn().equals("node_Y_Pos"))
						view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, 
								((1-alpha)*view.getNodeView(node).getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION)+alpha*(Double)interval.getValue()));
					else if (interval.getAttribute().getColumn().equals("node_Z_Pos"))
						view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Z_LOCATION, 
								((1-alpha)*view.getNodeView(node).getVisualProperty(BasicVisualLexicon.NODE_Z_LOCATION)+alpha*(Double)interval.getValue()));
			}

			view.updateView();
			timeEnd = System.currentTimeMillis();

			if (timeEnd-timeStart<50)
				try {
					Thread.sleep(50-(int) (timeEnd-timeStart));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}
	
}
