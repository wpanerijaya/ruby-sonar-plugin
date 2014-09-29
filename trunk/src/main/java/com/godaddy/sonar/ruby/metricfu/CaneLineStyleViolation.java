package com.godaddy.sonar.ruby.metricfu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sonar.api.scan.filesystem.PathResolver;

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
				CaneLineStyleViolation violation = new CaneLineStyleViolation(
						parts[0], Integer.parseInt(parts[1]),
						(String) caneViolationsLineResultRow
								.get(":description"));
				violations.add(violation);
			}
		}
		return violations;
	}
}
