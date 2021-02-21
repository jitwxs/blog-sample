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
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String roleId;
    private Date createDate;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "UserRole{" +
        ", userId=" + userId +
        ", roleId=" + roleId +
        ", createDate=" + createDate +
        "}";
    }
}
