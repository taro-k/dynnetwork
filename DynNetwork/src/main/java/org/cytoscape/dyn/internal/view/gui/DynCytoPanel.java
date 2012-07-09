package org.cytoscape.dyn.internal.view.gui;

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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.events.SetCurrentNetworkViewEvent;
import org.cytoscape.application.events.SetCurrentNetworkViewListener;
import org.cytoscape.application.events.SetSelectedNetworksEvent;
import org.cytoscape.application.events.SetSelectedNetworksListener;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewManager;
import org.cytoscape.dyn.internal.view.task.BlockingQueue;
import org.cytoscape.dyn.internal.view.task.DynNetworkViewTask;
import org.cytoscape.dyn.internal.view.task.DynNetworkViewTaskGroup;
import org.cytoscape.dyn.internal.view.task.DynNetworkViewTaskIterator;
import org.cytoscape.dyn.internal.view.task.DynNetworkViewTransparencyTask;
import org.cytoscape.group.events.GroupCollapsedEvent;
import org.cytoscape.group.events.GroupCollapsedListener;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.events.RowSetRecord;
import org.cytoscape.model.events.RowsSetEvent;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskManager;

/**
 * <code> DynCytoPanel </code> implements the a JPanel component in {@link CytoPanel} 
 * west to provide a time slider for controlling the dynamic visualization.
 * 
 * @author sabina
 *
 * @param <T>
 * @param <C>
 */
public final class DynCytoPanel<T,C> extends JPanel implements CytoPanelComponent, 
ChangeListener, ActionListener, SetCurrentNetworkViewListener, GroupCollapsedListener
//GraphViewChangeListener
{
	private static final long serialVersionUID = 1L;
	
	private final TaskManager<T,C> taskManager;
	private final BlockingQueue queue;
	private final CyApplicationManager appManager;
	private final DynNetworkViewManager<T> viewManager;
	
	private DynNetwork<T> network;
	private DynNetworkView<T> view;
	
	private double time;
	private double minTime;
	private double maxTime;
	
	private int visibility = 0;
	
	private int sliderMax;
	private DynNetworkViewTaskIterator<T> recursiveTask;

	private JPanel buttonPanel;
	private JPanel dynVizPanel;
	private JPanel featurePanel;
	private JLabel currentTime;
	private JSlider slider;
	private JComboBox resolutionComboBox;
	private JButton forwardButton, backwardButton,stopButton;
	private JCheckBox seeAllCheck;
	private Hashtable<Integer, JLabel> labelTable;
	private DecimalFormat formatter;

	public DynCytoPanel(
			final TaskManager<T,C> taskManager,
			final CyApplicationManager appManager,
			final DynNetworkViewManager<T> viewManager)
	{
		super();
		this.taskManager = taskManager;
		this.appManager = appManager;
		this.viewManager = viewManager;
		this.queue = new BlockingQueue();
		initComponents();
	}

	public synchronized void stateChanged(ChangeEvent event)
	{
		if (view!=null)
			updateView();
	}

	@Override
	public synchronized void actionPerformed(ActionEvent event)
	{
		if (event.getSource() instanceof JButton)
		{
			JButton source = (JButton)event.getSource();
			if (source.equals(forwardButton))
			{
				if (recursiveTask!=null)
					recursiveTask.cancel();
				recursiveTask = new DynNetworkViewTaskIterator<T>(slider, +1);
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
		else if (event.getSource() instanceof JCheckBox)
		{
			JCheckBox source = (JCheckBox)event.getSource();
			if (source.isSelected())
				visibility = 30;
			else
				visibility = 0;
			taskManager.execute(new TaskIterator(1,new DynNetworkViewTransparencyTask<T>(view, network, queue, time, time, visibility)));
		}
		else if (event.getSource() instanceof JComboBox)
		{
			JComboBox source = (JComboBox)event.getSource();
			updateSliderMax((double) slider.getValue()/sliderMax, ((NameIDObj)source.getSelectedItem()).id);
			updateGui();
		}
	}
	
	@Override
	public void handleEvent(SetCurrentNetworkViewEvent e) 
	{
		if (recursiveTask!=null)
			recursiveTask.cancel();

		if (e.getNetworkView()!=null)
		{
			if (view!=null)
				view.setCurrentTime((double) slider.getValue()/sliderMax);
			
			view = viewManager.getDynNetworkView(e.getNetworkView());
			
			if (view!=null)
			{
				network = view.getNetwork();
				minTime = network.getMinTime();
				maxTime = network.getMaxTime();
				updateSliderMax(view.getCurrentTime(), ((NameIDObj)resolutionComboBox.getSelectedItem()).id);
				updateGui();
				updateView();
			}
		}
	}
	
	@Override
	public void handleEvent(GroupCollapsedEvent e)
	{
		if (view!=null)
			taskManager.execute(new TaskIterator(1,new DynNetworkViewTaskGroup<T>(view, network, queue, time, time, visibility, e.getSource())));
	}
	
//	void handleEvent(RowsSetEvent e)
//	{
//	    if (!e.containsColumn(CyNetwork.SELECTED))
//	        return;
//	    
//	    
//
//	    for (RowSetRecord record : e.getColumnRecords(CyNetwork.SELECTED))
//	    {
//	    	if (e.getSource()==this.network.getNetwork().getDefaultNetworkTable())
//	    	{
//	    		CyTable table = (CyTable) e.getSource();
//	    		
//	    	}
//	    	else if (e.getSource()==this.network.getNetwork().getDefaultNetworkTable())
//	    	{
//	    		
//	    	}
//	    	else if (e.getSource()==this.network.getNetwork().getDefaultNetworkTable())
//	    	{
//	    		
//	    	}
//	    }
//	}
	
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
		formatter = new DecimalFormat("#0.000");
		currentTime = new JLabel("Current time = ");
		
		slider = new JSlider(JSlider.HORIZONTAL,0, 100, 0);
		labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer( 0 ),new JLabel(formatter.format(Double.NEGATIVE_INFINITY)) );
		labelTable.put(new Integer( 100 ),new JLabel(formatter.format(Double.POSITIVE_INFINITY)) );
		slider.setLabelTable(labelTable);
		slider.setMajorTickSpacing(25);
		slider.setMinorTickSpacing(5);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(this);

		NameIDObj[] items = { 
				new NameIDObj(10,"1/10"), 
				new NameIDObj(100,"1/100"), 
				new NameIDObj(1000,"1/1000"), 
				new NameIDObj(10000,"1/10000") };
		resolutionComboBox  = new JComboBox(items);
		resolutionComboBox.setSelectedIndex(1);
		resolutionComboBox.addActionListener(this);
		
		buttonPanel = new JPanel();
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
		
		seeAllCheck = new JCheckBox("Display all",false);
		seeAllCheck.addActionListener(this);
		
		dynVizPanel = new JPanel();
		dynVizPanel.setLayout(new GridLayout(6,1));
		dynVizPanel.add(currentTime);
		dynVizPanel.add(slider);
		dynVizPanel.add(new JLabel("Time resolution"));
		dynVizPanel.add(resolutionComboBox);
		dynVizPanel.add(seeAllCheck);
		dynVizPanel.add(buttonPanel);
		
		featurePanel = new JPanel();
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
				      .addComponent(dynVizPanel, 270,
				    		  GroupLayout.DEFAULT_SIZE, 270)
				      .addComponent(featurePanel, GroupLayout.DEFAULT_SIZE,
				    		  530 , Short.MAX_VALUE)
				);

		this.setVisible(true);
	}
	
	public void reset() 
	{
		view = viewManager.getDynNetworkView(appManager.getCurrentNetworkView());
		if (view!=null)
		{
			network = view.getNetwork();
			initTransparency();
			
			minTime = network.getMinTime();
			maxTime = network.getMaxTime();
			updateSliderMax(view.getCurrentTime(), ((NameIDObj)resolutionComboBox.getSelectedItem()).id);
			updateGui();
			updateView();
		}
	}
	
	private void updateView()
	{
		time = slider.getValue()*((maxTime-minTime)/sliderMax)+(minTime);
		currentTime.setText("Current time = " + formatter.format(time));
		if (time==this.network.getMaxTime())
			taskManager.execute(new TaskIterator(1,new DynNetworkViewTask<T>(view, network, queue, time-0.0000001, time+0.0000001, visibility)));
		else
			taskManager.execute(new TaskIterator(1,new DynNetworkViewTask<T>(view, network, queue, time, time, visibility)));
	}
	
	private void updateGui()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				labelTable.clear();
				labelTable.put(new Integer( 0 ),new JLabel(formatter.format(minTime)) );
				labelTable.put(new Integer( sliderMax ),new JLabel(formatter.format(maxTime)) );
				slider.setMaximum(sliderMax);
				slider.setLabelTable(labelTable);
				slider.setMajorTickSpacing((int) (0.5*sliderMax));
				slider.setMinorTickSpacing((int) (0.1*sliderMax));
				slider.setPaintTicks(true);
				slider.setPaintLabels(true);
			}
		});
	}
	
	private void updateSliderMax(double absoluteTime, int value)
	{
		sliderMax = value;
		slider.setMaximum(value);
		slider.setValue((int) (absoluteTime*(double) sliderMax));
	}
	
