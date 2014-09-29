package com.godaddy.sonar.ruby.cpd;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.common.StringUtil;
import com.godaddy.sonar.ruby.constants.RubyConstants;

/**
 * The Handler for SAX Events.
 */
public class CPDDuplicationHandler extends DefaultHandler {

	List<CPDDuplication> cpdList = new ArrayList<CPDDuplication>();
	CPDDuplication cpd = null;
	CPDDuplication.Match match = null;
	String content = null;
	int lines = 0;

	@Override
	// Triggered when the start of tag is found.
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// Create a new CPDDuplication object when the start tag is found
		if (RubyConstants.TAG_CPD_DUPLICATION.equals(qName)) {
			cpd = new CPDDuplication();
			lines = StringUtil.safeInteger(attributes
					.getValue(RubyConstants.TAG_CPD_LINES));
		} else if (RubyConstants.TAG_CPD_FILE.equals(qName)) {
			String filename = StringUtil.safeString(attributes
					.getValue(RubyConstants.TAG_CPD_PATH));
			int line = StringUtil.safeInteger(attributes
					.getValue(RubyConstants.TAG_CPD_LINE));
			match = cpd.new Match(filename, line, lines);
			cpd.addMatch(match);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (RubyConstants.TAG_CPD_DUPLICATION.equals(qName)) {
			// Add the duplication to list once end tag is found
			cpdList.add(cpd);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		content = String.copyValueOf(ch, start, length).trim();
	}
}