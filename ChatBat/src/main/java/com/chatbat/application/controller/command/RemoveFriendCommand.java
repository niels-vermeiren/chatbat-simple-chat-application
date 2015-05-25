/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.command;

import com.chatbat.domain.User;
import com.chatbat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author niels
 */
public class RemoveFriendCommand extends Command{
    
    public RemoveFriendCommand(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    
    @Override
    public void execute() throws CommandException {
        
        ChatService service = (ChatService) request.getServletContext().getAttribute("service");
        String emailUser =request.getParameter("email");
        
        User user =(User) request.getSession().getAttribute("user");
        User friend = service.getUserByMail(emailUser);
        boolean success = false;
  
        if(service.userHasFriend(user, friend)) {
            user.removeFriend(friend);
            friend.removeFriend(user);
            success = true;
        } 
        
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", success);
        try {
            String out = mapper.writeValueAsString(responseMap);
            
            response.getWriter().write(out);
        } catch (IOException ex) {
            Logger.getLogger(RemoveFriendCommand.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
    
}
