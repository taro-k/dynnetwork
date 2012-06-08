package org.cytoscape.dyn.internal.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <code> ObjectTypeMap </code> used to convert string types into the 
 * corresponding classes.
 * 
 * @author sabina
 *
 */
public final class ObjectTypeMap
{

    private Map<String, ObjectType> typeMap;

    public ObjectTypeMap()
    {
        typeMap = new HashMap<String, ObjectType>();

        for (ObjectType type : ObjectType.values())
            typeMap.put(type.getName(), type);
    }

    public ObjectType getType(String name)
    {
        final ObjectType type = typeMap.get(name);
        
        if (type != null)
            return type;
        else
            return ObjectType.NONE;
    }

    public Object getTypedValue(ObjectType type, String value)
    {

        switch (type) {
            case BOOLEAN:
                if (value != null) return fromXGMMLBoolean(""+value);
                break;
            case REAL:
                if (value != null) return new Double(value);
                break;
            case INTEGER:
                if (value != null) return new Integer(value);
                break;
            case STRING:
                if (value != null) {
                    // Make sure we convert our newlines and tabs back
                    String sAttr = value.replace("\\t", "\t");
                    sAttr = sAttr.replace("\\n", "\n");
                    return sAttr;
                }
                break;
            case LIST:
                return new ArrayList<Object>();
        }

        return null;
    }
    
    public static boolean fromXGMMLBoolean(String s)
    {
    	// should be only "1", but let's be nice and also accept "true"
    	// http://www.cs.rpi.edu/research/groups/pb/punin/public_html/XGMML/draft-xgmml-20001006.html#BT
    	return s != null && s.matches("(?i)1|true");
    }

    public static String toXGMMLBoolean(boolean value)
    {
    	return value ? "1" : "0";
    }
}
