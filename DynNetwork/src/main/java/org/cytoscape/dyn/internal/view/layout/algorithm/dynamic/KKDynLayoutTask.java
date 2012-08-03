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

import java.awt.Dimension;
import java.util.List;
import java.util.Set;

import org.cytoscape.dyn.internal.model.DynNetworkSnapshot;
import org.cytoscape.dyn.internal.model.DynNetworkSnapshotImpl;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.layout.DynLayout;
import org.cytoscape.dyn.internal.view.layout.algorithm.standard.KKLayout;
import org.cytoscape.dyn.internal.view.layout.algorithm.util.UnweightedShortestPath;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.layout.AbstractLayoutTask;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.undo.UndoSupport;

/**
 * <code> SmoothKKDynLayoutTask </code> is responsible for the generation of force-based network
 * dynamics by associating to each nodes in the network appropriate intervals of node positions,
 * which are stored in {@link DynLayout}. The algorithm is based on the Kamada-Kawai algorithm 
 * for node layout. Following options are available: set the number of maximum iterations per event
 * (number of iterations per event will be automatically computed depending on the event time differences);
 * setting past and future events value (it is possible to take into consideration previous and future 
 * events, which guarantees smoothness of transition in time).
 * 
 * @see "Tomihisa Kamada and Satoru Kawai: An algorithm for drawing general indirect graphs. Information Processing Letters 31(1):7-15, 1989" 
 * @see "Tomihisa Kamada: On visualization of abstract objects and relations. Ph.D. dissertation, Dept. of Information Science, Univ. of Tokyo, Dec. 1988."
 * 
 * @author Sabina Sara Pfister
 *
 * @param <T>
 */
public class KKDynLayoutTask<T> extends AbstractLayoutTask 
{
    
	private final DynLayout<T> layout;
	private final CyNetworkView view;
	private final DynNetworkView<T> dynView;
	
	private DynNetworkSnapshot<T> snap;
	private KKLayout<T> kklayout;
	
	private final double currentTime;
	private final boolean cancel;
	
	private final int pastEvents;
	private final int futureEvents;
	private final double iterationRate;
	
	/**
	 * <code> SmoothKKDynLayoutTask </code> constructor.
	 * @param name
	 * @param layout
	 * @param dynView
	 * @param nodesToLayOut
	 * @param layoutAttribute
	 * @param undo
	 * @param currentTime
	 * @param pastEvents
	 * @param futureEvents
	 * @param maxItertions
	 * @param cancel
	 */
    public KKDynLayoutTask(
                    final String name,
                    final DynLayout<T> layout,
                    final DynNetworkView<T> dynView,
                    final Set<View<CyNode>> nodesToLayOut, 
                    final String layoutAttribute,
                    final UndoSupport undo,
                    final double currentTime,
                    final int pastEvents,
                	final int futureEvents,
                	final double maxItertions,
                    final boolean cancel)
    {
            super(name, layout.getNetworkView(), nodesToLayOut, layoutAttribute, undo);
            this.layout = layout;
            this.view = layout.getNetworkView();
            this.dynView = dynView;
            this.currentTime = currentTime;
            this.pastEvents = pastEvents;
            this.futureEvents = futureEvents;
            this.iterationRate = maxItertions;
            this.cancel = cancel;
    }

	@Override
	protected void doLayout(TaskMonitor taskMonitor)
	{	
		if (!cancel && networkView!=null && dynView!=null)
		{
			taskMonitor.setTitle("Compute Dynamic Kamada Kawai Force Layout");
			taskMonitor.setStatusMessage("Running energy minimization...");
			taskMonitor.setProgress(0);
			
			int size = (int) (4*50*Math.sqrt(nodesToLayOut.size()));
			
			snap = new DynNetworkSnapshotImpl<T>(dynView);
			kklayout = new KKLayout<T>(snap,new Dimension(size,size));
			List<Double> events = dynView.getNetwork().getNodeEventTimeList();
			
			// Initialize node positions at the center of the screen
			initializePositions(size);
			
			// Full KK evaluation to initialize the network at time t=0
			kklayout.setAdjustForGravity(true);
			kklayout.setExchangeVertices(true);
			kklayout.setMaxIterations(2000);
			
			taskMonitor.setProgress(0.5);

			// Compute incremental KK. The number of iterations is proportional to the time to the next event.
			double t0,t1;
			for (int t=0;t<events.size()-1;t++)
			{
				t0 = events.get(Math.max(0,t-pastEvents));
				t1 = events.get(Math.min(events.size()-1,t+1+futureEvents));
				snap.setInterval(new DynInterval<T>(t0,t1));
				kklayout.setDistance(new UnweightedShortestPath<T>(snap));
				kklayout.initialize();
				kklayout.run();
				updateGraph(new DynInterval<T>(events.get(t),events.get(Math.min(events.size()-1,t+1))));
				
				kklayout.setExchangeVertices(false);
				kklayout.setMaxIterations((int) (iterationRate*(events.get(t+1)-events.get(t))));

				taskMonitor.setStatusMessage("Running energy minimization..." + t + "/" + (events.size()-1));
			}

			taskMonitor.setProgress(1);
			
			// Set the current network view.
			layout.initNodePositions(currentTime);
			view.fitContent();
    		view.updateView();
		}
	}
	
	private void initializePositions(int size)
	{
		for (CyNode node : dynView.getNetworkView().getModel().getNodeList())
		{
			dynView.writeVisualProperty(node, BasicVisualLexicon.NODE_X_LOCATION, size/2);
			dynView.writeVisualProperty(node, BasicVisualLexicon.NODE_Y_LOCATION, size/2);
		}
	}
	
	private void updateGraph(DynInterval<T> interval)
	{
		for (CyNode node : kklayout.getGraph().getNodes())
		{
			layout.insertNodePositionX(node, new DynInterval<T>(kklayout.getX(node),interval.getStart(),interval.getEnd()));
			layout.insertNodePositionY(node, new DynInterval<T>(kklayout.getY(node),interval.getStart(),interval.getEnd()));
		}
	}

}
