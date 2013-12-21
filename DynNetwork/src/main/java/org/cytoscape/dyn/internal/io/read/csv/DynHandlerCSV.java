/*
 * DynNetwork plugin for Cytoscape 3.0 (http://www.cytoscape.org/).
 * Copyright (C) 2013 Jimmy Mahesh Morzaria
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
package org.cytoscape.dyn.internal.io.read.csv;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import org.cytoscape.dyn.internal.layout.model.DynLayoutFactory;
import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.DynNetworkFactory;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewFactory;
import org.cytoscape.dyn.internal.vizmapper.model.DynVizMapFactory;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;

import au.com.bytecode.opencsv.CSVReader;

/**
 * @author Jimmy
 * 
 */
public class DynHandlerCSV<T> extends AbstractCSVSource<T>{

	
	private CSVReader csvReader;
	private File file;

	HashMap<Integer, String> nodeAttributeFieldMap = new HashMap<Integer, String>();
	HashMap<String, Integer> nodeFieldMap = new HashMap<String, Integer>();
	HashMap<Integer, String> edgeAttributeFieldMap = new HashMap<Integer, String>();
	HashMap<String, Integer> edgeFieldMap = new HashMap<String, Integer>();
	HashMap<String, Integer> nodeGraphicAttributesFieldMap = new HashMap<String, Integer>();
	HashMap<String, Integer> edgeGraphicAttributesFieldMap = new HashMap<String, Integer>();
	
	private String type;
	private String h;
	private String w;
	private String size;
	private String x;
	private String y;
	private String fill;
	private String width;
	private String outline;
	private String transparency;
	private String labelfill;
	private String labelsize;

	/**
	 * <code> DynHandlerCSV </code> constructor.
	 * 
	 * @param networkSink
	 * @param viewSink
	 * @param layoutSink
	 * @param csvReader
	 */
	public DynHandlerCSV(DynNetworkFactory<T> networkSink,
			DynNetworkViewFactory<T> viewSink, DynLayoutFactory<T> layoutSink,
			DynVizMapFactory<T> vizMapSink, CSVReader csvReader, File file) {
		super.networkSink = networkSink;
		super.viewSink = viewSink;
		super.layoutSink = layoutSink;
		super.vizMapSink = vizMapSink;
		this.csvReader = csvReader;
		this.file = file;
	}

