package org.cytoscape.dyn.internal.util;

public enum DynIntervalType {
    NONE("none"), STRING("string"), BOOLEAN("boolean"), REAL("real"), INTEGER("integer");

    private String name;

    private DynIntervalType(String s) {
        name = s;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
