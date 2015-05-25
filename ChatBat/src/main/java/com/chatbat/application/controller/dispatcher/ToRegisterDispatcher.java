/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.dispatcher;

import java.io.IOException;
import javax.servlet.ServletException;

/**
 *
 * @author niels
 */
@DispatcherAnnotation(dispatcher = "toRegister")
public class ToRegisterDispatcher extends Dispatcher {

    @Override
    public void execute() throws ServletException, IOException {
        forward("WEB-INF/jsp/register.jsp");
    }
    
}
