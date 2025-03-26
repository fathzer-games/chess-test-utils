package com.fathzer.chess.utils.test;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/** An annotation to exclude some methods from a test class. */
@Target( TYPE )
@Retention(RUNTIME)
@ExtendWith(ExcludeMethodsCondition.class)
public @interface ExcludeMethods {
	/** Gets the names of the methods to exclude.
	 * @return methods names
	 */
	String[] value();
}