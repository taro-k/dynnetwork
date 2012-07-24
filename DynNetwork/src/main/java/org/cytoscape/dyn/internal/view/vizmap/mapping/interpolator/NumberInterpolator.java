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
 * <code> NumberInterpolator </code> assumes that the domain values
 * are some kind of number, and extracts the values into ordinary doubles for
 * the convenience of subclasses. If any argument is null, or if any of the
 * domain values is not an instance of Number, null is returned.

 * 
 * @author cytoscape
 */
abstract public class NumberInterpolator<R> implements Interpolator<Number, R> 
{

	@Override
	public <T extends Number> R getRangeValue(T lowerDomain, R lowerRange,
			T upperDomain, R upperRange, T domainValue) 
	{

		if (lowerDomain == null || lowerRange == null || upperDomain == null
				|| upperRange == null || domainValue == null)
			return null;

		return getRangeValue(lowerDomain.doubleValue(), lowerRange, upperDomain
				.doubleValue(), upperRange, domainValue.doubleValue());
	}

	/**
	 * Get range value.
	 * @param lowerDomain
	 * @param lowerRange
	 * @param upperDomain
	 * @param upperRange
	 * @param domainValue
	 * @return value
	 */
	abstract public R getRangeValue(double lowerDomain, R lowerRange,
			double upperDomain, R upperRange, double domainValue);
}
