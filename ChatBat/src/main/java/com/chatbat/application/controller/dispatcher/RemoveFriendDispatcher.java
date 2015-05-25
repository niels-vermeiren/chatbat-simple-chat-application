/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.dispatcher;

import com.chatbat.application.controller.command.CommandException;
import com.chatbat.application.controller.command.RemoveFriendCommand;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;

/**
 *
 * @author niels
 */

@DispatcherAnnotation(dispatcher = "removeFriend")
public class RemoveFriendDispatcher extends Dispatcher {

    
    @Override
    public void execute() throws ServletException, IOException {
        response.setContentType("application/json");
        try {
            new RemoveFriendCommand(request, response).execute();
        } catch (CommandException ex) {
            Logger.getLogger(RemoveFriendDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
