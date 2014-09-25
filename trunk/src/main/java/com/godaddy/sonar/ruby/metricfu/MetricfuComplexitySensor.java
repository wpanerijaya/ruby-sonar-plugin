package com.godaddy.sonar.ruby.metricfu;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.RangeDistributionBuilder;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.FileType;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

import com.godaddy.sonar.ruby.core.Ruby;

public class MetricfuComplexitySensor implements Sensor {
	private static final Logger LOG = LoggerFactory
			.getLogger(MetricfuComplexitySensor.class);

	private MetricfuYamlParser metricfuYamlParser;
	private ModuleFileSystem moduleFileSystem;
	private static final Number[] FILES_DISTRIB_BOTTOM_LIMITS = { 0, 5, 10, 20,
			30, 60, 90 };
	private static final Number[] FUNCTIONS_DISTRIB_BOTTOM_LIMITS = { 1, 2, 4,
			6, 8, 10, 12, 20, 30 };

	public MetricfuComplexitySensor(ModuleFileSystem moduleFileSystem,
			MetricfuYamlParser metricfuYamlParser) {
		this.moduleFileSystem = moduleFileSystem;
		this.metricfuYamlParser = metricfuYamlParser;
	}

	public boolean shouldExecuteOnProject(Project project) {
		// WP
		// return Ruby.KEY.equals(project.getLanguageKey());
		return !moduleFileSystem.files(
				FileQuery.on(FileType.values()).onLanguage(Ruby.KEY)).isEmpty();
	}

	public void analyse(Project project, SensorContext context) {
		List<File> sourceDirs = moduleFileSystem.sourceDirs();
		// WP
		// List<File> rubyFilesInProject =
		// moduleFileSystem.files(FileQuery.onSource().onLanguage(project.getLanguageKey()));
		List<File> rubyFilesInProject = moduleFileSystem.files(FileQuery
				.onSource().onLanguage(Ruby.KEY));

		for (File file : rubyFilesInProject) {
			LOG.debug("analyzing functions for classes in the file: "
					+ file.getName());
			try {
				analyzeFile(file, sourceDirs, context);
			} catch (IOException e) {
				LOG.error("Can not analyze the file " + file.getAbsolutePath()
						+ " for complexity");
			}
		}
	}

	private void analyzeFile(File file, List<File> sourceDirs,
			SensorContext sensorContext) throws IOException {
		// WP
		Resource<?> resource = org.sonar.api.resources.File.fromIOFile(file,
				sourceDirs);
		// RubyFile resource = new RubyFile(file, sourceDirs);
		List<SaikuroComplexity> functions = metricfuYamlParser
				.parseSaikuro(file.getAbsolutePath());

		// if function list is empty, then return, do not compute any complexity
		// on that file
		if (functions.isEmpty()) {
			return;
		}

		// COMPLEXITY
		int fileComplexity = 0;
		for (SaikuroComplexity function : functions) {
			fileComplexity += function.getComplexity();
		}
		sensorContext.saveMeasure(resource, CoreMetrics.COMPLEXITY,
				Double.valueOf(fileComplexity));

		// FILE_COMPLEXITY_DISTRIBUTION
		RangeDistributionBuilder fileDistribution = new RangeDistributionBuilder(
				CoreMetrics.FILE_COMPLEXITY_DISTRIBUTION,
				FILES_DISTRIB_BOTTOM_LIMITS);
		fileDistribution.add(Double.valueOf(fileComplexity));
		sensorContext.saveMeasure(resource, fileDistribution.build()
				.setPersistenceMode(PersistenceMode.MEMORY));

		// FUNCTION_COMPLEXITY
		sensorContext.saveMeasure(resource, CoreMetrics.FUNCTION_COMPLEXITY,
				Double.valueOf(fileComplexity) / functions.size());

		// FUNCTION_COMPLEXITY_DISTRIBUTION
		RangeDistributionBuilder functionDistribution = new RangeDistributionBuilder(
				CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION,
				FUNCTIONS_DISTRIB_BOTTOM_LIMITS);
		for (SaikuroComplexity function : functions) {
			functionDistribution.add(Double.valueOf(function.getComplexity()));
		}
		sensorContext.saveMeasure(resource, functionDistribution.build()
				.setPersistenceMode(PersistenceMode.MEMORY));
	}
}
