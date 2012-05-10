package org.cytoscape.dyn.internal.read.xgmml;

import java.util.Stack;

import org.cytoscape.dyn.internal.events.DynNetworkEventManagerImpl;
import org.cytoscape.dyn.internal.read.xgmml.handler.DynHandlerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public final class XGMMLDynParser<T> extends DefaultHandler {
	
	private ParseDynState parseState;
	private Stack<ParseDynState> stateStack;
	
	private final DynHandlerFactory<T> handler;
	
	public XGMMLDynParser(DynNetworkEventManagerImpl<T> manager) {
		this.handler = new DynHandlerFactory<T>(manager);
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		stateStack = new Stack<ParseDynState>();
		parseState = ParseDynState.NONE;
		super.startDocument();
	}


	@Override
	public void startElement(String namespace, String localName, String qName, Attributes atts) throws SAXException {
		final ParseDynState nextState = handler.handleStartState(parseState, localName, atts);
		stateStack.push(parseState);
		parseState = nextState;
	}


	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		handler.handleEndState(parseState, localName, null);
		parseState = stateStack.pop();
	}


	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		String err = "Fatal parsing error on line " + e.getLineNumber() + " -- '" + e.getMessage() + "'";
		throw new SAXException(err);
	}


	@Override
	public void error(SAXParseException e) {

	}



}
