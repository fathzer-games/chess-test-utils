package com.fathzer.chess.utils.test.helper;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.util.AnnotationUtils;

class ExcludeMethodsCondition implements ExecutionCondition {

	private static final ConditionEvaluationResult ENABLED_IF_EXCLUDE_TAG_IS_INVALID = ConditionEvaluationResult.enabled(
					"@ExcludeMethods does not have a valid method name to exclude, all tests will be run");
	private static final ConditionEvaluationResult ENABLED_IF_CLASS = ConditionEvaluationResult.enabled(
					"@ExcludeMethods does not exclude classes");

	@Override
	public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
		final AnnotatedElement element = context.getElement().orElseThrow(IllegalStateException::new);
		if (element instanceof Class) {
			return ENABLED_IF_CLASS;
		} else if (!(element instanceof Method)) {
			throw new IllegalStateException("This method should not be called for non-method and non-class elements");
		}
		final Method method = (Method) element;

		final Class<?> testClass = context.getRequiredTestClass();
		final Optional<Set<String>> methodsToExclude = AnnotationUtils.findAnnotation(
				testClass,ExcludeMethods.class)
		.map(a -> 
			Arrays.asList(a.value())
					.stream()
					.collect(Collectors.toSet())
		);
		if (!methodsToExclude.isPresent() || methodsToExclude.get().stream()
				.allMatch(s -> (s == null) || s.trim().isEmpty())) {
			return ENABLED_IF_EXCLUDE_TAG_IS_INVALID;
		}
		final String methodName = method.getName();
		if (methodsToExclude.get().contains(methodName)) {
			return ConditionEvaluationResult.disabled(String.format(
					"Test method \"%s\" which is on the @ExcludeMethods list \"[%s]\", test will be skipped",
					methodName,
					methodsToExclude.get().stream().collect(Collectors.joining(","))
			));
		}
		return ConditionEvaluationResult.enabled(String.format(
				"Test method \"%s\" is not on the @ExcludeMethods list \"[%s]\", test will be run",
				methodName,
				methodsToExclude.get().stream().collect(Collectors.joining(","))
		));
	}
}