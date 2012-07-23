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

package org.cytoscape.dyn.internal.view.vizmap.mapping.interpolator;

import java.awt.Color;



/**
 * The class provides a linear interpolation between color values. The
 * (red,green,blue,alpha) values of the returned color are linearly
 * interpolated from the associated values of the lower and upper colors,
 * according the the fractional distance frac from the lower value.
 *
 * If either object argument is not a Color, null is returned.
 */
public class LinearNumberToColorInterpolator extends LinearNumberInterpolator<Color> {

    /**
     *  DOCUMENT ME!
     *
     * @param frac DOCUMENT ME!
     * @param lowerRange DOCUMENT ME!
     * @param upperRange DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
	@Override
	public Color getRangeValue(double frac, Color lowerRange,
        Color upperRange) {

        double red = lowerRange.getRed() +
            (frac * (upperRange.getRed() - lowerRange.getRed()));
        double green = lowerRange.getGreen() +
            (frac * (upperRange.getGreen() - lowerRange.getGreen()));
        double blue = lowerRange.getBlue() +
            (frac * (upperRange.getBlue() - lowerRange.getBlue()));
        double alpha = lowerRange.getAlpha() +
            (frac * (upperRange.getAlpha() - lowerRange.getAlpha()));

        return new Color((int) Math.round(red), (int) Math.round(green),
            (int) Math.round(blue), (int) Math.round(alpha));
    }
}
