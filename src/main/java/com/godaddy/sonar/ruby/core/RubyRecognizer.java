package com.godaddy.sonar.ruby.core;

import org.sonar.squid.recognizer.CodeRecognizer;

import com.godaddy.sonar.ruby.constants.RubyConstants;

public class RubyRecognizer extends CodeRecognizer {

	public RubyRecognizer() {
		super(RubyConstants.MAX_RECOGNIZE_PERCENT, new RubyFootPrint());
	}
}