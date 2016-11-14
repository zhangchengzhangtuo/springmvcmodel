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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author <a href='zhangzaijun_slkj@126.com'>ZhangZaijun</a>
 * @version 1.0.1
 * @since 2015/7/18
 */
@Component
public class ApinMapper {

    private static Logger logger = LoggerFactory.getLogger(ApinMapper.class);

    @Autowired
    protected ApinJdbcTemplate apinJdbcTemplate;

    @Autowired
    @Qualifier("batchDataSource")
    protected DataSource batchDatasource;

    /**
     * SQL 构建器不能为空
     *
     * @param builder
     * @throws SQLException
     */
    protected ApinMapper sqlBuilderIsNotNull(SqlBuilder builder) throws SQLException {
        if (builder == null) {
//            throw new SQLException(MsgUtil.getMsg("basic_mapper_apinMapper_001"));
        }
        return this;
    }

    /**
     * 查找对象并以Map返回,如果匹配多个对象则返回第一个
     *
     * @param builder
     * @return --以Map表示的对象属性
     * @throws Throwable
     */
    public Map<String, Object> find(SqlBuilder builder) throws Throwable {
        return this.sqlBuilderIsNotNull(builder).apinJdbcTemplate.select(builder.getSql(), builder.getParameters());
    }

    /**
     * 查的对象并以指定类型返回,如果匹配多个对象则返回第一个
     *
     * @param builder
     * @param tclass
     * @param <T>
     * @return
     * @throws Throwable
     */
    public <T> T find(Class<T> tclass, SqlBuilder builder) throws Throwable {
        return this.sqlBuilderIsNotNull(builder).apinJdbcTemplate.select(tclass, builder.getSql(), builder.getParameters());
    }

    /**
     * 查的对象并以指定类型返回,如果匹配多个对象则返回第一个
     *
     * @param tclass
     * @param bean
     * @param <T>
     * @return
     * @throws Throwable
     */
    public <T> T find(Class<T> tclass, Object bean) throws Throwable {
        SqlBuilder builder = SqlBuilder.select(bean);
        return this.find(tclass, builder);
    }

    /**
     * 查找对象列表
     *
     * @param builder
     * @return
     * @throws Throwable
     */
    public List<Map<String, Object>> findAll(SqlBuilder builder) throws Throwable {
        return this.sqlBuilderIsNotNull(builder).apinJdbcTemplate.selectList(builder.getSql(), builder.getParameters());
    }

    /**
     * 查找对象列表
     *
     * @param builder
     * @param tclass
     * @param <T>
     * @return
     * @throws Throwable
     */
    public <T> List<T> findAll(Class<T> tclass, SqlBuilder builder) throws Throwable {
        return this.sqlBuilderIsNotNull(builder).apinJdbcTemplate.selectList(tclass, builder.getSql(), builder.getParameters());
    }

    /**
     * 保存更改
     *
     * @param builders --SQL构建器
     * @return --受影响行数，多条SQL时返回总的行数
     * @throws Throwable
     */
    public int save(SqlBuilder... builders) throws Throwable {
        int row = 0;
        for (SqlBuilder builder : builders) {
            this.sqlBuilderIsNotNull(builder);
            if (builder.isSelect()) {
//                throw new SQLException(MsgUtil.getMsg("basic_mapper_apinMapper_002"));
            }
            row += apinJdbcTemplate.update(builder.getSql(), builder.getParameters());
        }
        return row;
    }


    /**
     * 保存更改
     * 批量保存对象，不受代理事务影响
     *
     * @param commitCount --事务提交间隔
     * @param builders    --SQL构建器
     * @return --受影响行数，多条SQL时返回总的行数
     * @throws Throwable
     */
    public int save(Integer commitCount, SqlBuilder... builders) throws Throwable {
        if (builders.length == 0) return 0;
        if (commitCount <= 0) commitCount = 5000;
        Connection conn = this.batchDatasource.getConnection();
        int totalEffRow = 0;
        int count = 0;
        try {
            conn.setAutoCommit(false);//关闭自动事务提交
            for (SqlBuilder builder : builders) {
                if (builder != null) {
                    PreparedStatement stm = conn.prepareStatement(builder.getSql());
                    int j = 0;
                    for (Object obj : builder.getParameters()) {
                        stm.setObject(++j, obj);
                    }
                    totalEffRow += stm.executeUpdate();
                    if (++count % commitCount == 0 || count >= builders.length) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("已经应用记录:[" + count + "],影响记录:[" + totalEffRow + "]");
                        }
                        conn.commit();
                        count = 0;
                    }
                }
            }
        } finally {
            DataSourceUtils.releaseConnection(conn, this.batchDatasource);
        }
        return totalEffRow;
    }

    /**
     * 保存对象
     *
     * @param beans --保存
     * @return --受影响行数，多条SQL时返回总的行数
     * @throws Throwable
     */
    public int save(Object... beans) throws Throwable {
        int i = 0;
        for (Object bean : beans) {
            if (bean != null) {
                i += apinJdbcTemplate.insert(bean);
            }
        }
        return i;
    }

    /**
     * 保存对象
     * 批量保存对象，不受代理事务影响
     *
     * @param beans --保存对象
     * @return --受影响行数，多条SQL时返回总的行数
     * @throws Throwable
     */
    public int save(Integer commitCount, Object... beans) throws Throwable {
        if (beans.length == 0) return 0;
        if (commitCount <= 0) commitCount = 5000;
        Connection conn = this.batchDatasource.getConnection();
        int totalEffRow = 0;
        int count = 0;
        try {
            conn.setAutoCommit(false);//关闭自动事务提交
            for (Object bean : beans) {
                if (bean != null) {
                    SqlBuilder builder = SqlBuilder.insert(bean);
                    PreparedStatement stm = conn.prepareStatement(builder.getSql());
                    int j = 0;
                    for (Object obj : builder.getParameters()) {
                        stm.setObject(++j, obj);
                    }
                    totalEffRow += stm.executeUpdate();
                    if (++count % commitCount == 0 || count >= beans.length) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("已经应用记录:[" + count + "],影响记录:[" + totalEffRow + "]");
                        }
                        conn.commit();
                        count = 0;
                    }
                }
            }
        } finally {
            DataSourceUtils.releaseConnection(conn, this.batchDatasource);
        }
        return totalEffRow;
    }


    /**
     * 保存更改
     *
     * @param builder
     * @return
     * @throws Throwable
     */
    public int[] save(SqlBuilder builder, List<Object[]> batchArray) throws Throwable {
        this.sqlBuilderIsNotNull(builder);
        if (builder.isSelect()) {
//            throw new SQLException(MsgUtil.getMsg("basic_mapper_apinMapper_002"));
        }
        return this.apinJdbcTemplate.batchUpdate(builder.getSql(), batchArray);
    }
}
