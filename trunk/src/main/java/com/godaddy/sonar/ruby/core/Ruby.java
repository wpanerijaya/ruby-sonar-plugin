package com.godaddy.sonar.ruby.core;

import org.sonar.api.resources.AbstractLanguage;

import com.godaddy.sonar.ruby.constants.RubyConstants;

public class Ruby extends AbstractLanguage {
	
	public static final Ruby INSTANCE = new Ruby();

	public Ruby() {
		super(RubyConstants.LANGUAGE_KEY, RubyConstants.LANGUAGE_NAME);
	}

	public String[] getFileSuffixes() {
		return RubyConstants.RUBY_EXTENSIONS;
	}
}
