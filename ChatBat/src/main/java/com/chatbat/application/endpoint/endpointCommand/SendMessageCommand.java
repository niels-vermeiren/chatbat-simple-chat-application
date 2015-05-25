/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.endpoint.endpointCommand;

import com.chatbat.application.endpoint.Endpoint;
import com.chatbat.domain.User;
import com.chatbat.service.ChatService;
import com.chatbat.service.ServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 *
 * @author niels
 */
public class SendMessageCommand extends EndpointCommand {

    public SendMessageCommand(Endpoint p, ChatService service) {
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
            Logger.getLogger(SendMessageCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JsonNode userFrom = root.path("userFrom");
        JsonNode userTo = root.path("userTo");
        JsonNode msge = root.path("message");
        User fromUser = null, toUser = null;
        String messageSent = null;
        if(!msge.isMissingNode() && !userTo.isMissingNode() && !userFrom.isMissingNode()) {
            fromUser = service.getUser(userFrom.asLong());
            toUser = service.getUser(userTo.asLong());
            messageSent = Jsoup.clean(msge.asText(), Whitelist.basic());
            
            try {
                service.sendMessage(messageSent, fromUser, toUser, !toUser.getState().equals("Offline"));
            } catch (ServiceException ex) {
                throw new EndpointException("Could not store message");
            }
        } else {
            throw new EndpointException("There are missing nodes");
        }
        
        ObjectNode result = mapper.createObjectNode();
        result.put("action", "messageSent");
        result.put("userFrom", fromUser.getId());
        result.put("nameUserFrom", fromUser.getName());
        result.put("userTo", toUser.getId());
        result.put("message", messageSent);
        return result.toString();
    }
    
}
