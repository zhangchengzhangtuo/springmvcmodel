package com.apin.filters.dao;


import com.apin.filters.po.ApinApplication;

/**
 * Created by Administrator on 2017/1/11.
 */
public interface ApplicationMapper {

    public ApinApplication selectApplicationById(String applicationId);
}
