package com.fathzer.chess.utils.test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

class TagOnTestMethodsTest {
	@Test
	void test() {
		final Reflections reflections = new Reflections("com.fathzer.chess.utils.test", new SubTypesScanner(false));
		reflections.getSubTypesOf(Object.class).stream()
				.filter(o -> AbstractAdaptableTest.class.isAssignableFrom(o) && !o.equals(AbstractAdaptableTest.class))
				.forEach(this::doTest);
	}

	private void doTest(Class<?> c) {
		final Method[] methods = c.getDeclaredMethods();
		final String classNamePrefix = c.getSimpleName() + ".";
        boolean noTest = true;
		for (Method m : methods) {
			if (m.getAnnotation(Test.class) != null) {
                noTest = false;
				final List<String> tags = Arrays.stream(m.getAnnotationsByType(Tag.class)).map(Tag::value).toList();
				assertFalse(tags.isEmpty(), String.format("Method %s of class %s should have a @Tag annotation", m, c));
				final String expectedTagName = classNamePrefix + m.getName();
				final Optional<String> found = tags.stream().filter(expectedTagName::equals).findAny();
				assertTrue(found.isPresent(), String.format("Method %s of class %s has an incorrect @tag annotation (%s)", m, c, tags));
            }
		}
        assertFalse(noTest, String.format("Class %s should have at least one test method", c));
	}
}
