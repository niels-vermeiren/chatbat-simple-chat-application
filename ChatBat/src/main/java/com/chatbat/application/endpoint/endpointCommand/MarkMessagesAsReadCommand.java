/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.endpoint.endpointCommand;

import com.chatbat.application.endpoint.Endpoint;
import com.chatbat.domain.Message;
import com.chatbat.domain.User;
import com.chatbat.service.ChatService;
import com.chatbat.service.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niels
 */
public class MarkMessagesAsReadCommand extends EndpointCommand {

    public MarkMessagesAsReadCommand(Endpoint p, ChatService service) {
        this.service = service;
        this.endpoint = p;
        this.mapper = new ObjectMapper();
    }

    @Override
    public String execute(String message) throws EndpointException {
        JsonNode root = null;
        try {
            root = mapper.readTree(message);
        } catch (IOException ex) {
            Logger.getLogger(MarkMessagesAsReadCommand.class.getName()).log(Level.SEVERE, null, ex);
        }

        JsonNode userFrom = root.path("fromUser");
        JsonNode userTo = root.path("toUser");

        if (!userFrom.isMissingNode() && !userTo.isMissingNode()) {
            User from = null, to = null;

            from = service.getUser(userFrom.asLong());
            to = service.getUser(userTo.asLong());

            Map<Long, Collection<Message>> messages = service.getMessagesUser(to);

            Collection<Message> messagesWithUser = messages.get(from.getId());
            
            if (messagesWithUser != null) {
                for (Message m : messagesWithUser) {
                    if (m.getFromUser().equals(from)) {
                        try {
                            m.setIsRead(true);
                            service.updateMessage(m);
                        } catch (ServiceException ex) {
                            throw new EndpointException("Error updating message");
                        }
                    }
                }
            }
        } else {
            throw new EndpointException("Nodes are missing");
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("action", "null");
        String response = null;
        try {
            response = mapper.writeValueAsString(responseMap);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(MarkMessagesAsReadCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

}
