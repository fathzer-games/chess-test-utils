package com.fathzer.chess.utils.test;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

import com.fathzer.chess.utils.model.Variant;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(VariantCondition.class)
@interface IfVariantSupported {
	Variant value();
}
