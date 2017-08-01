package com.vector.smartWeb.annonation;

import java.lang.annotation.*;

/**
 * Created by vector01.yao on 2017/8/1.
 * 切面注解
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    Class<? extends Annotation> value();
}
