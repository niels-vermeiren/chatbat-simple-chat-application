/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.dispatcher;

import com.chatbat.application.controller.command.ToLoginCommand;
import java.io.IOException;
import javax.servlet.ServletException;

/**
 *
 * @author niels
 */
@DispatcherAnnotation(dispatcher = "toLogin")
public class ToLoginDispatcher extends Dispatcher {

    @Override
    public void execute() throws ServletException, IOException {
        new ToLoginCommand(request, response).execute();
        forward("WEB-INF/jsp/login.jsp");
    }
    
}
