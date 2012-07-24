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

package org.cytoscape.dyn.internal.view.vizmap.mapping;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;

import org.cytoscape.dyn.internal.view.vizmap.mapping.interpolator.FlatInterpolator;
import org.cytoscape.dyn.internal.view.vizmap.mapping.interpolator.Interpolator;
import org.cytoscape.dyn.internal.view.vizmap.mapping.interpolator.LinearNumberToColorInterpolator;
import org.cytoscape.dyn.internal.view.vizmap.mapping.interpolator.LinearNumberToNumberInterpolator;
import org.cytoscape.model.CyRow;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.vizmap.mappings.AbstractVisualMappingFunction;
import org.cytoscape.view.vizmap.mappings.BoundaryRangeValues;
import org.cytoscape.view.vizmap.mappings.ContinuousMapping;
import org.cytoscape.view.vizmap.mappings.ContinuousMappingPoint;

/**
 * <code> DynContinousMappingImpl </code> is a dynamic implementation of a 
 * {@link AbstractVisualMappingFunction}.
 * 
 * @author cytoscape
 *
 */
public class DynContinousMappingImpl<K, V> extends AbstractVisualMappingFunction<K, V> implements ContinuousMapping<K, V>
{
	private Interpolator<K, V> interpolator;
	private List<ContinuousMappingPoint<K, V>> points;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DynContinousMappingImpl(final String attrName, final Class<K> attrType, final VisualProperty<V> vp) {
		super(attrName, attrType, vp);
		
		if (Number.class.isAssignableFrom(attrType) == false)
			throw new IllegalArgumentException("Attribute type should be Number.");
		
		this.points = new ArrayList<ContinuousMappingPoint<K, V>>();
		if (vp.getRange().getType() == Color.class || vp.getRange().getType() == Paint.class)
			interpolator = (Interpolator<K, V>) new LinearNumberToColorInterpolator();
		else if (Number.class.isAssignableFrom(vp.getRange().getType()))
			interpolator = (Interpolator<K, V>) new LinearNumberToNumberInterpolator();
		else
			interpolator = (Interpolator<K, V>) new FlatInterpolator();
	}

	@Override
	public String toString() 
	{
		return ContinuousMapping.CONTINUOUS;
	}

	@Override
	public List<ContinuousMappingPoint<K, V>> getAllPoints() 
	{
		return points;
	}

	@Override
	public void addPoint(K value, BoundaryRangeValues<V> brv) 
	{
		points.add(new ContinuousMappingPoint<K, V>(value, brv));
	}

	@Override
	public void removePoint(int index) 
	{
		points.remove(index);
	}

	@Override
	public int getPointCount() 
	{
		return points.size();
	}

	@Override
	public ContinuousMappingPoint<K, V> getPoint(int index) 
	{
		return points.get(index);
	}

	@Override
	public V getMappedValue(final CyRow row) 
	{
		V value = null;
		
		if (row != null && row.isSet(columnName)) 
		{
			final K attrValue = row.get(columnName, columnType);
			value = getRangeValue(attrValue);
		}
		
		return value;
	}

	private V getRangeValue(K domainValue) 
	{
		ContinuousMappingPoint<K, V> firstPoint = points.get(0);
		K minDomain = firstPoint.getValue();

		int firstCmp = compareValues(domainValue, minDomain);

		if (firstCmp <= 0) 
		{
			BoundaryRangeValues<V> bv = firstPoint.getRange();

			if (firstCmp < 0)
				return bv.lesserValue;
			else
				return bv.equalValue;
		}

		ContinuousMappingPoint<K, V> lastPoint = points.get(points.size() - 1);
		K maxDomain = lastPoint.getValue();

		if (compareValues(domainValue, maxDomain) > 0) 
		{
			BoundaryRangeValues<V> bv = lastPoint.getRange();

			return bv.greaterValue;
		}

		if (this.interpolator == null)
			return null;

		ContinuousMappingPoint<K, V> currentPoint;
		int index = 0;

		for (index = 0; index < points.size(); index++) 
		{
			currentPoint = points.get(index);

			K currentValue = currentPoint.getValue();
			int cmpValue = compareValues(domainValue, currentValue);

			if (cmpValue == 0) 
			{
				BoundaryRangeValues<V> bv = currentPoint.getRange();

				return bv.equalValue;
			} 
			else if (cmpValue < 0)
				break;
		}

		return getRangeValue(index, domainValue);
	}

	private V getRangeValue(int index, K domainValue) 
	{
		ContinuousMappingPoint<K, V> lowerBound = points.get(index - 1);
		K lowerDomain = lowerBound.getValue();
		BoundaryRangeValues<V> lv = lowerBound.getRange();
		V lowerRange = lv.greaterValue;

		ContinuousMappingPoint<K, V> upperBound = points.get(index);
		K upperDomain = upperBound.getValue();
		BoundaryRangeValues<V> gv = upperBound.getRange();
		V upperRange = gv.lesserValue;
		
		V value = interpolator.getRangeValue(lowerDomain, lowerRange, upperDomain, upperRange, domainValue);
		
		return value;
	}

	private int compareValues(K probe, K target) 
	{
		final Number n1 = (Number) probe;
		final Number n2 = (Number) target;
		double d1 = n1.doubleValue();
		double d2 = n2.doubleValue();

		if (d1 < d2)
			return -1;
		else if (d1 > d2)
			return 1;
		else
			return 0;
	}

}
