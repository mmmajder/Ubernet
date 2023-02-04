package com.example.ubernet.handler;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class ContextForChat  implements ApplicationContextAware {

    @Autowired
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }
}
