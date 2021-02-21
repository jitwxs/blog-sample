package jit.wxs.mapper;

import jit.wxs.entity.SysUserRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jitwxs
 * @date 2018/3/30 1:24
 */
@Mapper
public interface SysUserRoleMapper {
    @Select("SELECT * FROM sys_user_role WHERE user_id = #{userId}")
    List<SysUserRole> listByUserId(Integer userId);

    @Insert("INSERT INTO sys_user_role(user_id,role_id) VALUES(#{userId},#{roleId})")
    int insert(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
}
