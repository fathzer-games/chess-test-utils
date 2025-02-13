package com.fathzer.chess.utils.test.helper;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.fathzer.chess.utils.model.TestAdapter;

/** An annotation that lists the required interfaces of the underlying {@link TestAdapter}
 * <br>This annotation should be used in tests that requires a {@link TestAdapter} to support additional interface.
*/
@Target( TYPE )
@Retention(RUNTIME)
public @interface Requires {
	/** Gets the supported variants.
	 * @return methods names
	 */
	Class<?>[] value();
}