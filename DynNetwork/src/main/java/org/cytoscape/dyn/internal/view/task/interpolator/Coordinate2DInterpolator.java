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
 * <code> Coordinate2DInterpolator </code> is the abstract class for a 2D coordinate
 * interpolator. The interpolation routine is either a stepwise, simple linear, or 
 * smoothed interpolation between each of the points. The interpolator may take
 * arbitrarily spaced keyframes and compute correct values.
 *
 * @author Justin Couch, Sabina Sara Pfister
 *
 */
public class Coordinate2DInterpolator extends AbstractInterpolator
{

    private float[] sharedVector;
    private float[][] keyValues;
    private int valueLength;

    /**
     * <code> Coordinate2DInterpolator </code> constructor.
     */
    public Coordinate2DInterpolator()
    {
        super(DEFAULT_SIZE);
    }

    /**
     * <code> Coordinate2DInterpolator </code> constructor.
     * @param size
     */
    public Coordinate2DInterpolator(int size)
    {
        super(size);
        keyValues = new float[size][];
        valueLength = -1;
    }

   /**
    * Add a key frame set of values at the given key point.
    * @param key
    * @param coords
    */
    public void addKeyFrame(float key, float coords[])
    {
        int loc = findKeyIndex(key);
        if(loc < 0)
            loc = 0;
        while (loc<currentSize && keys[loc]<=key)
            loc++;

        realloc();

        if(coords == null)
            throw new IllegalArgumentException("Coord array is null");

        int len = coords.length;

        if(len < 2 || len % 2 != 0)
            throw new IllegalArgumentException("Coordinates length not x 2");

        float coords1[] = new float[len];

        System.arraycopy(coords, 0, coords1, 0, len);

        if(valueLength > len || valueLength < 0)
            valueLength = len;

        if(loc >= currentSize)
        {
            keyValues[currentSize] = coords1;
        }
        else
        {
            int k = currentSize - loc;
            System.arraycopy(keyValues, loc, keyValues, loc + 1, k);
            System.arraycopy(keys, loc, keys, loc + 1, k);

            keyValues[loc] = coords1;
        }

        keys[loc] = key;
        currentSize++;
    }

    /**
     * Get the step interpolated value of the point at the given key value.
     * @param key
     * @return value
     */
    public float[] floatValueStep(float key)
    {
    	if(sharedVector == null || sharedVector.length != valueLength)
    		sharedVector = new float[valueLength];

    	int loc = findKeyIndex(key);

    	if(loc < 0)
    		System.arraycopy(keyValues[0], 0, sharedVector, 0, valueLength);
    	else if(loc >= currentSize - 1)
    		System.arraycopy(keyValues[currentSize - 1], 0, sharedVector, 0, valueLength);
    	else
    	{
    		System.arraycopy(keyValues[loc], 0, sharedVector, 0, valueLength);
    	}

    	return sharedVector;
    }
    
    /**
     * Get the linear interpolated value of the point at the given key value.
     * @param key
     * @return value
     */
    public float[] floatValueLinear(float key)
    {
    	if(sharedVector == null || sharedVector.length != valueLength)
    		sharedVector = new float[valueLength];

    	int loc = findKeyIndex(key);

    	if(loc < 0)
    		System.arraycopy(keyValues[0], 0, sharedVector, 0, valueLength);
    	else if(loc >= currentSize - 1)
    		System.arraycopy(keyValues[currentSize - 1], 0, sharedVector, 0, valueLength);
    	else
    	{
    		float p1[] = keyValues[loc + 1];
    		float p0[] = keyValues[loc];
    		float fraction = 0;
    		float prev_key = keys[loc];
    		float next_key = keys[loc + 1];
    		float diff;

    		if(next_key != prev_key)
    			fraction = (key - prev_key) / (next_key - prev_key);

    		for(int j = valueLength; --j > 1; )
    		{
    			diff = p1[j] - p0[j];
    			sharedVector[j] = p0[j] + fraction * diff;
    			j--;
    			diff = p1[j] - p0[j];
    			sharedVector[j] = p0[j] + fraction * diff;
    		}
    	}

    	return sharedVector;
    }

    /**
     * Get the smooth interpolated value of the point at the given key value.
     * @param key
     * @return value
     */
    public float[] floatValueSmooth(float key)
    {
    	if(sharedVector == null || sharedVector.length != valueLength)
    		sharedVector = new float[valueLength];

    	int loc = findKeyIndex(key);

    	if(loc < 0)
    		System.arraycopy(keyValues[0], 0, sharedVector, 0, valueLength);
    	else if(loc >= currentSize - 1)
    		System.arraycopy(keyValues[currentSize - 1], 0, sharedVector, 0, valueLength);
    	else
    	{
    		float p1[] = keyValues[loc + 1];
    		float p0[] = keyValues[loc];
    		float fraction = 0;
    		float prev_key = keys[loc];
    		float next_key = keys[loc + 1];
    		float diff;

    		if(next_key != prev_key)
    			fraction = (key - prev_key) / (next_key - prev_key);

    		for(int j = valueLength; --j > 1; )
    		{
    			diff = p1[j] - p0[j];
    			sharedVector[j] = p0[j] + fraction * diff;
    			j--;
    			diff = p1[j] - p0[j];
    			sharedVector[j] = p0[j] + fraction * diff;
    		}
    	}

    	return sharedVector;
    }


    private final void realloc()
    {
        if(currentSize == allocatedSize)
        {
            int new_size = allocatedSize + ARRAY_INCREMENT;
            float[][] new_values = new float[new_size][];
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
        StringBuffer stringbuffer = new StringBuffer("<Coordinate interpolator>\n");
        stringbuffer.append("First coord for each key\n");
        for(int i = 0; i < currentSize; i++)
        {
            stringbuffer.append(i);
            stringbuffer.append(" key: ");
            stringbuffer.append(keys[i]);
            stringbuffer.append(" x: ");
            stringbuffer.append(keyValues[i][0]);
            stringbuffer.append(" y: ");
            stringbuffer.append(keyValues[i][1]);
            stringbuffer.append("\n");
        }

        stringbuffer.append("</Coordinate interpolator>");
        return stringbuffer.toString();
    }
}