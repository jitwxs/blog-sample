package jit.wxs.entity;

/**
 * @author jitwxs
 * @date 2018/3/14 15:03
 */
public class Error {

    private String url;

    private String message;

    public Error() {
    }

    public Error(String url, String message) {
        this.url = url;
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
