package com.godaddy.sonar.ruby.core;

import java.util.HashSet;
import java.util.Set;

import org.sonar.squid.recognizer.Detector;
import org.sonar.squid.recognizer.EndWithDetector;
import org.sonar.squid.recognizer.KeywordsDetector;
import org.sonar.squid.recognizer.LanguageFootprint;

import com.godaddy.sonar.ruby.constants.RubyConstants;

public class RubyFootPrint implements LanguageFootprint {
	private final Set<Detector> detectors = new HashSet<Detector>();

	public RubyFootPrint() {
		detectors.add(new EndWithDetector(RubyConstants.END_WITH_DETECTOR, ')',
				'"', '\''));
		detectors.add(new KeywordsDetector(RubyConstants.KEYWORDS_DETECTOR,
				RubyConstants.RUBY_KEYWORDS_ARRAY));
	}

	public Set<Detector> getDetectors() {
		return detectors;
	}
}