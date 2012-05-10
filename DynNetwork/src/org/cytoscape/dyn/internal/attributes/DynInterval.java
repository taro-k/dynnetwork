package org.cytoscape.dyn.internal.attributes;

/**
 * This Class represent a a vlue and its interval in time
 * @author sabina
 *
 * @param <T>
 */
public final class DynInterval<T> implements Comparable<DynInterval<T>> {

	private T value;
	private double start;
	private double end;
	
	public static double minTime = Double.POSITIVE_INFINITY;
	public static double maxTime = Double.NEGATIVE_INFINITY;
	
	public DynInterval() {
		this.start = Double.NEGATIVE_INFINITY;
		this.end = Double.POSITIVE_INFINITY;
	}
	
	public DynInterval(T value) {
		this.value = value;
		this.start = Double.NEGATIVE_INFINITY;
		this.end = Double.POSITIVE_INFINITY;
	}
	
	public DynInterval(T value, double start) {
		this.value = value;
		this.start = start;
		this.end = Double.POSITIVE_INFINITY;
		minTime = Math.min(minTime, start);
	}
	
	public DynInterval(T value, double start, double end) {
		this.value = value;
		this.start = start;
		this.end = end;
		minTime = Math.min(minTime, start);
		maxTime = Math.max(maxTime, end);
	}
	
	public DynInterval(double start) {
		this.start = start;
		this.end = Double.POSITIVE_INFINITY;
		minTime = Math.min(minTime, start);
	}
	
	public DynInterval(double start, double end) {
		this.start = start;
		this.end = end;
		minTime = Math.min(minTime, start);
		maxTime = Math.max(maxTime, end);
	}

	@Override
	public int compareTo(DynInterval<T> interval) {
//		System.out.println("  compare  = " + start + " <= " + interval.end + " && " + end + " >= " + interval.start);
//		System.out.println("  compare  = " + (start <= interval.end && end >= interval.start));
		if (start <= interval.end && end >= interval.start)
			return 1;
		else
			return -1;
	}

	public T getValue() {
		return value;
	}

	public void setStart(double start) {
		this.start = start;
	}

	public void setEnd(double end) {
		this.end = end;
	}

	public double getStart() {
		return start;
	}

	public double getEnd() {
		return end;
	}
    
	
    
}
