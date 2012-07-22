package org.cytoscape.dyn.internal.view.task;

import java.util.ArrayList;
import java.util.Collection;

import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.view.vizmap.VisualMappingFunction;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.mappings.ContinuousMapping;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public final class DynVizmapTask<T> extends AbstractTask
{
	private DynNetworkView<T> view;
	private VisualStyle visualStyle;
	private final VisualMappingFunctionFactory continousFactory;
	private final VisualMappingFunctionFactory discreteFactory;
	private final VisualMappingFunctionFactory passthroughFactory;
	private final BlockingQueue queue;
	
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
		Collection<VisualMappingFunction<?, ?>> mappings = visualStyle.getAllVisualMappingFunctions();
		Collection<VisualMappingFunction<?, ?>> newMappings = new ArrayList<VisualMappingFunction<?, ?>>();
		for (VisualMappingFunction<?, ?> visualMapping : mappings)
		{
			if (visualMapping instanceof ContinuousMapping<?,?>)
				newMappings.add(
						continousFactory.createVisualMappingFunction(
						visualMapping.getMappingColumnName(), visualMapping.getMappingColumnType(), visualMapping.getVisualProperty()));
			else if (visualMapping instanceof DiscreteMapping<?,?>)
				newMappings.add(
						discreteFactory.createVisualMappingFunction(
						visualMapping.getMappingColumnName(), visualMapping.getMappingColumnType(), visualMapping.getVisualProperty()));
			else if (visualMapping instanceof PassthroughMapping<?,?>)
				newMappings.add(
						passthroughFactory.createVisualMappingFunction(
						visualMapping.getMappingColumnName(), visualMapping.getMappingColumnType(), visualMapping.getVisualProperty()));
		}
		
		for (VisualMappingFunction<?, ?> visualMapping : mappings)
			visualStyle.removeVisualMappingFunction(visualMapping.getVisualProperty());
		
		for (VisualMappingFunction<?, ?> visualMapping : newMappings)
			visualStyle.addVisualMappingFunction(visualMapping);
		
		visualStyle.apply(view.getNetworkView());
		
		queue.unlock();
	}
	
	
}
