package com.tsxy.service.impl;

import com.tsxy.annotation.CacheLock;
import com.tsxy.vo.BaseResultVo;
import com.tsxy.vo.Constants;
import com.tsxy.service.CustomService;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.Map;

/**
 * @Author Liu_df
 * @Date 2023/11/2 11:34
 */
@Service
public class CustomServiceImpl implements CustomService, Serializable {
    private static final long serialVersionUID = 2844642108960271809L;


    @Override
    @CacheLock(key = {"#params[requestHisId]"}, cacheExpireTime = 60 * 60 * 2, LockExpireTime = 60, pre = "getProperty")
    public BaseResultVo aspectJMethod(Map<String, String> params) {

        BaseResultVo result = new BaseResultVo();

        result.setResultCode(Constants.RESULT_CODE_SUCCESS);

        return result;
    }
}
