package com.tsxy.service;

import com.tsxy.entity.BaseResultVo;

import java.util.Map;

/**
 * @Author Liu_df
 * @Date 2023/11/2 11:35
 */
public interface CustomService {
    BaseResultVo aspectJMethod(Map<String, String> parameters);

}
