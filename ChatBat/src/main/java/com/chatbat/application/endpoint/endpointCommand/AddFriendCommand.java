/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.endpoint.endpointCommand;

import com.chatbat.application.endpoint.Endpoint;
import com.chatbat.domain.User;
import com.chatbat.service.ChatService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niels
 */
public class AddFriendCommand extends EndpointCommand {
     
    public AddFriendCommand(Endpoint p, ChatService service) {
        this.service = service;
        endpoint = p;
        mapper = new ObjectMapper();
    }

    @Override
    public String execute(String message) throws EndpointException {
        JsonNode root = null;
        try {
             root = mapper.readTree(message);
        } catch (IOException ex) {
            Logger.getLogger(AddFriendCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JsonNode emailNode = root.path("email");
        
        boolean found = false;
        if(!emailNode.isMissingNode()) {
            long userid = endpoint.getUserId();
            String email = emailNode.asText();
            
            User user = service.getUser(userid);
            User friend = service.getUserByMail(email);
            
            if(friend != null) {
                found = true;
                user.addFriend(friend);
            }
            
        } else {
            throw new EndpointException("Node is missing");
        }
        
        ObjectNode resultNode = mapper.createObjectNode();
        resultNode.put("action", "addedFriend");
        resultNode.put("found", found);
        
        return resultNode.toString();
    }
    
}
