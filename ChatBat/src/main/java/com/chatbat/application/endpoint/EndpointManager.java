package com.chatbat.application.endpoint;

import com.chatbat.domain.User;
import com.chatbat.service.ChatService;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EndpointManager {

    private final Queue<Endpoint> endpoints = new ConcurrentLinkedQueue<>();

    private EndpointManager() {

    }

    public static EndpointManager getInstance() {
        return EndpointManagerHolder.getInstance();
    }

    private final static class EndpointManagerHolder {

        private static final EndpointManager INSTANCE = new EndpointManager();

        private static EndpointManager getInstance() {
            return INSTANCE;
        }
    }

    public void broadcast(Endpoint sender, String data) {
        for (Endpoint endpoint : endpoints) {
            if (endpoint == sender) {
                continue;
            }
            endpoint.send(data);
        }
    }
    
    public void broadcastToFriends(Endpoint sender, String data) {
        ChatService service = ChatService.getInstance();
        long id = sender.getUserId();
        User u = service.getUser(id);
        Collection<User> friends = u.getFriends();
        for(User user:friends) {
            for(Endpoint p:endpoints) {
                if(p.getUserId() == user.getId()) {
                    p.send(data);
                }
            }
        }
    }

    public void broadcast(String data) {
        System.out.println(data);
        for (Endpoint endpoint : endpoints) {
            endpoint.send(data);
        }
    }
    
    public void send(Endpoint receiver, String data) {
        receiver.send(data);
    }
    
    public void sendToUser(long userId, String data) {
        for (Endpoint endpoint : endpoints) {
            if (endpoint.getUserId() == userId) {
                endpoint.send(data);
            }
        }
    }

    public void offer(Endpoint endpoint) {
        endpoints.offer(endpoint);
    }

    public void remove(Endpoint endpoint) {
        endpoints.remove(endpoint);
    }

    public Queue<Endpoint> getEndpoints() {
        return endpoints;
    }
    
}
