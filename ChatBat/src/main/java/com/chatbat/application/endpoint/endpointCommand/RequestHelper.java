/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.endpoint.endpointCommand;

import com.chatbat.application.endpoint.Endpoint;
import com.chatbat.service.ChatService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 *
 * @author niels
 */
public class RequestHelper {
    private static ObjectMapper mapper = new ObjectMapper();
    
    public static EndpointCommand getCommand(String message, ChatService service, Endpoint p) throws IOException {
        
        JsonNode root = mapper.readTree(message);
        JsonNode actionNode = root.path("action");
        System.out.println(actionNode.asText());
        if(!actionNode.isMissingNode()) {
            String action = actionNode.asText();
            return EndpointCommandFactory.buildCommand(action, service, p);
        }
        return null;
    }
    
}
