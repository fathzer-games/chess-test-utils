package com.fathzer.chess.utils.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExtensionContext;

class ExcludeMethodsConditionTest {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void test() throws Exception {
		final ExcludeMethodsCondition condition = new ExcludeMethodsCondition();
		final ExtensionContext context = mock(ExtensionContext.class);
		when(context.getElement()).thenReturn(Optional.empty());
		assertThrows(IllegalStateException.class, () -> condition.evaluateExecutionCondition(context));

		// 1 - Check always enabled on class elements
		when(context.getElement()).thenReturn(Optional.of(MyTestClass.class));
		ConditionEvaluationResult result = condition.evaluateExecutionCondition(context);
		assertFalse(result.isDisabled());

		// 2 - Check IllegalStateException on non class and non method elements
		when(context.getElement()).thenReturn(Optional.of(MyTestClass.class.getPackage()));
		assertThrows(IllegalStateException.class, () -> condition.evaluateExecutionCondition(context));
		when(context.getElement()).thenReturn(Optional.of(MyTestClass.class.getDeclaredField("field")));
		assertThrows(IllegalStateException.class, () -> condition.evaluateExecutionCondition(context));
		
		// 3 - Test in standard usage
		when(context.getRequiredTestClass()).thenReturn((Class)MyTestClass.class);
		
		// 3.1 - Non excluded method
		when(context.getElement()).thenReturn(Optional.of(MyParentTestClass.class.getDeclaredMethod("method2")));
		result = condition.evaluateExecutionCondition(context);
		assertFalse(result.isDisabled());
		
		// 3.2 - Excluded method
		when(context.getElement()).thenReturn(Optional.of(MyParentTestClass.class.getDeclaredMethod("method1")));
		result = condition.evaluateExecutionCondition(context);
		assertTrue(result.isDisabled());
		
		// 4 Test when excluded method does not exists
		when(context.getRequiredTestClass()).thenReturn((Class)MyTestClassWithUnknownExcluded.class);
		result = condition.evaluateExecutionCondition(context);
		assertFalse(result.isDisabled());
		
		// 5 Test when no excluded method
		when(context.getRequiredTestClass()).thenReturn((Class)MyParentTestClass.class);
		result = condition.evaluateExecutionCondition(context);
		assertFalse(result.isDisabled());
	}

	static class MyParentTestClass {
		void method1() {
			// Does nothing
		}
		
		void method2() {
			// Does nothing
		}
	}

	@ExcludeMethods("method1")
	private static class MyTestClass extends MyParentTestClass {
		@SuppressWarnings("unused")
		private int field = 0;
		
	}


	@ExcludeMethods("method3")
	private static class MyTestClassWithUnknownExcluded extends MyParentTestClass {
	}
}
