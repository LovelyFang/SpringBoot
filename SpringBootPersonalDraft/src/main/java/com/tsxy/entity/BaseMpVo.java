package com.tsxy.entity;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author Liu_df
 * @Date 2023/2/3 15:45
 */

public class BaseMpVo implements Serializable {

    private static final long serialVersionUID = 97017195927120128L;
    /**
     * 交易结果代码 0-成功 其他都为失败
     */
    private String resultCode;
    /**
     * 当交易结果代码不成功时，该字段必须返回
     */
    private String resultMessage;

    /** 返回 交易结果代码 0-成功 其他都为失败 */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseMpVo baseMpVo = (BaseMpVo) o;
        return Objects.equals(getResultCode(), baseMpVo.getResultCode()) && Objects.equals(getResultMessage(), baseMpVo.getResultMessage()) && Objects.equals(getExtFields(), baseMpVo.getExtFields());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResultCode(), getResultMessage(), getExtFields());
    }

    @Override
    public String toString() {
        return "BaseMpVo{" +
                "resultCode='" + resultCode + '\'' +
                ", resultMessage='" + resultMessage + '\'' +
                ", extFields=" + extFields +
                '}';
    }
}