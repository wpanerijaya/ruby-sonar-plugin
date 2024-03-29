package com.godaddy.sonar.ruby;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.FileType;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.utils.SonarException;
import org.sonar.squid.measures.Metric;
import org.sonar.squid.text.Source;

import com.godaddy.sonar.ruby.constants.RubyConstants;
import com.godaddy.sonar.ruby.core.RubyPackage;
import com.godaddy.sonar.ruby.core.RubyRecognizer;
import com.godaddy.sonar.ruby.parsers.CommentCountParser;

public class RubySensor implements Sensor {
	private ModuleFileSystem moduleFileSystem;

	public RubySensor(ModuleFileSystem moduleFileSystem) {
		this.moduleFileSystem = moduleFileSystem;
	}

	public boolean shouldExecuteOnProject(Project project) {
		return !moduleFileSystem.files(
				FileQuery.on(FileType.values()).onLanguage(RubyConstants.LANGUAGE_KEY)).isEmpty();
	}

	public void analyse(Project project, SensorContext context) {
		computeBaseMetrics(context, project);
	}

	protected void computeBaseMetrics(SensorContext sensorContext,
			Project project) {
		Reader reader = null;

		Set<RubyPackage> packageList = new HashSet<RubyPackage>();
		for (File rubyFile : moduleFileSystem.files(FileQuery.onSource()
				.onLanguage(RubyConstants.LANGUAGE_KEY))) {
			try {
				reader = new StringReader(FileUtils.readFileToString(rubyFile,
						moduleFileSystem.sourceCharset().name()));
				Resource<?> resource = org.sonar.api.resources.File.fromIOFile(
						rubyFile, project);
				Source source = new Source(reader, new RubyRecognizer());
				packageList.add(new RubyPackage(resource.getParent().getKey()));

				sensorContext.saveMeasure(resource, CoreMetrics.LINES,
						(double) source.getMeasure(Metric.LINES));
				sensorContext.saveMeasure(resource, CoreMetrics.NCLOC,
						(double) source.getMeasure(Metric.LINES_OF_CODE));
				int numCommentLines = CommentCountParser
						.countLinesOfComment(rubyFile);
				sensorContext.saveMeasure(resource, CoreMetrics.COMMENT_LINES,
						(double) numCommentLines);
				sensorContext.saveMeasure(resource, CoreMetrics.FILES, 1.0);
				sensorContext.saveMeasure(resource, CoreMetrics.CLASSES, 1.0);
			} catch (Exception e) {
				throw new SonarException(
						"Error computing base metrics for project.", e);
			} finally {
				IOUtils.closeQuietly(reader);
			}
		}
		for (RubyPackage pack : packageList) {
			sensorContext.saveMeasure(pack, CoreMetrics.PACKAGES, 1.0);
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
