package com.github.jitwxs.sample.es;

/**
 * @author jitwxs
 * @since 2018/10/9 11:02
 */
public class ResultBean {
    private Boolean status;

    private String msg;

    private Object data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResultBean(Boolean status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static ResultBean success(String msg, Object data) {
        return new ResultBean(true, msg, data);
    }

    public static ResultBean error(String msg, Object data) {
        return new ResultBean(false, msg, data);
    }

    public static ResultBean success(String msg) {
        return new ResultBean(true, msg, null);
    }

    public static ResultBean error(String msg) {
        return new ResultBean(false, msg, null);
    }
}
