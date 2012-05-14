package org.cytoscape.dyn.internal.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.cytoscape.dyn.internal.attributes.DynInterval;
import org.cytoscape.dyn.internal.events.DynNetworkEventManagerImpl;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskManager;

/**
 * Tentative Class for continuous updating of the network and the view
 * @author sabina
 *
 * @param <T>
 */
public class DynControlPanel<T> extends JPanel implements ChangeListener, ActionListener {

	private final DynNetworkEventManagerImpl<T> eventManager;
	private final TaskManager taskManager;
	private final DynNetworkViewSync<T> syncView;
	private final BlockingQueue queue;
	
	private double time;
	private DynNetworkViewTaskIterator<T> recursiveTask;

	private DecimalFormat formatter = new DecimalFormat("#0.00");
	private JLabel currentTime = new JLabel("Current time = " + formatter.format(DynInterval.minTime));
	private JSlider slider = new JSlider(JSlider.HORIZONTAL,0, 100, 0);
	private JButton forwardButton, backwardButton,stopButton;


	public DynControlPanel(
			final CyNetworkView view,
			final DynNetworkEventManagerImpl<T> manager,
			TaskManager taskManager) {
		super();
		this.eventManager = manager;
		this.taskManager = taskManager;
		System.out.println(view);
		System.out.println(view.getModel());
		this.syncView = new DynNetworkViewSync<T>(view, view.getModel());
		this.queue = new BlockingQueue();
		init();
	}

	public String getTitle() {
		return "Dynamic Network";
	}

	public synchronized void stateChanged(ChangeEvent event) {
		JSlider source = (JSlider)event.getSource();
		if (!source.getValueIsAdjusting()) {
			time = source.getValue()*
			((DynInterval.maxTime-DynInterval.minTime)/100)
			+(DynInterval.minTime);

			currentTime.setText("Current time = " + formatter.format(time));
			taskManager.execute(new TaskIterator(1, new DynNetworkViewTask<T>(syncView, eventManager, queue, time, time)));
		}
	}
	
	@Override
	public synchronized void actionPerformed(ActionEvent event) {
		JButton source = (JButton)event.getSource();
		if (source.equals(forwardButton))
		{
			recursiveTask = new DynNetworkViewTaskIterator<T>(slider, 1);
			new Thread(recursiveTask).start();
		}
		else if (source.equals(backwardButton))
		{
			recursiveTask = new DynNetworkViewTaskIterator<T>(slider, -1);
			new Thread(recursiveTask).start();
		}
		else if (source.equals(stopButton))
		{
			if (recursiveTask!=null)
				recursiveTask.cancel();
		}
	}
	
	private void init() {
		this.setBorder(BorderFactory.createEmptyBorder(5,20,5,20));
		this.setLayout(new GridLayout(4,1));
		this.setSize(150, 50);

		Hashtable<Integer, JLabel> labelTable =
			new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer( 0 ),new JLabel(formatter.format(DynInterval.minTime)) );
		labelTable.put(new Integer( 50 ),new JLabel(formatter.format((DynInterval.minTime+DynInterval.maxTime)/2)) );
		labelTable.put(new Integer( 100 ),new JLabel(formatter.format(DynInterval.maxTime)) );
		slider.setLabelTable(labelTable);
		slider.setMajorTickSpacing(25);
		slider.setMinorTickSpacing(5);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(this);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,3));
		forwardButton = new JButton("Play >>");
		stopButton = new JButton("Stop");
		backwardButton = new JButton("<< Play");
		forwardButton.addActionListener(this);
		stopButton.addActionListener(this);
		backwardButton.addActionListener(this);
		panel.add(backwardButton);
		panel.add(stopButton);
		panel.add(forwardButton);

		this.add(currentTime);
		this.add(slider);
		this.add(new JLabel());
		this.add(panel);
		this.setVisible(true);
	}

}






