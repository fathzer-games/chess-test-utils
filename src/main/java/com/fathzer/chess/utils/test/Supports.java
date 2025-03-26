package com.fathzer.chess.utils.test;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.fathzer.chess.utils.model.TestAdapter;
import com.fathzer.chess.utils.model.Variant;

/** An annotation that lists supported chess variants.
 * <br>This annotation should be used to declare a {@link TestAdapter} supports non standard variants.
*/
@Target( TYPE )
@Retention(RUNTIME)
public @interface Supports {
	/** Gets the supported variants.
	 * @return methods names
	 */
	Variant[] value();
}