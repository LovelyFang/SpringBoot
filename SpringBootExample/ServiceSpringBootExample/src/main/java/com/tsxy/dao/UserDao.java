package com.tsxy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tsxy.dto.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Liu_df
 * @Date 2022/10/29 19:34
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

    User selectUserByIdNumber(String idNumber);

}
