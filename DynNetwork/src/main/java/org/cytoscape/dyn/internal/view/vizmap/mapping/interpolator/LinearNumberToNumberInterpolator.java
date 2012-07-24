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
 * The class assumes that the supplied range objects are Numbers, and returns a
 * linearly interplated value according to the value of frac.
 * 
 * If either object argument is not a Number, null is returned.
 * 
 * @author cytoscape
 */
public class LinearNumberToNumberInterpolator extends
		LinearNumberInterpolator<Number> 
{

	@Override
	public Number getRangeValue(double frac, Number lowerRange,
			Number upperRange) 
	{

		double lowerVal = lowerRange.doubleValue();
		double upperVal = upperRange.doubleValue();

		return (frac * upperVal) + ((1.0 - frac) * lowerVal);
	}
}