	public void readNetwork() {
		
		boolean flagNode = true;
		boolean flagEdge = true;
		boolean nodeTableFlag = true;
		int edgeCount = 1;
				
		String nextLine[];
		
		DynNetwork<T> dynamicNetwork = networkSink.addedGraph("1", file.getName().substring(0, file.getName().length()-4), null, null, "1");
		try {
			nextLine = this.csvReader.readNext();
			while (nextLine != null) {
				
				if (nextLine[0].equalsIgnoreCase("NodeId")) {
					
					if (!flagNode) {
						JOptionPane.showMessageDialog(null,"File not in the desired format. Redundant field!");
						
					}
					
					for (int i = 0; i < nextLine.length; i++) {
						if (nextLine[i].equalsIgnoreCase("NodeId"))
							nodeFieldMap.put("NodeId", i);
						else if(nextLine[i].equalsIgnoreCase("StartTime"))
							nodeFieldMap.put("StartTime", i);
						else if(nextLine[i].equalsIgnoreCase("EndTime"))
							nodeFieldMap.put("EndTime", i);
						else if(nextLine[i].equalsIgnoreCase("Label"))
							nodeFieldMap.put("Label", i);
						else if(nextLine[i].equalsIgnoreCase("StartTime"))
							nodeFieldMap.put("StartTime", i);
						else if(nextLine[i].equalsIgnoreCase("type"))
							nodeGraphicAttributesFieldMap.put("type", i);
						else if(nextLine[i].equalsIgnoreCase("height"))
							nodeGraphicAttributesFieldMap.put("height", i);
						else if(nextLine[i].equalsIgnoreCase("width"))
							nodeGraphicAttributesFieldMap.put("width", i);
						else if(nextLine[i].equalsIgnoreCase("size"))
							nodeGraphicAttributesFieldMap.put("size", i);
						else if(nextLine[i].equalsIgnoreCase("borderwidth"))
							nodeGraphicAttributesFieldMap.put("borderwidth", i);
						else if(nextLine[i].equalsIgnoreCase("fill"))
							nodeGraphicAttributesFieldMap.put("fill", i);
						else if(nextLine[i].equalsIgnoreCase("transparency"))
							nodeGraphicAttributesFieldMap.put("transparency", i);
						else
							nodeAttributeFieldMap.put(i,checkNodeAttributeName(nextLine[i]));
					}
					
					flagNode = false;
					
				} 
				else if (nextLine[0].equalsIgnoreCase("FromId")) {
					nodeTableFlag = false;
					if (!flagEdge) {
						JOptionPane.showMessageDialog(null,"File not in the desired format. Redundant field!");
					}
					
					for (int i = 0; i < nextLine.length; i++) {
						if (nextLine[i].equalsIgnoreCase("FromId"))
							edgeFieldMap.put("FromId",i);
						else if(nextLine[i].equalsIgnoreCase("ToId"))
							edgeFieldMap.put("ToId",i);
						else if(nextLine[i].equalsIgnoreCase("StartTime"))
							edgeFieldMap.put("StartTime",i);
						else if(nextLine[i].equalsIgnoreCase("EndTime"))
							edgeFieldMap.put("EndTime",i);
						else if(nextLine[i].equalsIgnoreCase("width"))
							edgeGraphicAttributesFieldMap.put("width", i);
						else if(nextLine[i].equalsIgnoreCase("fill"))
							edgeGraphicAttributesFieldMap.put("fill", i);
						else if(nextLine[i].equalsIgnoreCase("transparency"))
							edgeGraphicAttributesFieldMap.put("transparency", i);
						else
							edgeAttributeFieldMap.put(i, checkEdgeAttributeName(nextLine[i]));
					}
					flagEdge = false;
				}
				else if(nodeTableFlag){
					CyNode node = addNode(dynamicNetwork, nextLine[nodeFieldMap.get("NodeId")], nextLine[nodeFieldMap.get("Label")], nextLine[nodeFieldMap.get("StartTime")], nextLine[nodeFieldMap.get("EndTime")]);
					Iterator<Entry<Integer, String>> i = nodeAttributeFieldMap.entrySet().iterator();
					while(i.hasNext()){
						Map.Entry<Integer, String> pairs = (Map.Entry<Integer, String>)i.next();
						addNodeAttribute(dynamicNetwork, node, pairs.getValue(), nextLine[pairs.getKey()], "string", nextLine[nodeFieldMap.get("StartTime")], nextLine[nodeFieldMap.get("EndTime")]);
					}
					
					if(nodeGraphicAttributesFieldMap.containsKey("type"))
						type = nextLine[nodeGraphicAttributesFieldMap.get("type")];
					if(nodeGraphicAttributesFieldMap.containsKey("width"))
						w = nextLine[nodeGraphicAttributesFieldMap.get("width")];
					if(nodeGraphicAttributesFieldMap.containsKey("height"))
						h = nextLine[nodeGraphicAttributesFieldMap.get("height")];
					if(nodeGraphicAttributesFieldMap.containsKey("fill"))
						fill = nextLine[nodeGraphicAttributesFieldMap.get("fill")];
					if(nodeGraphicAttributesFieldMap.containsKey("borderwidth"))
						width = nextLine[nodeGraphicAttributesFieldMap.get("borderwidth")];
					if(nodeGraphicAttributesFieldMap.containsKey("size"))
						size = nextLine[nodeGraphicAttributesFieldMap.get("size")];
					if(nodeGraphicAttributesFieldMap.containsKey("transparency"))
						transparency = nextLine[nodeGraphicAttributesFieldMap.get("transparency")];
					
					addNodeGraphics(dynamicNetwork, node, type, h, w, size, fill, labelfill, labelsize, width, outline, transparency, nextLine[nodeFieldMap.get("StartTime")], nextLine[nodeFieldMap.get("EndTime")]);
					
				}	
				else{
					CyEdge edge = addEdge(dynamicNetwork, String.valueOf(edgeCount),nextLine[edgeFieldMap.get("FromId")]+"_"+nextLine[edgeFieldMap.get("ToId")] , nextLine[edgeFieldMap.get("FromId")], nextLine[edgeFieldMap.get("ToId")], nextLine[edgeFieldMap.get("StartTime")], nextLine[edgeFieldMap.get("EndTime")]);
					edgeCount++;
					Iterator<Entry<Integer, String>> i = edgeAttributeFieldMap.entrySet().iterator();
					while(i.hasNext()){
						Map.Entry<Integer, String> pairs = (Map.Entry<Integer, String>)i.next();
						addEdgeAttribute(dynamicNetwork, edge, pairs.getValue(), nextLine[pairs.getKey()], "string", nextLine[edgeFieldMap.get("StartTime")], nextLine[edgeFieldMap.get("EndTime")]);
					}
					
					if(edgeGraphicAttributesFieldMap.containsKey("width"))
						w = nextLine[edgeGraphicAttributesFieldMap.get("width")];
					if(edgeGraphicAttributesFieldMap.containsKey("fill"))
						fill = nextLine[edgeGraphicAttributesFieldMap.get("fill")];
					if(edgeGraphicAttributesFieldMap.containsKey("transparency"))
						transparency = nextLine[edgeGraphicAttributesFieldMap.get("transparency")];
								
					addEdgeGraphics(dynamicNetwork, edge, width, fill,null, null, null, transparency, nextLine[edgeFieldMap.get("StartTime")], nextLine[edgeFieldMap.get("EndTime")]);
					
				}
					
				nextLine = csvReader.readNext();
				
			}
			finalize(dynamicNetwork);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Error Reading file!");
		}
	}

