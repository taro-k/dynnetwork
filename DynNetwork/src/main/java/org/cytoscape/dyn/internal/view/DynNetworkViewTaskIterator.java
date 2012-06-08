package org.cytoscape.dyn.internal.view;


import javax.swing.JSlider;

/**
 * <code> DynNetworkViewTaskIterator </code> implements a task iterator to update
 * the visualization sequentially.
 * 
 * @author sabina
 *
 * @param <T>
 */
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
	public void run() 
	{
		slider.setValueIsAdjusting(true);
		
		while((timeStep==1 && slider.getValue()<100) || (timeStep==-1 && slider.getValue()>0))
		{
			if (this.cancelled==true)
			{
				slider.setValueIsAdjusting(false);
				break;
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (this.cancelled==true)
				break;
			
			if ((timeStep==1 && slider.getValue()==100) || (timeStep==-1 && slider.getValue()==0))
				slider.setValueIsAdjusting(false);
			
			slider.setValue(slider.getValue()+timeStep);
		}

		
	}

	public void cancel() 
	{
		this.cancelled = true;
	}
	
	
}
