package org.cytoscape.dyn.internal.read.xgmml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cytoscape.io.BasicCyFileFilter;
import org.cytoscape.io.DataCategory;
import org.cytoscape.io.util.StreamUtil;

public final class XGMMLDynFileFilter extends BasicCyFileFilter {
	
	public static final Pattern XGMML_HEADER_PATTERN = Pattern
			.compile("<graph[^<>]+[\\'\"]http://www.cs.rpi.edu/XGMML[\\'\"][^<>]*>");
	
	public static final Pattern XGMML_VIEW_ATTRIBUTE_PATTERN = Pattern.compile("cy:view=[\\'\"](1|true)[\\'\"]");


	public XGMMLDynFileFilter(Set<String> extensions, Set<String> contentTypes,
			String description, DataCategory category, StreamUtil streamUtil) {
		super(extensions, contentTypes, description, category, streamUtil);
	}

	public XGMMLDynFileFilter(String[] extensions, String[] contentTypes,
			String description, DataCategory category, StreamUtil streamUtil) {
		super(extensions, contentTypes, description, category, streamUtil);
	}

	@Override
	public boolean accepts(InputStream stream, DataCategory category) {
		// Check data category
		if (category != this.category)
			return false;
		
		final String header = this.getHeader(stream, 20);
		Matcher matcher = XGMML_HEADER_PATTERN.matcher(header);
		
		if (matcher.find()) {
			// It looks like an XGMML graph tag
			final String graph = matcher.group(0);
			// But we still have to check if it is NOT the session-view type:
			matcher = XGMML_VIEW_ATTRIBUTE_PATTERN.matcher(graph);
			
			if (!matcher.find())
				return true;
		}
		
		return false;
	}

	@Override
	public boolean accepts(URI uri, DataCategory category) {
		try {
			return accepts(uri.toURL().openStream(), category);
		} catch (IOException e) {
//			logger.error("Error while opening stream: " + uri, e);
			return false;
		}
	}
}
