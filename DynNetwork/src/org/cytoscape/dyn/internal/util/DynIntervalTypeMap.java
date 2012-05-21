package org.cytoscape.dyn.internal.util;

import java.util.HashMap;
import java.util.Map;

import org.cytoscape.dyn.internal.model.DynBooleanAttribute;
import org.cytoscape.dyn.internal.model.DynDoubleAttribute;
import org.cytoscape.dyn.internal.model.DynIntegerAttribute;
import org.cytoscape.dyn.internal.model.DynStringAttribute;

public final class DynIntervalTypeMap {

    private Map<String, DynIntervalType> typeMap;

    public DynIntervalTypeMap() {
        typeMap = new HashMap<String, DynIntervalType>();

        for (DynIntervalType type : DynIntervalType.values())
            typeMap.put(type.getName(), type);
    }

    public DynIntervalType getType(String name) {
        final DynIntervalType type = typeMap.get(name);
        
        if (type != null)
            return type;
        else
            return DynIntervalType.NONE;
    }

    public Object getTypedValue(DynIntervalType type) {

        switch (type) {
        	case NONE:
        		return new DynBooleanAttribute();
            case BOOLEAN:
                return new DynBooleanAttribute();
            case REAL:
                return new DynDoubleAttribute();
            case INTEGER:
            	return new DynIntegerAttribute();
            case STRING:
            	return new DynStringAttribute();
        }

        return null;
    }
    
}
