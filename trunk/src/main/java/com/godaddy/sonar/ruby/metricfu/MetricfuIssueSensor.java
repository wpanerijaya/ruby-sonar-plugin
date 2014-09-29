package com.godaddy.sonar.ruby.metricfu;

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
import org.sonar.api.rule.Severity;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.FileType;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

import com.godaddy.sonar.ruby.constants.RubyConstants;
import com.godaddy.sonar.ruby.metricfu.RoodiProblem.RoodiCheck;

public class MetricfuIssueSensor implements Sensor {
	private static final Logger LOG = LoggerFactory
			.getLogger(MetricfuIssueSensor.class);

	private static final Integer NO_LINE_NUMBER = -1;

	private MetricfuYamlParser metricfuYamlParser;
	private ModuleFileSystem moduleFileSystem;
	private final ResourcePerspectives perspectives;

	public MetricfuIssueSensor(ModuleFileSystem moduleFileSystem,
			MetricfuYamlParser metricfuYamlParser,
			ResourcePerspectives perspectives) {
		this.moduleFileSystem = moduleFileSystem;
		this.metricfuYamlParser = metricfuYamlParser;
		this.perspectives = perspectives;
	}

	public boolean shouldExecuteOnProject(Project project) {
		return !moduleFileSystem.files(
				FileQuery.on(FileType.values()).onLanguage(
						RubyConstants.LANGUAGE_KEY)).isEmpty();
	}

	public void analyse(Project project, SensorContext context) {
		List<File> sourceDirs = moduleFileSystem.sourceDirs();
		List<File> rubyFilesInProject = moduleFileSystem.files(FileQuery
				.onSource().onLanguage(RubyConstants.LANGUAGE_KEY));

		for (File file : rubyFilesInProject) {
			LOG.debug("analyzing issues in the file: " + file.getName());
			try {
				analyzeFile(file, sourceDirs, context);
			} catch (IOException e) {
				LOG.error("Can not analyze the file " + file.getAbsolutePath()
						+ " for issues", e);
			}
		}
	}

	private void analyzeFile(File file, List<File> sourceDirs,
			SensorContext sensorContext) throws IOException {
		Resource<?> resource = org.sonar.api.resources.File.fromIOFile(file,
				sourceDirs);
		List<ReekSmell> smells = metricfuYamlParser.parseReek(file
				.getAbsolutePath());

		for (ReekSmell smell : smells) {
			addIssue(resource, RubyConstants.KEY_REPOSITORY_REEK,
					smell.getType(), ReekSmell.toSeverity(smell.getType()),
					smell.getMessage());
		}

		List<RoodiProblem> problems = metricfuYamlParser.parseRoodi(file
				.getAbsolutePath());
		for (RoodiProblem problem : problems) {
			RoodiCheck check = RoodiProblem.messageToKey(problem.getProblem());
			addIssue(resource, problem.getLine(),
					RubyConstants.KEY_REPOSITORY_ROODI, check.toString(),
					RoodiProblem.toSeverity(check), problem.getProblem());
		}

		List<CaneViolation> violations = metricfuYamlParser.parseCane(file
				.getAbsolutePath());
		for (CaneViolation violation : violations) {
			if (violation instanceof CaneCommentViolation) {
				CaneCommentViolation c = (CaneCommentViolation) violation;
				addIssue(
						resource,
						c.getLine(),
						RubyConstants.KEY_REPOSITORY_CANE,
						c.getKey(),
						Severity.MINOR,
						"Class ' "
								+ c.getClassName()
								+ "' requires explanatory comments on preceding line.");
			} else if (violation instanceof CaneComplexityViolation) {
				CaneComplexityViolation c = (CaneComplexityViolation) violation;
				addIssue(resource, NO_LINE_NUMBER,
						RubyConstants.KEY_REPOSITORY_CANE, c.getKey(),
						Severity.MAJOR,
						"Method '" + c.getMethod() + "' has ABC complexity of "
								+ c.getComplexity() + ".");
			} else if (violation instanceof CaneLineStyleViolation) {
				CaneLineStyleViolation c = (CaneLineStyleViolation) violation;
				addIssue(resource, c.getLine(),
						RubyConstants.KEY_REPOSITORY_CANE, c.getKey(),
						Severity.MINOR, c.getDescription() + ".");
			}
		}
	}

	public void addIssue(Resource<?> resource, Integer line, String repo,
			String key, String severity, String message) {
		try {
			Issuable issuable = perspectives.as(Issuable.class, resource);
			IssueBuilder bld = issuable.newIssueBuilder()
					.ruleKey(RuleKey.of(repo, key)).message(message)
					.severity(severity);
			if (line != NO_LINE_NUMBER) {
				bld = bld.line(line);
			}
			Issue issue = bld.build();
			if (!issuable.addIssue(issue)) {
				LOG.error("Failed to register issue.\nIssue Object : "
						+ issue.toString());
			}
		} catch (Exception e) {
			LOG.error("line " + line);
			LOG.error("Resource " + resource.getKey());
			LOG.error("key " + key);
			LOG.error("severity " + severity);
			LOG.error("message " + message);
			LOG.error("Error in create issue object ", e);
		}
	}

	public void addIssue(Resource<?> resource, String repo, String key,
			String severity, String message) {
		addIssue(resource, NO_LINE_NUMBER, repo, key, severity, message);
	}
}
