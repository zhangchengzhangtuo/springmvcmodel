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

import java.beans.IntrospectionException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类型转换工具
 *
 * @author <a href='zhangzaijun_slkj@126.com'>ZhangZaijun</a>
 * @version 1.0.1
 * @since 2015/7/17
 */
public class BeanCovertUtil {

    private static Logger logger = LoggerFactory.getLogger(BeanCovertUtil.class);

    /**
     * 返回转换工具
     *
     * @return --转换器实例
     */
    public static BeanCovertUtil getInstance() {
        return new BeanCovertUtil();
    }

    /**
     * 将Bean转换成Map并返回<BR>
     * 根据Bean的get方法来对Map填充数据
     *
     * @param obj --解析对象
     * @return --Map
     * @throws IntrospectionException --如果无法分析对象则抛出此异常
     */
    public Map<String, Object> covert(Object obj) throws IntrospectionException {
        if (obj == null) return null;
        return obj instanceof Map ? (Map) obj : this.covert(obj, BeanMetaData.getInstance(obj.getClass()), false);
    }

    /**
     * 将Bean转换成Map并返回<BR>
     * 根据Bean的get方法来对Map填充数据
     *
     * @param obj       --解析对象
     * @param humpNamed --是否驼峰命名
     * @return --Map
     * @throws IntrospectionException --如果无法分析对象则抛出此异常
     */
    public Map<String, Object> covert(Object obj, boolean humpNamed) throws IntrospectionException {
        if (obj == null) return null;
        return obj instanceof Map ? (Map) obj : this.covert(obj, BeanMetaData.getInstance(obj.getClass()), humpNamed);
    }

    /**
     * 将Bean转换成Map并返回<BR>
     * 根据Bean的get方法来对Map填充数据
     *
     * @param objectList --解析对象
     * @param humpNamed  --是否采用驼峰命名
     * @return -- List<Map<String,Object></String,Object>
     * @throws IntrospectionException --如果无法分析对象则抛出此异常
     */
    public List<Map<String, Object>> covert(List<Object> objectList, boolean humpNamed) throws IntrospectionException {
        if (objectList == null) return null;
        List<Map<String, Object>> result = new ArrayList<>();
        //
        BeanMetaData meta = null;
        for (Object bean : objectList) {
            if (bean == null) continue;
            if (meta == null) {
                meta = BeanMetaData.getInstance(bean.getClass());
            }
            //采用列表首位类型进行转换，暂时不考虑混合列表
            result.add(this.covert(bean, meta, humpNamed));
        }
        return result;
    }

