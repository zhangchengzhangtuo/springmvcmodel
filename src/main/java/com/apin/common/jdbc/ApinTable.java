package com.apin.common.jdbc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2016/8/3.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ApinTable {
    /**
     * ±íÃû
     *
     * @return
     */
    public String name() default "";
}
