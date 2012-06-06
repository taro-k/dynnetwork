package org.cytoscape.dyn.internal.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.events.SetCurrentNetworkEvent;
import org.cytoscape.application.events.SetCurrentNetworkListener;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.DynNetworkManager;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewFactory;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewManager;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskManager;

public class DynCytoPanel<T> extends JPanel implements CytoPanelComponent, ChangeListener, ActionListener, SetCurrentNetworkListener
{
	private final TaskManager taskManager;
	private final BlockingQueue queue;
	
	private final CyApplicationManager appManager;
	private final DynNetworkManager<T> networkManager;
	private final DynNetworkViewManager<T> viewManager;
	private final DynNetworkViewFactory<T> dynNetworkViewFactory;
	
	private DynNetwork<T> network;
	private DynNetworkView<T> view;
	
	private double time;
	private double offset;
	private double minTime;
	private double maxTime;
	
	private DynNetworkViewTaskIterator<T> recursiveTask;

	private JPanel buttonPanel = new JPanel();
	private JPanel dynVizPanel = new JPanel();
	private JPanel featurePanel = new JPanel();
	private JLabel currentTime = new JLabel("Current time = ");
	private JSlider slider = new JSlider(JSlider.HORIZONTAL,0, 100, 0);
	private JButton forwardButton, backwardButton,stopButton;
	private Hashtable<Integer, JLabel> labelTable;
	private DecimalFormat formatter = new DecimalFormat("#0.00");

	public DynCytoPanel(
			final TaskManager taskManager,
			final CyApplicationManager appManager,
			final DynNetworkManager<T> networkManager,
			final DynNetworkViewManager<T> viewManager,
			final DynNetworkViewFactory<T> dynNetworkViewFactory)
	{
		super();
		this.taskManager = taskManager;
		this.appManager = appManager;
		this.networkManager = networkManager;
		this.viewManager = viewManager;
		this.dynNetworkViewFactory = dynNetworkViewFactory;
		this.queue = new BlockingQueue();
		initComponents();
	}

	public synchronized void stateChanged(ChangeEvent event)
	{
		// TODO: check current network
		if (view!=null)
		{
			JSlider source = (JSlider)event.getSource();
			if (!source.getValueIsAdjusting())
			{
				offset = source.getValue()!=100?source.getValue():source.getValue()-0.000000001;
				time = offset*((maxTime-minTime)/100)+(minTime);
				currentTime.setText("Current time = " + formatter.format(time));
				taskManager.execute(new TaskIterator(1,new DynNetworkViewTask<T>(view, network, queue, time, time)));
			}
		}
	}
	
	@Override
	public synchronized void actionPerformed(ActionEvent event)
	{
		JButton source = (JButton)event.getSource();
		if (source.equals(forwardButton))
		{
			if (recursiveTask!=null)
				recursiveTask.cancel();
			
			recursiveTask = new DynNetworkViewTaskIterator<T>(slider, 1);
			new Thread(recursiveTask).start();
		}
		else if (source.equals(backwardButton))
		{
			if (recursiveTask!=null)
				recursiveTask.cancel();
			
			recursiveTask = new DynNetworkViewTaskIterator<T>(slider, -1);
			new Thread(recursiveTask).start();
		}
		else if (source.equals(stopButton))
		{
			if (recursiveTask!=null)
				recursiveTask.cancel();
		}
	}
	
	public Component getComponent() 
	{
		return this;
	}


	public CytoPanelName getCytoPanelName() 
	{
		return CytoPanelName.WEST;
	}


	public String getTitle() 
	{
		return "Dynamic Network";
	}


	public Icon getIcon() 
	{
		return null;
	}
	
	private void initComponents()
	{
		labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer( 0 ),new JLabel(formatter.format(Double.NEGATIVE_INFINITY)) );
		labelTable.put(new Integer( 100 ),new JLabel(formatter.format(Double.POSITIVE_INFINITY)) );
		slider.setLabelTable(labelTable);
		slider.setMajorTickSpacing(25);
		slider.setMinorTickSpacing(5);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(this);
		
		buttonPanel.setLayout(new GridBagLayout());
		forwardButton = new JButton("Play >>");
		stopButton = new JButton("Stop");
		backwardButton = new JButton("<< Play");
		forwardButton.addActionListener(this);
		stopButton.addActionListener(this);
		backwardButton.addActionListener(this);
		buttonPanel.add(backwardButton);
		buttonPanel.add(stopButton);
		buttonPanel.add(forwardButton);
		
		dynVizPanel.setLayout(new GridLayout(4,1));
		dynVizPanel.add(currentTime);
		dynVizPanel.add(slider);
		dynVizPanel.add(buttonPanel);
		
		featurePanel.setLayout(new GridBagLayout());
		
		dynVizPanel
		.setBorder(BorderFactory.createTitledBorder(null,
				"Dynamic Visualization",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("SansSerif", 1, 12),
				Color.darkGray));
		
		featurePanel
		.setBorder(BorderFactory.createTitledBorder(null,
				"Options",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("SansSerif", 1, 12),
				Color.darkGray));
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				   layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				           .addComponent(dynVizPanel, GroupLayout.DEFAULT_SIZE,
				        		   280 , Short.MAX_VALUE)
				           .addComponent(featurePanel, GroupLayout.DEFAULT_SIZE,
				        		   280, Short.MAX_VALUE)
				);
				layout.setVerticalGroup(
				   layout.createSequentialGroup()
				      .addComponent(dynVizPanel, 200,
				    		  GroupLayout.DEFAULT_SIZE, 200)
				      .addComponent(featurePanel, GroupLayout.DEFAULT_SIZE,
				    		  600 , Short.MAX_VALUE)
				);

		this.setVisible(true);
	}

	public void reset() 
	{
		network = networkManager.getDynNetwork(appManager.getCurrentNetwork());
		if (viewManager.getDynNetworkView(network)==null)
			view = dynNetworkViewFactory.createView(network);
		else
			view = viewManager.getDynNetworkView(network);
		view.updateView();
		updateGui();
	}

	// TODO: register listener
	
	@Override
	public void handleEvent(SetCurrentNetworkEvent e) 
	{
		System.out.println("netwrok selected");
		network = networkManager.getDynNetwork(e.getNetwork());
		if (viewManager.getDynNetworkView(network)==null)
			view = dynNetworkViewFactory.createView(network);
		else
			view = viewManager.getDynNetworkView(network);
		view.updateView();
		updateGui();
	}
	
	private void updateGui()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
		    	minTime = DynInterval.getMinTime();
				maxTime = DynInterval.getMaxTime();
				
				labelTable.clear();
				labelTable.put(new Integer( 0 ),new JLabel(formatter.format(minTime)) );
				labelTable.put(new Integer( 100 ),new JLabel(formatter.format(maxTime)) );
				slider.setLabelTable(labelTable);
				slider.setMajorTickSpacing(25);
				slider.setMinorTickSpacing(5);
				slider.setPaintTicks(true);
				slider.setPaintLabels(true);

				time = slider.getValue()*((maxTime-minTime)/100)+(minTime);
				currentTime.setText("Current time = " + formatter.format(time));
				taskManager.execute(new TaskIterator(1, new DynNetworkViewTask<T>(view, network, queue, time, time)));
			}
		});
	}

}

