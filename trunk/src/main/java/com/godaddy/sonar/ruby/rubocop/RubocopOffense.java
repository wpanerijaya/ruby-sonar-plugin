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
		Validation,
	};

	private static final Map<String, RubocopCop> messageToKeyMap;
	private static final Map<String, String> keyToSeverityMap;

	static {
		Map<String, RubocopCop> mapPatternToCheck = new HashMap<String, RubocopCop>();

		mapPatternToCheck.put(RubocopCop.AmbiguousOperator.toString(), RubocopCop.AmbiguousOperator);
		mapPatternToCheck.put(RubocopCop.AmbiguousRegexpLiteral.toString(), RubocopCop.AmbiguousRegexpLiteral);
		mapPatternToCheck.put(RubocopCop.AssignmentInCondition.toString(), RubocopCop.AssignmentInCondition);
		mapPatternToCheck.put(RubocopCop.BlockAlignment.toString(), RubocopCop.BlockAlignment);
		mapPatternToCheck.put(RubocopCop.ConditionPosition.toString(), RubocopCop.ConditionPosition);
		mapPatternToCheck.put(RubocopCop.Debugger.toString(), RubocopCop.Debugger);
		mapPatternToCheck.put(RubocopCop.DeprecatedClassMethods.toString(), RubocopCop.DeprecatedClassMethods);
		mapPatternToCheck.put(RubocopCop.ElseLayout.toString(), RubocopCop.ElseLayout);
		mapPatternToCheck.put(RubocopCop.EmptyEnsure.toString(), RubocopCop.EmptyEnsure);
		mapPatternToCheck.put(RubocopCop.EmptyInterpolation.toString(), RubocopCop.EmptyInterpolation);
		mapPatternToCheck.put(RubocopCop.EndAlignment.toString(), RubocopCop.EndAlignment);
		mapPatternToCheck.put(RubocopCop.EndInMethod.toString(), RubocopCop.EndInMethod);
		mapPatternToCheck.put(RubocopCop.EnsureReturn.toString(), RubocopCop.EnsureReturn);
		mapPatternToCheck.put(RubocopCop.Eval.toString(), RubocopCop.Eval);
		mapPatternToCheck.put(RubocopCop.HandleExceptions.toString(), RubocopCop.HandleExceptions);
		mapPatternToCheck.put(RubocopCop.InvalidCharacterLiteral.toString(), RubocopCop.InvalidCharacterLiteral);
		mapPatternToCheck.put(RubocopCop.LiteralInCondition.toString(), RubocopCop.LiteralInCondition);
		mapPatternToCheck.put(RubocopCop.LiteralInInterpolation.toString(), RubocopCop.LiteralInInterpolation);
		mapPatternToCheck.put(RubocopCop.Loop.toString(), RubocopCop.Loop);
		mapPatternToCheck.put(RubocopCop.ParenthesesAsGroupedExpression.toString(), RubocopCop.ParenthesesAsGroupedExpression);
		mapPatternToCheck.put(RubocopCop.RequireParentheses.toString(), RubocopCop.RequireParentheses);
		mapPatternToCheck.put(RubocopCop.RescueException.toString(), RubocopCop.RescueException);
		mapPatternToCheck.put(RubocopCop.ShadowingOuterLocalVariable.toString(), RubocopCop.ShadowingOuterLocalVariable);
		mapPatternToCheck.put(RubocopCop.SpaceBeforeFirstArg.toString(), RubocopCop.SpaceBeforeFirstArg);
		mapPatternToCheck.put(RubocopCop.StringConversionInInterpolation.toString(), RubocopCop.StringConversionInInterpolation);
		mapPatternToCheck.put(RubocopCop.UnderscorePrefixedVariableName.toString(), RubocopCop.UnderscorePrefixedVariableName);
		mapPatternToCheck.put(RubocopCop.UnreachableCode.toString(), RubocopCop.UnreachableCode);
		mapPatternToCheck.put(RubocopCop.UnusedBlockArgument.toString(), RubocopCop.UnusedBlockArgument);
		mapPatternToCheck.put(RubocopCop.UnusedMethodArgument.toString(), RubocopCop.UnusedMethodArgument);
		mapPatternToCheck.put(RubocopCop.UselessAccessModifier.toString(), RubocopCop.UselessAccessModifier);
		mapPatternToCheck.put(RubocopCop.UselessAssignment.toString(), RubocopCop.UselessAssignment);
		mapPatternToCheck.put(RubocopCop.UselessComparison.toString(), RubocopCop.UselessComparison);
		mapPatternToCheck.put(RubocopCop.UselessElseWithoutRescue.toString(), RubocopCop.UselessElseWithoutRescue);
		mapPatternToCheck.put(RubocopCop.UselessSetterCall.toString(), RubocopCop.UselessSetterCall);
		mapPatternToCheck.put(RubocopCop.Void.toString(), RubocopCop.Void);
		mapPatternToCheck.put(RubocopCop.AccessModifierIndentation.toString(), RubocopCop.AccessModifierIndentation);
		mapPatternToCheck.put(RubocopCop.AccessorMethodName.toString(), RubocopCop.AccessorMethodName);
		mapPatternToCheck.put(RubocopCop.Alias.toString(), RubocopCop.Alias);
		mapPatternToCheck.put(RubocopCop.AlignArray.toString(), RubocopCop.AlignArray);
		mapPatternToCheck.put(RubocopCop.AlignHash.toString(), RubocopCop.AlignHash);
		mapPatternToCheck.put(RubocopCop.AlignParameters.toString(), RubocopCop.AlignParameters);
		mapPatternToCheck.put(RubocopCop.AndOr.toString(), RubocopCop.AndOr);
		mapPatternToCheck.put(RubocopCop.ArrayJoin.toString(), RubocopCop.ArrayJoin);
		mapPatternToCheck.put(RubocopCop.AsciiComments.toString(), RubocopCop.AsciiComments);
		mapPatternToCheck.put(RubocopCop.AsciiIdentifiers.toString(), RubocopCop.AsciiIdentifiers);
		mapPatternToCheck.put(RubocopCop.Attr.toString(), RubocopCop.Attr);
		mapPatternToCheck.put(RubocopCop.BeginBlock.toString(), RubocopCop.BeginBlock);
		mapPatternToCheck.put(RubocopCop.BlockComments.toString(), RubocopCop.BlockComments);
		mapPatternToCheck.put(RubocopCop.BlockNesting.toString(), RubocopCop.BlockNesting);
		mapPatternToCheck.put(RubocopCop.Blocks.toString(), RubocopCop.Blocks);
		mapPatternToCheck.put(RubocopCop.BracesAroundHashParameters.toString(), RubocopCop.BracesAroundHashParameters);
		mapPatternToCheck.put(RubocopCop.CaseEquality.toString(), RubocopCop.CaseEquality);
		mapPatternToCheck.put(RubocopCop.CaseIndentation.toString(), RubocopCop.CaseIndentation);
		mapPatternToCheck.put(RubocopCop.CharacterLiteral.toString(), RubocopCop.CharacterLiteral);
		mapPatternToCheck.put(RubocopCop.ClassAndModuleCamelCase.toString(), RubocopCop.ClassAndModuleCamelCase);
		mapPatternToCheck.put(RubocopCop.ClassAndModuleChildren.toString(), RubocopCop.ClassAndModuleChildren);
		mapPatternToCheck.put(RubocopCop.ClassLength.toString(), RubocopCop.ClassLength);
		mapPatternToCheck.put(RubocopCop.ClassMethods.toString(), RubocopCop.ClassMethods);
		mapPatternToCheck.put(RubocopCop.ClassVars.toString(), RubocopCop.ClassVars);
		mapPatternToCheck.put(RubocopCop.CollectionMethods.toString(), RubocopCop.CollectionMethods);
		mapPatternToCheck.put(RubocopCop.ColonMethodCall.toString(), RubocopCop.ColonMethodCall);
		mapPatternToCheck.put(RubocopCop.CommentAnnotation.toString(), RubocopCop.CommentAnnotation);
		mapPatternToCheck.put(RubocopCop.ConstantName.toString(), RubocopCop.ConstantName);
		mapPatternToCheck.put(RubocopCop.CyclomaticComplexity.toString(), RubocopCop.CyclomaticComplexity);
		mapPatternToCheck.put(RubocopCop.DefWithParentheses.toString(), RubocopCop.DefWithParentheses);
		mapPatternToCheck.put(RubocopCop.DeprecatedHashMethods.toString(), RubocopCop.DeprecatedHashMethods);
		mapPatternToCheck.put(RubocopCop.Documentation.toString(), RubocopCop.Documentation);
		mapPatternToCheck.put(RubocopCop.DotPosition.toString(), RubocopCop.DotPosition);
		mapPatternToCheck.put(RubocopCop.DoubleNegation.toString(), RubocopCop.DoubleNegation);
		mapPatternToCheck.put(RubocopCop.EmptyLineBetweenDefs.toString(), RubocopCop.EmptyLineBetweenDefs);
		mapPatternToCheck.put(RubocopCop.EmptyLines.toString(), RubocopCop.EmptyLines);
		mapPatternToCheck.put(RubocopCop.EmptyLinesAroundAccessModifier.toString(), RubocopCop.EmptyLinesAroundAccessModifier);
		mapPatternToCheck.put(RubocopCop.EmptyLinesAroundBody.toString(), RubocopCop.EmptyLinesAroundBody);
		mapPatternToCheck.put(RubocopCop.EmptyLiteral.toString(), RubocopCop.EmptyLiteral);
		mapPatternToCheck.put(RubocopCop.Encoding.toString(), RubocopCop.Encoding);
		mapPatternToCheck.put(RubocopCop.EndBlock.toString(), RubocopCop.EndBlock);
		mapPatternToCheck.put(RubocopCop.EndOfLine.toString(), RubocopCop.EndOfLine);
		mapPatternToCheck.put(RubocopCop.EvenOdd.toString(), RubocopCop.EvenOdd);
		mapPatternToCheck.put(RubocopCop.FileName.toString(), RubocopCop.FileName);
		mapPatternToCheck.put(RubocopCop.FlipFlop.toString(), RubocopCop.FlipFlop);
		mapPatternToCheck.put(RubocopCop.For.toString(), RubocopCop.For);
		mapPatternToCheck.put(RubocopCop.FormatString.toString(), RubocopCop.FormatString);
		mapPatternToCheck.put(RubocopCop.GlobalVars.toString(), RubocopCop.GlobalVars);
		mapPatternToCheck.put(RubocopCop.GuardClause.toString(), RubocopCop.GuardClause);
		mapPatternToCheck.put(RubocopCop.HashSyntax.toString(), RubocopCop.HashSyntax);
		mapPatternToCheck.put(RubocopCop.IfUnlessModifier.toString(), RubocopCop.IfUnlessModifier);
		mapPatternToCheck.put(RubocopCop.IfWithSemicolon.toString(), RubocopCop.IfWithSemicolon);
		mapPatternToCheck.put(RubocopCop.IndentArray.toString(), RubocopCop.IndentArray);
		mapPatternToCheck.put(RubocopCop.IndentHash.toString(), RubocopCop.IndentHash);
		mapPatternToCheck.put(RubocopCop.IndentationConsistency.toString(), RubocopCop.IndentationConsistency);
		mapPatternToCheck.put(RubocopCop.IndentationWidth.toString(), RubocopCop.IndentationWidth);
		mapPatternToCheck.put(RubocopCop.Lambda.toString(), RubocopCop.Lambda);
		mapPatternToCheck.put(RubocopCop.LambdaCall.toString(), RubocopCop.LambdaCall);
		mapPatternToCheck.put(RubocopCop.LeadingCommentSpace.toString(), RubocopCop.LeadingCommentSpace);
		mapPatternToCheck.put(RubocopCop.LineEndConcatenation.toString(), RubocopCop.LineEndConcatenation);
		mapPatternToCheck.put(RubocopCop.LineLength.toString(), RubocopCop.LineLength);
		mapPatternToCheck.put(RubocopCop.MethodCallParentheses.toString(), RubocopCop.MethodCallParentheses);
		mapPatternToCheck.put(RubocopCop.MethodCalledOnDoEndBlock.toString(), RubocopCop.MethodCalledOnDoEndBlock);
		mapPatternToCheck.put(RubocopCop.MethodDefParentheses.toString(), RubocopCop.MethodDefParentheses);
		mapPatternToCheck.put(RubocopCop.MethodLength.toString(), RubocopCop.MethodLength);
		mapPatternToCheck.put(RubocopCop.MethodName.toString(), RubocopCop.MethodName);
		mapPatternToCheck.put(RubocopCop.ModuleFunction.toString(), RubocopCop.ModuleFunction);
		mapPatternToCheck.put(RubocopCop.MultilineBlockChain.toString(), RubocopCop.MultilineBlockChain);
		mapPatternToCheck.put(RubocopCop.MultilineIfThen.toString(), RubocopCop.MultilineIfThen);
		mapPatternToCheck.put(RubocopCop.MultilineTernaryOperator.toString(), RubocopCop.MultilineTernaryOperator);
		mapPatternToCheck.put(RubocopCop.NegatedIf.toString(), RubocopCop.NegatedIf);
		mapPatternToCheck.put(RubocopCop.NegatedWhile.toString(), RubocopCop.NegatedWhile);
		mapPatternToCheck.put(RubocopCop.NestedTernaryOperator.toString(), RubocopCop.NestedTernaryOperator);
		mapPatternToCheck.put(RubocopCop.NilComparison.toString(), RubocopCop.NilComparison);
		mapPatternToCheck.put(RubocopCop.NonNilCheck.toString(), RubocopCop.NonNilCheck);
		mapPatternToCheck.put(RubocopCop.Not.toString(), RubocopCop.Not);
		mapPatternToCheck.put(RubocopCop.NumericLiterals.toString(), RubocopCop.NumericLiterals);
		mapPatternToCheck.put(RubocopCop.OneLineConditional.toString(), RubocopCop.OneLineConditional);
		mapPatternToCheck.put(RubocopCop.OpMethod.toString(), RubocopCop.OpMethod);
		mapPatternToCheck.put(RubocopCop.ParameterLists.toString(), RubocopCop.ParameterLists);
		mapPatternToCheck.put(RubocopCop.ParenthesesAroundCondition.toString(), RubocopCop.ParenthesesAroundCondition);
		mapPatternToCheck.put(RubocopCop.PercentLiteralDelimiters.toString(), RubocopCop.PercentLiteralDelimiters);
		mapPatternToCheck.put(RubocopCop.PerlBackrefs.toString(), RubocopCop.PerlBackrefs);
		mapPatternToCheck.put(RubocopCop.PredicateName.toString(), RubocopCop.PredicateName);
		mapPatternToCheck.put(RubocopCop.Proc.toString(), RubocopCop.Proc);
		mapPatternToCheck.put(RubocopCop.RaiseArgs.toString(), RubocopCop.RaiseArgs);
		mapPatternToCheck.put(RubocopCop.RedundantBegin.toString(), RubocopCop.RedundantBegin);
		mapPatternToCheck.put(RubocopCop.RedundantException.toString(), RubocopCop.RedundantException);
		mapPatternToCheck.put(RubocopCop.RedundantReturn.toString(), RubocopCop.RedundantReturn);
		mapPatternToCheck.put(RubocopCop.RedundantSelf.toString(), RubocopCop.RedundantSelf);
		mapPatternToCheck.put(RubocopCop.RegexpLiteral.toString(), RubocopCop.RegexpLiteral);
		mapPatternToCheck.put(RubocopCop.RescueModifier.toString(), RubocopCop.RescueModifier);
		mapPatternToCheck.put(RubocopCop.SelfAssignment.toString(), RubocopCop.SelfAssignment);
		mapPatternToCheck.put(RubocopCop.Semicolon.toString(), RubocopCop.Semicolon);
		mapPatternToCheck.put(RubocopCop.SignalException.toString(), RubocopCop.SignalException);
		mapPatternToCheck.put(RubocopCop.SingleLineBlockParams.toString(), RubocopCop.SingleLineBlockParams);
		mapPatternToCheck.put(RubocopCop.SingleLineMethods.toString(), RubocopCop.SingleLineMethods);
		mapPatternToCheck.put(RubocopCop.SingleSpaceBeforeFirstArg.toString(), RubocopCop.SingleSpaceBeforeFirstArg);
		mapPatternToCheck.put(RubocopCop.SpaceAfterColon.toString(), RubocopCop.SpaceAfterColon);
		mapPatternToCheck.put(RubocopCop.SpaceAfterComma.toString(), RubocopCop.SpaceAfterComma);
		mapPatternToCheck.put(RubocopCop.SpaceAfterControlKeyword.toString(), RubocopCop.SpaceAfterControlKeyword);
		mapPatternToCheck.put(RubocopCop.SpaceAfterMethodName.toString(), RubocopCop.SpaceAfterMethodName);
		mapPatternToCheck.put(RubocopCop.SpaceAfterNot.toString(), RubocopCop.SpaceAfterNot);
		mapPatternToCheck.put(RubocopCop.SpaceAfterSemicolon.toString(), RubocopCop.SpaceAfterSemicolon);
		mapPatternToCheck.put(RubocopCop.SpaceAroundEqualsInParameterDefault.toString(), RubocopCop.SpaceAroundEqualsInParameterDefault);
		mapPatternToCheck.put(RubocopCop.SpaceAroundOperators.toString(), RubocopCop.SpaceAroundOperators);
		mapPatternToCheck.put(RubocopCop.SpaceBeforeBlockBraces.toString(), RubocopCop.SpaceBeforeBlockBraces);
		mapPatternToCheck.put(RubocopCop.SpaceBeforeModifierKeyword.toString(), RubocopCop.SpaceBeforeModifierKeyword);
		mapPatternToCheck.put(RubocopCop.SpaceInsideBlockBraces.toString(), RubocopCop.SpaceInsideBlockBraces);
		mapPatternToCheck.put(RubocopCop.SpaceInsideBrackets.toString(), RubocopCop.SpaceInsideBrackets);
		mapPatternToCheck.put(RubocopCop.SpaceInsideHashLiteralBraces.toString(), RubocopCop.SpaceInsideHashLiteralBraces);
		mapPatternToCheck.put(RubocopCop.SpaceInsideParens.toString(), RubocopCop.SpaceInsideParens);
		mapPatternToCheck.put(RubocopCop.SpecialGlobalVars.toString(), RubocopCop.SpecialGlobalVars);
		mapPatternToCheck.put(RubocopCop.StringLiterals.toString(), RubocopCop.StringLiterals);
		mapPatternToCheck.put(RubocopCop.SymbolArray.toString(), RubocopCop.SymbolArray);
		mapPatternToCheck.put(RubocopCop.Tab.toString(), RubocopCop.Tab);
		mapPatternToCheck.put(RubocopCop.TrailingBlankLines.toString(), RubocopCop.TrailingBlankLines);
		mapPatternToCheck.put(RubocopCop.TrailingComma.toString(), RubocopCop.TrailingComma);
		mapPatternToCheck.put(RubocopCop.TrailingWhitespace.toString(), RubocopCop.TrailingWhitespace);
		mapPatternToCheck.put(RubocopCop.TrivialAccessors.toString(), RubocopCop.TrivialAccessors);
		mapPatternToCheck.put(RubocopCop.UnlessElse.toString(), RubocopCop.UnlessElse);
		mapPatternToCheck.put(RubocopCop.UnneededCapitalW.toString(), RubocopCop.UnneededCapitalW);
		mapPatternToCheck.put(RubocopCop.VariableInterpolation.toString(), RubocopCop.VariableInterpolation);
		mapPatternToCheck.put(RubocopCop.VariableName.toString(), RubocopCop.VariableName);
		mapPatternToCheck.put(RubocopCop.WhenThen.toString(), RubocopCop.WhenThen);
		mapPatternToCheck.put(RubocopCop.WhileUntilDo.toString(), RubocopCop.WhileUntilDo);
		mapPatternToCheck.put(RubocopCop.WhileUntilModifier.toString(), RubocopCop.WhileUntilModifier);
		mapPatternToCheck.put(RubocopCop.WordArray.toString(), RubocopCop.WordArray);
		mapPatternToCheck.put(RubocopCop.ActionFilter.toString(), RubocopCop.ActionFilter);
		mapPatternToCheck.put(RubocopCop.DefaultScope.toString(), RubocopCop.DefaultScope);
		mapPatternToCheck.put(RubocopCop.Delegate.toString(), RubocopCop.Delegate);
		mapPatternToCheck.put(RubocopCop.HasAndBelongsToMany.toString(), RubocopCop.HasAndBelongsToMany);
		mapPatternToCheck.put(RubocopCop.Output.toString(), RubocopCop.Output);
		mapPatternToCheck.put(RubocopCop.ReadWriteAttribute.toString(), RubocopCop.ReadWriteAttribute);
		mapPatternToCheck.put(RubocopCop.ScopeArgs.toString(), RubocopCop.ScopeArgs);
		mapPatternToCheck.put(RubocopCop.Validation.toString(), RubocopCop.Validation);
		mapPatternToCheck.put(RubocopCop.Syntax.toString(), RubocopCop.Syntax);

		messageToKeyMap = Collections.unmodifiableMap(mapPatternToCheck);

		Map<String, String> mapKeyToSeverity = new HashMap<String, String>();
		mapKeyToSeverity.put("convention", Severity.MINOR);
		mapKeyToSeverity.put("warning", Severity.INFO);
		mapKeyToSeverity.put("refactor", Severity.MINOR);
		mapKeyToSeverity.put("error", Severity.CRITICAL);
		mapKeyToSeverity.put("fatal", Severity.BLOCKER);
		keyToSeverityMap = Collections.unmodifiableMap(mapKeyToSeverity);
	}

	public RubocopOffense(String file, String severity, String message, String copname, String corrected, int line, int column, int length) {
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
		return "file: " + file + " severity: " + severity + " message: " + message + " copname: " + copname + " corrected: " + corrected
				+ " line: " + line + " column: " + column + " length: " + length;
	}

	public static RubocopCop messageToKey(String message) {
		for (String s : messageToKeyMap.keySet()) {
			if (s.equalsIgnoreCase(message)) {
				return messageToKeyMap.get(s);
			}
		}
		return null;
	}

	/*
	 * public static String toSeverity(RubocopCop cop) { if
	 * (keyToSeverityMap.containsKey(check)) { return
	 * keyToSeverityMap.get(check); } return Severity.BLOCKER; // Make sure we
	 * catch this case. }
	 */

	public static String toSeverity(String check) {
		if (keyToSeverityMap.containsKey(check)) {
			return keyToSeverityMap.get(check);
		}
		return Severity.BLOCKER; // Make sure we catch this case.
	}
}