//	public void graphViewChanged(GraphViewChangeEvent event){
//
//        if(event.isEdgesSelectedType()){
//                Edge[] selectedEdges = event.getSelectedEdges();
//
//                //some code to process the edges
//        }
//
//        if(event.isNodesSelectedType()){
//                
//                Node[] selectedNodes = event.getSelectedNodes();
//                
//                //some code to process the nodes
//        
//        }
//}
	
	private void initTransparency()
	{
		for (final View<CyNode> nodeView : view.getNetworkView().getNodeViews())
		{
			nodeView.setVisualProperty(BasicVisualLexicon.NODE_TRANSPARENCY, visibility);
			nodeView.setVisualProperty(BasicVisualLexicon.NODE_BORDER_TRANSPARENCY, visibility);
			nodeView.setVisualProperty(BasicVisualLexicon.NODE_LABEL_TRANSPARENCY, visibility);
		}
		
		for (final View<CyEdge> edgeView : view.getNetworkView().getEdgeViews())
			edgeView.setVisualProperty(BasicVisualLexicon.EDGE_TRANSPARENCY, visibility);
	}
	
//	void handleEvent(RowsSetEvent e) {
//		
//		
//
//	    if (e.getSource() != tableICareAbout)
//
//	        return;
//
//	    if (!e.containsColumn(CyNetwork.SELECTED)
//
//	        return;
//
//	    for (RowSetRecord record : e.getColumnRecords(CyNetwork.SELECTED)) {
//
//	        // do something for the nodes you care about!
//
//	    }
//
//	}

}

final class NameIDObj
{
	int id;
	String name;

	NameIDObj(int id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public String toString()
	{
		return name;
	}
}


