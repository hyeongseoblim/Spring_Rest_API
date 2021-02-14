package com.inflearn.study.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)//얼마나 오래 가져갈 것인가
public @interface TestDescription {

    String value();


}