    /**
     * 将Map转换成Bean并返回<BR>
     * 根据对象set方法查找Map数据来对返回Bean进行填充
     * 默认关闭驼峰命名
     *
     * @param tClass
     * @param data
     * @param <T>
     * @return
     * @throws IntrospectionException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public <T> T covert(Class<T> tClass, Map<String, ?> data) throws IntrospectionException, InstantiationException, IllegalAccessException {
        return this.covert(tClass, BeanMetaData.getInstance(tClass), data, false);
    }

    /**
     * 将Map转换成Bean并返回<BR>
     * 根据对象set方法查找Map数据来对返回Bean进行填充
     * 在对MAP进行搜索时同时搜索下划线和驼峰键
     *
     * @param tClass
     * @param data
     * @param humpNamed --驼峰命名
     * @return
     * @throws IntrospectionException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public <T> T covert(Class<T> tClass, Map<String, ?> data, boolean humpNamed) throws IntrospectionException, InstantiationException, IllegalAccessException {
        return this.covert(tClass, BeanMetaData.getInstance(tClass), data, humpNamed);
    }

    /**
     * 将Map转换成Bean并返回<BR>
     * 根据对象set方法查找Map数据来对返回Bean进行填充
     *
     * @param tClass
     * @param data
     * @param <T>
     * @return
     * @throws IntrospectionException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public <T> List<T> covert(Class<T> tClass, List<Map<String, Object>> data) throws IntrospectionException, InstantiationException, IllegalAccessException {
        if (tClass == null || data == null) {
            return null;
        }
        List<T> result = new ArrayList<>();
        BeanMetaData meta = BeanMetaData.getInstance(tClass);
        for (Map<String, Object> map : data) {
            result.add(this.covert(tClass, meta, map, false));
        }
        return result;
    }

    /**
     * 将map转为对象
     *
     * @param tClass    --对象类型
     * @param meta      --对象无数据
     * @param data      --数据
     * @param humpNamed --驼峰命名
     * @return --返回对象,如果任意参数为空则返回空
     * @throws IntrospectionException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected <T> T covert(Class<T> tClass, BeanMetaData meta, Map<String, ?> data, boolean humpNamed) throws IntrospectionException, InstantiationException, IllegalAccessException {
        if (tClass == null || meta == null || data == null) {
            return null;
        }
        //创建实例
        Object result = tClass.newInstance();
        int errCount = 0;
        for (FieldMetaData m : meta.getFieldMetaDataList()) {
            //map中没有查询的列
            if (!data.containsKey(humpNamed ? m.getName() : m.getColumnName())) {
                continue;
            }
            Method set = m.getWriteMethod();
            Object obj = humpNamed ? data.get(m.getName()) : data.get(m.getColumnName());
            if (set != null && obj != null) {
                Class type = set.getParameterTypes()[0];
                try {
                    if (!type.isInstance(obj)) {
                        //如果类型兼容则 则获取目标类型 构造 方法
                        Constructor constructor = null;
                        try {//如果目标类型存在该参数的构造方法则直接构造 目标对象
                            constructor = type.getConstructor(obj.getClass());
                            obj = constructor.newInstance(obj);//构造并指向新的对象
                        } catch (NoSuchMethodException e) {
                            //如果目标没有直接构造 函数 则尝试用String来代替
                            constructor = type.getConstructor(obj.toString().getClass());
                            obj = constructor.newInstance(obj.toString());
                        }
                        set.invoke(result, type.cast(obj));//强制类型转换
                    } else {
                        set.invoke(result, obj);
                    }
                } catch (Throwable e) {
                    //LoggerUtil.sendError(logger, e);
                    errCount++;
//                    logger.info(MsgUtil.getMsg("basic_util_covert_003", tClass, m.getName(), type, obj, obj.getClass()));
                }
            }
        }
        //如果调试模式则打印转换失败计数
        if (errCount > 0 && logger.isDebugEnabled()) {
//            logger.info(MsgUtil.getMsg("basic_util_covert_001", tClass.getName(), errCount));
        }
        return (T) result;
    }

    /**
     * 将Bean转换成Map并返回<BR>
     * 根据Bean的get方法来对Map填充数据
     *
     * @param data
     * @param meta
     * @return
     * @throws IntrospectionException
     */
    protected Map<String, Object> covert(Object data, BeanMetaData meta, boolean humpNamed) throws IntrospectionException {
        if (data == null || meta == null) {
            return null;
        }
        //创建map
        Map<String, Object> result = new HashMap();
        int errCount = 0;
        for (FieldMetaData m : meta.getFieldMetaDataList()) {
            Method get = m.getReadMethod();
            if (get != null) {
                try {
                    //调用set方法设置值
                    Object obj = get.invoke(data);
                    if (obj != null) result.put(humpNamed ? m.getName() : m.getColumnName(), obj);
                } catch (Throwable e) {
                    //do nothing
                    errCount++;
                    if (logger.isDebugEnabled()) {
//                        logger.debug(MsgUtil.getMsg("basic_util_covert_002", m.getName(), m.getColumnName(), e.getMessage()));
                    }
                }
            }
        }
        //如果调试模式则打印转换失败计数
        if (errCount > 0 && logger.isDebugEnabled()) {
//            logger.debug(MsgUtil.getMsg("basic_util_covert_001", data.getClass().getName(), errCount));
        }
        return result;
    }
}
