/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application;

import com.chatbat.application.endpoint.EndpointManager;
import com.chatbat.service.ChatService;
import com.chatbat.service.ServiceException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author niels
 */
public class ContextListener implements ServletContextListener {

    private ChatService service;
    private EndpointManager manager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        service = ChatService.getInstance();
        sce.getServletContext().setAttribute("service", service);
        manager = EndpointManager.getInstance();
        sce.getServletContext().setAttribute("manager", manager);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            service.close();
        } catch (ServiceException ex) {
            //exit quietly
        }
    }
}
