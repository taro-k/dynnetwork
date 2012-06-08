package org.cytoscape.dyn.internal.read.xgmml;

import java.util.Stack;

import org.cytoscape.dyn.internal.model.DynNetworkFactory;
import org.cytoscape.dyn.internal.read.xgmml.handler.DynHandlerXGMMLFactory;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public final class XGMMLDynParser<T> extends DefaultHandler
{
	private final DynHandlerXGMMLFactory<T> handler;
	
	private ParseDynState parseState;
	private Stack<ParseDynState> startStack;

	public XGMMLDynParser(DynNetworkFactory<T> sink)
	{
		this.handler = new DynHandlerXGMMLFactory<T>(sink);
	}

	@Override
	public void startDocument() throws SAXException
	{
		startStack = new Stack<ParseDynState>();
		parseState = ParseDynState.NONE;
		super.startDocument();
	}

	@Override
	public void startElement(String namespace, String localName, String qName, Attributes atts) throws SAXException
	{
		final ParseDynState nextState = handler.handleStartState(parseState, localName, atts);
		startStack.push(parseState);
		parseState = nextState;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		handler.handleEndState(parseState, localName, null);
		parseState = startStack.pop();
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException
	{
		String err = "Fatal parsing error on line " + e.getLineNumber() + " -- '" + e.getMessage() + "'";
		throw new SAXException(err);
	}

}
