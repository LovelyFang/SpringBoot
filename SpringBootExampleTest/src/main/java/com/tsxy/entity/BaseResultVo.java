package com.tsxy.entity;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author Liu_df
 * @Date 2023/11/2 12:00
 */
public class BaseResultVo implements Serializable {


    private static final long serialVersionUID = -500798293963465350L;

    /*
     * 交易结果代码 0-成功 其他都为失败
     */
    private String resultCode;
    /*
     * 当交易结果代码不成功时，该字段必须返回
     */
    private String resultMessage;

    /* 返回 交易结果代码 0-成功 其他都为失败 */
    /** 扩展字段 **/
    private Map<String, String> extFields;

    public String getResultCode() {
        return resultCode;
    }

    /** 设置 交易结果代码 0-成功 其他都为失败 */
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    /** 返回 当交易结果代码不成功时，该字段必须返回 */
    public String getResultMessage() {
        return resultMessage;
    }

    /** 设置 当交易结果代码不成功时，该字段必须返回 */
    public void setResultMessage(String resultMessage) {
        if(StringUtils.isNotBlank(resultMessage) && resultMessage.length()>=195) {
            this.resultMessage = resultMessage.substring(0, 190)+"...";
        }else {
            this.resultMessage = resultMessage;
        }
    }

    public Map<String, String> getExtFields() {
        return extFields;
    }

    public void setExtFields(Map<String, String> extFields) {
            this.extFields = extFields;
        }


}
