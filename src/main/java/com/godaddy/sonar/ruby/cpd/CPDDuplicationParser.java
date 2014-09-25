package com.godaddy.sonar.ruby.cpd;

import java.io.File;
import java.util.List;

import org.sonar.api.BatchExtension;

public interface CPDDuplicationParser extends BatchExtension {
	List<CPDDuplication> parse(File file) throws Exception;
}
