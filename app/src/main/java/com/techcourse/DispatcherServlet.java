package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.view.JspView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping){
        handlerMappings.add(handlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final var handler = getHandler(request);

        if(handler instanceof Controller){
            handlerManualHandler(request, response, (Controller) handler);
        }
        if(handler instanceof HandlerExecution){
            handlerAnnotationHandler(request, response, (HandlerExecution)handler);
        }
    }

    private void handlerManualHandler(final HttpServletRequest request, final HttpServletResponse response
                                      ,final Controller handler) throws ServletException{
        try{
            String viewName = handler.execute(request, response);
            final ModelAndView modelAndView = new ModelAndView(new JspView(viewName));
            renderView(modelAndView, request, response);
        }catch (Throwable e){
            throw new ServletException(e.getMessage());
        }
    }

    private static void handlerAnnotationHandler(final HttpServletRequest request, final HttpServletResponse response,
                                                 final HandlerExecution handler){
        try{
            ModelAndView modelAndView = handler.handle(request, response);
            renderView(modelAndView, request, response);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request){
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(RuntimeException::new);
    }
    private static void renderView(final ModelAndView modelAndView, final HttpServletRequest  request,
                                   final HttpServletResponse response) throws Exception{
        final Map<String, Object> model = modelAndView.getModel();
        final View view = modelAndView.getView();
        view.render(model, request, response);
    }
}
