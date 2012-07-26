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
 * <code> IntegerInterpolator </code> is the abstract class for a integer interpolator.
 * The interpolation routine is either a stepwise, simple linear, or smoothed
 * interpolation between each of the points. The interpolator may take
 * arbitrarily spaced keyframes and compute correct values.
 *
 * @author Alan Hudson, Sabina Sara Pfister
 */
public class IntegerInterpolator extends AbstractInterpolator
{
    private int[] keyValues;

    /**
     * <code> IntegerInterpolator </code> constructor.
     */
    public IntegerInterpolator()
    {
    	super(DEFAULT_SIZE);
    }

    /**
     * <code> IntegerInterpolator </code> constructor.
     * @param size
     */
    public IntegerInterpolator(int size)
    {
        super(size);
    }

   /**
    * Add a key frame set of values at the given key point.
    * @param key
    * @param value
    */
    public void addKeyFrame(float key, int value)
    {
        int loc = findKeyIndex(key);
        
        if(loc < 0)
            loc = 0;
        while (loc<currentSize && keys[loc]<=key)
            loc++;

        realloc();

        if(loc >= currentSize)
        {
            keyValues[currentSize] = value;
        }
        else
        {
            int num_moving = currentSize - loc;

            System.arraycopy(keyValues, loc, keyValues, loc + 1, num_moving);
            System.arraycopy(keys, loc, keys, loc + 1, num_moving);

            keyValues[loc] = value;
        }

        keys[loc] = key;
        currentSize++;
    }
    
    /**
     * Get the step interpolated value of the point at the given key value
     * @param key
     * @return value
     */
    public int intValueStep(float key)
    {
        int loc = findKeyIndex(key);
        int ret_val;

        if(loc < 0)
        {
           ret_val = keyValues[0];
        }
        else if(loc >= (currentSize - 1))
        {
        	ret_val = keyValues[currentSize - 1];
        }
        else
        {
        	ret_val = keyValues[loc];
        }

        return ret_val;
    }

    /**
     * Get the linear interpolated value of the point at the given key value.
     * @param key
     * @return value
     */
    public int intValueLinear(float key)
    {
    	int loc = findKeyIndex(key);
    	int ret_val;

    	if(loc < 0)
    	{
    		ret_val = keyValues[0];
    	}
    	else if(loc >= (currentSize - 1))
    	{
    		ret_val = keyValues[currentSize - 1];
    	}
    	else
    	{
    		int p1 = keyValues[loc + 1];
    		int p0 = keyValues[loc];

    		float fraction = 0;

    		float prev_key = keys[loc];
    		float found_key = keys[loc + 1];

    		if(found_key != prev_key)
    			fraction = (key - prev_key) / (found_key - prev_key);

    		if (fraction <= 0.5)
    			ret_val = p0;
    		else
    			ret_val = p1;

    	}

    	return ret_val;
    }
    
    /**
     * Get the smooth interpolated value of the point at the given key value.
     * @param key
     * @return value
     */
    public int intValueSmooth(float key)
    {
    	int loc = findKeyIndex(key);
    	int ret_val;

    	if(loc < 0)
    	{
    		ret_val = keyValues[0];
    	}
    	else if(loc >= (currentSize - 1))
    	{
    		ret_val = keyValues[currentSize - 1];
    	}
    	else
    	{
    		int p1 = keyValues[loc + 1];
    		int p0 = keyValues[loc];

    		float fraction = 0;

    		float prev_key = keys[loc];
    		float found_key = keys[loc + 1];

    		if(found_key != prev_key)
    			fraction = (key - prev_key) / (found_key - prev_key);

    		if (fraction <= 0.5)
    			ret_val = p0;
    		else
    			ret_val = p1;

    	}

    	return ret_val;
    }

    private final void realloc()
    {
        if(currentSize == allocatedSize)
        {
            int new_size = allocatedSize + ARRAY_INCREMENT;
            int[] new_values = new int[new_size];
            System.arraycopy(keyValues, 0, new_values, 0, allocatedSize);

            float[] new_keys = new float[new_size];
            System.arraycopy(keys, 0, new_keys, 0, allocatedSize);

            keys = new_keys;
            keyValues = new_values;

            allocatedSize = new_size;
        }
    }

    @Override
    public String toString()
    {
        StringBuffer buf = new StringBuffer("<integer interpolator>\n");

        for(int i = 0; i < currentSize; i++)
        {
            buf.append(i);
            buf.append(" key: ");
            buf.append(keys[i]);
            buf.append(" value: ");
            buf.append("\n");
        }

        buf.append("</integer interpolator>");
        return buf.toString();
    }
}