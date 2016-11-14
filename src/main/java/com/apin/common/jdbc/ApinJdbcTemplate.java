/**************************************************************************
 * Copyright (c) 2013-2023  杭州爱拼机网络科技有限公司
 * All rights reserved.
 * <p/>
 * 项目名称：apin-back-interface
 * 版权说明：本软件属杭州爱拼机网络科技有限公司所有，在未获杭州爱拼机网络科技有限公司正式授权
 * 情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受
 * 知识产权保护的内容。
 ***************************************************************************/
package com.apin.common.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href='zhangzaijun_slkj@126.com'>ZhangZaijun</a>
 * @version 1.0.1
 * @since 2015/7/16
 */
public class ApinJdbcTemplate extends JdbcTemplate {

    private static Logger logger = LoggerFactory.getLogger(ApinJdbcTemplate.class);

    public ApinJdbcTemplate() {
        super();
        init();
    }

//    private Connection conn=null;
//    private PreparedStatement stm = null;

    public ApinJdbcTemplate(DataSource dataSource) {
        super(dataSource);
        init();
    }

    public ApinJdbcTemplate(DataSource dataSource, boolean lazyInit) {
        super(dataSource, lazyInit);
        init();
    }

    /**
     * 初始化部分参数
     */
    private void init() {
        //默认抓取行数50
        this.setFetchSize(50);
        //默认返回最大行数9999
        this.setMaxRows(9999);
        //设置默认超时为一分钟
        this.setQueryTimeout(60);
    }

    /**
     * 查询列表
     *
     * @param sql   --查询语句
     * @param param --参数
     * @return --数据列表
     * @throws SQLException --抛出异常，如果执行查询失败
     */
    public List<Map<String, Object>> selectList(String sql, Object... param) throws Throwable {
        Connection conn=null;
        try {
            conn = DataSourceUtils.doGetConnection(this.getDataSource());
            ResultSet resultSet = this.executeQuery(conn,sql, param);
            List<Map<String, Object>> data = this.readList(resultSet);
            return data;
        } catch (Throwable e) {
            throw e;
        }finally {
           if(null!=conn){
               DataSourceUtils.releaseConnection(conn, this.getDataSource());
           }
        }
    }

    /**
     * 释放资源
     */
//    private void release() throws SQLException {
//        if (this.conn != null) {
//            DataSourceUtils.releaseConnection(this.conn, this.getDataSource());
//        }
//    }

    /**
     * 查询列表<BR>
     * 期间会进行一次数据复制，如有必要请重写此方法
     *
     * @param tClass --返回对象类型
     * @param sql    --查询语句
     * @param param  --参数
     * @return --数据列表
     * @throws Throwable --抛出异常，如果执行SQL失败或对象转换失败
     */
    public <T> List<T> selectList(Class<T> tClass, String sql, Object... param) throws Throwable {
        return BeanCovertUtil.getInstance().covert(tClass, this.selectList(sql, param));
    }

    /**
     * 执行SQL查询
     *
     * @param sql   --查询语句
     * @param param --参数列表
     * @return --返回MAP表示的对象
     * @throws SQLException
     */
    public Map<String, Object> select(String sql, Object... param) throws Throwable {
        Connection conn=null;
        try {
            conn = DataSourceUtils.doGetConnection(this.getDataSource());
            ResultSet resultSet = this.executeQuery(conn,sql, param);
            Map<String, Object> data = this.readMap(resultSet, this.getColumnNames(resultSet));
            return data;
        } catch (Throwable e) {
            throw e;
        }finally {
            if(null!=conn){
                DataSourceUtils.releaseConnection(conn, this.getDataSource());
            }
        }
    }

    /**
     * 查询并返回指定类型的对象
     *
     * @param tClass --对象类型
     * @param sql    --查询语句
     * @param paras  --参数列表
     * @return --转换后的对象
     * @throws Throwable --抛出异常，如果执行SQL失败或转换失败
     */
    public <T> T select(Class<T> tClass, String sql, Object... paras) throws Throwable {
        return BeanCovertUtil.getInstance().covert(tClass, this.select(sql, paras));
    }


    /**
     * @param sql   --查询语句
     * @param param --参数列表
     * @return --查询结果集
     * @throws SQLException --
     */
    protected ResultSet executeQuery(Connection conn,String sql, Object... param) throws Throwable {
        if (sql == null) {
//            throw new SQLException(MsgUtil.getMsg("basic_advice_basic_0104"));
        }
        PreparedStatement stm;
        try {
            stm = conn.prepareStatement(sql);
            stm.setQueryTimeout(this.getQueryTimeout());
            stm.setFetchSize(this.getFetchSize());
            if (stm.getParameterMetaData().getParameterCount() != param.length) {
//                throw new SQLException(MsgUtil.getMsg("basic_mapper_jdbc_001"));
            }
            //
            for (int i = 0; i < param.length; i++) {
                stm.setObject(i + 1, param[i]);
            }
            return stm.executeQuery();
        } catch (SQLException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e.getMessage());
            }
            throw e;
        }
    }

    /**
     * 获取下一个Map
     *
     * @param result  --结果集对象
     * @param columns --列名
     * @return --返回MAP
     * @throws SQLException
     */
    protected Map<String, Object> readMap(ResultSet result, String... columns) throws Throwable {
        if (result == null || !result.next()) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < columns.length; i++) {
            map.put(columns[i], result.getObject(i + 1));
        }
        return map;
    }

    /**
     * 返回列名
     * 约定：查询结果列名统一为大写
     *
     * @return --如果结果集为空则返回空
     */
    protected String[] getColumnNames(ResultSet result) throws Throwable {
        if (result == null) {
            return null;
        }
        ResultSetMetaData metaData = result.getMetaData();
        String[] columns = new String[metaData.getColumnCount()];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = metaData.getColumnLabel(i + 1).toLowerCase();
        }
        return columns;
    }

    /**
     * 读取并返回当前值
     *
     * @param result --返回list
     * @return --如果结果集为空则返回空
     * @throws SQLException
     */
    protected List<Map<String, Object>> readList(ResultSet result) throws Throwable {
        if (result == null) return null;
        //返回结果集
        List<Map<String, Object>> list = new ArrayList<>();
        String[] columns = this.getColumnNames(result);
        Map<String, Object> data;
        int row = 0;
        int max = this.getMaxRows();
        while ((data = this.readMap(result, columns)) != null && ++row <= max) {
            list.add(data);
        }
        return list;
    }

    /**
     * 插入对象 并 通过 GeneratedKeys 返回ID
     * bean必须存在setId方法 否则报错
     *
     * @param bean --要保存的BEAN
     * @return --对象ID
     * @throws Throwable
     */
    public int insert(final Object bean) throws Throwable {
        if (bean == null) throw new SQLException("Save bean is not be null!");
        Method method = bean.getClass().getMethod("setId", Integer.class);
        if (method == null) {
            throw new SQLException("The bean has not method setId!");
        }
        final SqlBuilder sqlBuilder = SqlBuilder.insert(bean);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowCount = this.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sqlBuilder.getSql(), Statement.RETURN_GENERATED_KEYS);
                int i = 1;
                for (Object obj : sqlBuilder.getParameters()) {
                    ps.setObject(i++, obj);
                }
                return ps;
            }
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        method.invoke(bean, id);
        return rowCount;
    }

}
