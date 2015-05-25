/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.endpoint.endpointCommand;

import com.chatbat.application.endpoint.Endpoint;
import com.chatbat.domain.User;
import com.chatbat.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niels
 */
public class GetFriendsCommand extends EndpointCommand {

    public GetFriendsCommand(Endpoint p, ChatService service) {
        this.service = service;
        endpoint = p;
        mapper = new ObjectMapper();
    }

    @Override
    public String execute(String message) throws EndpointException {
        
        JsonNode root = null;
        List<User> friends = new ArrayList<>();
        //Read message
        try {
            root = mapper.readTree(message);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        JsonNode userIdNode = root.path("userId");
        JsonNode filterNode = root.path("filterFriends");

        //If nodes are not missing, get friends
        if (!userIdNode.isMissingNode() && !filterNode.isMissingNode()) {

            //Get friends user
            String filter = filterNode.asText();
            long userId = userIdNode.asLong();
            endpoint.setUserId(userId);
            User user = service.getUser(userId);
            List<User> userFriends = new ArrayList(user.getFriends());
            
            //Filter friends based on filterNode
            for (User usr : userFriends) {
                if (usr.getName().toLowerCase().contains(filter.trim().toLowerCase()) || filter.trim().equals("")) {
                    friends.add(usr);
                }
            }
        } else {
            throw new EndpointException("Nodes are missing");
        }
        
        Collections.sort(friends);
        
        //Generate response
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("action", "getFriends");
        responseMap.put("friends", friends);
        String response = null;
        try {
            response = mapper.writeValueAsString(responseMap);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(GetFriendsCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return response;
    }

}
