package com.github.jitwxs.sample.shiro.entity;

import com.baomidou.mybatisplus.annotations.TableField;

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
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private Date createDate;
    @TableField(update = "new()")
    private Date modifiedDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public String toString() {
        return "Permission{" +
        ", id=" + id +
        ", name=" + name +
        ", createDate=" + createDate +
        ", modifiedDate=" + modifiedDate +
        "}";
    }
}
