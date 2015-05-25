/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.dispatcher;

import com.chatbat.application.controller.command.CommandException;
import com.chatbat.application.controller.command.LoginCommand;
import java.io.IOException;
import javax.servlet.ServletException;

/**
 *
 * @author niels
 */
@DispatcherAnnotation(dispatcher = "login")
public class LoginDispatcher extends Dispatcher {
     @Override
    public void execute() throws ServletException, IOException {
        try {
            new LoginCommand(request, response).execute();
            super.redirectToDispatcher(new ToHomeDispatcher());
        } catch (CommandException ex) {
            forward("WEB-INF/jsp/login.jsp");
        }
    }
}
