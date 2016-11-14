package com.apin.common.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/3.
 */
public class SqlBuilder {
    private static Logger logger = LoggerFactory.getLogger(SqlBuilder.class);

    //����/�ֶ���������ʽ
    protected final String NAME_PATTEN = "[_a-zA-Z]{1}[_a-zA-Z0-9]{1,29}|[_a-zA-Z]{1}[_a-zA-Z0-9]{0,29}[\\.]{0,1}[_a-zA-Z]{1}[_a-zA-Z0-9]{0,29}";
    //�Ƿ�Ϊ��ѯ
    protected boolean select = false;
    //�Ƿ����ô�д
    public static final boolean SQL_LOWERCASE = true;
    //�߼������
    protected String logicHander = null;
    //����
    protected String columnName = null;
    //SQL
    protected StringBuilder sqlBuffer = new StringBuilder();
    //����
    protected List<Object> params = new ArrayList<Object>();

    private boolean debuge = false;
    public SqlBuilder eq;

    protected SqlBuilder() {
        this.debuge = logger.isDebugEnabled();
    }

    /**
     * ͨ��SQL��乹��������ѯ<BR>
     * �����������with��select��ͷ���Զ����'SELECT * FROM' ǰ׺
     *
     * @param query --��ѯ���
     * @return --mapper
     * @throws SQLException
     */
    public static SqlBuilder select(String query, Object... paras) throws SQLException {
        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder.sqlNotIsNull(query);
        sqlBuilder.select = true;
        String w = query.trim().split(" ")[0].toUpperCase();
        //��ʱ��֧��with
        if (w.indexOf("SELECT") != 0) {
            sqlBuilder.append("select * from");
        }
        sqlBuilder.append(query);
        for (Object obj : paras) {
            sqlBuilder.addParam(obj);
        }
        return sqlBuilder;
    }

    /**
     * ͨ��SQL��乹��������ѯ<BR>
     * �����������with��select��ͷ���Զ����'SELECT * FROM' ǰ׺
     *
     * @param query --��ѯ���
     * @return --mapper
     * @throws SQLException
     */
    public static SqlBuilder select(String query) throws SQLException {
        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder.sqlNotIsNull(query);
        sqlBuilder.select = true;
        String w = query.trim().split(" ")[0].toUpperCase();
        //��ʱ��֧��with
        if (w.indexOf("SELECT") != 0) {
            sqlBuilder.append("select * from");
        }
        sqlBuilder.append(query);
        return sqlBuilder;
    }


