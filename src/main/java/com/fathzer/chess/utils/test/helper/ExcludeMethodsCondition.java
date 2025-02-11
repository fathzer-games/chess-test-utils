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

    private static final ConditionEvaluationResult ENABLED_IF_EXCLUDE_TAG_IS_INVALID =
            ConditionEvaluationResult.enabled(
                    "@ExcludeMethods does not have a valid method name to exclude, all tests will be run");

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(
            ExtensionContext context) {
        final AnnotatedElement element = context
                .getElement()
                .orElseThrow(IllegalStateException::new);
        final Optional<Set<String>> tagsToExclude = AnnotationUtils.findAnnotation(
                context.getRequiredTestClass(),
                ExcludeMethods.class
        )
        .map(a -> 
            Arrays.asList(a.value())
                    .stream()
                    .collect(Collectors.toSet())
        );
        if (!tagsToExclude.isPresent() || tagsToExclude.get().stream()
                .allMatch(s -> (s == null) || s.trim().isEmpty())) {
            return ENABLED_IF_EXCLUDE_TAG_IS_INVALID;
        }
        final Optional<String> tag = (element instanceof Method method) ? Optional.of(method.getName()) : Optional.empty();
        if (tagsToExclude.get().contains(tag.map(String::trim).orElse(""))) {
            return ConditionEvaluationResult
                    .disabled(String.format(
                            "test method \"%s\" has tag \"%s\" which is on the @ExcludeTags list \"[%s]\", test will be skipped",
                            (element instanceof Method method) ? method.getName()
                                    : element.getClass().getSimpleName(),
                            tag.get(),
                            tagsToExclude.get().stream().collect(Collectors.joining(","))
                    ));
        }
        return ConditionEvaluationResult.enabled(
                String.format(
                        "test method \"%s\" has no tag or tag \"%s\" which is not on the @ExcludeTags list \"[%s]\", test will be run",
                        (element instanceof Method method) ? method.getName()
                                : element.getClass().getSimpleName(),
                        tag.orElse("<no tag present>"),
                        tagsToExclude.get().stream().collect(Collectors.joining(","))
                ));
    }
    
    public static void main(String[] args) {
    	new ExcludeMethodsCondition() {
		};
    }
}