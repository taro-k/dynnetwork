package org.cytoscape.dyn.internal.view;

import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.dyn.internal.action.DynNetworkEventManagerImpl;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class DynCytoPanelTask<T> extends AbstractTask {

	private final DynCytoPanel<T> panel;
	private final CytoPanel cytoPanelWest;
	private final CyAppAdapter adapter;
	private final DynNetworkEventManagerImpl<T> eventManager;
	
	public DynCytoPanelTask(
			final DynCytoPanel<T> panel,
			final CytoPanel cytoPanelWest,
			final CyAppAdapter adapter,
			final DynNetworkEventManagerImpl<T> manager)
	{
		this.panel = panel;
		this.cytoPanelWest = cytoPanelWest;
		this.adapter = adapter;
		this.eventManager = manager;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception
	{
		CyNetworkView view = adapter.getCyApplicationManager().getCurrentNetworkView();
		if (view!=null)
		{
			panel.update(eventManager, view);
			
			if (cytoPanelWest.getState() == CytoPanelState.HIDE)
			{
				cytoPanelWest.setState(CytoPanelState.DOCK);
			}	

			int index = cytoPanelWest.indexOfComponent(panel);
			if (index == -1)
			{
				return;
			}
			cytoPanelWest.setSelectedIndex(index);
		}
	}
	
}
