package com.godaddy.sonar.ruby.rubocop;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issuable.IssueBuilder;
import org.sonar.api.issue.Issue;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.FileType;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.scan.filesystem.PathResolver;

import com.common.FileUtil;
import com.godaddy.sonar.ruby.constants.RubyConstants;

/**
 * @author Widianto Panerijaya
 */
public class RubocopSensor implements Sensor {
	private static final Logger LOG = LoggerFactory
			.getLogger(RubocopSensor.class);

	private RubocopJsonParser rubocopJsonParser;
	private ModuleFileSystem moduleFileSystem;
	private final ResourcePerspectives perspectives;
	private PathResolver pathResolver;

	/**
	 * Use of IoC to get Settings
	 */
	public RubocopSensor(ModuleFileSystem moduleFileSystem,
			RubocopJsonParser rubocopJsonParser,
			ResourcePerspectives perspectives, PathResolver pathResolver) {
		this.moduleFileSystem = moduleFileSystem;
		this.rubocopJsonParser = rubocopJsonParser;
		this.perspectives = perspectives;
		this.pathResolver = pathResolver;
	}

	public boolean shouldExecuteOnProject(Project project) {
		return !moduleFileSystem.files(
				FileQuery.on(FileType.values()).onLanguage(
						RubyConstants.LANGUAGE_KEY)).isEmpty();
	}

	public void analyse(Project project, SensorContext context) {
		LOG.debug("Enter analyze");
		if (moduleFileSystem.baseDir() == null) {
			return;
		}
		List<File> files = FileUtil.findDirectories(moduleFileSystem.baseDir(),
				"spec", true);
		for (File source : files) {
			LOG.debug("ruby project folder : " + source.getParent());
		}
		for (File source : files) {
			File jsonFile = pathResolver.relativeFile(source.getParentFile(),
					RubyConstants.RUBOCOP_REPORT_FILE);
			LOG.debug("jsonFile : " + jsonFile.getAbsolutePath());
			if (!jsonFile.exists()) {
				continue;
			}
			try {
				calculateMetrics(project, jsonFile, source.getParentFile(),
						context);
			} catch (IOException e) {
				LOG.error("unable to calculate Metrics", e);
			}
		}
		LOG.debug("Exit analyze");
	}

	private void calculateMetrics(Project project, File jsonFile, File source,
			final SensorContext context) throws IOException {
		LOG.debug("Enter calculateMetrics");
		List<RubocopOffense> offenses = rubocopJsonParser.parse(jsonFile);

		File sourceFile = null;
		try {
			for (RubocopOffense offense : offenses) {
				String fileName = offense.getFile();
				LOG.debug("fileName : " + fileName);
				sourceFile = pathResolver.relativeFile(source, fileName);
				LOG.debug("sourceFile : " + sourceFile);
				if (moduleFileSystem.files(
						FileQuery.onSource().onLanguage(
								RubyConstants.LANGUAGE_KEY)).contains(
						sourceFile)) {
					Resource<?> rubyFile = org.sonar.api.resources.File
							.fromIOFile(sourceFile, project);
					addIssue(rubyFile, offense.getLine(),
							RubyConstants.KEY_REPOSITORY_RUBOCOP,
							offense.getCopname(),
							RubocopOffense.toSeverity(offense.getSeverity()),
							offense.getMessage());
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
		} finally {
			LOG.debug("Exit calculateMetrics");
		}
	}

	public void addIssue(Resource<?> resource, Integer line, String repo,
			String key, String severity, String message) {
		LOG.debug("Enter addIsue");
		try {
			Issuable issuable = perspectives.as(Issuable.class, resource);
			IssueBuilder bld = issuable.newIssueBuilder()
					.ruleKey(RuleKey.of(repo, key)).message(message)
					.severity(severity);
			if (line != RubyConstants.NO_LINE_NUMBER) {
				bld = bld.line(line);
			}
			Issue issue = bld.build();
			if (!issuable.addIssue(issue)) {
				LOG.error("Failed to register issue.\nIssue Object : "
						+ issue.toString());
			}
		} catch (Exception e) {
			LOG.error("Error in create issue object", e);
		} finally {
			LOG.debug("Exit addIsue");
		}
	}

	public void addIssue(Resource<?> resource, String repo, String key,
			String severity, String message) {
		addIssue(resource, RubyConstants.NO_LINE_NUMBER, repo, key, severity,
				message);
	}
}
