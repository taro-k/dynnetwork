package org.cytoscape.dyn.internal.view;

import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JFrame;

import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.dyn.internal.events.DynNetworkEventManagerImpl;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

// TODO: integrate this in cytoscape panel, where???

public class DynControlPanelTask<T> extends AbstractTask {

	private final CyAppAdapter adapter;
	private final DynNetworkEventManagerImpl<T> eventManager;
	
	public DynControlPanelTask(
			final CyAppAdapter adapter,
			final DynNetworkEventManagerImpl<T> manager) {
		this.adapter = adapter;
		this.eventManager = manager;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {

		if (adapter.getCyApplicationManager().getCurrentNetworkView()!=null)
		{
			DynControlPanel<T> panel = new DynControlPanel<T>(
					adapter.getCyApplicationManager().getCurrentNetworkView(),
					eventManager,
					adapter.getTaskManager());

			JFrame frame = new JFrame("Dynamic Network");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			Container contentPane = frame.getContentPane();
			contentPane.setLayout(new FlowLayout());
			contentPane.add(panel);
			frame.add(panel);
			frame.pack();
			frame.setVisible(true);
		}
	}
	
}
