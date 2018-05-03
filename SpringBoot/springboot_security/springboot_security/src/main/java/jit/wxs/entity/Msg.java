package jit.wxs.entity;

/**
 * @author jitwxs
 * @date 2018/3/13 22:43
 */
public class Msg<T> {
    private Boolean status;

    private String info;

    private T data;

    public Msg() {

    }

    public Msg(Boolean status, String info, T data) {
        this.status = status;
        this.info = info;
        this.data = data;
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
