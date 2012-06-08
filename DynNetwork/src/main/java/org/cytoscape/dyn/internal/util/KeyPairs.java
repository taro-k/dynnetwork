package org.cytoscape.dyn.internal.util;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;

/**
 * <code> KeyPairs </code> implements hash map with two keys to store information
 * about {@link CyNode}s, connecting {@link CyEdge}s, and the respective attributes.
 * Each pair is identified by an id long value and by a String column.
 * 
 * @author sabina
 *
 */
public class KeyPairs 
{
	
	private String column;
	private Long row;
	
	public KeyPairs(String column, Long row) 
	{
		this.column = column;
		this.row = row;
	}
	
	public String getColumn() 
	{
		return column;
	}

	public Long getRow() 
	{
		return row;
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result + ((row == null) ? 0 : row.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		KeyPairs o = (KeyPairs) obj;
		if (this.row==o.row && this.column.equals(o.column))
			return true;
		return true;
	}
	
}