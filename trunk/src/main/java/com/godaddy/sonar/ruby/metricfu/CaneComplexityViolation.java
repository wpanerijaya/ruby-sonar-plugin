package com.godaddy.sonar.ruby.metricfu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sonar.api.scan.filesystem.PathResolver;

import com.godaddy.sonar.ruby.constants.RubyConstants;

public class CaneComplexityViolation extends CaneViolation {
	private String method;
	private int complexity;

	public CaneComplexityViolation(String file, String method, int complexity) {
		super(file);
		this.method = method;
		this.complexity = complexity;
	}

	public CaneComplexityViolation() {
	}

	public String getKey() {
		return RubyConstants.CANE_KEY_COMPLEXITY_VIOLATION;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getComplexity() {
		return complexity;
	}

	public void setComplexity(int complexity) {
		this.complexity = complexity;
	}

	@Override
	public String toString() {
		return "file: " + getFile() + " line: " + complexity + " method: "
				+ method;
	}

	public List<CaneViolation> getViolations(String fileNameFromModule,
			File filepath, List<Map<String, Object>> caneViolationsResult) {
		PathResolver pathResolver = new PathResolver();
		List<CaneViolation> violations = new ArrayList<CaneViolation>();
		for (Map<String, Object> caneViolationsLineResultRow : caneViolationsResult) {
			String file = (String) caneViolationsLineResultRow.get(":file");
			String fileNameFromResults = pathResolver.relativeFile(filepath,
					file).getAbsolutePath();
			if (file.length() > 0
					&& fileNameFromModule.contains(fileNameFromResults)) {
				CaneComplexityViolation violation = new CaneComplexityViolation(
						file,
						(String) caneViolationsLineResultRow.get(":method"),
						Integer.parseInt((String) caneViolationsLineResultRow
								.get(":complexity")));
				violations.add(violation);
			}
		}
		return violations;
	}
}
