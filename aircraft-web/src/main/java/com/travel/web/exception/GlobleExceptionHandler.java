package com.travel.web.exception;

import com.travel.commons.enums.ResultStatus;
import com.travel.commons.resultbean.ResultGeekQ;
import com.travel.function.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther 邱润泽 bullock
 * @date 2019/11/9
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobleExceptionHandler {
    @ModelAttribute(name = "md")
    public Map<String,Object> mydata() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("age", 99);
        map.put("gender", "男");
        return map;
    }
    @ExceptionHandler(value = Exception.class)
    public ResultGeekQ<String> exceptionHandler(HttpServletRequest request,Exception e){
        log.error("======== 拦截到异常请注意！==========");
        e.printStackTrace();
        ResultGeekQ resultGeekQ = ResultGeekQ.build();
        if(e instanceof BindException){
            BindException ex = (BindException)e;
            List<ObjectError> errorList = ex.getAllErrors();
            //找到第一个错误
            ObjectError error = errorList.stream().findFirst().get();
            String msg = error.getDefaultMessage();
            log.error("======异常信息为==== error:{}",msg);
            resultGeekQ.withErrorArgs(ResultStatus.BIND_ERROR.getCode(), msg, error);
        } else if(e instanceof UserException){
            log.error("***用户不存在请检查!***");
            resultGeekQ.withErrorArgs(ResultStatus.USER_NOT_EXIST.getCode(),ResultStatus.USER_NOT_EXIST.getMessage());
        }
        return resultGeekQ;
    }
}
