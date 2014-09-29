package com.godaddy.sonar.ruby.metricfu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.sonar.api.BatchExtension;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.scan.filesystem.PathResolver;
import org.yaml.snakeyaml.Yaml;

import com.common.FileUtil;
import com.common.StringUtil;
import com.godaddy.sonar.ruby.constants.RubyConstants;
import com.godaddy.sonar.ruby.metricfu.FlayReason.Match;

public class MetricfuYamlParser implements BatchExtension {
	private static final Logger LOG = Logger
			.getLogger(MetricfuYamlParser.class);

	private static Pattern escapePattern = Pattern.compile("\\e\\[\\d+m",
			Pattern.CASE_INSENSITIVE);

	protected Map<Map<String, Object>, Object> metricfuResults = null;

	List<Map<String, Object>> saikuroFiles = null;
	Map<String, Object> caneViolations = null;
	List<Map<String, Object>> roodiProblems = null;
	List<Map<String, Object>> reekFiles = null;
	List<Map<String, Object>> flayReasons = null;
	private ModuleFileSystem moduleFileSystem;
	private PathResolver pathResolver;

	public MetricfuYamlParser(ModuleFileSystem moduleFileSystem) {
		this(moduleFileSystem.baseDir() != null ? moduleFileSystem.baseDir()
				.getAbsolutePath() : null);
		this.moduleFileSystem = moduleFileSystem;
		this.pathResolver = new PathResolver();
	}

	@SuppressWarnings("unchecked")
	public MetricfuYamlParser(String basedir) {
		LOG.debug("Enter MetricfuYamlParser Contructor");
		try {
			metricfuResults = new HashMap<Map<String, Object>, Object>();
			List<File> files = FileUtil.findDirectories(basedir, "spec", true);
			for (File source : files) {
				LOG.debug("ruby project folder : " + source.getAbsolutePath());
			}
			for (File file : files) {
				File report = new File(file.getParent() + "/"
						+ RubyConstants.METRIC_FU_REPORT_FILE);
				if (report.exists()) {
					FileInputStream input = new FileInputStream(report);
					String inputStr = IOUtils.toString(input);
					// Yaml can't read !binary
					inputStr = inputStr.replaceAll("!binary", "! binary");
					Yaml yaml = new Yaml();

					this.metricfuResults.put((Map<String, Object>) yaml.loadAs(
							inputStr, Map.class), file.getParentFile());
				}
			}
		} catch (FileNotFoundException e) {
			LOG.error(e);
		} catch (IOException ioe) {
			LOG.error(ioe);
		} finally {
			LOG.debug("Exit MetricfuYamlParser Contructor");
		}
	}