	private String checkGraphAttributeName(String name) {
		if (name.equals("name")) {
			System.out
					.println("\nCSV Parser Error: Reserved attribute name: the tag 'name' is reserved and cannot be uded.");
			throw new IllegalArgumentException(
					"Invalid attribute name: the tag 'name' is reserved and cannot be uded.");
		}
		if (name.equals("shared name")) {
			System.out
					.println("\nCSV Parser Error: Reserved attribute name: the tag 'shared name' is reserved and cannot be uded.");
			throw new IllegalArgumentException(
					"Invalid attribute name: the tag 'shared name' is reserved and cannot be uded.");
		}
		if (name.equals("__Annotations")) {
			System.out
					.println("\nCSV Parser Error: Reserved attribute name: the tag '__Annotations' is reserved and cannot be uded.");
			throw new IllegalArgumentException(
					"Invalid attribute name: the tag '__Annotations' is reserved and cannot be uded.");
		}

		return name;
	}

	private String checkNodeAttributeName(String name) {
		if (name.equals("name")) {
			System.out
					.println("\nCSV Parser Error: Reserved attribute name: the tag 'name' is reserved and cannot be uded.");
			throw new IllegalArgumentException(
					"Invalid attribute name: the tag 'name' is reserved and cannot be uded.");
		}
		if (name.equals("shared name")) {
			System.out
					.println("\nCSV Parser Error: Reserved attribute name: the tag 'shared name' is reserved and cannot be uded.");
			throw new IllegalArgumentException(
					"Invalid attribute name: the tag 'shared name' is reserved and cannot be uded.");
		}

		return name;
	}

	private String checkEdgeAttributeName(String name) {
		if (name.equals("name")) {
			System.out
					.println("\nCSV Parser Error: Reserved attribute name: the tag 'name' is reserved and cannot be uded.");
			throw new IllegalArgumentException(
					"Invalid attribute name: the tag 'name' is reserved and cannot be uded.");
		}
		if (name.equals("shared name")) {
			System.out
					.println("\nCSV Parser Error: Reserved attribute name: the tag 'shared name' is reserved and cannot be uded.");
			throw new IllegalArgumentException(
					"Invalid attribute name: the tag 'shared name' is reserved and cannot be uded.");
		}
		if (name.equals("interaction")) {
			System.out
					.println("\nCSV Parser Error: Reserved attribute name: the tag 'interaction' is reserved and cannot be uded.");
			throw new IllegalArgumentException(
					"Invalid attribute name: the tag 'interaction' is reserved and cannot be uded.");
		}
		if (name.equals("shared interaction")) {
			System.out
					.println("\nCSV Parser Error: Reserved attribute name: the tag 'shared interaction' is reserved and cannot be uded.");
			throw new IllegalArgumentException(
					"Invalid attribute name: the tag 'shared interaction' is reserved and cannot be uded.");
		}

		return name;
	}
	
}
