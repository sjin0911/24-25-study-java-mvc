package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);

        for(Class<?> clazz : classes){
            Object instance = getInstance(clazz);
            final List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .collect(Collectors.toList());
            methods.forEach(method -> {
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                for(RequestMethod requestMethod: requestMapping.method()){
                    final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                    final HandlerExecution handlerExecution = new HandlerExecution(instance, method);
                    handlerExecutions.put(handlerKey, handlerExecution);
                }
            });
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        }catch (NoSuchMethodException e){
            log.error(e.getMessage());
            throw new IllegalArgumentException("생성자를 가져올 수 없습니다. "+clazz.getName());
        }catch (InstantiationException | IllegalAccessException | InvocationTargetException e){
            log.error(e.getMessage());
            throw new IllegalArgumentException("인스턴스화 할 수 없습니다. "+clazz.getName());
        }
    }
    public HandlerExecution getHandler(final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        final String method = request.getMethod();
        final RequestMethod requestMethod = RequestMethod.valueOf(method);

        final HandlerKey handlerKey = new HandlerKey(uri, requestMethod);

        if(handlerExecutions.containsKey(handlerKey)){
            return handlerExecutions.get(handlerKey);
        }
        return null;
    }
}
