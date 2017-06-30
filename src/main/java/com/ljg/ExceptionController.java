package com.ljg;


import com.ljg.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import javax.servlet.http.HttpServletRequest;


/**
 * 集中处理各种异常
 */
@ControllerAdvice
public class ExceptionController {

    /**
     * 只处理SinoException业务异常
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String serviceExceptionHandler(HttpServletRequest request, ServiceException ex) {
        return "SinoException";
    }

    /**
     * 其他所有异常
     *
     * @param request
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler
    @ResponseBody
    public String defaultExceptionHandler(HttpServletRequest request, Exception ex) throws Exception {
        return "其他所有异常";
    }

}
