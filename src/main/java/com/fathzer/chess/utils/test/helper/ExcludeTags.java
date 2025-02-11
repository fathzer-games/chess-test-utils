package com.fathzer.chess.utils.test.helper;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/** An annotation to exclude the methods tagged with the specified tags from a test class. */
@Target(TYPE)
@Retention(RUNTIME)
@ExtendWith(ExcludeTagsCondition.class)
public @interface ExcludeTags {
    /** Gets the tags to exclude.
     * @return tags names
     */
    String[] value();
}