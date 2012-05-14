package org.cytoscape.dyn.internal.view;


import javax.swing.JSlider;

public class DynNetworkViewTaskIterator<T> implements Runnable 
{

	private final JSlider slider;
	private final int timeStep;
	private boolean cancelled = false;
	
	public DynNetworkViewTaskIterator(JSlider slider, int timestep)
	{
		super();
		this.slider = slider;
		this.timeStep = timestep;
	}

	@Override
	public void run() {
		
		System.out.println("Trhead running");
		
		while((timeStep==1 && slider.getValue()<100) || (timeStep==-1 && slider.getValue()>0))
		{
			if (this.cancelled==true)
				break;
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (this.cancelled==true)
				break;
			
//			slider.setValueIsAdjusting(true);
			slider.setValue(slider.getValue()+timeStep);
//			slider.setValueIsAdjusting(false);
		}
		
		System.out.println("Trhead finished running");
	}

	public void cancel() {
		this.cancelled = true;
	}
	
	
}
