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
 * <code> LinearNumberInterpolator </code> further assumes a linear interpolation,
 * and calculates the fractional distance of the target domain value from the
 * lower boundary value for the convenience of subclasses.
 * 
 * @author cytoscape
 */
abstract public class LinearNumberInterpolator<R> extends NumberInterpolator<R> 
{

	/**
	 * Get range value.
	 * @param frac
	 * @param lowerRange
	 * @param upperRange
	 * @return value
	 */
	abstract public R getRangeValue(double frac, R lowerRange, R upperRange);

	@Override
	public R getRangeValue(double lowerDomain, R lowerRange,
			double upperDomain, R upperRange, double domainValue) 
	{

		if (lowerDomain == upperDomain)
			return lowerRange;

		double frac = (domainValue - lowerDomain) / (upperDomain - lowerDomain);

		return getRangeValue(frac, lowerRange, upperRange);
	}
}
