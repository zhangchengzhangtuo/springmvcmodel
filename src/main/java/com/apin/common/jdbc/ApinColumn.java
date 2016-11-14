package com.apin.common.jdbc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2016/8/3.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ApinColumn {


    /**
     * 列名
     *
     * @return
     */
    String name() default "";

    /**
     * 是否为主键
     *
     * @return
     */
    boolean isPrimaryKey() default false;

    /**
     * 是否持久化
     *
     * @return
     */
    boolean persistence() default true;

    /**
     * 是否可为空
     */
    boolean isNullable() default true;

    /**
     * java类型
     * @return
     */
    String javaType() default "";

}
