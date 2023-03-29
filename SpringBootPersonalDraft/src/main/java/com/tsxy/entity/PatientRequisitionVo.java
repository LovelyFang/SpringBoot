package entity;

import com.alibaba.fastjson.JSONArray;
import com.tsxy.entity.BaseMpVo;

/**
 * 医技预约--病人申请单
 */
public class PatientRequisitionVo extends BaseMpVo {

    private static final long serialVersionUID = 29879819879849858L;

    /**
     * 病人id
     */
    private String patHisNo;

    /**
     * 健康卡号
     */
    private String healthCard;

    /**
     * 申请单号
     */
    private String applyNo;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 年龄
     */
    private String age;

    /**
     * 出生日期
     */
    private String birthDay;

    /**
     * 申请时间 yyyy-MM-dd HH:mm:ss
     */
    private String reqDateTime;

    /**
     * 申请院区
     */
    private String reqHospital;

    /**
     * 病人来源
     */
    private String patientSource;

    /**
     * 门诊号
     */
    private String outpatientNo;

    /**
     * 缴费状态（0:未缴费 1:已缴费 2:已作废或已退费）
     */
    private String chargeFlag;

    /**
     * 检查类别
     */
    private String examClass;

    /**
     * 检查子类
     */
    private String examSubClass;
    /**
     * 预约状态(0未预约 1已锁定 2已预约 3已报到 5已申请 6已退回)
     */
    private String scheduleStatus;
    /**
     * 预约日期
     */
    private String scheduleDate;
    /**
     * 预约时段
     */
    private String scheduleApm;
    /**
     * 申请科室名称
     */
    private String reqDeptName;
    /**
     * 预约队列名称
     */
    private String queueName;
    /**
     * 病区名称
     */
    private String reqWardName;
    /**
     * 执行科室名称
     */
    private String performDeptName;
    /**
     * 标识位 按位定义(从左到右 0开始)  7：是否遵循临床路径（1是0否） 8：是否床边检查（1是0否） 9：是否VIP病人（1是0否） 10：是否绝经（1是0否） 11：是否病重（1是0否） 12：是否无痛检查（1是0否）
     */
    private String flags;
    /**
     * 孕周要求 0否1是
     */
    private String gestational;
    /**
     * 末次月经日期
     */
    private String lastMensesDate;
    /**
     * 预约院区名称
     */
    private String scheduleHospitalName;
    /**
     * 预约ID
     */
    private String scheduledId;
    /**
     * 合单号
     */
    private String mergeNo;


    //预约成功后才有
    /**
     * 排队号(预约后立即报到才有值)
     */
    private String queueNo;
    /**
     * 检查号(预约后立即报到才有值)
     */
    private String patLocalId;
    /**
     * 当前等待人数(预约后立即报到才有值)
     */
    private String waitCount;
    /**
     * 检查队列所在位置
     */
    private String location;
    /**
     * 提前到达时间（提示）
     */
    private String advanceTime;
    /**
     * 必须到达时间（提示）
     */
    private String reqReachTime;
    /**
     * 是否打印凭条（0:否 1:是）
     */
    private String isPrint;
    /**
     * 用药时间
     */
    private String drugTime;
    /**
     * 建议
     */
    private JSONArray notices;
    /**
     * 提醒
     */
    private JSONArray reminders;
    /**
     * 科室位置
     */
    private String signLocation;
    /**
     * 信息
     */
    private String desc;

    /**
     * 阅读信息
     */
    private String itemTable;

    public String getPatHisNo() {
        return patHisNo;
    }

    public void setPatHisNo(String patHisNo) {
        this.patHisNo = patHisNo;
    }

    public String getHealthCard() {
        return healthCard;
    }

