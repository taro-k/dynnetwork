package org.cytoscape.dyn.internal.read.xgmml;

import java.io.InputStream;

import org.cytoscape.dyn.internal.events.DynNetworkEventManagerImpl;
import org.cytoscape.dyn.internal.read.AbstractDynNetworkReaderFactory;
import org.cytoscape.io.CyFileFilter;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.work.TaskIterator;
import org.xml.sax.helpers.DefaultHandler;

public final class XGMMLDynNetworkReaderFactory<T> extends AbstractDynNetworkReaderFactory {

	private final DefaultHandler parser;
	private final DynNetworkEventManagerImpl<T> manager;
	private final RenderingEngineManager renderingEngineMgr;

	public XGMMLDynNetworkReaderFactory(final CyFileFilter filter,
			                         final CyNetworkViewFactory cyNetworkViewFactory,
									 final CyNetworkFactory cyNetworkFactory,
									 final DynNetworkEventManagerImpl<T> manager,
									 final DefaultHandler parser,
									 final RenderingEngineManager renderingEngineMgr) {
		super(filter, cyNetworkViewFactory, cyNetworkFactory);
		this.parser = parser;
		this.manager = manager;
		this.renderingEngineMgr = renderingEngineMgr;
	}

	@Override
	public TaskIterator createTaskIterator(InputStream inputStream, String inputName) {
		return new TaskIterator(new XGMMLDynNetworkReader<T>(inputStream, cyNetworkViewFactory, cyNetworkFactory,
				manager, parser, renderingEngineMgr));
	}
	
}
