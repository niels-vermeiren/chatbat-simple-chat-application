/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.dispatcher;

import com.chatbat.application.controller.command.CommandException;
import com.chatbat.application.controller.command.RegisterCommand;
import java.io.IOException;
import javax.servlet.ServletException;

/**
 *
 * @author niels
 */
@DispatcherAnnotation(dispatcher = "register")
public class RegisterDispatcher extends Dispatcher{

    @Override
    public void execute() throws ServletException, IOException {
        try {
            new RegisterCommand(request, response).execute();     
            redirectToDispatcher(new ToLoginDispatcher());
        } catch (CommandException ex) {
            forward("WEB-INF/jsp/register.jsp");
        }
    }
    
}
