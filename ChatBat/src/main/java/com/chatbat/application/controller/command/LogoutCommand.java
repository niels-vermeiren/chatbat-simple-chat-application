/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.command;

import com.chatbat.domain.User;
import com.chatbat.service.ChatService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author niels
 */
public class LogoutCommand extends Command {

    public LogoutCommand(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public void execute() throws CommandException {

        //If you have added a friend who has not added you back, remove this friend on logout and login
        ChatService service = (ChatService) request.getServletContext().getAttribute("service");
        User user = (User) request.getSession().getAttribute("user");

        
        if (user != null) {
            List<User> usersToRemove = new ArrayList<>();
            for (User friend : user.getFriends()) {
                if (!friend.getFriends().contains(user)) {
                    usersToRemove.add(friend);
                }
            }

            for (User friend : usersToRemove) {
                user.removeFriend(friend);
            }
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        Cookie emailCookie = getCookie("email");
        if (emailCookie != null) {
            request.setAttribute("email", emailCookie.getValue());
        }

    }

}
