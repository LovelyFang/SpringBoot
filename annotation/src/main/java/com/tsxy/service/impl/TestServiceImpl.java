package com.tsxy.service.impl;

import com.tsxy.entity.SysLog;
import com.tsxy.mapper.TestMapper;
import com.tsxy.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Liu_df
 * @Date 2022/10/29 19:33
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public int saveLog(SysLog sysLog) {
        return testMapper.insert(sysLog);
    }
}
