package com.rofe.util;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MyNoLoginExceptionHandler {

    @ExceptionHandler(MyNotLoginException.class)
    public String handleException(Exception e, HttpServletRequest request){
        return "forward:noLogin.html";
    }
}