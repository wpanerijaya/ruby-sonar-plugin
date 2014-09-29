package com.godaddy.sonar.ruby.constants;

public class RubyConstants {
	public static final String KEY_REPOSITORY_CANE = "cane";
	public static final String NAME_REPOSITORY_CANE = "Cane";

	public static final String KEY_REPOSITORY_REEK = "reek";
	public static final String NAME_REPOSITORY_REEK = "Reek";

	public static final String KEY_REPOSITORY_ROODI = "roodi";
	public static final String NAME_REPOSITORY_ROODI = "Roodi";

	public static final String KEY_REPOSITORY_RUBOCOP = "rubocop";
	public static final String NAME_REPOSITORY_RUBOCOP = "Rubocop";

	public static final String CANE_KEY_COMMENT_VIOLATION = "CommentViolation";
	public static final String CANE_KEY_COMPLEXITY_VIOLATION = "ComplexityViolation";
	public static final String CANE_KEY_UNKNOWN_VIOLATION = "UnknownViolation";
	public static final String CANE_KEY_TABS_VIOLATION = "LineStyleTabsViolation";
	public static final String CANE_KEY_WHITESPACE_VIOLATION = "LineStyleWhitespaceViolation";
	public static final String CANE_KEY_LENGTH_VIOLATION = "LineStyleLengthViolation";
	public static final String CANE_DESCRIPTION_TABS = "tabs";
	public static final String CANE_DESCRIPTION_WHITESPACE = "whitespace";
	public static final String CANE_DESCRIPTION_CHARACTERS = "characters";

	public static final String RUBOCOP_REPORT_FILE = "tmp/rubocop.json";

	public static final String CPD_REPORT_FILE = "tmp/cpd.xml";

	public static final String METRIC_FU_REPORT_FILE = "tmp/metric_fu/report.yml";

	public static final Integer NO_LINE_NUMBER = -1;

	public static final String SONAR_WAY_PROFILE_NAME = "Sonar way";

	public static final String LANGUAGE_KEY = "ruby";
	public static final String LANGUAGE_NAME = "Ruby";

	public static final String[] RUBY_EXTENSIONS = new String[] { ".rb",
			".rake" };

	public static final double END_WITH_DETECTOR = 0.95;
	public static final double KEYWORDS_DETECTOR = 0.3;

	public static final double MAX_RECOGNIZE_PERCENT = 0.95;

	public static final String[] RUBY_KEYWORDS_ARRAY = new String[] { "alias",
			"and", "BEGIN", "begin", "break", "case", "class", "def",
			"defined?", "do", "else", "elsif", "END", "end", "ensure", "false",
			"for", "if", "in", "module", "next", "nil", "not", "or", "redo",
			"rescue", "retry", "return", "self", "super", "then", "true",
			"undef", "unless", "until", "when", "while", "yield" };

	public static final String[] RUBY_RESERVED_VARIABLES_ARRAY = new String[] {
			"__FILE__", "__LINE__" };

	public static final String RUBY_PROFILE = "/ruby/profiles/sonar-way-profile.xml";
	public static final String CANE_RULES_REPOSITORY = "/com/godaddy/sonar/ruby/metricfu/CaneRulesRepository.xml";
	public static final String REEK_RULES_REPOSITORY = "/com/godaddy/sonar/ruby/metricfu/ReekRulesRepository.xml";
	public static final String ROODI_RULES_REPOSITORY = "/com/godaddy/sonar/ruby/metricfu/RoodiRulesRepository.xml";
	public static final String RUBOCOP_RULES_REPOSITORY = "/com/godaddy/sonar/ruby/rubocop/RubocopRulesRepository.xml";

	public static final String TAG_CPD_DUPLICATION = "duplication";
	public static final String TAG_CPD_FILE = "file";
	public static final String TAG_CPD_LINE = "line";
	public static final String TAG_CPD_LINES = "lines";
	public static final String TAG_CPD_PATH = "path";

	public static final Number[] FILES_DISTRIB_BOTTOM_LIMITS = { 0, 5, 10, 20,
			30, 60, 90 };
	public static final Number[] FUNCTIONS_DISTRIB_BOTTOM_LIMITS = { 1, 2, 4,
			6, 8, 10, 12, 20, 30 };

	private RubyConstants() {

	}
}
