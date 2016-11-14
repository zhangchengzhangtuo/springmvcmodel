package com.apin.common.jdbc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2016/8/3.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ApinColumn {


    /**
     * ����
     *
     * @return
     */
    String name() default "";

    /**
     * �Ƿ�Ϊ����
     *
     * @return
     */
    boolean isPrimaryKey() default false;

    /**
     * �Ƿ�־û�
     *
     * @return
     */
    boolean persistence() default true;

    /**
     * �Ƿ��Ϊ��
     */
    boolean isNullable() default true;

    /**
     * java����
     * @return
     */
    String javaType() default "";

}
