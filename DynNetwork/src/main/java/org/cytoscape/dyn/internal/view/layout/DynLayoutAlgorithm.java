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

import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.layout.AbstractLayoutAlgorithm;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.undo.UndoSupport;

public class DynLayoutAlgorithm<T> extends AbstractLayoutAlgorithm
{
    private final DynNetworkViewManager<T> networkViewManager;
    
    public DynLayoutAlgorithm(
                    String computerName, 
                    String humanName,
                    UndoSupport undoSupport,
                    DynNetworkViewManager<T> networkViewManager)
    {
            super(computerName, humanName, undoSupport);
            this.networkViewManager = networkViewManager;
    }

    @Override
    public TaskIterator createTaskIterator(
                    CyNetworkView networkView,
                    Object layoutContext, 
                    Set<View<CyNode>> nodesToLayOut,
                    String layoutAttribute)
    {
            DynNetworkView<T> view = networkViewManager.getDynNetworkView(networkView);
            
            if (view!=null)
                    return new TaskIterator(new DynLayoutAlgorithmTask<T>(getName(), view, nodesToLayOut, layoutAttribute, undoSupport));
            else
                    return new TaskIterator();
    }

}
