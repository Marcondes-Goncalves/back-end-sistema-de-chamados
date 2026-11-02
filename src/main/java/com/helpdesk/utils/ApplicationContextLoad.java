package com.helpdesk.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * Classe para carregar o contexto da aplicação Spring e fornecer acesso a ele em qualquer parte do código.
 * Isso é útil para obter beans do Spring fora do contexto de injeção de dependência, como em classes utilitárias ou estáticas.
 * ApplicationContextLoad
 */
@Component 
public class ApplicationContextLoad implements ApplicationContextAware {
    
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextLoad.applicationContext = applicationContext;
    }

       public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
