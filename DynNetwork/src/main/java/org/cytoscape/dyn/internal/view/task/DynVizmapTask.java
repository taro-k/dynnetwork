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

package org.cytoscape.dyn.internal.view.task;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Collection;

import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.vizmap.VisualMappingFunction;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.mappings.BoundaryRangeValues;
import org.cytoscape.view.vizmap.mappings.ContinuousMapping;
import org.cytoscape.view.vizmap.mappings.ContinuousMappingPoint;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

/**
 * <code> DynVizmapTask </code> is responsible for updating the {@link VisualStyle} by computing
 * the mapped values range in oder to take into account the whole dynamic range and not only the 
 * current time snapshot.
 * 
 * @author sabina
 *
 * @param <T>
 */
public final class DynVizmapTask<T> extends AbstractTask
{
	private DynNetworkView<T> view;
	private VisualStyle visualStyle;
	private final VisualMappingFunctionFactory continousFactory;
	private final VisualMappingFunctionFactory discreteFactory;
	private final VisualMappingFunctionFactory passthroughFactory;
	private final BlockingQueue queue;
	
	/**
	 * <code> DynVizmapTask </code> constructor.
	 * @param view
	 * @param visualStyle
	 * @param continousFactory
	 * @param discreteFactory
	 * @param passthroughFactory
	 * @param queue
	 */
	public DynVizmapTask(
			DynNetworkView<T> view, 
			VisualStyle visualStyle,
			final VisualMappingFunctionFactory continousFactory,
			final VisualMappingFunctionFactory discreteFactory,
			final VisualMappingFunctionFactory passthroughFactory,
			final BlockingQueue queue) 
	{
		this.view = view;
		this.visualStyle = visualStyle;
		this.continousFactory = continousFactory;
		this.discreteFactory = discreteFactory;
		this.passthroughFactory = passthroughFactory;
		this.queue = queue;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception 
	{
		queue.lock();
		
		visualStyle = view.getCurrentVisualStyle();
		ArrayList<VisualMappingFunction<?, ?>> mappings = copy(visualStyle.getAllVisualMappingFunctions());
		for (VisualMappingFunction<?, ?> visualMapping : mappings)
		{
			if (visualMapping instanceof ContinuousMapping<?,?>)
			{
				VisualMappingFunction<?, ?> newVisualMapping = continousFactory.createVisualMappingFunction(
						visualMapping.getMappingColumnName(), visualMapping.getMappingColumnType(), visualMapping.getVisualProperty());
				makeContinousMapping(newVisualMapping);
//				printContinousMapping(visualMapping);
//				printContinousMapping(newVisualMapping);
				visualStyle.removeVisualMappingFunction(visualMapping.getVisualProperty());
				visualStyle.addVisualMappingFunction(newVisualMapping);
				
			}
//			else if (visualMapping instanceof DiscreteMapping<?,?>)
//			{
//
//			}
//			else if (visualMapping instanceof PassthroughMapping<?,?>)
//			{
//
//			}
		}

		visualStyle.apply(view.getNetworkView());
		
		queue.unlock();
	}
	
	@SuppressWarnings("unchecked")
	private <K, V> void makeContinousMapping(VisualMappingFunction<?, ?> visualMapping)
	{
		ContinuousMapping<K, V> mapping = (ContinuousMapping<K, V>) visualMapping;
		String name = mapping.getMappingColumnName();
		VisualProperty vp = mapping.getVisualProperty();
		
		K min = (K) view.getNetwork().getMinValue(name, vp.getTargetDataType());
		K max = (K) view.getNetwork().getMaxValue(name, vp.getTargetDataType());

		if (vp.getRange().getType() == Color.class || vp.getRange().getType() == Paint.class)
		{
			mapping.addPoint(min, new BoundaryRangeValues<V>((V) Color.BLACK, (V) Color.BLACK, (V) Color.BLACK));
			mapping.addPoint(max, new BoundaryRangeValues<V>((V) Color.WHITE, (V) Color.WHITE, (V) Color.WHITE));
		}
		else if (Number.class.isAssignableFrom(vp.getRange().getType()))
		{
			mapping.addPoint(min, new BoundaryRangeValues<V>((V) min, (V) min, (V) min));
			mapping.addPoint(max, new BoundaryRangeValues<V>((V) max, (V) max, (V) max));
		}
		else
		{
			mapping.addPoint(min, new BoundaryRangeValues<V>((V) new Boolean(false), (V) new Boolean(false), (V) new Boolean(false)));
			mapping.addPoint(max, new BoundaryRangeValues<V>((V) new Boolean(true), (V) new Boolean(true), (V) new Boolean(true)));
		}

		
	}
	
	@SuppressWarnings("unchecked")
	private <K, V> void printContinousMapping(VisualMappingFunction<?, ?> visualMapping)
	{
		System.out.println("\nMAPPING attribute : " + visualMapping.getMappingColumnName());
		ContinuousMapping<K, V> mapping = (ContinuousMapping<K, V>) visualMapping;
		for (ContinuousMappingPoint<K, V> point : mapping.getAllPoints())
		{
			System.out.println(
					" K=" + point.getValue() + 
					" lesser=" + point.getRange().lesserValue +
					" equal=" + point.getRange().equalValue +
					" greater=" + point.getRange().greaterValue);
		}
	}
	
	private ArrayList<VisualMappingFunction<?, ?>> copy(Collection<VisualMappingFunction<?, ?>> mappings)
	{
		ArrayList<VisualMappingFunction<?, ?>> newList = new ArrayList<VisualMappingFunction<?, ?>>(mappings.size());
		for (VisualMappingFunction<?, ?> visualMapping : mappings)
			newList.add(visualMapping);
		return newList;
	}
	

}
