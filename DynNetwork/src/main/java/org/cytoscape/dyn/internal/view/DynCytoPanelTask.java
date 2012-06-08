package org.cytoscape.dyn.internal.view;

import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class DynCytoPanelTask<T,C> extends AbstractTask 
{
	private final DynCytoPanel<T,C> panel;
	private final CytoPanel cytoPanelWest;
	
	public DynCytoPanelTask(
			final DynCytoPanel<T,C> panel,
			final CytoPanel cytoPanelWest)
	{
		this.panel = panel;
		this.cytoPanelWest = cytoPanelWest;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception
	{
		panel.reset();

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
