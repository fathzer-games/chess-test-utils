package com.fathzer.chess.utils.test;

import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.fathzer.chess.utils.model.Variant;

class VariantCondition implements ExecutionCondition {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        Optional<Class<?>> testClass = context.getTestClass();
        Optional<Method> testMethod = context.getTestMethod();

        if (testClass.isPresent() && testMethod.isPresent()) {
            IfVariantSupported annotation = testMethod.get().getAnnotation(IfVariantSupported.class);
            if (annotation != null) {
                Variant variant = annotation.value();
                try {
                    Object instance = context.getTestInstance().orElse(null);
                    if (instance != null) {
                        Method method = testClass.get().getMethod("isSupported", Variant.class);
                        final Object invoke = method.invoke(instance, variant);
						boolean result = (boolean) invoke;

                        if (result) {
                            return ConditionEvaluationResult.enabled("Test enabled by annotation");
                        } else {
                            return ConditionEvaluationResult.disabled("Test disabled because variant " + variant + " is not supported");
                        }
                    }
                } catch (Exception e) {
                    return ConditionEvaluationResult.disabled("Error evaluating condition: " + e.getMessage());
                }
            }
        }
        return ConditionEvaluationResult.enabled("No condition annotation found, test enabled by default");
    }
}
