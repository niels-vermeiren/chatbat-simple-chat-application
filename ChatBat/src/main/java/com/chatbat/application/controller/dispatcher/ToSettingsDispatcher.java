/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.dispatcher;

import java.io.IOException;
import javax.servlet.ServletException;

/**
 *
 * @author niels
 */
@DispatcherAnnotation(dispatcher = "toSettings")
public class ToSettingsDispatcher extends Dispatcher {

    @Override
    public void execute() throws ServletException, IOException {
        if(request.getSession().getAttribute("user") != null) {
            request.setAttribute("location", "settings");
            forward("WEB-INF/jsp/settings.jsp");
        } else {
            forward("WEB-INF/jsp/login.jsp");
        }
    }
}
