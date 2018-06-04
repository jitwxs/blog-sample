package jit.wxs.demo.utils;

import java.io.Serializable;

/**
 * 前后端交互
 * @author jitwxs
 * @date 2018/3/13 22:43
 */
public class Msg implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean status;

    private String info;

    private Object data;

    public Msg(Boolean status, String info, Object data) {
        this.status = status;
        this.info = info;
        this.data = data;
    }

    public static Msg ok() {
        return ok(null,null);
    }

    public static Msg ok(String info) {
        return ok(info,null);
    }

    public static Msg ok(String info, Object data) {
        return new Msg(true, info, data);
    }

    public static Msg error(String info) {
        return error(info,null);
    }

    public static Msg error(String info, Object data) {
        return new Msg(false, info, data);
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
