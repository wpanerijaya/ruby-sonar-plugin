package com.godaddy.sonar.ruby.cpd;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.FileType;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.scan.filesystem.PathResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.common.FileUtil;
import com.godaddy.sonar.ruby.constants.RubyConstants;

/**
 * @author Widianto Panerijaya
 */
public class CPDDuplicationSensor implements Sensor {
	private static final Logger LOG = LoggerFactory
			.getLogger(CPDDuplicationSensor.class);

	private CPDDuplicationParser cpdDuplicationParser;
	private ModuleFileSystem moduleFileSystem;
	private PathResolver pathResolver;

	/**
	 * Use of IoC to get Settings
	 */
	public CPDDuplicationSensor(ModuleFileSystem moduleFileSystem,
			CPDDuplicationParser cpdDuplicationParser, PathResolver pathResolver) {
		this.moduleFileSystem = moduleFileSystem;
		this.cpdDuplicationParser = cpdDuplicationParser;
		this.pathResolver = pathResolver;
	}

	public boolean shouldExecuteOnProject(Project project) {
		return !moduleFileSystem.files(
				FileQuery.on(FileType.values()).onLanguage(
						RubyConstants.LANGUAGE_KEY)).isEmpty();
	}

	public void analyse(Project project, SensorContext context) {
		if (moduleFileSystem.baseDir() == null) {
			return;
		}
		List<File> rubyFilesInProject = moduleFileSystem.files(FileQuery
				.onSource().onLanguage(RubyConstants.LANGUAGE_KEY));
		List<File> files = FileUtil.findDirectories(moduleFileSystem.baseDir(),
				"spec", true);
		for (File source : files) {
			LOG.debug("ruby project folder : " + source.getParent());
		}

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document doc = builder.newDocument();
			Element root = doc.createElement("duplications");
			doc.appendChild(root);

			Map<String, Double> duplicatedBlocks = new HashMap<String, Double>();
			Map<String, Double> duplicatedLines = new HashMap<String, Double>();
			Map<String, Document> duplicatedXml = new HashMap<String, Document>();

			for (File source : files) {
				File xmlFile = pathResolver.relativeFile(
						source.getParentFile(), RubyConstants.CPD_REPORT_FILE);

				if (!xmlFile.exists()) {
					return;
				}

				List<CPDDuplication> duplications = cpdDuplicationParser
						.parse(xmlFile);
				for (CPDDuplication duplication : duplications) {
					Element group = doc.createElement("g");
					for (CPDDuplication.Match match : duplication.getMatches()) {
						File file = new File(match.getFile());
						String resourceKey = pathResolver.relativePath(
								moduleFileSystem.baseDir(), file);
						LOG.debug("resourceKey : " + resourceKey);
						LOG.debug("project.getKey() : " + project.getKey());
						String key = project.getKey() + ":" + resourceKey;
						LOG.debug("key : " + key);
						if (duplicatedBlocks.containsKey(key)) {
							duplicatedBlocks.put(key,
									duplicatedBlocks.get(key) + 1);
						} else {
							duplicatedBlocks.put(key, 1.0);
						}

						if (duplicatedLines.containsKey(key)) {
							duplicatedLines.put(key, duplicatedLines.get(key)
									+ match.getLines());
						} else {
							duplicatedLines.put(key, match.getLines() * 1.0);
						}

						Element block = doc.createElement("b");
						block.setAttribute("r", key);
						block.setAttribute("s", match.getStartLine().toString());
						block.setAttribute("l", match.getLines().toString());
						group.appendChild(block);
					}

					// Now that we have the group XML, add it to each file.
					Set<String> alreadyAdded = new HashSet<String>();
					for (CPDDuplication.Match match : duplication.getMatches()) {
						File file = new File(match.getFile());
						String resourceKey = pathResolver.relativePath(
								moduleFileSystem.baseDir(), file);
						LOG.debug("resourceKey : " + resourceKey);
						LOG.debug("project.getKey() : " + project.getKey());
						String key = project.getKey() + ":" + resourceKey;
						LOG.debug("key : " + key);
						if (!duplicatedXml.containsKey(key)) {
							Document d = builder.newDocument();
							Element r = d.createElement("duplications");
							d.appendChild(r);
							duplicatedXml.put(key, d);
						}

						// If we have duplications in the same file, only add
						// them once.
						if (!alreadyAdded.contains(key)) {
							Document d = duplicatedXml.get(key);
							d.getFirstChild().appendChild(
									d.importNode(group, true));
							alreadyAdded.add(key);
						}
					}
				}
			}

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");

			for (File file : rubyFilesInProject) {
				// WP
				Resource<?> resource = org.sonar.api.resources.File.fromIOFile(
						file, project);
				String resourceKey = pathResolver.relativePath(
						moduleFileSystem.baseDir(), file);
				LOG.debug("resourceKey : " + resourceKey);
				LOG.debug("project.getKey() : " + project.getKey());
				String key = project.getKey() + ":" + resourceKey;
				LOG.debug("key : " + key);
				if (duplicatedBlocks.containsKey(key)) {
					context.saveMeasure(resource, CoreMetrics.DUPLICATED_FILES,
							1.0);
					context.saveMeasure(resource,
							CoreMetrics.DUPLICATED_BLOCKS,
							duplicatedBlocks.get(key));
					context.saveMeasure(resource, CoreMetrics.DUPLICATED_LINES,
							duplicatedLines.get(key));
				} else {
					context.saveMeasure(resource, CoreMetrics.DUPLICATED_FILES,
							0.0);
				}

				if (duplicatedXml.containsKey(key)) {
					StringWriter writer = new StringWriter();
					transformer.transform(
							new DOMSource(duplicatedXml.get(key)),
							new StreamResult(writer));
					context.saveMeasure(resource, new Measure(
							CoreMetrics.DUPLICATIONS_DATA, writer.getBuffer()
									.toString()));
				}
			}

		} catch (Exception e) {
			LOG.error("Exception raised while processing duplications.", e);
		}
	}
}
