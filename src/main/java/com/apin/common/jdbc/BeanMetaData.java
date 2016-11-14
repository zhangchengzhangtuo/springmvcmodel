package com.apin.common.jdbc;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/3.
 */
public class BeanMetaData {
    private String name;
    private String tableName;
    private Class type;
    private List<FieldMetaData> fieldMetaDataList;
    //�Ƿ�Ϊ�շ�������
    private boolean humpNamed = true;

    /**
     * ˽�л����췽��
     */
    private BeanMetaData() {
    }

    /**
     * ����ClassRoom��ȡBeanԪ����
     *
     * @param tclass
     * @return
     * @throws IntrospectionException
     */
    public static BeanMetaData getInstance(Class tclass) throws IntrospectionException {
        if (tclass == null) return null;
        BeanMetaData b = new BeanMetaData();
        b.setName(tclass.getSimpleName());
        b.setType(tclass);
        b.setFieldMetaDataList(b.getFieldMetaDataList(tclass));
        Object ano = tclass.getAnnotation(ApinTable.class);
        if (ano != null) {
            ApinTable tab = (ApinTable) ano;
            b.setTableName(tab.name());
        }
        return b;
    }

    public boolean isHumpNamed() {
        return humpNamed;
    }

    public void setHumpNamed(boolean humpNamed) {
        this.humpNamed = humpNamed;
    }

    /**
     * ��������
     *
     * @param meta --����Ԫ����
     * @return
     */
    public void setColumnName(FieldMetaData meta) {
        if (meta == null) return;
        ApinColumn column = meta.getColumnAnnotation();
        if (column == null || column.name().trim().length() == 0) {
            if (humpNamed) {
                char[] car = meta.getName().trim().toCharArray();
                StringBuilder cname = new StringBuilder();
                for (char c : car) {
                    cname.append((c >= 'A' && c <= 'Z') ? ("_" + (char) (c + 32)) : c);
                }
                meta.setColumnName(cname.toString());
            } else {
                meta.setColumnName(meta.getName());
            }
        } else {
            meta.setColumnName(column.name());
        }
    }

    /**
     * ��ȡ����Ԫ����
     *
     * @param tclass
     * @return
     * @throws IntrospectionException
     */
    protected List<FieldMetaData> getFieldMetaDataList(Class tclass) throws IntrospectionException {
        List<FieldMetaData> list = new ArrayList<FieldMetaData>();
        for (PropertyDescriptor p : Introspector.getBeanInfo(tclass).getPropertyDescriptors()) {
            if (!p.getName().equals("class")) {
                FieldMetaData m = new FieldMetaData();
                m.setName(p.getName());
                m.setReadMethod(p.getReadMethod());
                m.setWriteMethod(p.getWriteMethod());
                m.setType(p.getPropertyType());
                try {
                    Object an = tclass.getDeclaredField(p.getName()).getAnnotation(ApinColumn.class);
                    m.setColumnAnnotation(an == null ? null : (ApinColumn) an);
                    if (m.getColumnAnnotation() != null && an != null) {
                        //�Ƿ�Ϊ����
                        m.setPrimaryKey(m.getColumnAnnotation().isPrimaryKey());
                        //���ע��Ϊ���־û����������
                        m.setPersistence(m.getColumnAnnotation().persistence());
                    }
                } catch (NoSuchFieldException e) {
                    //�޷���ȡע��ʱֱ��ʹ���������д���
                    continue; //��ʱ��֧��
                }
                //��������
                ApinColumn ac = m.getColumnAnnotation();
                if (ac != null && ac.name().trim().length() > 0) {
                    m.setColumnName(ac.name());
                } else {
                    this.setColumnName(m);
                }
                //����ǰ������ӵ��б�
                list.add(m);
            }
        }
        return list;
    }

    protected void setTableName(String tableName) {
        this.tableName = tableName;
    }

    protected void setFieldMetaDataList(List<FieldMetaData> fieldMetaDataList) {
        this.fieldMetaDataList = fieldMetaDataList;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setType(Class type) {
        this.type = type;
    }

    public Class getType() {
        return type;
    }

    public List<FieldMetaData> getFieldMetaDataList() {
        return fieldMetaDataList;
    }

    public String getName() {
        return name;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public String toString() {
        return tableName + "(\n" + fieldMetaDataList + "\n)";
    }
}
