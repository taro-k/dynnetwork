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

package org.cytoscape.dyn.internal.io.read.util;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.cytoscape.view.presentation.property.NodeShapeVisualProperty;

/**
 * <code> AttributeTypeMap </code> used to convert string attribute types into the 
 * corresponding classes.
 * 
 * @author Sabina Sara Pfister
 *
 */
public final class AttributeTypeMap
{
    private Map<String, AttributeType> typeMap;

    /**
     * <code> AttributeTypeMap </code> constructor.
     */
    public AttributeTypeMap()
    {
        typeMap = new HashMap<String, AttributeType>();

        for (AttributeType type : AttributeType.values())
            typeMap.put(type.getName(), type);
    }

    /**
     * Get type.
     * @param name
     * @return type.
     */
    public AttributeType getType(String name)
    {
        final AttributeType type = typeMap.get(name);
        
        if (type != null)
            return type;
        else
            return AttributeType.NONE;
    }

    /**
     * Get object associated with value.
     * @param type
     * @param value
     * @return object
     */
    public Object getTypedValue(AttributeType type, String value)
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
                    String sAttr = value.replace("\\t", "\t");
                    sAttr = sAttr.replace("\\n", "\n");
                    return sAttr;
                }
                break;
            case LIST:
            	if (value != null) return new ArrayList<Object>();
            case PAINT:
            	if (value != null) return decodeHEXColor(value);
            case RECTANGLE:
            	return NodeShapeVisualProperty.RECTANGLE;
            case RECT:
            	return NodeShapeVisualProperty.RECTANGLE;
            case BOX:
            	return NodeShapeVisualProperty.RECTANGLE;
            case ROUND_RECTANGLE:
            	return NodeShapeVisualProperty.ROUND_RECTANGLE;
            case ROUND_RECT:
            	return NodeShapeVisualProperty.ROUND_RECTANGLE;
            case TRIANGLE:
            	return NodeShapeVisualProperty.TRIANGLE;
            case PARALLELOGRAM:
            	return NodeShapeVisualProperty.PARALLELOGRAM;
            case RHOMBUS:
            	return NodeShapeVisualProperty.PARALLELOGRAM;
            case DIAMOND:
            	return NodeShapeVisualProperty.DIAMOND;
            case ELLIPSE:
            	return NodeShapeVisualProperty.ELLIPSE;
            case VER_ELLIPSE:
            	return NodeShapeVisualProperty.ELLIPSE;
            case HOR_ELLIPSE:
            	return NodeShapeVisualProperty.ELLIPSE;
            case CIRCLE:
            	return NodeShapeVisualProperty.ELLIPSE;
            case HEXAGON:
            	return NodeShapeVisualProperty.HEXAGON;
            case OCTAGON:
            	return NodeShapeVisualProperty.OCTAGON;
        }

        return null;
    }
    
    private static boolean fromXGMMLBoolean(String s)
    {
    	return s != null && s.matches("(?i)1|true");
    }

//    private static String toXGMMLBoolean(boolean value)
//    {
//    	return value ? "1" : "0";
//    }
    
    private static Paint decodeHEXColor(String nm) throws NumberFormatException 
    {
    	Integer intval = Integer.decode(nm);
    	int i = intval.intValue();
    	return (Paint) new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
    }
}
