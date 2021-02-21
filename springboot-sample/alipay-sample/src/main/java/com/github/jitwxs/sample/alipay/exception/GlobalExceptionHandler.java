package com.github.jitwxs.sample.alipay.exception;

import com.github.jitwxs.sample.alipay.utils.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jitwxs
 * @date 2018/4/25 16:43
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public Result customException(HttpServletRequest request, CustomException e) {
        return Result.error(e.getCode(), e.getMessage());
    }
}
