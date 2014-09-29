package com.godaddy.sonar.ruby.metricfu;

import java.io.File;
import java.util.List;
import java.util.Map;

public abstract class CaneViolation {
	private String file;

	public CaneViolation(String file) {
		this.file = file;
	}

	public CaneViolation() {
	}

	public abstract String getKey();

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "file: " + file;
	}

	public abstract List<CaneViolation> getViolations(
			String fileNameFromModule, File filepath,
			List<Map<String, Object>> caneViolationResult);
}
