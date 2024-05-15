package com.fishep.spring.debug.jdbc.mapper;

import com.fishep.spring.debug.jdbc.po.Demo;
import org.apache.ibatis.annotations.*;

/**
 * @Author fly.fei
 * @Date 2024/5/10 10:09
 * @Desc
 **/
@Mapper
public interface DemoMapper {

    @Insert("insert into `demo`(`comment`) VALUES (#{demo.comment})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Boolean insert(@Param("demo") Demo demo);

    @Delete("delete from `demo` where `id` = #{demo.id}")
    Boolean delete(@Param("demo") Demo demo);

    @Update("update `demo` set `comment`=#{demo.comment} where `id` = #{demo.id}")
    Boolean update(@Param("demo") Demo demo);

    @Select("select * from `demo` where `id` = #{demo.id}")
    Demo select(@Param("demo") Demo demo);

}
