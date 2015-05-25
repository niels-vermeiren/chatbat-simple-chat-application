/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.command;

import com.chatbat.domain.DomainException;
import javax.servlet.http.HttpServletRequest;
import com.chatbat.domain.User;
import com.chatbat.service.ChatService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author niels
 */
public class LoginCommand extends Command {

    public LoginCommand(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public void execute() throws CommandException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.equals("")) {
            this.addErrorMessage("Email field cannot be empty");
        }

        if (password == null || password.equals("")) {
            this.addErrorMessage("Password field cannot be empty");
        }

        ChatService service = (ChatService) request.getServletContext().getAttribute("service");
        User user = service.getUserByMail(email);

        try {
            if (user == null || !user.isPasswordCorrect(password)) {
                this.addErrorMessage("Invalid user credentials");
            }

            if (getErrors().size() > 0) {
                request.setAttribute("hasErrors", true);
                throw new CommandException("There are errors");
            }

            //If you have added a friend who has not added you back, remove this friend on logout and login
            List<User> usersToRemove = new ArrayList<>();

            for (User friend : user.getFriends()) {
                if (!friend.getFriends().contains(user)) {
                    usersToRemove.add(friend);
                }
            }

            for (User friend : usersToRemove) {
                user.removeFriend(friend);
            }

            remember(request, response, email);

            request.getSession(true).setAttribute("user", user);

        } catch (DomainException ex) {
            request.setAttribute("hasErrors", true);
            this.addErrorMessage("Something went wrong");
            throw new CommandException("Something went wrong");
        }

    }

}
