package com.lys.generator.exception;

import com.lys.generator.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ControllerAdvice + @ExceptionHandler 实现全局的 Controller 层的异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    // 实例化一个原始被调用的类 Logger 对象，并且带上 方法名称、行号，更方便的通过日志定位代码
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理所有不可知的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    Result handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.error(StatusCode.UNKNOWN_ERR.getName());
    }

    /**
     * 处理所有业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(RRException.class)
    @ResponseBody
    Result handleRRException(RRException e) {
        log.error(e.getMessage(), e);
        Result error = Result.error(e.getCode(), e.getMsg());
        return error;
    }

    /**
     * 处理所有接口数据验证异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        Result error = Result.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return error;
    }
}