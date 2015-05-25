/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.endpoint.endpointCommand;

/**
 *
 * @author niels
 */
public class EndpointException extends Exception {
    public EndpointException() {
        super();
    }

    public EndpointException(String message) {
        super(message);
    }

    public EndpointException(Throwable t) {
        super(t);
    }

    public EndpointException(String message, Throwable t) {
        super(message, t);
    }
}
