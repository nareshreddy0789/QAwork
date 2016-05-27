package com.apple.carnival.qa.coreframework.dataproviders;

import java.lang.annotation.Retention;


@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Data {
	
	public String dataFile() default "";

}
