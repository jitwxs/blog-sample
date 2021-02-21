package com.github.jitwxs.sample.shiro.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author jitwxs
 * @since 2018-03-20
 */
public class RolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    private String roleId;
    private String permissionId;
    private Date createDate;


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "RolePermission{" +
        ", roleId=" + roleId +
        ", permissionId=" + permissionId +
        ", createDate=" + createDate +
        "}";
    }
}
