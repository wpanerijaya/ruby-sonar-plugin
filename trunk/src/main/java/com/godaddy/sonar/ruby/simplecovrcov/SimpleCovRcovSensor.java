package com.godaddy.sonar.ruby.simplecovrcov;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoverageMeasuresBuilder;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.FileType;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

import com.godaddy.sonar.ruby.constants.RubyConstants;

public class SimpleCovRcovSensor implements Sensor {
	private static final Logger LOG = LoggerFactory
			.getLogger(SimpleCovRcovSensor.class);

	private SimpleCovRcovJsonParser simpleCovRcovJsonParser;
	private ModuleFileSystem moduleFileSystem;

	/**
	 * Use of IoC to get Settings
	 */
	public SimpleCovRcovSensor(ModuleFileSystem moduleFileSystem,
			SimpleCovRcovJsonParser simpleCovRcovJsonParser) {
		this.moduleFileSystem = moduleFileSystem;
		this.simpleCovRcovJsonParser = simpleCovRcovJsonParser;
	}

	public boolean shouldExecuteOnProject(Project project) {
		return !moduleFileSystem.files(
				FileQuery.on(FileType.values()).onLanguage(
						RubyConstants.LANGUAGE_KEY)).isEmpty();
	}

	public void analyse(Project project, SensorContext context) {
		File jsonFile = new File("coverage/.resultset.json");

		if (!jsonFile.exists()) {
			return;
		}

		List<File> sourceDirs = moduleFileSystem.sourceDirs();

		try {
			calculateMetrics(sourceDirs, jsonFile, context);
		} catch (IOException e) {
			LOG.error("unable to calculate Metrics", e);
		}
	}

	private void calculateMetrics(List<File> sourceDirs, File jsonFile,
			final SensorContext context) throws IOException {
		Map<String, CoverageMeasuresBuilder> jsonResults = simpleCovRcovJsonParser
				.parse(jsonFile);

		File sourceFile = null;
		for (Entry<String, CoverageMeasuresBuilder> entry : jsonResults
				.entrySet()) {
			try {
				String fileName = entry.getKey();
				sourceFile = new File(fileName);
				Resource<?> rubyFile = org.sonar.api.resources.File.fromIOFile(
						sourceFile, sourceDirs);

				CoverageMeasuresBuilder fileCoverage = entry.getValue();
				if (fileCoverage != null) {
					for (Measure measure : fileCoverage.createMeasures()) {
						context.saveMeasure(rubyFile, measure);
					}
				}
			} catch (Exception e) {
				if (sourceFile != null) {
					LOG.error(
							"Unable to save metrics for file: "
									+ sourceFile.getName(), e);
				} else {
					LOG.error("Unable to save metrics.", e);
				}
			}
		}
	}
}
