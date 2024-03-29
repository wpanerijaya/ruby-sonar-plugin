package com.godaddy.sonar.ruby.rubocop;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.rules.XMLRuleParser;

import com.godaddy.sonar.ruby.constants.RubyConstants;

public class RubocopRulesRepository extends RuleRepository {
	public RubocopRulesRepository() {
		super(RubyConstants.KEY_REPOSITORY_RUBOCOP, RubyConstants.LANGUAGE_KEY);
		setName(RubyConstants.NAME_REPOSITORY_RUBOCOP);
	}

	@Override
	public List<Rule> createRules() {
		XMLRuleParser parser = new XMLRuleParser();
		InputStream input = RubocopRulesRepository.class
				.getResourceAsStream(RubyConstants.RUBOCOP_RULES_REPOSITORY);
		try {
			return parser.parse(input);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}
}
