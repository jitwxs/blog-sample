package jit.wxs.exception;

import jit.wxs.entity.Error;
import jit.wxs.entity.Msg;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * @author jitwxs
 * @date 2018/3/14 15:06
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Msg globalException(HttpServletRequest request, Exception e) {
        Msg<Error> msg = new Msg<>();
        msg.setCode(Msg.ERROR);
        Error error = new Error(request.getRequestURL().toString(),e.getMessage());
        msg.setData(error);

        return msg;
    }
}
