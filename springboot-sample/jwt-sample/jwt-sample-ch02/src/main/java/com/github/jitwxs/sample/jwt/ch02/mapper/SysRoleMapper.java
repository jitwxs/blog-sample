package com.github.jitwxs.sample.jwt.ch02.mapper;

import com.github.jitwxs.sample.jwt.ch02.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author jitwxs
 * @date 2018/3/30 1:23
 */
@Mapper
public interface SysRoleMapper {
    @Select("SELECT * FROM sys_role WHERE id = #{id}")
    SysRole selectById(Integer id);
}
