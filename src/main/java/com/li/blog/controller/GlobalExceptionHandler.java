package com.li.blog.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.Arrays;


/**
 * @author li
 * @version 1.0
 * @since 18-9-8 下午4:21
 **/
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String DEFAULT_ERROR_VIEW = "error";
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(SQLException.class)
    public ModelAndView sqlexception(SQLException e) {
        LOGGER.error(Arrays.toString(e.getStackTrace()));
        return getModelAndView("对不起，我们获取不到您的Github账号信息，您有可能没有将邮件地址公开，请您将Github邮件地址公开然后重试～");
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public ModelAndView socketTimeoutException(SocketTimeoutException e) {
        LOGGER.error(Arrays.toString(e.getStackTrace()));
        return getModelAndView("服务器超时，请稍后重试～");
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView defaultRuntimeException(RuntimeException e) {
        LOGGER.error(Arrays.toString(e.getStackTrace()));
        return getModelAndView("服务器异常，请稍后重试～");
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultExceptionHandler(Exception e) {
        LOGGER.error(Arrays.toString(e.getStackTrace()));
        return getModelAndView("您访问的页面不存在！");
    }

    private ModelAndView getModelAndView(String errorMsg) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMsg", errorMsg);
        modelAndView.setViewName(DEFAULT_ERROR_VIEW);
        return modelAndView;
    }
}
