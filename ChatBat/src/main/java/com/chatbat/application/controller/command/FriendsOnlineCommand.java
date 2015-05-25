/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.command;

import com.chatbat.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
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
public class FriendsOnlineCommand extends Command {

    public FriendsOnlineCommand(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    
    @Override
    public void execute() throws CommandException {
        ObjectMapper mapper = new ObjectMapper();
        User user = (User) request.getSession().getAttribute("user");
        Collection<User> friends = user.getFriends();
        int amount = 0;
        
        for(User friend: friends) {
            amount += friend.getState().equals("Online")?1:0;
        }
        
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("amount", amount);
        
        try {
            response.getWriter().write(mapper.writeValueAsString(resultMap));
        } catch (IOException ex) {
            Logger.getLogger(FriendsOnlineCommand.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
}
