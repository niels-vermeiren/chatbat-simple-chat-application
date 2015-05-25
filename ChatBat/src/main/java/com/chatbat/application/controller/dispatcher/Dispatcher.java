/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.dispatcher;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A dispatcher is responsible for dispatching the request sent by the
 * FrontController to one or more commands.
 *
 * <p>Classes which inherit from Dispatcher must implement the execute method.
 */
public abstract class Dispatcher {

    //The request object that is passed by the Front Controller
    protected HttpServletRequest request = null;
    //The response object that is passed by the Front Controller
    protected HttpServletResponse response = null;

    public void init(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public abstract void execute() throws ServletException, IOException;

    protected void forward(String target) throws java.io.IOException, ServletException {
        request.getRequestDispatcher(target).forward(request, response);
    }

    protected void include(String target) throws java.io.IOException, ServletException {
        request.getRequestDispatcher(target).include(request, response);
    }

    protected void redirectToDispatcher(Dispatcher dispatcher) throws ServletException, IOException {
        dispatcher.init(request, response);
        dispatcher.execute();
    }
}
