package jit.wxs.demo.exception;

import jit.wxs.demo.utils.ResultEnum;
import lombok.Data;

/**
 * @author jitwxs
 * @since 2018/6/6 9:56
 */
@Data
public class CustomException extends RuntimeException {
    private Integer code;

    public CustomException(ResultEnum resultEnum) {
        super(resultEnum.getInfo());
        this.code = resultEnum.getCode();
    }

    public CustomException(Integer code , String info) {
        super(info);
        this.code = code;
    }
}
