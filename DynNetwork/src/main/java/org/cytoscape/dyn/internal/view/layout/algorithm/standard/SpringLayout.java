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
 * 
 * The code below was adapted from the JUNG Project.
 * 
 * *********************************************************************** 
 * Copyright (c) 2003, the JUNG Project and the Regents of the University 
 * of California
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see either
 * "license.txt" or
 * http://jung.sourceforge.net/license.txt for a description.
 * ***********************************************************************
 */

package org.cytoscape.dyn.internal.view.layout.algorithm.standard;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cytoscape.dyn.internal.model.DynNetworkSnapshot;
import org.cytoscape.dyn.internal.view.layout.algorithm.util.Distance;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;

/**
 * <code> SpringLayout </code> assigns X/Y locations to each node. 
 * When called, the it moves the visualization forward one step.
 * 
 * @author Danyel Fisher
 * @author Joshua O'Madadhain
 * @author Sabina Sara Pfister - adaptation for Cytoscape
 *
 * @param <T>
 */
public class SpringLayout<T> extends AbstractLayout<T>
{

    protected double stretch = 0.70;
    protected int repulsion_range_sq = 100 * 100;
    protected double force_multiplier = 1.0 / 3.0;
    
    protected Map<CyEdge, Integer> lengthFunction;
    protected Map<CyNode, SpringVertexData> springVertexData;

    /**
     * <code> SpringLayout </code> constructor.
     * @param g
     * @param size
     */
    public SpringLayout(DynNetworkSnapshot<T> g, Dimension size) 
    {
    	super(g, size);
        this.lengthFunction = new HashMap<CyEdge, Integer>();
        this.springVertexData = new HashMap<CyNode, SpringVertexData>();
    }

    /**
     * Sets the stretch parameter for this instance.
     * @param stretch
     */
    public void setStretch(double stretch) 
    {
        this.stretch = stretch;
    }

    /**
     * Sets the node repulsion range  for this instance.
     * @param range
     */
    public void setRepulsionRange(int range) 
    {
        this.repulsion_range_sq = range * range;
    }

    /**
     * Sets the force multiplier for this instance.
     * @param force
     */
    public void setForceMultiplier(double force) 
    {
        this.force_multiplier = force;
    }
    
	@Override
	public void setDistance(Distance<T> distance) 
	{
		// TODO Auto-generated method stub
	}

    @Override
    public void initialize() 
    {
    	super.updateLocations();
    	for (CyEdge edge : this.graph.getEdges())
    		this.lengthFunction.put(edge, 1);
    }
    
    @Override
    public boolean done() 
    {
        return false;
    }

    @Override
	public void reset() {}
    
	@Override
	public void print() {}

	@Override
    public void step() 
    {
    	try {
    		for(CyNode node : this.graph.getNodes()) 
    		{
    			SpringVertexData svd = springVertexData.get(node);
    			if (svd == null) {
    				continue;
    			}
    			svd.dx /= 4;
    			svd.dy /= 4;
    			svd.edgedx = svd.edgedy = 0;
    			svd.repulsiondx = svd.repulsiondy = 0;
    		}
    	} catch(ConcurrentModificationException cme) 
    	{
    		step();
    	}

    	relaxEdges();
    	calculateRepulsion();
    	moveNodes();
    }

    protected void relaxEdges() 
    {
    	try {
    		for(CyEdge edge : this.graph.getEdges()) 
    		{
    			List<CyNode> endpoints = this.graph.getNodes(edge);
    			CyNode v1 = endpoints.get(0);
    			CyNode v2 = endpoints.get(1);

    			Point2D p1 = transform(v1);
    			Point2D p2 = transform(v2);
    			if(p1 == null || p2 == null) continue;
    			double vx = p1.getX() - p2.getX();
    			double vy = p1.getY() - p2.getY();
    			double len = Math.sqrt(vx * vx + vy * vy);

    			double desiredLen = lengthFunction.get(edge);

    			len = (len == 0) ? .0001 : len;

    			double f = force_multiplier * (desiredLen - len) / len;

    			f = f * Math.pow(stretch, (this.graph.getDegree(v1) + this.graph.getDegree(v2) - 2));

    			double dx = f * vx;
    			double dy = f * vy;
    			SpringVertexData v1D, v2D;
    			v1D = springVertexData.get(v1);
    			v2D = springVertexData.get(v2);
    			
    			v1D.edgedx += dx;
    			v1D.edgedy += dy;
    			v2D.edgedx += -dx;
    			v2D.edgedy += -dy;
    		}
    	} catch(ConcurrentModificationException cme) {
    		relaxEdges();
    	}
    }

    protected void calculateRepulsion() 
    {
        try {
        for (CyNode v : this.graph.getNodes()) 
        {
            if (isLocked(v)) continue;

            SpringVertexData svd = springVertexData.get(v);
            if(svd == null) continue;
            double dx = 0, dy = 0;

            for (CyNode v2 : this.graph.getNodes()) 
            {
                if (v == v2) continue;
                Point2D p = transform(v);
                Point2D p2 = transform(v2);
                if(p == null || p2 == null) continue;
                double vx = p.getX() - p2.getX();
                double vy = p.getY() - p2.getY();
                double distanceSq = p.distanceSq(p2);
                if (distanceSq == 0) 
                {
                    dx += Math.random();
                    dy += Math.random();
                } 
                else if (distanceSq < repulsion_range_sq) 
                {
                    double factor = 1;
                    dx += factor * vx / distanceSq;
                    dy += factor * vy / distanceSq;
                }
            }
            double dlen = dx * dx + dy * dy;
            if (dlen > 0) {
                dlen = Math.sqrt(dlen) / 2;
                svd.repulsiondx += dx / dlen;
                svd.repulsiondy += dy / dlen;
            }
        }
        } catch(ConcurrentModificationException cme) {
            calculateRepulsion();
        }
    }

    protected void moveNodes()
    {
        synchronized (getSize()) 
        {
            try {
                for (CyNode v : this.graph.getNodes()) 
                {
                    if (isLocked(v)) continue;
                    SpringVertexData vd = springVertexData.get(v);
                    if(vd == null) continue;
                    Point2D xyd = transform(v);

                    vd.dx += vd.repulsiondx + vd.edgedx;
                    vd.dy += vd.repulsiondy + vd.edgedy;

                    xyd.setLocation(xyd.getX()+Math.max(-5, Math.min(5, vd.dx)),
                    		xyd.getY()+Math.max(-5, Math.min(5, vd.dy)));

                    Dimension d = getSize();
                    int width = d.width;
                    int height = d.height;

                    if (xyd.getX() < 0) {
                        xyd.setLocation(0, xyd.getY());
                    } else if (xyd.getX() > width) {
                        xyd.setLocation(width, xyd.getY());
                    }
                    if (xyd.getY() < 0) {
                        xyd.setLocation(xyd.getX(), 0);
                    } else if (xyd.getY() > height) {
                        xyd.setLocation(xyd.getX(), height);
                    }

                }
            } catch(ConcurrentModificationException cme) {
                moveNodes();
            }
        }
    }

    protected static class SpringVertexData 
    {
        protected double edgedx;
        protected double edgedy;
        protected double repulsiondx;
        protected double repulsiondy;
        protected double dx;
        protected double dy;
    }

}