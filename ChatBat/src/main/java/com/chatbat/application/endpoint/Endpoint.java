package com.chatbat.application.endpoint;

import com.chatbat.application.endpoint.endpointCommand.EndpointCommand;
import com.chatbat.application.endpoint.endpointCommand.EndpointException;
import com.chatbat.application.endpoint.endpointCommand.RequestHelper;
import com.chatbat.service.ChatService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/wsConnect", configurator = EndpointConfigurator.class)
public class Endpoint {

    private Session outbound;
    private final EndpointManager manager;
    private long userId;
    private ObjectMapper mapper = new ObjectMapper();
    private ChatService service;

    public Endpoint(EndpointManager manager, ChatService chatService) {
        this.manager = manager;
        service = chatService;
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException {
        
        EndpointCommand epc = RequestHelper.getCommand(message, service, this);
        
        String msge = null;
        try {
            msge = epc.execute(message);
        } catch (EndpointException ex) {
            Logger.getLogger(Endpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JsonNode root = mapper.readTree(msge);
        
        if(msge != null) {
            String action = root.path("action").textValue();
            if(action.equals("statusChange")) {
                manager.broadcastToFriends(this, msge);
            }
            if(action.equals("getFriends") || action.equals("addedFriend")) {
                manager.send(this, msge);
            }
            if(action.equals("messageSent")) {
                manager.send(this, msge);
                manager.sendToUser(root.path("userTo").asLong(), msge);
            }
        }
    }

    @OnOpen
    public void onOpen(Session peer) {
        outbound = peer;
        manager.offer(this);
    }

    @OnClose
    public void onClose(Session peer) {
        manager.remove(this);
        outbound = null;
    }

    public void send(String data) {
        try {
            if (outbound != null && outbound.isOpen()) {
                outbound.getBasicRemote().sendText(data);
            }
        } catch (IOException e) {
            try {
                outbound.close();
            } catch (IOException ex) {
            }
        }
    }
    
    public long getUserId() {
        return userId;
    }
    
    public void setUserId(long id) {
        userId = id;
    }
    
}
