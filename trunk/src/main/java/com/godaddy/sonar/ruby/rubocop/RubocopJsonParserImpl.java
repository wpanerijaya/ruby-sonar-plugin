package com.godaddy.sonar.ruby.rubocop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.godaddy.sonar.ruby.core.Ruby;

public class RubocopJsonParserImpl implements RubocopJsonParser {
	public List<RubocopOffense> parse(File file) throws IOException {
		File rubocopResultFile = file;

		String fileString = FileUtils.readFileToString(rubocopResultFile, "UTF-8");

		JSONObject resultJsonObject = (JSONObject) JSONValue.parse(fileString);
		JSONArray fileArray = (JSONArray) resultJsonObject.get("files");

		List<RubocopOffense> offenses = new ArrayList<RubocopOffense>();
		for (Object obj1 : fileArray) {
			JSONObject fileJsonObj = (JSONObject) obj1;
			String path = fileJsonObj.get("path").toString();
			boolean isRubyFile = false;
			for (String suffix : new Ruby().getFileSuffixes()) {
				if (path.endsWith(suffix))
					isRubyFile = true;
			}
			if (!isRubyFile)
				continue;
			JSONArray offenseArray = (JSONArray) fileJsonObj.get("offenses");
			for (Object obj2 : offenseArray) {
				JSONObject offenseJsonObj = (JSONObject) obj2;
				String severity = offenseJsonObj.get("severity") != null ? offenseJsonObj.get("severity").toString() : null;
				String message = offenseJsonObj.get("message") != null ? offenseJsonObj.get("message").toString() : null;
				String copname = offenseJsonObj.get("cop_name") != null ? offenseJsonObj.get("cop_name").toString() : null;
				String corrected = offenseJsonObj.get("corrected") != null ? offenseJsonObj.get("corrected").toString() : null;
				JSONObject locationJsonObj = (JSONObject) offenseJsonObj.get("location");
				int line = locationJsonObj.get("line") != null ? Integer.parseInt(locationJsonObj.get("line").toString()) : 0;
				int column = locationJsonObj.get("column") != null ? Integer.parseInt(locationJsonObj.get("column").toString()) : 0;
				int length = locationJsonObj.get("length") != null ? Integer.parseInt(locationJsonObj.get("length").toString()) : 0;
				RubocopOffense offense = new RubocopOffense();
				offense.setFile(path);
				offense.setSeverity(severity);
				offense.setColumn(column);
				offense.setCorrected(corrected);
				offense.setLength(length);
				offense.setLine(line);
				offense.setMessage(message);
				offense.setCopname(copname);
				offenses.add(offense);
			}
		}
		return offenses;
	}
}
