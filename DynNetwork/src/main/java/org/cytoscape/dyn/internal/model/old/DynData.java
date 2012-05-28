package org.cytoscape.dyn.internal.model.old;

import java.util.HashMap;
import java.util.Map;

import org.cytoscape.dyn.internal.model.DynInterval;
import org.cytoscape.dyn.internal.model.attributes.DynAttribute;
import org.cytoscape.dyn.internal.util.DynIntervalTypeMap;
import org.cytoscape.dyn.internal.util.KeyPairs;

//TODO: find a better way to store the information, maybe a big global tree??

/**
 * this Class is used to store the dynamical data, it's a temporary solution before implmenting a IntervalTree
 */
public final class DynData<T> {
	
	// Provides a sparse representation of the interval data, i.e. we can iterate only on the existing data points
	// A tree structure would be even more appropriate, since now we have to check all intervals if they are in a particular time range
	private final Map<KeyPairs,DynAttribute<T>> dynTable;
	
	private KeyPairs key;
	
	private DynIntervalTypeMap typeMap;

	public DynData()
	{
		typeMap = new DynIntervalTypeMap();
		dynTable = new HashMap<KeyPairs,DynAttribute<T>>();
	}

	@SuppressWarnings("unchecked")
	public void add(DynInterval<T> interval, String classType, long row, String column)
	{
		key = new KeyPairs(column, row);
		if (!dynTable.containsKey(key))
			dynTable.put(key, (DynAttribute<T>) typeMap.getTypedValue(typeMap.getType(classType)));
		dynTable.get(key).addInterval(interval);
		key = new KeyPairs(column, row);
	}
	
	public void add(DynInterval<T> interval, DynAttribute<T> attribute, long row, String column)
	{
		key = new KeyPairs(column, row);
		if (!dynTable.containsKey(key))
			dynTable.put(key, attribute);
		dynTable.get(key).addInterval(interval);
		key = new KeyPairs(column, row);
	}

	public boolean isInRange(DynInterval<T> interval, long row, String column)
	{
		key = new KeyPairs(column, row);
		if(dynTable.containsKey(key))
		{
			return dynTable.get(key).getIsInRange(interval);
		}
		return false;
	}
	
	public T getValue(DynInterval<T> interval, long row, String column)
	{
		key = new KeyPairs(column, row);
		if(dynTable.containsKey(key))
			return dynTable.get(key).getFirst(interval);
		return null;
	}
	
	public DynAttribute<T> getDynAttribute(long row, String column)
	{
		key = new KeyPairs(column, row);
		if(dynTable.containsKey(key))
			return dynTable.get(key);
		return null;
	}
	
    public Map<KeyPairs, DynAttribute<T>> getDynTable() {
        return dynTable;
}


}