    /**
     * ����ʵ���ѯ<BR>
     * ���ʵ�����Բ�Ϊ������Ϊ���������ѯ
     *
     * @param bean
     * @return
     * @throws SQLException
     */
    public static SqlBuilder select(Object bean) throws SQLException, IntrospectionException {
        if (bean == null) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_012"));
        }
        SqlBuilder sqlBuilder = select(bean.getClass());
        sqlBuilder.select = true;
        BeanMetaData meta = BeanMetaData.getInstance(bean.getClass());
        boolean flg = false;
        for (FieldMetaData fm : meta.getFieldMetaDataList()) {
            try {
                if (!fm.isPersistence()) continue;
                Object value = fm.getReadMethod().invoke(bean);
                if (value != null) {
                    if (!flg) {
                        flg = true;
                        sqlBuilder.where(fm.getColumnName()).eq(value);
                    } else {
                        sqlBuilder.and(fm.getColumnName()).eq(value);
                    }
                }
            } catch (Throwable e) {
                //do nothing
                e.printStackTrace();
            }
        }
        return sqlBuilder;
    }

    /**
     * ����ʵ���ѯ<BR>
     * ���ʵ�����Բ�Ϊ������Ϊ���������ѯ
     *
     * @param tclass
     * @return
     * @throws SQLException
     */
    public static SqlBuilder select(Class tclass) throws SQLException, IntrospectionException {
        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder.select = true;
        BeanMetaData beanMetaData = BeanMetaData.getInstance(tclass);
        sqlBuilder.tableNameNotIsNull(beanMetaData.getTableName());
        boolean flg = false;
        sqlBuilder.append("select");
        for (FieldMetaData fm : beanMetaData.getFieldMetaDataList()) {
            if (!fm.isPersistence()) continue;
            if (!flg) {
                flg = true;
                sqlBuilder.append(fm.getColumnName());
            } else {
                sqlBuilder.sqlBuffer.append("," + fm.getColumnName());
            }
        }
        sqlBuilder.append("from");
        sqlBuilder.append(beanMetaData.getTableName());
        return sqlBuilder;
    }

    /**
     * ���ݱ�������ɾ�����<BR>
     * ��where ���ʹ��
     *
     * @param sql --ɾ���������
     * @return
     * @throws SQLException
     */
    public static SqlBuilder delete(String sql, Object... paras) throws SQLException {
        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder.sqlNotIsNull(sql);
        String[] strs = sql.trim().split(" ");
        String first = strs[0].toUpperCase();
        if (first.equals("DELETE")) {
            sqlBuilder.append(sql);
        } else {
            sqlBuilder.append("DELETE FROM " + sql);
        }
        for (Object o : paras) {
            sqlBuilder.addParam(o);
        }
        return sqlBuilder;
    }

    /**
     * ���ݱ�������ɾ�����<BR>
     * ��where ���ʹ��
     *
     * @param sql --ɾ���������
     * @return
     * @throws SQLException
     */
    public static SqlBuilder delete(String sql) throws SQLException {
        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder.sqlNotIsNull(sql);
        String[] strs = sql.trim().split(" ");
        String first = strs[0].toUpperCase();
        if (first.equals("DELETE")) {
            sqlBuilder.append(sql);
        } else {
            sqlBuilder.append("DELETE FROM " + sql);
        }
        return sqlBuilder;
    }

    /**
     * ���ݴ�����󹹽�ɾ�����<BR>
     * �����Ҫ����д�˷���ʵ��
     *
     * @param bean --ʵ�����
     * @return
     * @throws SQLException
     */
    public static SqlBuilder delete(Object bean) throws SQLException, IntrospectionException {
        if (bean == null) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_012"));
        }
        SqlBuilder sqlBuilder = new SqlBuilder();
        BeanMetaData metaData = BeanMetaData.getInstance(bean.getClass());
        sqlBuilder.tableNameNotIsNull(metaData.getTableName());
        Map<String, Object> map = new HashMap<String,Object>();
        int pkcount = 0;
        for (FieldMetaData fm : metaData.getFieldMetaDataList()) {
            if (!fm.isPersistence()) continue;
            Method get = fm.getReadMethod();
            if (get != null) {
                try {
                    Object o = get.invoke(bean);
                    if (o != null) {
                        map.put(fm.getColumnName(), o);
                        if (fm.isPrimaryKey()) {
                            pkcount++;
                        }
                    } else {
                        if (fm.isPrimaryKey()) {
//                            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_014", fm.getColumnName()));
                        }
                    }
                } catch (Throwable e) {
                    throw new SQLException(e);
                }
            }
        }
        //
        if (pkcount == 0) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_013"));
        } else {
            sqlBuilder.append("delete from").append(metaData.getTableName());
            boolean flg = false;
            for (String key : map.keySet()) {
                if (!flg) {
                    flg = true;
                    sqlBuilder.where(key).eq(map.get(key));
                } else {
                    sqlBuilder.and(key).eq(map.get(key));
                }
            }
        }
        return sqlBuilder;
    }

    /**
     * ���ݴ�����󹹽�ɾ�����<BR>
     * �����Ҫ����д�˷���ʵ��
     *
     * @param tclass --ʵ�����
     * @param id     --ʵ��ID
     * @return
     * @throws SQLException
     */
    public static SqlBuilder delete(Class tclass, Integer id) throws SQLException, IntrospectionException {
        if (tclass == null) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_012"));
        }
        SqlBuilder sqlBuilder = new SqlBuilder();
        BeanMetaData metaData = BeanMetaData.getInstance(tclass);
        sqlBuilder.tableNameNotIsNull(metaData.getTableName());
        sqlBuilder.sqlBuffer.append("DELETE FROM " + metaData.getTableName() + " WHERE ID=?");
        sqlBuilder.params.add(id);
        return sqlBuilder;
    }

    /**
     * ���ݱ��������������<BR>
     * ��set��where���ʹ��
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static SqlBuilder update(String sql, Object... paras) throws SQLException {
        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder.sqlNotIsNull(sql);
        String first = sql.trim().split(" ")[0].toUpperCase();
        if (!first.equals("UPDATE")) {
            sqlBuilder.append("update");
        }
        sqlBuilder.append(sql);
        for (Object obj : paras) {
            sqlBuilder.addParam(obj);
        }
        return sqlBuilder;
    }

    /**
     * ���ݱ��������������<BR>
     * ��set��where���ʹ��
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static SqlBuilder update(String sql) throws SQLException {
        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder.sqlNotIsNull(sql);
        String first = sql.trim().split(" ")[0].toUpperCase();
        if (!first.equals("UPDATE")) {
            sqlBuilder.append("update");
        }
        sqlBuilder.append(sql);
        return sqlBuilder;
    }

    /**
     * ���ݴ�����󹹽��������<BR>
     * ������������������Խ�������
     *
     * @param bean       --ʵ�����
     * @param ignoreNull --�Ƿ���Կ�ֵ
     * @return
     * @throws SQLException
     */
    public static SqlBuilder update(Object bean, boolean ignoreNull) throws SQLException, IntrospectionException {
        if (bean == null) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_012"));
        }
        SqlBuilder sqlBuilder = new SqlBuilder();
        BeanMetaData metaData = BeanMetaData.getInstance(bean.getClass());
        sqlBuilder.tableNameNotIsNull(metaData.getTableName());
        Map<String, Object> keyMap = new HashMap<String,Object>();
        Map<String, Object> setMap = new HashMap<String,Object>();
        for (FieldMetaData fm : metaData.getFieldMetaDataList()) {
            if (!fm.isPersistence()) continue;
            Method get = fm.getReadMethod();
            if (get != null) {
                try {
                    Object obj = get.invoke(bean);
                    if (obj != null || !ignoreNull) {
                        if (fm.isPrimaryKey()) {
                            keyMap.put(fm.getColumnName(), obj);
                        } else {
                            setMap.put(fm.getColumnName(), obj);
                        }
                    } else {
                        if (fm.isPrimaryKey()) {
//                            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_014", fm.getColumnName()));
                        }
                    }
                } catch (Throwable e) {
                    throw new SQLException(e);
                }
            }
        }
        //�����������Ϊ0
        if (keyMap.size() == 0) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_013"));
        } else {
            sqlBuilder.append("update").append(metaData.getTableName()).set(setMap);
            boolean flg = false;
            for (String key : keyMap.keySet()) {
                if (!flg) {
                    flg = true;
                    sqlBuilder.where(key).eq(keyMap.get(key));
                } else {
                    sqlBuilder.and(key).eq(keyMap.get(key));
                }
            }
        }
        return sqlBuilder;
    }

    /**
     * ���ݴ�����󹹽��������<BR>
     * ������������������Խ�������
     *
     * @param bean --ʵ�����
     * @return
     * @throws SQLException
     */
    public static SqlBuilder update(Object bean) throws SQLException, IntrospectionException {
        return SqlBuilder.update(bean, true);
    }

    /**
     * SQL��䲻��Ϊ��
     *
     * @throws SQLException
     */
    protected void sqlNotIsNull(String sql) throws SQLException {
        if (sql == null || sql.trim().length() == 0) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_001"));
        }
    }

    /**
     * ��������Ϊ��
     *
     * @throws SQLException
     */
    protected SqlBuilder setColumnName(String columnName) throws SQLException {
        if (columnName == null || columnName.trim().length() == 0) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_003"));
        }
        //
        this.columnName = columnName;
        return this;
    }

    /**
     * ��������Ϊ��
     *
     * @param str
     * @throws SQLException
     */
    protected SqlBuilder beforeColumnIsNotNull(String str) throws SQLException {
        if (this.columnName == null) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_006", str));
        }
        return this;
    }

    /**
     * ����Ϊ��ѯ ���
     *
     * @param builder
     * @throws SQLException
     */
    protected SqlBuilder mustSelectQuery(SqlBuilder builder) throws SQLException {
        if (!builder.isSelect()) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_007"));
        }
        return this;
    }

    /**
     * ��������Ϊ��
     *
     * @throws SQLException
     */
    protected SqlBuilder tableNameNotIsNull(String tableName) throws SQLException {
        if (tableName == null || tableName.trim().length() == 0) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_002"));
        }
        return this;
    }

    /**
     * ��������ʹ���߼������
     *
     * @throws SQLException
     */
    protected SqlBuilder setLogicHander(String hander) throws SQLException {
        if (this.logicHander != null) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_004", this.logicHander, logicHander));
        }
        this.logicHander = hander;
        return this;
    }

    /**
     * ���߼������ѹ��SQL BUFFER
     *
     * @throws SQLException
     */
    public SqlBuilder pushLogicHander() throws SQLException {
        if (this.logicHander == null) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_005"));
        }
        this.append(this.logicHander).logicHander = null;
        if (this.columnName != null) {
            this.append(this.columnName).columnName = null;
        }
        return this;
    }

    /**
     * �ͷ���������
     *
     * @return
     * @throws SQLException
     */
    public SqlBuilder releashLogicHander() throws SQLException {
        this.logicHander = null;
        this.columnName = null;
        return this;
    }

    /**
     * @param dataMap --
     * @return
     * @throws SQLException
     */
    public SqlBuilder set(Map<String, Object> dataMap) throws SQLException {
        if (this.sqlBuffer.toString().toUpperCase().indexOf("UPDATE") == -1) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_015"));
        }
        int i = 0;//������
        this.append("set");
        for (String name : dataMap.keySet()) {
            this.checkTableName(name);
            sqlBuffer.append(++i == 1 ? " " : ",");
            sqlBuffer.append(name + "=?");
            this.addParam(dataMap.get(name));
        }
        return this;
    }

    /**
     * ����SQL����ָ���������в���
     *
     * @param sql
     * @param values
     * @return
     * @throws SQLException --���SQL���Ϊ�����׳����쳣
     */
    public static SqlBuilder insert(String sql, Object... values) throws SQLException {
        SqlBuilder sqlBuilder = new SqlBuilder();
        if (sql == null || sql.trim().length() == 0) {
//            throw new SQLException(MsgUtil.getMsg("basic_mapper_insert_00"));
        }
        if (sqlBuilder.sqlBuffer.length() > 0) {
            sqlBuilder.sqlBuffer.append(";");
        }
        sqlBuilder.append(sql);
        for (Object obj : values) {
            sqlBuilder.addParam(obj);
        }
        return sqlBuilder;
    }

    /**
     * ����ʵ��<BR>
     * ���ʵ��ӳ�䵽��ͼ������ͼ�����ǿ���ִ�в����
     * �����Ҫ����д�˷���ʵ��
     *
     * @param objects --ʵ������б�
     * @return
     * @throws SQLException
     */
    public static SqlBuilder insert(Object... objects) throws SQLException {
        SqlBuilder sqlBuilder = new SqlBuilder();
        for (Object obj : objects) {
            try {
                BeanMetaData meta = BeanMetaData.getInstance(obj.getClass());
                int i = 0;
                StringBuilder builder = new StringBuilder("insert into " + meta.getTableName() + " ");
                List<Object> values = new ArrayList<Object>();
                for (FieldMetaData fm : meta.getFieldMetaDataList()) {
                    if (!fm.isPersistence()) continue;
                    Method get = fm.getReadMethod();
                    if (get != null) {
                        Object o = get.invoke(obj);
                        if (o != null) {
                            builder.append((++i == 1 ? "(" : ",") + fm.getColumnName());
                            values.add(o);
                        }
                    }
                }
                builder.append(")");
                //���ʵ�����û�а��κ�ֵ ���׳��쳣
                if (values.size() == 0) {
//                    throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_011"));
                }
                //append sql
                if (sqlBuilder.sqlBuffer.length() > 0) {
                    sqlBuilder.sqlBuffer.append(";");
                }
                sqlBuilder.append(builder.toString()).values(values.toArray());
            } catch (Throwable e) {
                throw new SQLException(e);
            }
        }
        return sqlBuilder;
    }

    /**
     * ֵ�б�<BR>
     * ���������ж���Ϊ��ʱ��Ȼ��NULL����ռλ
     *
     * @param values --VALUES����
     * @return
     * @throws SQLException --���������鳤��Ϊ0ʱ�׳����쳣
     */
    public SqlBuilder values(Object... values) throws SQLException {
        if (values.length == 0) {
//            throw new SQLException(MsgUtil.getMsg("basic_mapper_values_00"));
        }
        this.append("values(");
        for (int i = 0; i < values.length; i++) {
            sqlBuffer.append(i == 0 ? "?" : ",?");
            this.addParam(values[i]);
        }
        sqlBuffer.append(")");
        return this;
    }

    /**
     * ��ȡSQL���
     *
     * @return
     */
    public String getSql() throws SQLException {
        String sql;
        if (SQL_LOWERCASE) {
            StringBuilder sqlBuilder = new StringBuilder();
            //��һ�ո� ��ֹ ������ '' ʱ��ʧ
            String[] array = (this.sqlBuffer.toString() + " ").split("'");
            //����ƥ����
            if (array.length > 1 && (array.length + 1) % 2 != 0) {
//                throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_010"));
            }
            for (int i = 0; i < array.length; i++) {
                String str = array[i] == null ? "" : array[i];
                if ((i + 1) % 2 == 0) {
                    sqlBuilder.append("'" + str + "'");
                } else {
                    sqlBuilder.append(str.toLowerCase());
                }
            }
            sql = sqlBuilder.toString();
        } else {
            sql = this.sqlBuffer.toString();
        }
        //������Դ�,��ӡ������Ϣ
        if (logger.isDebugEnabled()) {
//            logger.debug(MsgUtil.getMsg("basic_util_sqlBuilder_018", sql, this.params.toString()));
        }
        return sql;
    }

    /**
     * ���ز����б�
     *
     * @return --�����б�
     */
    public Object[] getParameters() {
        return this.params.toArray();
    }


    /**
     * �жϵ�ǰ�������Ƿ�Ϊ��ѯ
     *
     * @return
     */
    public boolean isSelect() {
        return select;
    }

    /**
     * ������SQL��ֱ�Ӽ���SQLƬ��
     *
     * @param sql --Ҫ����Ƭ���б�
     * @return
     */
    public SqlBuilder append(String sql) throws SQLException {
        this.sqlNotIsNull(sql);
        //ƴ��SQLƬ��
        sqlBuffer.append(sqlBuffer.length() > 0 && sqlBuffer.charAt(sqlBuffer.length() - 1) != ' ' ? (" " + sql) : sql);
        return this;
    }

    /**
     * ��Ӳ���
     *
     * @param obj
     * @return
     */
    public SqlBuilder addParam(Object obj) {
        this.params.add(obj);
        return this;
    }

    /**
     * ������<BR>
     *
     * @param name
     * @throws SQLException
     */
    protected void checkTableName(String name) throws SQLException {
        if (name == null || !name.matches(NAME_PATTEN)) {
//            throw new SQLException(MsgUtil.getMsg("basic_mapper_insert_00"));
        }
    }

    /**
     * WHERE����<br>
     * �˷��������ж�����е�WHERE������б�������д�˷���ʵ�ּ��
     *
     * @return
     */
    public SqlBuilder where(String column) throws SQLException {
        return this.where().setColumnName(column);
    }

    /**
     * WHERE����<br>
     * �˷��������ж�����е�WHERE������б�������д�˷���ʵ�ּ��
     *
     * @return
     */
    public SqlBuilder where() throws SQLException {
        this.setLogicHander("where").columnName = null;
        return this;
    }


    /**
     * and ���
     *
     * @param column
     * @return
     */
    public SqlBuilder and(String column) throws SQLException {
        this.and();
        this.setColumnName(column);
        return this;
    }

    /**
     * ��
     *
     * @return
     */
    public SqlBuilder and() throws SQLException {
        this.setLogicHander("and");
        return this;
    }

    /**
     * ��
     *
     * @param column
     * @return
     */
    public SqlBuilder or(String column) throws SQLException {
        this.or();
        this.setColumnName(column);
        return this;
    }

    /**
     * ��
     *
     * @return
     */
    public SqlBuilder or() throws SQLException {
        this.setLogicHander("or");
        return this;
    }

    /**
     * IS NULL
     *
     * @return
     * @throws SQLException
     */
    public SqlBuilder isNull() throws SQLException {
        return this.beforeColumnIsNotNull("is null").pushLogicHander().append("is null");
    }

    /**
     * IS NOT NULL
     *
     * @return
     * @throws SQLException
     */
    public SqlBuilder isNotNull() throws SQLException {
        return this.beforeColumnIsNotNull("is not null").pushLogicHander().append("is not null");
    }

    /**
     * in
     * ��������ݲ����б�Ϊ���򹹽��Ӳ�ѯ
     *
     * @param paras
     * @return
     */
    public SqlBuilder in(Object... paras) throws SQLException {
        if (paras.length == 0) return this.releashLogicHander();
        this.beforeColumnIsNotNull("in");
        this.pushLogicHander();
        int i = 0;
        this.append("in(");
        for (Object obj : paras) {
            if (obj != null) {
                sqlBuffer.append(++i == 1 ? "?" : ",?");
                this.addParam(obj);
            }
        }
        sqlBuffer.append(")");
        return this;
    }

    /**
     * in
     *
     * @return
     * @throws SQLException
     */
    public SqlBuilder in(SqlBuilder builder) throws SQLException {
        this.beforeColumnIsNotNull("in");
        if (builder != null) {
            if (!builder.isSelect()) {
//                throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_007"));
            } else {
                this.pushLogicHander();
                this.append("in(" + builder.getSql() + ")");
                for (Object obj : builder.getParameters()) {
                    this.addParam(obj);
                }
            }
        }
        return this;
    }

    /**
     * in
     *
     * @return
     * @throws SQLException
     */
    public SqlBuilder in(String query) throws SQLException {
        this.in(SqlBuilder.select(query));
        return this;
    }

    /**
     * not in
     * ����Ϊ��ʱ�����Ӳ�ѯ
     *
     * @param paras
     * @return
     */
    public SqlBuilder notIn(Object... paras) throws SQLException {
        if (paras.length == 0) return this.releashLogicHander();
        this.beforeColumnIsNotNull("not in");
        this.pushLogicHander();
        this.append("not in(");
        int i = 0;
        for (Object obj : paras) {
            if (obj != null) {
                sqlBuffer.append(++i == 1 ? "?" : ",?");
                this.addParam(obj);
            }
        }
        sqlBuffer.append(")");
        return this;
    }

    /**
     * not in
     *
     * @return
     * @throws SQLException
     */
    public SqlBuilder notIn(SqlBuilder builder) throws SQLException {
        this.beforeColumnIsNotNull("not in");
        if (builder != null) {
            if (!builder.isSelect()) {
//                throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_007"));
            } else {
                this.pushLogicHander();
                this.append("not in(" + builder.getSql() + ")");
                for (Object obj : builder.getParameters()) {
                    this.addParam(obj);
                }
            }
        }
        return this;
    }

    /**
     * not in
     *
     * @return
     * @throws SQLException
     */
    public SqlBuilder notIn(String query) throws SQLException {
        this.beforeColumnIsNotNull("not in");
        this.pushLogicHander();
        this.append("not");
        this.logicHander = "";
        this.columnName = "";
        this.in(query);
        return this;
    }

    /**
     * Between And
     *
     * @param flg   --ռλ�����������ڽ���������ó�ͻ��
     * @param start --����
     * @param end   --����
     * @return
     * @throws SQLException
     */
    protected SqlBuilder betweenAnd(boolean flg, Object start, Object end) throws SQLException {
        if (start == null || end == null) return this.releashLogicHander();
        this.pushLogicHander();
        if (start == null || end == null) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_008"));
        }
        this.append("between").append("?").append("and").append("?");
        this.addParam(start).addParam(end);
        return this;
    }

    /**
     * between and
     *
     * @param start
     * @param end
     * @return
     * @throws SQLException
     */
    public SqlBuilder betweenAnd(java.util.Date start, java.util.Date end) throws SQLException {
        this.betweenAnd(true, start, end);
        return this;
    }

    /**
     * between and
     *
     * @param start
     * @param end
     * @return
     * @throws SQLException
     */
    public SqlBuilder betweenAnd(Date start, Date end) throws SQLException {
        this.betweenAnd(true, start, end);
        return this;
    }

    /**
     * between and
     *
     * @param start
     * @param end
     * @return
     * @throws SQLException
     */
    public SqlBuilder betweenAnd(Timestamp start, Timestamp end) throws SQLException {
        this.betweenAnd(true, start, end);
        return this;
    }

    /**
     * like����ʵ��
     *
     * @param value --ƥ���
     * @return
     * @throws SQLException
     */
    public SqlBuilder like(String value) throws SQLException {
        if (value == null) return this.releashLogicHander();
        return this.beforeColumnIsNotNull("like").pushLogicHander().append("like").append("'" + value + "'");
    }

    /**
     * not like ����ʵ��
     *
     * @param value
     * @return
     * @throws SQLException
     */
    public SqlBuilder notLike(String value) throws SQLException {
        if (value == null) return this.releashLogicHander();
        return this.beforeColumnIsNotNull("not like").pushLogicHander().append("not like").append("'" + value + "'");
    }

    /**
     * ����
     *
     * @return
     */
    public SqlBuilder eq(Object value) throws SQLException {
        if (value == null) return this.releashLogicHander();
        this.beforeColumnIsNotNull("eq").pushLogicHander().sqlBuffer.append("=?");
        return this.addParam(value);
    }

    /**
     * ������
     *
     * @return
     */
    public SqlBuilder ne(Object value) throws SQLException {
        if (value == null) return this.releashLogicHander();
        this.beforeColumnIsNotNull("ne").pushLogicHander().sqlBuffer.append("<>?");
        return this.addParam(value);
    }


    /**
     * ����
     *
     * @return
     */

    public SqlBuilder gt(Object value) throws SQLException {
        if (value == null) return this.releashLogicHander();
        this.beforeColumnIsNotNull("gt").pushLogicHander().sqlBuffer.append(">?");
        return this.addParam(value);
    }

    /**
     * ���ڵ���
     *
     * @return
     */
    public SqlBuilder ge(Object value) throws SQLException {
        if (value == null) return this.releashLogicHander();
        this.beforeColumnIsNotNull("ge").pushLogicHander().sqlBuffer.append(">=?");
        return this.addParam(value);
    }

    /**
     * С��
     *
     * @return
     */
    public SqlBuilder lt(Object value) throws SQLException {
        if (value == null) return this.releashLogicHander();
        this.beforeColumnIsNotNull("lt").pushLogicHander().sqlBuffer.append("<?");
        return this.addParam(value);
    }

    /**
     * С�ڵ���
     *
     * @return
     */
    public SqlBuilder le(Object value) throws SQLException {
        if (value == null) return this.releashLogicHander();
        this.beforeColumnIsNotNull("le").pushLogicHander().sqlBuffer.append("<=?");
        return this.addParam(value);
    }

    /**
     * exists<BR>
     * ����Ӳ�ѯΪ����ͨ���������󹹽�
     *
     * @return
     * @throws SQLException
     */
    public SqlBuilder exists(SqlBuilder builder) throws SQLException {
        this.mustSelectQuery(builder);
        this.pushLogicHander();
        this.append("exists(").sqlBuffer.append(builder.getSql());
        for (Object obj : builder.getParameters()) {
            this.addParam(obj);
        }
        return this.append(")");
    }

    /**
     * exists<BR>
     * ����Ӳ�ѯΪ����ͨ���������󹹽�
     *
     * @param query
     * @return
     * @throws SQLException
     */
    public SqlBuilder exists(String query) throws SQLException {
        return this.exists(SqlBuilder.select(query));
    }

    /**
     * not exists<BR>
     * ����Ӳ�ѯΪ����ͨ���������󹹽�
     *
     * @return
     * @throws SQLException
     */
    public SqlBuilder notExists(SqlBuilder builder) throws SQLException {
        this.mustSelectQuery(builder);
        this.pushLogicHander();
        this.append("not exists(").sqlBuffer.append(builder.getSql());
        for (Object obj : builder.getParameters()) {
            this.addParam(obj);
        }
        return this.append(")");
    }

    /**
     * not exists<BR>
     * ����Ӳ�ѯΪ����ͨ���������󹹽�
     *
     * @param query
     * @return
     * @throws SQLException
     */
    public SqlBuilder notExists(String query) throws SQLException {
        return this.notExists(SqlBuilder.select(query));
    }

    /**
     * ������MYSQL
     *
     * @param startRow --��ʼ��(Ĭ��0)
     * @param catchRow --��ȡ������Ĭ��50��
     */
    public SqlBuilder limit(int startRow, int catchRow) throws SQLException {
        if (!this.isSelect()) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_017", "limit"));
        }
        if (startRow < 0) startRow = 0;
        if (catchRow <= 0) catchRow = 50;
        this.append("limit ?,?");
        this.addParam(startRow);
        this.addParam(catchRow);
        return this;
    }

    /**
     * ������MYSQL
     *
     * @param startRow --��ʼ��
     * @throws SQLException
     */
    public SqlBuilder limit(int startRow) throws SQLException {
        if (!this.isSelect()) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_017", "limit"));
        }
        if (startRow < 0) startRow = 0;
        this.limit(startRow, 50);
        return this;
    }

    /**
     * @param columns --�����ֶ��б�
     * @return
     * @throws SQLException
     */
    public SqlBuilder orderBy(String... columns) throws SQLException {
        if (!this.isSelect()) {
//            throw new SQLException(MsgUtil.getMsg("basic_util_sqlBuilder_017", "order by"));
        }
        if (columns.length > 0) {
            this.append("order by ");
            for (int i = 0; i < columns.length; i++) {
                this.sqlBuffer.append(i == 0 ? columns[i] : "," + columns[i]);
            }
        }
        return this;
    }
}
