/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.endpoint.endpointCommand;

import com.chatbat.application.endpoint.Endpoint;
import com.chatbat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author niels
 */
public abstract class EndpointCommand {
    protected ChatService service;
    protected Endpoint endpoint;
    protected ObjectMapper mapper;
    public abstract String execute(String message) throws EndpointException;
}
