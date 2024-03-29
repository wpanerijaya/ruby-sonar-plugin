package com.godaddy.sonar.ruby.core.profiles;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.utils.ValidationMessages;

import com.godaddy.sonar.ruby.constants.RubyConstants;

public final class SonarWayProfile extends ProfileDefinition {
	private final XMLProfileParser parser;

	public SonarWayProfile(XMLProfileParser parser) {
		this.parser = parser;
	}

	@Override
	public RulesProfile createProfile(ValidationMessages messages) {
		InputStream input = getClass().getResourceAsStream(
				RubyConstants.RUBY_PROFILE);
		InputStreamReader reader = new InputStreamReader(input);
		try {
			RulesProfile profile = parser.parse(reader, messages);
			profile.setName(RubyConstants.SONAR_WAY_PROFILE_NAME);
			profile.setDefaultProfile(true);
			return profile;
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}
}
