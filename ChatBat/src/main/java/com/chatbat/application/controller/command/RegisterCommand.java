/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.command;

import com.chatbat.domain.DomainException;
import com.chatbat.domain.User;
import com.chatbat.service.ChatService;
import com.chatbat.service.ServiceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author niels
 */
public class RegisterCommand extends Command {

    private final static String ALLOWED_CHARS = "[A-Za-z0-9_\\- ]";

    public RegisterCommand(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public void execute() throws CommandException {

        ChatService service = (ChatService) request.getServletContext().getAttribute("service");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String passwordAgain = request.getParameter("passwordAgain");
        
        if (name == null || name.equals("")) {
            addErrorMessage("Name can't be empty");
        } else {

            if (name.length() < 3 || name.length() > 20) {
                addErrorMessage("Names must contain 3 to 20 characters");
            }

            if (name.replaceAll(ALLOWED_CHARS, "").length() > 0) {
                addErrorMessage("Name contains an invalid character");
            }
        }

        //Email
        if (email == null || email.equals("")) {
            addErrorMessage("Email can't be empty");
        } else {
            if (service.getUserByMail(email) != null) {
                addErrorMessage("Email has already been taken");
            }
        }

        //Password
        if (password == null || password.equals("")) {
            addErrorMessage("Password can't be empty");

        } else if (password.length() < 6) {
            addErrorMessage("Password must be at least 6 characters");
        }
        
        if(passwordAgain == null || passwordAgain.equals("")) {
            addErrorMessage("Please retype your password");
        } else if (!password.equals(passwordAgain)) {
            addErrorMessage("Passwords don't match");
        }

        if (getErrors().size() > 0) {
            request.setAttribute("hasErrors", true);
            throw new CommandException("There are errors");
        }

        try {
            User user = new User(name, email, password);
            service.addUser(user);
        } catch (DomainException | ServiceException ex) {
            throw new CommandException();
        }

    }

}
