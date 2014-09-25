package com.godaddy.sonar.ruby.rubocop;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.sonar.api.BatchExtension;

public interface RubocopJsonParser extends BatchExtension {
	List<RubocopOffense> parse(File file) throws IOException;
}
