package org.cytoscape.dyn.internal.view.task;

import org.cytoscape.dyn.internal.view.task.interpolator.Coordinate2DInterpolator;
import org.cytoscape.dyn.internal.view.task.interpolator.ScalarInterpolator;

/**
 * <code> Transformator </code> is used to change visual properties by interpolating
 * the values to change. The supported interpolation methods are STEP, LINEAR, or 
 * SMOOTH. By default SMOOTH interpolation is set.
 * 
 * @author Sabina Sara Pfister
 *
 */
public class Transformator 
{
	public static final int STEP = 1;
    public static final int LINEAR = 2;
    public static final int SMOOTH = 3;
    
    private int interpolationType;
    
	private final ScalarInterpolator scalarInterpolator;
	private final Coordinate2DInterpolator coordInterpolator;

	/**
	 * <code> Transformator </code> constructor.
	 */
	public Transformator() 
	{
		this.scalarInterpolator = new ScalarInterpolator();
		this.coordInterpolator = new Coordinate2DInterpolator();
		this.interpolationType = LINEAR;
	}
	
	public void run()
	{
		switch(interpolationType)
		{
		case STEP:
		{
			break;
		}
		case LINEAR:
		{
			break;
		}
		case SMOOTH:
		{
			break;
		}
		}
	}
	
	public void setLinearInterpolation()
	{
		this.interpolationType = LINEAR;
	}
	
	public void setStepInterpolation()
	{
		this.interpolationType = STEP;
	}
	
	public void setSmoothInterpolation()
	{
		this.interpolationType = SMOOTH;
	}
	
	
}
