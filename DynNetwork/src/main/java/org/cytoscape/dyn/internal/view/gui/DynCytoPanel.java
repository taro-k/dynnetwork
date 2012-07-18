package org.cytoscape.dyn.internal.view.gui;

import org.cytoscape.application.swing.CytoPanelComponent;

public interface DynCytoPanel<T,C> extends CytoPanelComponent
{
	/**
	 * Initialize visualization.
	 */
	public void initView();
	
	/**
	 * Set value is adjusting.
	 * @param valueIsAdjusting
	 */
	public void setValueIsAdjusting(boolean valueIsAdjusting);

	/**
	 * Get current time.
	 * @return
	 */
	public double getTime();
	
	/**
	 * Get minimum time.
	 * @return
	 */
	public double getMinTime();
	
	/**
	 * Set minimum time.
	 * @return
	 */
	public void setMinTime(double minTime);
	
	/**
	 * Get maximum time.
	 * @return
	 */
	public double getMaxTime();

	/**
	 * Set maximum time.
	 * @return
	 */
	public void setMaxTime(double maxTime);
	
	/**
	 * Get slider maximum value;
	 * @return
	 */
	public int getSliderMax();
	
	/**
	 * Set number of visible nodes.
	 * @param nodes
	 */
	public void setNodes(int nodes);
	
	/**
	 * Set number of visible edges.
	 * @param edges
	 */
	public void setEdges(int edges);
	
	/**
	 * Get visibility;
	 * @return
	 */
	public int getVisibility();
}
