/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.command;

import com.chatbat.domain.User;
import com.chatbat.service.ChatService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author niels
 */
public class ToHomeCommand extends Command {

    public ToHomeCommand(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    
    @Override
    public void execute() throws CommandException {
        ChatService service = (ChatService) request.getServletContext().getAttribute("service");
        User user =(User) request.getSession().getAttribute("user");
        if(user == null) {
            throw new CommandException("Not logged in");
        }
        request.setAttribute("userId", user.getId());
        request.setAttribute("messages", service.getMessagesUser(user));
    }
    
}
