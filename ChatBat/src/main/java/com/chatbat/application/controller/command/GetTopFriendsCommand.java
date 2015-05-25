/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.command;

import com.chatbat.domain.User;
import com.chatbat.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author niels
 */
public class GetTopFriendsCommand extends Command {

    public GetTopFriendsCommand(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public void execute() throws CommandException {
        ObjectMapper mapper = new ObjectMapper();
        ChatService service = (ChatService) request.getServletContext().getAttribute("service");
        User user = (User) request.getSession().getAttribute("user");
        Map<User, Integer> map = service.getTopFriends(user);
        Map<String, Integer> top5 = new LinkedHashMap<>();
        
        //Prepare response
        Iterator<Entry<User, Integer>> mapIterator = map.entrySet().iterator();
        ObjectNode root = mapper.createObjectNode();
        ArrayNode friends = mapper.createArrayNode();
        
        for (int i = 0; i != 5; i++) {
            try {
                Entry<User, Integer> entry = mapIterator.next();
                ObjectNode friend = mapper.createObjectNode();
                friend.put("name", entry.getKey().getName());
                friend.put("email", entry.getKey().getEmail());
                friend.put("amount", entry.getValue());
                friends.add(friend);
            } catch (NoSuchElementException ex) {
                break;
            }
        }
        root.put("friends", friends);
        
        try {
         
            System.out.println(root.toString());
            response.getWriter().write(root.toString());
        } catch (IOException ex) {
            Logger.getLogger(GetTopFriendsCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
