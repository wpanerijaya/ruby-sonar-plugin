package com.godaddy.sonar.ruby.metricfu;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.rules.XMLRuleParser;

import com.godaddy.sonar.ruby.constants.RubyConstants;
import com.godaddy.sonar.ruby.core.Ruby;

public class RoodiRulesRepository extends RuleRepository {
	public RoodiRulesRepository() {
		super(RubyConstants.KEY_REPOSITORY_ROODI, Ruby.KEY);
		setName(RubyConstants.NAME_REPOSITORY_ROODI);
	}

	@Override
	public List<Rule> createRules() {
		XMLRuleParser parser = new XMLRuleParser();
		InputStream input = RoodiRulesRepository.class
				.getResourceAsStream("/com/godaddy/sonar/ruby/metricfu/RoodiRulesRepository.xml");
		try {
			return parser.parse(input);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}
}
