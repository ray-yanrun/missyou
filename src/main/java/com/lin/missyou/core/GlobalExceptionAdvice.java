package com.lin.missyou.core;

import com.lin.missyou.core.configuration.ExceptionCodeConfiguration;
import com.lin.missyou.exception.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionAdvice {

    private ExceptionCodeConfiguration exceptionCodeConfiguration;

    @Autowired
    public GlobalExceptionAdvice(ExceptionCodeConfiguration exceptionCodeConfiguration){
        this.exceptionCodeConfiguration = exceptionCodeConfiguration;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody  // 返回对象给前端，需要使用SpringBoot进行序列化
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR) // 将HTTP状态码设置为500
    public UnifyResponse handleException(HttpServletRequest req, Exception e){
        // 所有的未知异常，状态码均设置为9999，异常原因模糊处理
        String requestURL = req.getRequestURI();
        String method = req.getMethod();
        return new UnifyResponse(9999, "服务器内部错误", method + " "+requestURL);
    }

    // 由于Http异常的状态码不一样，需要动态的返回状态码信息，所以使用了ResponseEntity
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<UnifyResponse> handleHttpException(HttpServletRequest req, HttpException e){
        String requestURL = req.getRequestURI();
        String method = req.getMethod();

        UnifyResponse response = new UnifyResponse(e.getCode(), exceptionCodeConfiguration.getMessage(e.getCode()), method+" "+requestURL);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.resolve(e.getHttpStatusCode());
        return new ResponseEntity<>(response, header, httpStatus);
    }

    // 全局处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public UnifyResponse handleBeanValidation(HttpServletRequest req, MethodArgumentNotValidException e){
        String requestURL = req.getRequestURI();
        String method = req.getMethod();

        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        String message = formatAllErrorMessages(errorList);
        return new UnifyResponse(10001, message, method + " " + requestURL);
    }

    // 全局处理URL中的参数异常 （不知道异常类的名字可以通过看控制台来获取，然后单独为其写处理类）
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public UnifyResponse handleConstraintException(HttpServletRequest req, ConstraintViolationException e){
        String requestURL = req.getRequestURI();
        String method = req.getMethod();
        String message = e.getMessage();
        // 如果返回的异常消息需要更多的信息或者定制化，则可以通过循环e.getConstraintViolations()得到
        return new UnifyResponse(10001, message, method + " " + requestURL);
    }

    private String formatAllErrorMessages(List<ObjectError> errorList){
        StringBuffer sb = new StringBuffer();
        // 如果不清楚ObjectError的结构，可以通过debug断点调试的方式查看
        errorList.forEach(error -> {
            sb.append(error.getDefaultMessage()).append(";");
        });
        return String.valueOf(sb);
    }
}
