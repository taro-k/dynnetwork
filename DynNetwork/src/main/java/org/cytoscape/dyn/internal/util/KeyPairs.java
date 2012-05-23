package org.cytoscape.dyn.internal.util;

/**
 * Class to implement hash map with two keys
 * @author sabina
 *
 */
public class KeyPairs {
	
	private String column;
	private Long row;
	
	public KeyPairs(String column, Long row) {
		this.column = column;
		this.row = row;
	}
	
	public String getColumn() {
		return column;
	}

	public Long getRow() {
		return row;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result + ((row == null) ? 0 : row.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		KeyPairs o = (KeyPairs) obj;
		if (this.row==o.row && this.column.equals(o.column))
			return true;
		return true;
	}
	
}