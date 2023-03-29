package entity;

import java.io.Serializable;

/**
 * @Author Liu_df
 * @Date 2022/12/26 10:52
 */
public class OutpatientWaitPayVO implements Serializable {

    private static final long serialVersionUID = -5938030659499036626L;

    /**
     * 门诊号
     */
    private String outpatientNo;
    /**
     * 就诊卡类型
     */
    private String patCardType;
    /**
     * 就诊卡号
     */
    private String patCardNo;
    /**
     * 姓名
     */
    private String patientName;
    /**
     * 挂号单据号
     */
    private String serialnumber;
    /**
     * 病人id
     */
    private String patId;

    /**
     * 处方单据号
     */
    private String prescriptionNo;

    /**
     * 医嘱id
     */
    private String orderId;

    /**
     * 项目名称
     */
    private String entryName;
    /**
     * 待缴费金额
     */
    private String payAmout;
    /**
     * 接诊医生姓名
     */
    private String doctorName;
    /**
     * 接诊科室名称
     */
    private String deptName;
    /**
     * 登记时间:格式：YYYY-MM-DD hh24:mi:ss
     */
    private String registrationTime;

    /**
     * 是否医保账号
     */
    private String isMedicalPatient;

    public String getIsMedicalPatient() {
        return isMedicalPatient;
    }

    public void setIsMedicalPatient(String isMedicalPatient) {
        this.isMedicalPatient = isMedicalPatient;
    }

    public String getOutpatientNo() {
        return outpatientNo;
    }

    public void setOutpatientNo(String outpatientNo) {
        this.outpatientNo = outpatientNo;
    }

    public String getPatCardType() {
        return patCardType;
    }

    public void setPatCardType(String patCardType) {
        this.patCardType = patCardType;
    }

    public String getPatCardNo() {
        return patCardNo;
    }

    public void setPatCardNo(String patCardNo) {
        this.patCardNo = patCardNo;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }


    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getPrescriptionNo() {
        return prescriptionNo;
    }

    public void setPrescriptionNo(String prescriptionNo) {
        this.prescriptionNo = prescriptionNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getPayAmout() {
        return payAmout;
    }

    public void setPayAmout(String payAmout) {
        this.payAmout = payAmout;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    @Override
    public String toString() {
        return "OutpatientWaitPayVO{" +
                "outpatientNo='" + outpatientNo + '\'' +
                ", patCardType='" + patCardType + '\'' +
                ", patCardNo='" + patCardNo + '\'' +
                ", patientName='" + patientName + '\'' +
                ", serialnumber='" + serialnumber + '\'' +
                ", patId='" + patId + '\'' +
                ", prescriptionNo='" + prescriptionNo + '\'' +
                ", orderId='" + orderId + '\'' +
                ", entryName='" + entryName + '\'' +
                ", payAmout='" + payAmout + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", deptName='" + deptName + '\'' +
                ", registrationTime='" + registrationTime + '\'' +
                ", isMedicalPatient='" + isMedicalPatient + '\'' +
                '}';
    }
}
