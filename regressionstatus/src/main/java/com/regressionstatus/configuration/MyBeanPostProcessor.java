package com.regressionstatus.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@EnableWebMvc
public class MyBeanPostProcessor implements BeanPostProcessor {
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof RequestMappingHandlerMapping) {
            setRemoveSemicolonContent((RequestMappingHandlerMapping) bean,
                    beanName);
            setUseSuffixPatternMatch((RequestMappingHandlerMapping) bean,
                    beanName);
        }
        return bean;
    }

    private void setRemoveSemicolonContent(RequestMappingHandlerMapping requestMappingHandlerMapping, String beanName) {

        requestMappingHandlerMapping.setRemoveSemicolonContent(false);
    }

    private void setUseSuffixPatternMatch(
            RequestMappingHandlerMapping requestMappingHandlerMapping,
            String beanName) {

        requestMappingHandlerMapping.setUseSuffixPatternMatch(false);
    }
}