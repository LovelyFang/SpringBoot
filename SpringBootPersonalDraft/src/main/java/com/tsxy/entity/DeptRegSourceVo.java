package entity;

import java.util.Date;
import java.util.Map;

/**
 * @Author Liu_df
 * @Date 2022/10/11 21:55
 */

public class DeptRegSourceVo implements java.io.Serializable {
    /**
     * serialVersionUID:(用一句话描述这个变量表示什么).
     */
    private static final long serialVersionUID = -8676828267066460609L;
    /**
     * 科室编码
     */
    private String deptNo;

    /**
     * 号源日期
     */
    private Date time;
    /**
     * 医生编码
     */
    private String doctorNo;

    /**
     * 是否有分时 0 无 1 有
     */
    private Integer hasDetailTime;
    /**
     * 是否出诊 0 休息 1 出诊
     */
    private Integer workStatus;
    /**
     * 挂号费
     */
    private Integer regFee;
    /**
     * 号源总数
     */
    private Integer totalNum;
    /**
     * 剩余号源数
     */
    private Integer leftNum;

    /**
     * 是否为精准号源  0 否; 1是
     */
    private Integer preciseReg;


    /** 拓展字段 */
    private Map<String, String> extPropes;

    public Map<String, String> getExtPropes() {
        return extPropes;
    }

    public void setExtPropes(Map<String, String> extPropes) {
        this.extPropes = extPropes;
    }
    /**
     * 获取科室编码
     * @return deptNo 科室编码
     */
    public String getDeptNo() {
        return deptNo;
    }

    /**
     * 设置科室编码
     * @param deptNo 科室编码
     */
    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    /**
     * 获取号源日期
     * @return time 号源日期
     */
    public Date getTime() {
        return time;
    }

    /**
     * 设置号源日期
     * @param time 号源日期
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * 获取医生编码
     * @return doctorNo 医生编码
     */
    public String getDoctorNo() {
        return doctorNo;
    }

    /**
     * 设置医生编码
     * @param doctorNo 医生编码
     */
    public void setDoctorNo(String doctorNo) {
        this.doctorNo = doctorNo;
    }

    /**
     * 获取是否有分时0无1有
     * @return hasDetailTime 是否有分时0无1有
     */
    public Integer getHasDetailTime() {
        return hasDetailTime;
    }

    /**
     * 设置是否有分时0无1有
     * @param hasDetailTime 是否有分时0无1有
     */
    public void setHasDetailTime(Integer hasDetailTime) {
        this.hasDetailTime = hasDetailTime;
    }

    /**
     * 获取是否出诊0休息1出诊
     * @return workStatus 是否出诊0休息1出诊
     */
    public Integer getWorkStatus() {
        return workStatus;
    }

    /**
     * 设置是否出诊0休息1出诊
     * @param workStatus 是否出诊0休息1出诊
     */
    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    /**
     * 获取挂号费
     * @return regFee 挂号费
     */
    public Integer getRegFee() {
        return regFee;
    }

    /**
     * 设置挂号费
     * @param regFee 挂号费
     */
    public void setRegFee(Integer regFee) {
        this.regFee = regFee;
    }

    /**
     * 获取号源总数
     * @return totalNum 号源总数
     */
    public Integer getTotalNum() {
        return totalNum;
    }

    /**
     * 设置号源总数
     * @param totalNum 号源总数
     */
    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    /**
     * 获取剩余号源数
     * @return leftNum 剩余号源数
     */
    public Integer getLeftNum() {
        return leftNum;
    }

    /**
     * 设置剩余号源数
     * @param leftNum 剩余号源数
     */
    public void setLeftNum(Integer leftNum) {
        this.leftNum = leftNum;
    }

    public Integer getPreciseReg() {
        return preciseReg;
    }

    public void setPreciseReg(Integer preciseReg) {
        this.preciseReg = preciseReg;
    }

}