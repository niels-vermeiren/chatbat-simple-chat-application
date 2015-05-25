/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.endpoint.endpointCommand;

import com.chatbat.application.endpoint.Endpoint;
import com.chatbat.service.ChatService;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niels
 */
public class EndpointCommandFactory {
    
    public static EndpointCommand buildCommand(String action, ChatService service, Endpoint endpoint) {
        EndpointCommand instance = null;
        String path = null;
        for(EndpointCommandEnum commandEnum: EndpointCommandEnum.values()) {
            System.out.println(action);
            if(commandEnum.getAction().equals(action)) {
                path = commandEnum.getPath();
            }
        }
        System.out.println(path);
        try {
            Class<?> clazz = Class.forName(path);
            Class[] classes = { endpoint.getClass(), service.getClass()};
            Constructor<?> constr = clazz.getConstructor(classes);
            Object[] objects = {endpoint, service};
            instance = (EndpointCommand) constr.newInstance(objects);
            
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(EndpointCommandFactory.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return instance;
    }
    
}
