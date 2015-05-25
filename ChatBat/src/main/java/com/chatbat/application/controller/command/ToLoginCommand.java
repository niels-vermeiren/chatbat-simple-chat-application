/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.command;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author niels
 */
public class ToLoginCommand extends Command {

    public ToLoginCommand(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public void execute() {
        Cookie emailCookie = getCookie("email");
        if (emailCookie != null) {
            request.setAttribute("email", emailCookie.getValue());
        }
    }
}
