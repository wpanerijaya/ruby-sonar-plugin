package com.godaddy.sonar.ruby.cpd;

import java.io.File;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * The Handler for SAX Events.
 */
public class CPDDuplicationParserImpl implements CPDDuplicationParser {

	@Override
	public List<CPDDuplication> parse(File file) throws Exception {
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
		CPDDuplicationHandler handler = new CPDDuplicationHandler();
		parser.parse(file, handler);

		return handler.cpdList;
	}
}