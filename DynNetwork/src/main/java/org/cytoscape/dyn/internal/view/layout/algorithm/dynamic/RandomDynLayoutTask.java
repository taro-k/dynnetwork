/*
 * DynNetwork plugin for Cytoscape 3.0 (http://www.cytoscape.org/).
 * Copyright (C) 2012 Sabina Sara Pfister
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.cytoscape.dyn.internal.view.layout.algorithm.dynamic;

import java.util.Set;

import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.layout.DynLayout;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.layout.AbstractLayoutTask;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.undo.UndoSupport;

/**
 * <code> RandomDynLayoutTask </code> is responsible for the generation of random network
 * dynamics by associating to each nodes in the network ransom intervals of node positions,
 * which are stored in {@link DynLayout}. Use this class as an example of how to write custom
 * dynamic layout algorithms.
 * 
 * @author Sabina Sara Pfister
 *
 * @param <T>
 */
public final class RandomDynLayoutTask<T> extends AbstractLayoutTask 
{
    
	private final DynLayout<T> layout;
	private final CyNetworkView view;
	private final DynNetworkView<T> dynView;
	
	private final double currentTime;

	
	/**
	 * <code> RandomDynLayoutTask </code> constructor.
	 * @param name
	 * @param layout
	 * @param nodesToLayOut
	 * @param layoutAttribute
	 * @param undo
	 * @param currentTime
	 */
    public RandomDynLayoutTask(
                    final String name,
                    final DynLayout<T> layout,
                    final DynNetworkView<T> dynView,
                    final Set<View<CyNode>> nodesToLayOut, 
                    final String layoutAttribute,
                    final UndoSupport undo,
                    final double currentTime)
    {
            super(name, layout.getNetworkView(), nodesToLayOut, layoutAttribute, undo);
            this.layout = layout;
            this.view = layout.getNetworkView();
            this.dynView = dynView;
            this.currentTime = currentTime;
    }

    @Override
    protected void doLayout(TaskMonitor taskMonitor)
    {	
    	if (networkView!=null && dynView!=null)
    	{
    		// Compute distance between nodes depending on the number of present nodes
    		double dist = 2*50*Math.sqrt(nodesToLayOut.size());
    		
    		// First we set the initial node positions to some random location.
    		for (View<CyNode> nv : nodesToLayOut)
    		{
    			nv.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, Math.random()*dist);
    			nv.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, Math.random()*dist);
    		}

    		// Get a sorted list of all node events: we will make every node move randomly
    		// every time a node appears and disappears.
    		for (DynInterval<T> interval : dynView.getNetwork().getNodesIntervals())
    		{
    			CyNode node = dynView.getNetwork().getNode(interval);
    			if (node!=null)
    			{
    				layout.insertNodePositionX(node, new DynInterval<T>(Math.random()*dist,Math.random()*dist,interval.getStart(),interval.getEnd()));
    				layout.insertNodePositionY(node, new DynInterval<T>(Math.random()*dist,Math.random()*dist,interval.getStart(),interval.getEnd()));
    			}
    		}

    		// We update the visualization
    		layout.initNodePositions(currentTime);
    		view.fitContent();
    		view.updateView();

    	}
    }
}
