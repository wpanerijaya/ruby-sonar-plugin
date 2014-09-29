package com.godaddy.sonar.ruby.rubocop;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sonar.api.rule.Severity;

/**
 * @author Widianto Panerijaya
 */
public class RubocopOffense {
	private String file;
	private String severity;
	private String message;
	private String copname;
	private String corrected;
	private int line = 0;
	private int column = 0;
	private int length = 0;

	public static enum RubocopCop {
		AmbiguousOperator,
		AmbiguousRegexpLiteral,
		AssignmentInCondition,
		BlockAlignment,
		ConditionPosition,
		Debugger,
		DeprecatedClassMethods,
		ElseLayout,
		EmptyEnsure,
		EmptyInterpolation,
		EndAlignment,
		EndInMethod,
		EnsureReturn,
		Eval,
		HandleExceptions,
		InvalidCharacterLiteral,
		LiteralInCondition,
		LiteralInInterpolation,
		Loop,
		ParenthesesAsGroupedExpression,
		RequireParentheses,
		RescueException,
		ShadowingOuterLocalVariable,
		SpaceBeforeFirstArg,
		StringConversionInInterpolation,
		UnderscorePrefixedVariableName,
		UnreachableCode,
		UnusedBlockArgument,
		UnusedMethodArgument,
		UselessAccessModifier,
		UselessAssignment,
		UselessComparison,
		UselessElseWithoutRescue,
		UselessSetterCall,
		Void,
		AccessModifierIndentation,
		AccessorMethodName,
		Alias,
		AlignArray,
		AlignHash,
		AlignParameters,
		AndOr,
		ArrayJoin,
		AsciiComments,
		AsciiIdentifiers,
		Attr,
		BeginBlock,
		BlockComments,
		BlockNesting,
		Blocks,
		BracesAroundHashParameters,
		CaseEquality,
		CaseIndentation,
		CharacterLiteral,
		ClassAndModuleCamelCase,
		ClassAndModuleChildren,
		ClassLength,
		ClassMethods,
		ClassVars,
		CollectionMethods,
		ColonMethodCall,
		CommentAnnotation,
		ConstantName,
		CyclomaticComplexity,
		DefWithParentheses,
		DeprecatedHashMethods,
		Documentation,
		DotPosition,
		DoubleNegation,
		EmptyLineBetweenDefs,
		EmptyLines,
		EmptyLinesAroundAccessModifier,
		EmptyLinesAroundBody,
		EmptyLiteral,
		Encoding,
		EndBlock,
		EndOfLine,
		EvenOdd,
		FileName,
		FlipFlop,
		For,
		FormatString,
		GlobalVars,
		GuardClause,
		HashSyntax,
		IfUnlessModifier,
		IfWithSemicolon,
		IndentArray,
		IndentHash,
		IndentationConsistency,
		IndentationWidth,
		Lambda,
		LambdaCall,
		LeadingCommentSpace,
		LineEndConcatenation,
		LineLength,
		MethodCallParentheses,
		MethodCalledOnDoEndBlock,
		MethodDefParentheses,
		MethodLength,
		MethodName,
		ModuleFunction,
		MultilineBlockChain,
		MultilineIfThen,
		MultilineTernaryOperator,
		NegatedIf,
		NegatedWhile,
		NestedTernaryOperator,
		NilComparison,
		NonNilCheck,
		Not,
		NumericLiterals,
		OneLineConditional,
		OpMethod,
		ParameterLists,
		ParenthesesAroundCondition,
		PercentLiteralDelimiters,
		PerlBackrefs,
		PredicateName,
		Proc,
		RaiseArgs,
		RedundantBegin,
		RedundantException,
		RedundantReturn,
		RedundantSelf,
		RegexpLiteral,
		RescueModifier,
		SelfAssignment,
		Semicolon,
		SignalException,
		SingleLineBlockParams,
		SingleLineMethods,
		SingleSpaceBeforeFirstArg,
		SpaceAfterColon,
		SpaceAfterComma,
		SpaceAfterControlKeyword,
		SpaceAfterMethodName,
		SpaceAfterNot,
		SpaceAfterSemicolon,
		SpaceAroundEqualsInParameterDefault,
		SpaceAroundOperators,
		SpaceBeforeBlockBraces,
		SpaceBeforeModifierKeyword,
		SpaceInsideBlockBraces,
		SpaceInsideBrackets,
		SpaceInsideHashLiteralBraces,
		SpaceInsideParens,
		SpecialGlobalVars,
		StringLiterals,
		SymbolArray,
		Syntax,
		Tab,
		TrailingBlankLines,
		TrailingComma,
		TrailingWhitespace,
		TrivialAccessors,
		UnlessElse,
		UnneededCapitalW,
		VariableInterpolation,
		VariableName,
		WhenThen,
		WhileUntilDo,
		WhileUntilModifier,
		WordArray,
		ActionFilter,
		DefaultScope,
		Delegate,
		HasAndBelongsToMany,
		Output,
		ReadWriteAttribute,
		ScopeArgs,
		Validation
	}

	private static Map<String, String> keyToSeverityMap;

	static {
		Map<String, String> mapKeyToSeverity = new HashMap<String, String>();
		mapKeyToSeverity.put("convention", Severity.MINOR);
		mapKeyToSeverity.put("warning", Severity.INFO);
		mapKeyToSeverity.put("refactor", Severity.MINOR);
		mapKeyToSeverity.put("error", Severity.CRITICAL);
		mapKeyToSeverity.put("fatal", Severity.BLOCKER);
		keyToSeverityMap = Collections.unmodifiableMap(mapKeyToSeverity);
	}

	public RubocopOffense(String file, String severity, String message,
			String copname, String corrected, int line, int column, int length) {
		this.file = file;
		this.severity = severity;
		this.message = message;
		this.copname = copname;
		this.corrected = corrected;
		this.line = line;
		this.column = column;
		this.length = length;
	}

	public RubocopOffense() {
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCopname() {
		return copname;
	}

	public void setCopname(String copname) {
		this.copname = copname;
	}

	public String getCorrected() {
		return corrected;
	}

	public void setCorrected(String corrected) {
		this.corrected = corrected;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "file: " + file + " severity: " + severity + " message: "
				+ message + " copname: " + copname + " corrected: " + corrected
				+ " line: " + line + " column: " + column + " length: "
				+ length;
	}

	public static String toSeverity(String check) {
		if (keyToSeverityMap.containsKey(check)) {
			return keyToSeverityMap.get(check);
		}
		// Make sure we catch this case.
		return Severity.BLOCKER; 
	}
}
