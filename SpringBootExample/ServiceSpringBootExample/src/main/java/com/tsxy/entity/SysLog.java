package com.tsxy.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author Liu_df
 * @Date 2022/10/29 19:26
 */
@Data
@TableName("sys_log")
public class SysLog {
    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    @TableId
    private Long id;

    /**
     * 操作模块
     */
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    private Integer businessType;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 操作人员
     */
    private String operName;

    /**
     * 请求url
     */
    private String operUrl;

    /**
     * 操作地址
     */
    private String operIp;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operTime;

}