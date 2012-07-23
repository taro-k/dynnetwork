package org.cytoscape.dyn.internal.view.vizmap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.cytoscape.view.model.VisualLexicon;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.vizmap.VisualMappingManager;

public class DynValueRangeTracer 
{

	private final Map<VisualProperty<?>, Range> rangeMap;

	public DynValueRangeTracer(final VisualMappingManager vmm) 
	{

		final Set<VisualLexicon> lexSet = vmm.getAllVisualLexicon();
		rangeMap = new HashMap<VisualProperty<?>, Range>();

		for (VisualLexicon lexicon : lexSet) {
			for (VisualProperty<?> v : lexicon.getAllVisualProperties()) 
			{
				Range r = new Range(0d, 0d);
				rangeMap.put(v, r);
			}
		}
	}

	public Double getRange(VisualProperty<?> t) 
	{
		return rangeMap.get(t).getRange();
	}

	public Double getMin(VisualProperty<?> t) 
	{
		return rangeMap.get(t).getMin();
	}

	public Double getMax(VisualProperty<?> t) 
	{
		return rangeMap.get(t).getMax();
	}

	public void setMin(VisualProperty<?> t, Double min) 
	{
		rangeMap.get(t).setMin(min);
	}

	public void setMax(VisualProperty<?> t, Double max) 
	{
		rangeMap.get(t).setMax(max);
	}

	private final class Range 
	{
		private Double min;
		private Double max;

		public Range(Double min, Double max) 
		{
			this.min = min;
			this.max = max;
		}

		public void setMin(final Double min) 
		{
			this.min = min;
		}

		public void setMax(final Double max) 
		{
			this.max = max;
		}

		public Double getMin() 
		{
			return min;
		}

		public Double getMax() 
		{
			return max;
		}

		public Double getRange() 
		{
			return Math.abs(min - max);
		}
	}
}
