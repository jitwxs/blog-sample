package com.github.jitwxs.sample.mp.enity;

/**
 * @author jitwxs
 * @date 2018/3/13 22:43
 */
public class Msg<T> {
    public static Integer OK = 200;

    public static Integer ERROR = 404;

    private Integer code;

    private Boolean status;

    private String info;

    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
