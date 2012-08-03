package org.cytoscape.dyn.internal.view.layout.algorithm.dynamic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.cytoscape.dyn.internal.view.model.DynNetworkView;

public class KKDynLayoutDialog<T> extends JDialog implements ActionListener, ChangeListener
{
	private static final long serialVersionUID = 1L;

	private final DynNetworkView<T> dynView;
	
	private double maxTime;
	private boolean cancel;
	
	private JLabel currentPast;
	private JLabel currentFuture;
	private JLabel currentItertions;
	
	private JButton okButton;
    private JButton cancelButton;
    private JSlider sliderPast;
    private JSlider sliderFuture;
    private JSlider sliderIterations;
    private Hashtable<Integer, JLabel> labelTablePast;
    private Hashtable<Integer, JLabel> labelTableFuture;
    private Hashtable<Integer, JLabel> labelTableIterations;
	
	public KKDynLayoutDialog(
			final JFrame parent, 
			final DynNetworkView<T> dynView) 
	{
		super(parent, "Dynamic Kamada Kawai", true);
		this.dynView = dynView;
		initComponents();
	}

	private void initComponents() 
	{	
		List<Double> events = dynView.getNetwork().getNodeEventTimeList();
		maxTime = getMaxDiff(events);
		
		JPanel topPanel = new JPanel(new GridLayout(6,1));
		
		currentItertions = new JLabel("Maximum iterations = 10");
		sliderIterations = new JSlider(JSlider.HORIZONTAL, 0, 1000, 10);
		labelTableIterations = new Hashtable<Integer, JLabel>();
		labelTableIterations.put(new Integer( 0 ),new JLabel("0") );
		labelTableIterations.put(new Integer( 500 ),new JLabel("500") );
		labelTableIterations.put(new Integer( 1000 ),new JLabel("1000") );
		sliderIterations.setLabelTable(labelTableIterations);
		sliderIterations.setMajorTickSpacing(250);
		sliderIterations.setMinorTickSpacing(50);
		sliderIterations.setPaintTicks(true);
		sliderIterations.setPaintLabels(true);
		sliderIterations.addChangeListener(this);
		
		currentPast = new JLabel("Past events = 0");
		sliderPast = new JSlider(JSlider.HORIZONTAL, 0, events.size(), 0);
		labelTablePast = new Hashtable<Integer, JLabel>();
		labelTablePast.put(new Integer( 0 ),new JLabel("0") );
		labelTablePast.put(new Integer( events.size() ),new JLabel(Integer.toString(events.size())) );
		sliderPast.setLabelTable(labelTablePast);
		sliderPast.setMajorTickSpacing(250);
		sliderPast.setMinorTickSpacing(50);
		sliderPast.setPaintTicks(true);
		sliderPast.setPaintLabels(true);
		sliderPast.addChangeListener(this);
		
		currentFuture = new JLabel("Future events = 0");
		sliderFuture = new JSlider(JSlider.HORIZONTAL, 0, events.size(), 0);
		labelTableFuture = new Hashtable<Integer, JLabel>();
		labelTableFuture.put(new Integer( 0 ),new JLabel("0") );
		labelTableFuture.put(new Integer( events.size() ),new JLabel(Integer.toString(events.size())) );
		sliderFuture.setLabelTable(labelTableFuture);
		sliderFuture.setMajorTickSpacing(250);
		sliderFuture.setMinorTickSpacing(50);
		sliderFuture.setPaintTicks(true);
		sliderFuture.setPaintLabels(true);
		sliderFuture.addChangeListener(this);
		
		topPanel.add(currentItertions);
		topPanel.add(sliderIterations);
		topPanel.add(currentPast);
		topPanel.add(sliderPast);
		topPanel.add(currentFuture);
		topPanel.add(sliderFuture);
		
		
		this.okButton = new JButton("OK");
        this.okButton.addActionListener(this);
        this.cancelButton = new JButton("Cancel");
        this.cancelButton.addActionListener(this);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
		bottomPanel.add(Box.createHorizontalGlue());
		bottomPanel.add(this.okButton);
		bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		bottomPanel.add(this.cancelButton);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(9,9,9,9));
		mainPanel.add(topPanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
		
		this.add(mainPanel);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(700,300);
		pack();

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		JButton source = (JButton)event.getSource();
		if (source.equals(okButton))
		{
			setVisible(false); 
		    dispose();
		}
		else if (source.equals(cancelButton))
		{
			this.cancel = true;
			setVisible(false); 
		    dispose();
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent event) 
	{
		JSlider source = (JSlider)event.getSource();
		if (source.equals(sliderIterations))
			currentItertions.setText("Maximum iterations = " + Integer.toString(Math.max(1, sliderIterations.getValue())));
		else if (source.equals(sliderPast))
			currentPast.setText("Past events = " + Integer.toString(sliderPast.getValue()));
		else if (source.equals(sliderFuture))
			currentFuture.setText("Future events = " + Integer.toString(sliderFuture.getValue()));
	}
	
	public double getIterationRate() 
	{
		return (Math.max(1, sliderIterations.getValue())/maxTime)-1;
	}

	public int getPastEvents() 
	{
		return sliderPast.getValue();
	}

	public int getFutureEvents() 
	{
		return sliderFuture.getValue();
	}

	public boolean isCancel() 
	{
		return cancel;
	}

//	private double getMinDiff(List<Double> eventList)
//	{
//		double min = Double.POSITIVE_INFINITY;
//		for (int i=0; i<eventList.size()-1; i++)
//			min = Math.min(min,eventList.get(i+1)-eventList.get(i));
//		if (min==Double.POSITIVE_INFINITY)
//			return 0;
//		else 
//			return min;
//		}
	
	private double getMaxDiff(List<Double> eventList)
	{
		double max = Double.NEGATIVE_INFINITY;
		for (int i=0; i<eventList.size()-1; i++)
			max = Math.max(max,eventList.get(i+1)-eventList.get(i));
		if (max==Double.NEGATIVE_INFINITY)
			return 0;
		else 
			return max;
		}
	
}
