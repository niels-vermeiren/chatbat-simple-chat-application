package com.chatbat.application.endpoint;

import com.chatbat.service.ChatService;
import javax.websocket.server.ServerEndpointConfig.Configurator;

public class EndpointConfigurator extends Configurator {

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return (T) new Endpoint(EndpointManager.getInstance(), ChatService.getInstance());
    }
}
