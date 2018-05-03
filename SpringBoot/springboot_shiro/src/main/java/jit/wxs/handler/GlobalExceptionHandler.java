package jit.wxs.handler;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author jitwxs
 * @date 2018/3/20 15:46
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String globalException(HttpServletRequest request, Exception e) {
        return "发生错误: " + e.getMessage();
    }
}
