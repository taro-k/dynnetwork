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
package org.cytoscape.dyn.internal.graphMetrics;

import java.net.URL;

import javax.help.HelpBroker;
import javax.help.HelpSet;


/**
 * 
 * @author Jimmy
 *
 */
public class DynamicNetworkHelp {
	
	private HelpBroker helpBroker;
	private HelpSet newHelpSet;
	public DynamicNetworkHelp(){
		final String HELP_SET_NAME = "/help/jhelpset";
		final ClassLoader classLoader = DynamicNetworkHelp.class.getClassLoader();
		URL helpSetURL;
		try {
			helpSetURL = HelpSet.findHelpSet(classLoader, HELP_SET_NAME);
			newHelpSet = new HelpSet(classLoader, helpSetURL);
			helpBroker = newHelpSet.createHelpBroker();
		} catch (final Exception e) {
			System.out.println("Sample24: Could not find help set: \"" + HELP_SET_NAME + ".");
		}
	}
	
	public void displayHelp(){
		try{
			helpBroker.setDisplayed(true);
		}catch(Exception e){
			System.out.println("Something didn't work as I intended it to!");
		}
	}
}
