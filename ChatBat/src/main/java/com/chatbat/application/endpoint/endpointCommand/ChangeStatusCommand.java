/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.endpoint.endpointCommand;

import com.chatbat.application.endpoint.Endpoint;
import com.chatbat.domain.User;
import com.chatbat.domain.UserState;
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
public class ChangeStatusCommand extends EndpointCommand {

    public ChangeStatusCommand(Endpoint p, ChatService service) {
        this.service = service;
        endpoint = p;
        mapper = new ObjectMapper();
    }
    
    @Override
    public String execute(String message) throws EndpointException {
        User u = null;
        JsonNode root = null;
        try {
            root = mapper.readTree(message);
        } catch (IOException ex) {
            Logger.getLogger(ChangeStatusCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        JsonNode userIdNode = root.path("userId");
        if (!userIdNode.isMissingNode()) {
            long userId = userIdNode.asLong();
            endpoint.setUserId(userId);
            
            u = service.getUser(userId);
            UserState state = null;
            for(UserState userState:UserState.values()) {
                if(userState.getStatus().equals(root.path("status").asText())) {
                    state = userState;
                }
            }
            
            service.changeUserState(u, state);
            
            System.out.println("USERID: "+userId);
            System.out.println("USERSTATUS: "+service.getUserState(u));
        } else {
            throw new EndpointException("Json node not present");
        }
        
        ObjectNode resultNode = mapper.createObjectNode();
        resultNode.put("action", "statusChange");
        resultNode.put("userName", u.getName());
        resultNode.put("userId", u.getId());
        resultNode.put("status", u.getState());
        return resultNode.toString();
    }
    
}
