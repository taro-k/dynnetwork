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

/**
 * <code> AttributeType </code> implements hash map to convert string attribute types into the 
 * corresponding classes.
 * 
 * @author Sabina Sara Pfister
 *
 */
public enum AttributeType
{
    NONE("none"), 
    STRING("string"), 
    BOOLEAN("boolean"), 
    REAL("real"), 
    INTEGER("integer"), 
    LIST("list"),
    PAINT("paint"),
    
    NODE_RECTANGLE("NODE_RECTANGLE"), 
    NODE_RECT("NODE_RECT"), 
    NODE_BOX("NODE_BOX"), 
    NODE_ROUND_RECTANGLE("NODE_ROUND_RECTANGLE"), 
    NODE_ROUND_RECT("NODE_ROUND_RECT"),
    NODE_TRIANGLE("NODE_TRIANGLE"), 
    NODE_PARALLELOGRAM("NODE_PARALLELOGRAM"), 
    NODE_RHOMBUS("NODE_RHOMBUS"), 
    NODE_DIAMOND("NODE_DIAMOND"), 
    NODE_ELLIPSE("NODE_ELLIPSE"), 
    NODE_VER_ELLIPSE("NODE_VER_ELLIPSE"), 
    NODE_HOR_ELLIPSE("NODE_HOR_ELLIPSE"), 
    NODE_CIRCLE("NODE_CIRCLE"), 
    NODE_HEXAGON("NODE_HEXAGON"), 
    NODE_OCTAGON("NODE_OCTAGON"),
    
	EDGE_DIAMOND("EDGE_DIAMOND"),
	EDGE_DELTA("EDGE_DELTA"),
	EDGE_ARROW("EDGE_ARROW"),
	EDGE_T("EDGE_T"),
	EDGE_CIRCLE("EDGE_CIRCLE"),
	EDGE_HALF_TOP("EDGE_HALF_TOP"),
	EDGE_HALF_BOTTOM("EDGE_HALF_BOTTOM");

    private String name;

    private AttributeType(String s)
    {
        name = s;
    }

    /**
     * Get name.
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * To string.
     */
    public String toString()
    {
        return name;
    }
}
