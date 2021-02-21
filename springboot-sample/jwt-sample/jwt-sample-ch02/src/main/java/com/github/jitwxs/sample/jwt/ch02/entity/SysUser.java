package com.github.jitwxs.sample.jwt.ch02.entity;

import java.io.Serializable;

/**
 * @author jitwxs
 * @date 2018/3/30 1:18
 */
public class SysUser implements Serializable{
    static final long serialVersionUID = 1L;

    private Integer id;

    private String username;

    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