    public void setHealthCard(String healthCard) {
        this.healthCard = healthCard;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getReqDateTime() {
        return reqDateTime;
    }

    public void setReqDateTime(String reqDateTime) {
        this.reqDateTime = reqDateTime;
    }

    public String getReqHospital() {
        return reqHospital;
    }

    public void setReqHospital(String reqHospital) {
        this.reqHospital = reqHospital;
    }

    public String getPatientSource() {
        return patientSource;
    }

    public void setPatientSource(String patientSource) {
        this.patientSource = patientSource;
    }

    public String getOutpatientNo() {
        return outpatientNo;
    }

    public void setOutpatientNo(String outpatientNo) {
        this.outpatientNo = outpatientNo;
    }

    public String getChargeFlag() {
        return chargeFlag;
    }

    public void setChargeFlag(String chargeFlag) {
        this.chargeFlag = chargeFlag;
    }

    public String getExamClass() {
        return examClass;
    }

    public void setExamClass(String examClass) {
        this.examClass = examClass;
    }

    public String getExamSubClass() {
        return examSubClass;
    }

    public void setExamSubClass(String examSubClass) {
        this.examSubClass = examSubClass;
    }

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(String scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleApm() {
        return scheduleApm;
    }

    public void setScheduleApm(String scheduleApm) {
        this.scheduleApm = scheduleApm;
    }

    public String getReqDeptName() {
        return reqDeptName;
    }

    public void setReqDeptName(String reqDeptName) {
        this.reqDeptName = reqDeptName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getReqWardName() {
        return reqWardName;
    }

    public void setReqWardName(String reqWardName) {
        this.reqWardName = reqWardName;
    }

    public String getPerformDeptName() {
        return performDeptName;
    }

    public void setPerformDeptName(String performDeptName) {
        this.performDeptName = performDeptName;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public String getGestational() {
        return gestational;
    }

    public void setGestational(String gestational) {
        this.gestational = gestational;
    }

    public String getLastMensesDate() {
        return lastMensesDate;
    }

    public void setLastMensesDate(String lastMensesDate) {
        this.lastMensesDate = lastMensesDate;
    }

    public String getScheduleHospitalName() {
        return scheduleHospitalName;
    }

    public void setScheduleHospitalName(String scheduleHospitalName) {
        this.scheduleHospitalName = scheduleHospitalName;
    }

    public String getScheduledId() {
        return scheduledId;
    }

    public void setScheduledId(String scheduledId) {
        this.scheduledId = scheduledId;
    }

    public String getMergeNo() {
        return mergeNo;
    }

    public void setMergeNo(String mergeNo) {
        this.mergeNo = mergeNo;
    }


    public String getQueueNo() {
        return queueNo;
    }

    public void setQueueNo(String queueNo) {
        this.queueNo = queueNo;
    }

    public String getPatLocalId() {
        return patLocalId;
    }

    public void setPatLocalId(String patLocalId) {
        this.patLocalId = patLocalId;
    }

    public String getWaitCount() {
        return waitCount;
    }

    public void setWaitCount(String waitCount) {
        this.waitCount = waitCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAdvanceTime() {
        return advanceTime;
    }

    public void setAdvanceTime(String advanceTime) {
        this.advanceTime = advanceTime;
    }

    public String getReqReachTime() {
        return reqReachTime;
    }

    public void setReqReachTime(String reqReachTime) {
        this.reqReachTime = reqReachTime;
    }

    public String getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(String isPrint) {
        this.isPrint = isPrint;
    }

    public String getDrugTime() {
        return drugTime;
    }

    public void setDrugTime(String drugTime) {
        this.drugTime = drugTime;
    }

    public JSONArray getNotices() {
        return notices;
    }

    public void setNotices(JSONArray notices) {
        this.notices = notices;
    }

    public JSONArray getReminders() {
        return reminders;
    }

    public void setReminders(JSONArray reminders) {
        this.reminders = reminders;
    }

    public String getSignLocation() {
        return signLocation;
    }

    public void setSignLocation(String signLocation) {
        this.signLocation = signLocation;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getItemTable() {
        return itemTable;
    }

    public void setItemTable(String itemTable) {
        this.itemTable = itemTable;
    }
}
