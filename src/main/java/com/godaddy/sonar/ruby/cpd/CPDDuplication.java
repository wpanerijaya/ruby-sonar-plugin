package com.godaddy.sonar.ruby.cpd;

import java.util.ArrayList;

public class CPDDuplication {

	public class Match {
		private String file;
		private Integer start;
		private Integer lines;

		public Match(String file, Integer start, Integer lines) {
			this.file = file;
			this.start = start;
			this.lines = lines;
		}

		public Match(String file, Integer start) {
			this(file, start, 1);
		}

		public Match(String file) {
			this(file, 1, 1);
		}

		public Match() {

		}

		public String getFile() {
			return file;
		}

		public void setFile(String file) {
			this.file = file;
		}

		public Integer getStartLine() {
			return start;
		}

		public void setStartLine(Integer start) {
			this.start = start;
		}

		public Integer getLines() {
			return lines;
		}

		public void setLines(Integer lines) {
			this.lines = lines;
		}

		@Override
		public String toString() {
			return "Match [file=" + file + ", start=" + start + ", lines="
					+ lines + "]";
		}
	}

	private String reason;
	private ArrayList<Match> matches = new ArrayList<Match>();

	public CPDDuplication(String reason) {
		this.reason = reason;
	}

	public CPDDuplication() {
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public ArrayList<Match> getMatches() {
		return matches;
	}

	public void addMatch(String file, Integer start) {
		matches.add(new Match(file, start));
	}

	public void addMatch(Match match) {
		matches.add(match);
	}

	@Override
	public String toString() {
		return "CPDDuplication [reason=" + reason + ", matches=" + matches
				+ "]";
	}
}
