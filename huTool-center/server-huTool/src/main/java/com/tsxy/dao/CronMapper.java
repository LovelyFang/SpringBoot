package com.tsxy.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author Liu_df
 * @Date 2023/5/12 16:06
 */
@Repository
@Mapper
public interface CronMapper {

    @Select("select cron from scheduled where cron_id = #{id}")
    String getCron(int id);

}
