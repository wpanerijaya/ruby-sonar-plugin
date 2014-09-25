package com.godaddy.sonar.ruby.metricfu;

import com.godaddy.sonar.ruby.constants.RubyConstants;

public class CaneCommentViolation extends CaneViolation {
	private int line;
	private String className;

	public CaneCommentViolation(String file, int line, String className) {
		super(file);
		this.line = line;
		this.className = className;
	}

	public CaneCommentViolation() {
	}

	public String getKey() {
		return RubyConstants.CANE_KEY_COMMENT_VIOLATION;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return "file: " + getFile() + " line: " + line + " class: " + className;
	}
}
