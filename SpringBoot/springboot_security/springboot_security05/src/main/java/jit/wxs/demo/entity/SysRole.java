package jit.wxs.demo.entity;

import java.io.Serializable;

/**
 * @author jitwxs
 * @date 2018/3/30 1:20
 */
public class SysRole implements Serializable {
    static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
