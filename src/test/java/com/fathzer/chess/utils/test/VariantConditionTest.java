package com.fathzer.chess.utils.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.fathzer.chess.utils.model.Variant;

class VariantConditionTest {

	@Test
	void test() throws Exception {
		// 1 - Check always enabled on empty context
        final VariantCondition condition = new VariantCondition();
		final ExtensionContext context = mock(ExtensionContext.class);
		when(context.getTestClass()).thenReturn(Optional.empty());
		when(context.getTestMethod()).thenReturn(Optional.empty());
		assertFalse(condition.evaluateExecutionCondition(context).isDisabled());
		when(context.getTestClass()).thenReturn(Optional.of(MyTestClass.class));
		assertFalse(condition.evaluateExecutionCondition(context).isDisabled());

		// 2 - Check ok on non annotated method
		when(context.getTestMethod()).thenReturn(Optional.of(MyTestClass.class.getDeclaredMethod("noAnnotation")));
		assertFalse(condition.evaluateExecutionCondition(context).isDisabled());

		when(context.getTestMethod()).thenReturn(Optional.of(MyTestClass.class.getDeclaredMethod("annotated")));
		
		// 3 - Check ok on annotated method with no instance class
		when(context.getTestInstance()).thenReturn(Optional.empty());
		assertFalse(condition.evaluateExecutionCondition(context).isDisabled());

		MyTestClass instance = new MyTestClass();
		when(context.getTestInstance()).thenReturn(Optional.of(instance));
		// 4 - Check ko on annotated method when instance class is not null and isSupported throws an exception
		instance.isChess960Supported = null;
		assertTrue(condition.evaluateExecutionCondition(context).isDisabled());

		// 5 - Check ok on standard situation
		instance.isChess960Supported = true;
		assertFalse(condition.evaluateExecutionCondition(context).isDisabled());

		// 6 - Check ko on standard situation
		instance.isChess960Supported = false;
		assertTrue(condition.evaluateExecutionCondition(context).isDisabled());
	}

	@SuppressWarnings("unused")
	private static class MyTestClass {
		private Boolean isChess960Supported;
		
		void noAnnotation() {
			// Does nothing
		}

		@IfVariantSupported(Variant.CHESS960)
		void annotated() {
			// Does nothing
		}

		public boolean isSupported(Variant variant) {
			return isChess960Supported || variant != Variant.CHESS960;
		}
	}
}
