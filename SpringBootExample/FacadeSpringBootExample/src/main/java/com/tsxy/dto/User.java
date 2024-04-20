package com.tsxy.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName t_user
 */
@Data
public class User implements Serializable {


    /**
     * 主键
     */
    private Integer id;

    /**
     * 联合id
     */
    private String unionId;

    /**
     * 证件类型(1-身份证 2-港澳居民身份证 3-台湾居民身份证)
     */
    private Integer idType;

    /**
     * 证件号码
     */
    private String idNumber;

    /**
     * 证件号码密文
     */
    private String idNumberCipher;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 姓名密文
     */
    private String realNameCipher;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 性别
     */
    private String sex;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 地址
     */
    private String address;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 手机号码密文
     */
    private String mobileCipher;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}