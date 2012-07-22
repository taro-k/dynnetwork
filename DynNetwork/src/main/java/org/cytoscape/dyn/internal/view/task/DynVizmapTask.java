package org.cytoscape.dyn.internal.view.task;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.vizmap.VisualMappingFunction;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.mappings.ContinuousMapping;
import org.cytoscape.view.vizmap.mappings.ContinuousMappingPoint;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;

public final class DynVizmapTask<T>  implements Runnable
{
	private DynNetworkView<T> view;
	private VisualStyle visualStyle;
	
	public DynVizmapTask(
			DynNetworkView<T> view, 
			VisualStyle visualStyle) 
	{
		this.view = view;
		this.visualStyle = visualStyle;
	}

	@Override
	public void run() 
	{
		visualStyle = view.getCurrentVisualStyle();
		Collection<VisualMappingFunction<?, ?>> mappings = visualStyle.getAllVisualMappingFunctions();
		for (VisualMappingFunction<?, ?> function : mappings)
		{
			if (function instanceof ContinuousMapping<?,?>)
				fixContinuousMapping((VisualMappingFunction<?, ?>) function);
			else if (function instanceof DiscreteMapping<?,?>)
				fixDiscreteMapping((VisualMappingFunction<?, ?>) function);
			else if (function instanceof PassthroughMapping<?,?>)
				fixPassthroughMapping((VisualMappingFunction<?, ?>) function);
		}
	}
	
	private <K,V> void fixContinuousMapping(VisualMappingFunction<?,?> visualMapping)
	{
		ContinuousMapping<K,V> mapping = (ContinuousMapping<K,V>) visualMapping;
		System.out.println(mapping.getMappingColumnName() + " " + mapping.getClass());
		for (ContinuousMappingPoint<K, V> point : mapping.getAllPoints())
			System.out.println(" point " + point.getValue() + " " + point.getRange().lesserValue + " " + point.getRange().equalValue + " " + point.getRange().greaterValue);
//		visualStyle.removeVisualMappingFunction(mapping.getVisualProperty());
	}

	private <K,V> void fixDiscreteMapping(VisualMappingFunction<?,?> visualMapping)
	{
		DiscreteMapping<K,V> mapping = (DiscreteMapping<K,V>) visualMapping;
		System.out.println(mapping.getMappingColumnName() + " " + mapping.getClass());
		Iterator<K> iterator = mapping.getAll().keySet().iterator();
		while (iterator.hasNext())
		{
			K key = iterator.next();
		    System.out.println(" point " +  key + " " + mapping.getMapValue(key));
		}
//		visualStyle.removeVisualMappingFunction(mapping.getVisualProperty());
	
	}
	
	private <K,V> void fixPassthroughMapping(VisualMappingFunction<?,?> visualMapping)
	{
		PassthroughMapping<K,V> mapping = (PassthroughMapping<K,V>) visualMapping;
		System.out.println(mapping.getMappingColumnName() + " " + mapping.getClass());
		List<CyNode> nodeList = view.getNetwork().getNetwork().getNodeList();
		for (CyNode node : nodeList)
		{
			 System.out.println(" point " +  node.getSUID() + " " + mapping.getMappedValue(view.getNetwork().getNetwork().getRow(node)));
		}
//		visualStyle.removeVisualMappingFunction(mapping.getVisualProperty());
	}
	
}
