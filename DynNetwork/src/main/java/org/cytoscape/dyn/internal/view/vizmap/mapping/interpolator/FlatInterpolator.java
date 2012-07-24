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

package org.cytoscape.dyn.internal.view.vizmap.mapping.interpolator;

/**
 * <code> FlatInterpolator </code> returns the value at either the lower or upper
 * boundary of the domain. Note that no check is made whether the supplied
 * domainValue is actually within the boundaries.
 * 
 * @author cytoscape
 */
public class FlatInterpolator<V, R> implements Interpolator<V, R> 
{

	public static final Integer LOWER = Integer.valueOf(0);

	public static final Integer UPPER = Integer.valueOf(1);
	private boolean useLower;

	/**
	 * <code> FlatInterpolator </code> constructor.
	 */
	public FlatInterpolator() 
	{
		useLower = true;
	}

	/**
	 * <code> FlatInterpolator </code> constructor.
	 * @param mode
	 */
	public FlatInterpolator(Integer mode)
	{
		if (mode.equals(UPPER))
			useLower = false;
		else
			useLower = true;
	}

	@Override
	public <T extends V> R getRangeValue(T lowerDomain, R lowerRange,
			T upperDomain, R upperRange, T domainValue) 
	{
		return ((useLower) ? lowerRange : upperRange);
	}
}
