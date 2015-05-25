/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller;

import com.chatbat.application.controller.dispatcher.Dispatcher;
import com.chatbat.application.controller.dispatcher.DispatcherFactory;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author niels
 */
@WebServlet(name = "ChatController", urlPatterns = {"/ChatController"})
public class ChatController extends HttpServlet {

    private static final String defaultDispatcher = "login";
    private static final String errorJSP = "WEB-INF/jsp/errors.jsp";
  
    @Override
    public void init() throws ServletException {
       
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            System.out.println(getDispatcherName(request));
            Dispatcher dispatcher = DispatcherFactory.getDispatcher(getDispatcherName(request));
            dispatcher.init(request, response);
            dispatcher.execute();
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.setAttribute("exception", ex);
            throw new RuntimeException(ex);
            //request.getRequestDispatcher(errorJSP).forward(request, response);
        }
    }

     /**
     * This tries to get the command (dispatcher) name from the request, first
     * looking for a parameter, and then an attribute with the name
     * <i>'dispatcher'</i>.
     *
     * @param request
     * @return non-null and non-empty command name.
     * @throws ServletException Whenever there is no command in the request, an
     * Exception to that effect will be thrown.
     */
    protected String getDispatcherName(HttpServletRequest request) throws ServletException {
        String parameterName = "dispatcher";
        String dispatcherName = request.getParameter(parameterName);
        if (dispatcherName != null && !dispatcherName.isEmpty()) {
            return dispatcherName;
        }
        dispatcherName = (String) request.getAttribute(parameterName);
        if (dispatcherName != null && !dispatcherName.isEmpty()) {
            return dispatcherName;
        }
        return defaultDispatcher;
    }

    private boolean isUserAuthenticated(HttpServletRequest request) {
        return request.getSession().getAttribute("user") != null;
    }
    
}
