package com.godaddy.sonar.ruby.metricfu;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.rules.XMLRuleParser;

import com.godaddy.sonar.ruby.constants.RubyConstants;

public class CaneRulesRepository extends RuleRepository {
	public CaneRulesRepository() {
		super(RubyConstants.KEY_REPOSITORY_CANE, RubyConstants.LANGUAGE_KEY);
		setName(RubyConstants.NAME_REPOSITORY_CANE);
	}

	@Override
	public List<Rule> createRules() {
		XMLRuleParser parser = new XMLRuleParser();
		InputStream input = CaneRulesRepository.class
				.getResourceAsStream(RubyConstants.CANE_RULES_REPOSITORY);
		try {
			return parser.parse(input);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}
}
