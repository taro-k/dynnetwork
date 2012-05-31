package org.cytoscape.dyn.internal.read.xgmml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.cytoscape.dyn.internal.action.DynNetworkEventManagerImpl;
import org.cytoscape.dyn.internal.read.AbstractDynNetworkReader;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.VisualLexicon;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.work.TaskMonitor;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.ParserAdapter;

public final class XGMMLDynNetworkReader<T> extends AbstractDynNetworkReader {

	protected static final String CY_NAMESPACE = "http://www.cytoscape.org";
	protected final DynNetworkEventManagerImpl<T> manager;
	
	protected final DefaultHandler parser;
	
	protected final VisualLexicon visualLexicon;

	public XGMMLDynNetworkReader(final InputStream inputStream,
							  final CyNetworkViewFactory cyNetworkViewFactory,
							  final CyNetworkFactory cyNetworkFactory,
							  final DynNetworkEventManagerImpl<T> manager,
							  final DefaultHandler parser,
							  final RenderingEngineManager renderingEngineMgr) {
		super(inputStream, cyNetworkViewFactory, cyNetworkFactory);
		this.parser = parser;
		this.manager = manager;
		this.visualLexicon = renderingEngineMgr.getDefaultVisualLexicon();
	}

	@Override
	public void run(TaskMonitor tm) throws Exception {
		tm.setProgress(0.0);
		try {
			readXGMML(tm);
		} catch (Exception e) {
			throw new IOException("Could not parse XGMML file.", e);
		} finally
		{
			Set<CyNetwork> netSet = manager.getNetworks();
			this.cyNetworks = netSet.toArray(new CyNetwork[netSet.size()]);
		}
		tm.setProgress(1.0);
	}
	
	protected void readXGMML(TaskMonitor tm) throws SAXException, IOException
	{
		final SAXParserFactory spf = SAXParserFactory.newInstance();

		try {
			SAXParser sp = spf.newSAXParser();
			ParserAdapter pa = new ParserAdapter(sp.getParser());
			pa.setContentHandler(parser);
			pa.setErrorHandler(parser);
			pa.parse(new InputSource(inputStream));
		} catch (OutOfMemoryError oe) {
			System.gc();
			throw new RuntimeException("Out of memory error caught! The network being loaded is too large for the current memory allocation.  Use the -Xmx flag for the java virtual machine to increase the amount of memory available, e.g. java -Xmx1G cytoscape.jar -p apps ....");
		} catch (ParserConfigurationException e) {
			
		} catch (SAXParseException e) {

			throw e;
		} finally {
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
		}
	}
	
	
	//TODO: ????????? This part of the code should be somewhere else!!!
	
	@Override
	public CyNetworkView buildCyNetworkView(final CyNetwork network) {
		final CyNetworkView netView = cyNetworkViewFactory.createNetworkView(network);
		netView.updateView();
		return netView;
	}

}
