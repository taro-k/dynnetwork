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

package org.cytoscape.dyn.internal.view.layout;

import java.util.Set;

import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.layout.AbstractLayoutTask;
import org.cytoscape.view.model.View;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.undo.UndoSupport;

public final class DynLayoutAlgorithmTask<T> extends AbstractLayoutTask 
{
    
	private final DynNetworkView<T> view;
	
    public DynLayoutAlgorithmTask(
                    String name, 
                    DynNetworkView<T> view,
                    Set<View<CyNode>> nodesToLayOut, 
                    String layoutAttribute,
                    UndoSupport undo)
    {
            super(name, view.getNetworkView(), nodesToLayOut, layoutAttribute, undo);
            this.view = view;
    }

    @Override
    protected void doLayout(TaskMonitor taskMonitor)
    {
    	double timeMin = 0;
    	double timeMax = 10;
    	double timeStep = 1;

    	for (double time=timeMin; time<timeMax; time=time+timeStep)
    		for (View<CyNode> nv : nodesToLayOut)
    		{
    			if (Math.random()>0.2)
    				view.insertNodePositionX(nv.getModel(), new DynInterval<T>((Class<T>) Double.class,(T) new Double(Math.random()*100-50),time,time+timeStep));
    			if (Math.random()>0.2)
    				view.insertNodePositionY(nv.getModel(), new DynInterval<T>((Class<T>) Double.class,(T) new Double(Math.random()*100-50),time,time+timeStep));
    		}

    	//            networkView.fitContent();
    }

}
