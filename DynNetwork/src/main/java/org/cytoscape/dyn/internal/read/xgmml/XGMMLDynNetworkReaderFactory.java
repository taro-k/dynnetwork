package org.cytoscape.dyn.internal.read.xgmml;

import java.io.InputStream;

import org.cytoscape.dyn.internal.read.AbstractDynNetworkReaderFactory;
import org.cytoscape.io.CyFileFilter;
import org.cytoscape.work.TaskIterator;
import org.xml.sax.helpers.DefaultHandler;

public final class XGMMLDynNetworkReaderFactory extends AbstractDynNetworkReaderFactory
{
	private final DefaultHandler parser;

	public XGMMLDynNetworkReaderFactory(
			final CyFileFilter filter,
			final DefaultHandler parser)
	{
		super(filter);
		this.parser = parser;
	}

	@Override
	public TaskIterator createTaskIterator(InputStream inputStream, String inputName)
	{
		return new TaskIterator(new XGMMLDynNetworkReader(inputStream, parser));
	}
}
