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

package org.cytoscape.dyn.internal.vizmap.mapping.interpolator;



//----------------------------------------------------------------------------
/**
 * This simple Interpolator returns the value at either the lower or upper
 * boundary of the domain. Note that no check is made whether the supplied
 * domainValue is actually within the boundaries.
 */
public class FlatInterpolator<V, R> implements Interpolator<V, R> {
	/**
	 *
	 */
	public static final Integer LOWER = Integer.valueOf(0);

	/**
	 *
	 */
	public static final Integer UPPER = Integer.valueOf(1);
	private boolean useLower;

	/**
	 * The default FlatInterpolator returns the range value at the lower
	 * boundary.
	 */
	public FlatInterpolator() {
		useLower = true;
	}

	/**
	 * Constructs a FlatInterpolator which returns the range value at the lower
	 * boundary unless the argument 'mode' is equal to FlatInterpolator.UPPER.
	 */
	public FlatInterpolator(Integer mode) {
		if (mode.equals(UPPER))
			useLower = false;
		else
			useLower = true;
	}
//
//	/**
//	 * DOCUMENT ME!
//	 *
//	 * @param lowerDomain
//	 *            DOCUMENT ME!
//	 * @param lowerRange
//	 *            DOCUMENT ME!
//	 * @param upperDomain
//	 *            DOCUMENT ME!
//	 * @param upperRange
//	 *            DOCUMENT ME!
//	 * @param domainValue
//	 *            DOCUMENT ME!
//	 *
//	 * @return DOCUMENT ME!
//	 */
//	public R getRangeValue(V lowerDomain, R lowerRange, V upperDomain, R upperRange, V domainValue) {
//		return ((useLower) ? lowerRange : upperRange);
//	}

	public <T extends V> R getRangeValue(T lowerDomain, R lowerRange,
			T upperDomain, R upperRange, T domainValue) {
		return ((useLower) ? lowerRange : upperRange);
	}
}
