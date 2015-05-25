/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.command;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author niels
 */
public abstract class Command {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    
    private List<String> errors;

    public static final String ERRORS_ATTR = "errors";
    public static final String REQUIRED_FIELDS_ATTR = "requiredFields";

    public Command(HttpServletRequest request, HttpServletResponse response) {
        this();
        this.request = request;
        this.response = response;
    }

    private Command() {
    }

    public abstract void execute() throws CommandException;

    public void addErrorMessage(String errorMessage) {
        getErrorInstance().add(errorMessage);
    }

    public List<String> getErrors() {
        return getErrorInstance();
    }

    private List<String> getErrorInstance() {
        if (errors == null) {
            errors = (List<String>) request.getAttribute(ERRORS_ATTR);
            if (errors == null) {
                errors = new ArrayList<>();
                request.setAttribute(ERRORS_ATTR, errors);
            }
        }
        return errors;
    }

    public Cookie getCookie(String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    protected void remember(HttpServletRequest request, HttpServletResponse response, String email) {
        boolean rememberMe = request.getParameter("remember") != null;

        if (rememberMe) {
            Cookie cookie = new Cookie("email", email);
            response.addCookie(cookie);
        } else {
            Cookie cookie = getCookie("email");
            if (cookie != null) {
                cookie.setMaxAge(0);
            }
        }
    }

}
