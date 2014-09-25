package com.godaddy.sonar.ruby.metricfu;

import com.godaddy.sonar.ruby.constants.RubyConstants;

public class CaneLineStyleViolation extends CaneViolation {
	private int line;
	private String description;
	private String key = RubyConstants.CANE_KEY_UNKNOWN_VIOLATION;

	public CaneLineStyleViolation(String file, int line, String description) {
		super(file);
		setLine(line);
		setDescription(description);
	}

	public CaneLineStyleViolation() {
	}

	public String getKey() {
		return key;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;

		if (description.contains(RubyConstants.CANE_DESCRIPTION_TABS)) {
			key = RubyConstants.CANE_KEY_TABS_VIOLATION;
		} else if (description
				.contains(RubyConstants.CANE_DESCRIPTION_WHITESPACE)) {
			key = RubyConstants.CANE_KEY_WHITESPACE_VIOLATION;
		} else if (description
				.contains(RubyConstants.CANE_DESCRIPTION_CHARACTERS)) {
			key = RubyConstants.CANE_KEY_LENGTH_VIOLATION;
		}
	}

	@Override
	public String toString() {
		return "file: " + getFile() + " line: " + line + " description: "
				+ description;
	}
}