	@SuppressWarnings("unchecked")
	public List<SaikuroComplexity> parseSaikuro(String fileNameFromModule) {
		LOG.debug("Enter Parse Saikuro");
		List<SaikuroComplexity> complexities = new ArrayList<SaikuroComplexity>();

		Iterator<?> it = metricfuResults.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			Map<String, Object> metricfuResult = (Map<String, Object>) pairs
					.getKey();
			File filepath = pathResolver.relativeFile((File) pairs.getValue(),
					"");

			Map<String, Object> saikuro = (Map<String, Object>) metricfuResult
					.get(":saikuro");
			saikuroFiles = (ArrayList<Map<String, Object>>) saikuro
					.get(":files");

			if (saikuroFiles != null) {
				for (Map<String, Object> fileInfo : saikuroFiles) {
					String file = (String) fileInfo.get(":filename");
					String fileNameFromResults = pathResolver.relativeFile(
							filepath, file).getAbsolutePath();
					if (fileNameFromModule.contains(fileNameFromResults)) {
						List<Map<String, Object>> classesInfo = (ArrayList<Map<String, Object>>) fileInfo
								.get(":classes");

						for (Map<String, Object> classInfo : classesInfo) {
							List<Map<String, Object>> methods = (ArrayList<Map<String, Object>>) classInfo
									.get(":methods");

							for (Map<String, Object> method : methods) {
								SaikuroComplexity complexity = new SaikuroComplexity();
								complexity.setFile(fileNameFromResults);
								complexity
										.setName((String) method.get(":name"));
								complexity.setComplexity((Integer) method
										.get(":complexity"));
								complexity.setLine((Integer) method
										.get(":lines"));
								complexities.add(complexity);
							}
						}
					}
				}
			}
		}
		LOG.debug("Exit Parse Saikuro");
		return complexities;
	}

	@SuppressWarnings("unchecked")
	public List<CaneViolation> parseCane(String fileNameFromModule) {
		LOG.debug("Enter Parse Cane");
		List<CaneViolation> violations = new ArrayList<CaneViolation>();
		Iterator<?> it = metricfuResults.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			Map<String, Object> metricfuResult = (Map<String, Object>) pairs
					.getKey();
			File filepath = pathResolver.relativeFile((File) pairs.getValue(),
					"");

			Map<String, Object> caneResult = (Map<String, Object>) metricfuResult
					.get(":cane");
			caneViolations = (Map<String, Object>) caneResult
					.get(":violations");

			if (caneViolations != null) {
				LOG.debug("fileNameFromModule : " + fileNameFromModule);
				List<Map<String, Object>> caneViolationsComplexityResult = (ArrayList<Map<String, Object>>) caneViolations
						.get(":abc_complexity");
				violations.addAll(new CaneComplexityViolation().getViolations(
						fileNameFromModule, filepath,
						caneViolationsComplexityResult));

				List<Map<String, Object>> caneViolationsLineResult = (ArrayList<Map<String, Object>>) caneViolations
						.get(":line_style");
				violations
						.addAll(new CaneLineStyleViolation().getViolations(
								fileNameFromModule, filepath,
								caneViolationsLineResult));

				List<Map<String, Object>> caneViolationsCommentResult = (ArrayList<Map<String, Object>>) caneViolations
						.get(":comment");
				violations.addAll(new CaneCommentViolation().getViolations(
						fileNameFromModule, filepath,
						caneViolationsCommentResult));
			}
		}
		LOG.debug("Exit Parse Cane");
		return violations;
	}

	@SuppressWarnings("unchecked")
	public List<RoodiProblem> parseRoodi(String fileNameFromModule) {
		LOG.debug("Enter Parse Roodi");
		List<RoodiProblem> problems = new ArrayList<RoodiProblem>();
		Iterator<?> it = metricfuResults.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			Map<String, Object> metricfuResult = (Map<String, Object>) pairs
					.getKey();
			File filepath = pathResolver.relativeFile((File) pairs.getValue(),
					"");

			Map<String, Object> roodi = (Map<String, Object>) metricfuResult
					.get(":roodi");
			roodiProblems = (ArrayList<Map<String, Object>>) roodi
					.get(":problems");

			if (roodiProblems != null) {
				LOG.debug("fileNameFromModule : " + fileNameFromModule);
				for (Map<String, Object> prob : roodiProblems) {
					String file = escapePattern.matcher(
							StringUtil.safeString((String) prob.get(":file")))
							.replaceAll("");
					String fileNameFromResults = pathResolver.relativeFile(
							filepath, file).getAbsolutePath();
					if (fileNameFromModule.contains(fileNameFromResults)) {
						RoodiProblem problem = new RoodiProblem();
						problem.setFile(file);
						problem.setLine(StringUtil.safeInteger((String) prob
								.get(":line")));
						problem.setProblem(escapePattern.matcher(
								StringUtil.safeString((String) prob
										.get(":problem"))).replaceAll(""));

						if (problem.getFile().length() > 0
								&& problem.getLine() > 0) {
							problems.add(problem);
						}
					}
				}
			}
		}
		LOG.debug("Exit Parse Roodi");
		return problems;
	}

	@SuppressWarnings("unchecked")
	public List<ReekSmell> parseReek(String fileNameFromModule) {
		LOG.debug("Enter Parse Reek");
		List<ReekSmell> smells = new ArrayList<ReekSmell>();
		Iterator<?> it = metricfuResults.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			Map<String, Object> metricfuResult = (Map<String, Object>) pairs
					.getKey();
			File filepath = pathResolver.relativeFile((File) pairs.getValue(),
					"");
			LOG.debug(pairs.getValue());

			Map<String, Object> reek = (Map<String, Object>) metricfuResult
					.get(":reek");
			reekFiles = (ArrayList<Map<String, Object>>) reek.get(":matches");

			if (reekFiles != null) {
				LOG.debug("fileNameFromModule : " + fileNameFromModule);
				for (Map<String, Object> resultFile : reekFiles) {
					String file = StringUtil.safeString((String) resultFile
							.get(":file_path"));
					String fileNameFromResults = pathResolver.relativeFile(
							filepath, file).getAbsolutePath();
					if (file.length() > 0
							&& fileNameFromModule.contains(fileNameFromResults)) {
						List<Map<String, Object>> resultSmells = (ArrayList<Map<String, Object>>) resultFile
								.get(":code_smells");

						for (Map<String, Object> resultSmell : resultSmells) {
							ReekSmell smell = new ReekSmell();
							smell.setFile(file);
							smell.setMethod(StringUtil
									.safeString((String) resultSmell
											.get(":method")));
							smell.setMessage(StringUtil
									.safeString((String) resultSmell
											.get(":message")));
							smell.setType(StringUtil
									.safeString((String) resultSmell
											.get(":type")));
							smells.add(smell);
						}
					}
				}
			}
		}
		LOG.debug("Exit Parse Reek");
		return smells;
	}

	@SuppressWarnings("unchecked")
	public List<FlayReason> parseFlay() {
		LOG.debug("Enter Parse Flay");
		List<FlayReason> reasons = new ArrayList<FlayReason>();
		Iterator<?> it = metricfuResults.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			Map<String, Object> metricfuResult = (Map<String, Object>) pairs
					.getKey();
			LOG.debug(pairs.getValue());

			Map<String, Object> flay = (Map<String, Object>) metricfuResult
					.get(":flay");
			flayReasons = (ArrayList<Map<String, Object>>) flay.get(":matches");

			if (flayReasons != null) {

				for (Map<String, Object> resultReason : flayReasons) {
					FlayReason reason = new FlayReason();
					reason.setReason(StringUtil
							.safeString((String) resultReason.get(":reason")));

					List<Map<String, Object>> resultMatches = (ArrayList<Map<String, Object>>) resultReason
							.get(":matches");
					for (Map<String, Object> resultDuplication : resultMatches) {
						Match match = reason.new Match(
								(String) resultDuplication.get(":name"));

						// If flay was run with --diff, we should have the
						// number of lines in the duplication. If not, make it
						// 1.
						Integer line = StringUtil
								.safeInteger((String) resultDuplication
										.get(":line"));
						if (line > 0) {
							match.setStartLine(line);
							match.setLines(1);
						} else {
							Integer start = StringUtil
									.safeInteger((String) resultDuplication
											.get(":start"));
							if (start > 0) {
								match.setStartLine(start);
							}
							Integer lines = StringUtil
									.safeInteger((String) resultDuplication
											.get(":lines"));
							if (lines > 0) {
								match.setLines(lines);
							}
						}
						reason.getMatches().add(match);
					}
					reasons.add(reason);
				}
			}
		}
		LOG.debug("Exit Parse Flay");
		return reasons;
	}

	public static void main(String[] args) {
		MetricfuYamlParser parser = new MetricfuYamlParser(
				"/home/gallen/work/report.yml");
		parser.parseFlay();
	}
}
