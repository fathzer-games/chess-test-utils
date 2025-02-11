package com.fathzer.chess.utils.test.helper;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;
import java.util.Optional;

public class Chess960Condition implements ExecutionCondition {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        Optional<Class<?>> testClass = context.getTestClass();

        if (testClass.isPresent()) {
            try {
                Object instance = context.getTestInstance().orElse(null);
                if (instance != null) {
                	
                    Method method = testClass.get().getDeclaredMethod("shouldRun");
                    method.setAccessible(true);
                    boolean result = (boolean) method.invoke(instance);
                    
                    if (result) {
                        return ConditionEvaluationResult.enabled("Test enabled by predicate");
                    } else {
                        return ConditionEvaluationResult.disabled("Test disabled by predicate");
                    }
                }
            } catch (Exception e) {
                return ConditionEvaluationResult.disabled("Error evaluating condition: " + e.getMessage());
            }
        }
        return ConditionEvaluationResult.enabled("No condition found, test enabled by default");
    }
}
