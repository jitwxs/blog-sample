package jit.wxs.demo.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 前后端交互
 * @author jitwxs
 * @date 2018/3/13 22:43
 */
@Data
@AllArgsConstructor
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 信息
     */
    private String info;

    /**
     * 数据
     */
    private Object data;

    public static Result ok(String info, Object data) {
        return new Result(ResultEnum.OK.getCode(), info, data);
    }

    public static Result ok() {
        return ok(null,null);
    }

    public static Result error(Integer code, String info) {
        return error(code, info,null);
    }

    public static Result error(Integer code, String info, Object data) {
        return new Result(code, info, data);
    }

    public static Result error(ResultEnum resultEnum) {
        return error(resultEnum,null);
    }

    public static Result error(ResultEnum resultEnum, Object data) {
        return error(resultEnum.getCode(), resultEnum.getInfo(),data);
    }
}
