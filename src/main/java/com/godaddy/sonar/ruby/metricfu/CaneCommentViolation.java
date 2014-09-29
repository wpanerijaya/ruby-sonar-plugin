package com.godaddy.sonar.ruby.metricfu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sonar.api.scan.filesystem.PathResolver;

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

	@Override
	public List<CaneViolation> getViolations(String fileNameFromModule,
			File filepath, List<Map<String, Object>> caneViolationResult) {
		PathResolver pathResolver = new PathResolver();
		List<CaneViolation> violations = new ArrayList<CaneViolation>();
		for (Map<String, Object> caneViolationsLineResultRow : caneViolationResult) {
			String parts[] = ((String) caneViolationsLineResultRow.get(":line"))
					.split(":");
			String file = parts[0];
			String fileNameFromResults = pathResolver.relativeFile(filepath,
					file).getAbsolutePath();
			if (parts[0].length() > 0
					&& fileNameFromModule.contains(fileNameFromResults)) {
				CaneCommentViolation violation = new CaneCommentViolation(
						parts[0], Integer.parseInt(parts[1]),
						(String) caneViolationsLineResultRow.get(":class_name"));
				violations.add(violation);
			}
		}
		return violations;
	}
}
