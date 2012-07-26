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
 * 
 * The code below is modified from the org.j3d.util.interpolator library.
 * 
 ********************************************************************************
 *                        J3D.org Copyright (c) 2000
 *                               Java Source
 *
 * This source is licensed under the GNU LGPL v2.1
 * Please read http://www.gnu.org/copyleft/lgpl.html for more information
 *
 ********************************************************************************
 */

package org.cytoscape.dyn.internal.view.task.interpolator;

/**
 * <code> AbstractInterpolator </code> is the abstract class for a generic interpolator.
 * The interpolation routine is just a simple interpolation between
 * each of the points. The interpolator may take arbitrarily spaced keyframes
 * and compute correct values.
 *
 * @author Justin Couch
 * 
 */
public abstract class AbstractInterpolator
{
    protected static final int DEFAULT_SIZE = 20;
    protected static final int ARRAY_INCREMENT = 5;
    protected int allocatedSize;
    protected int currentSize;
    protected float[] keys;

    /**
     * <code> AbstractInterpolator </code> constructor.
     * @param size
     */
    protected AbstractInterpolator(int size)
    {
    	keys = new float[size];
    }

    /**
     * Reset the interpolator to be empty so that new key values are replacing
     * the old ones.
     */
    public void clear()
    {
        currentSize = 0;
    }
    
    /**
     * Create a string representation of this interpolator's values
     *
     * @return string
     */
    abstract public String toString();
    
    protected int findKeyIndex(float key)
    {
        if((currentSize == 0) || (key <= keys[0]))
            return -1;
        else if(key == keys[currentSize - 1])
            return currentSize - 1;
        else if(key > keys[currentSize - 1])
            return currentSize;

        int start = 0;
        int end = currentSize - 1;
        int mid = currentSize >> 1; 

        while(start < end)
        {
            float test = keys[mid];

            if(test >= key)
                end = mid - 1;
            else
                start = mid;  
            mid = (start + end + 1) >> 1;
        }

        return mid;
    }
    
    
}