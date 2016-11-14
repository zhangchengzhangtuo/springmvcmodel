package com.apin.common.jdbc;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/8/3.
 */
public class FieldMetaData {
    private String name;
    private Class<?> type;
    private Method readMethod;
    private Method writeMethod;
    private ApinColumn columnAnnotation;
    private boolean primaryKey = false;
    private String columnName;
    private boolean persistence = true;

    public String getColumnName() {
        return columnName;
    }

    /**
     * 约定：SQL查询列名统一为大写
     *
     * @param columnName
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : SqlBuilder.SQL_LOWERCASE ? columnName.toLowerCase() : columnName;
    }

    public boolean isPersistence() {
        return persistence;
    }

    public void setPersistence(boolean persistence) {
        this.persistence = persistence;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public ApinColumn getColumnAnnotation() {
        return columnAnnotation;
    }

    public Method getReadMethod() {
        return readMethod;
    }

    public Method getWriteMethod() {
        return writeMethod;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColumnAnnotation(ApinColumn columnAnnotation) {
        this.columnAnnotation = columnAnnotation;
    }

    public void setReadMethod(Method readMethod) {
        this.readMethod = readMethod;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    @Override
    public String toString() {
        return "\n\tname:[" + name + "],column:[" + columnName + "], primaryKey:[" + isPrimaryKey() +
                "], get:[" + (readMethod != null)
                + "], set:[" + (writeMethod != null) + "], type:[" + type + "]";
    }
}
