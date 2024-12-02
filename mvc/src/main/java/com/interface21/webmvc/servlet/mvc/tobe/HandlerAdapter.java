package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    public Boolean support(Object handler);
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
