/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.application.controller.command;

/**
 *
 * @author niels
 */
public class CommandException extends Exception {

    private static final long serialVersionUID = 1L;

    public CommandException() {
        super();
    }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(Throwable exception) {
        super(exception);
    }

    public CommandException(String message, Throwable exception) {
        super(message, exception);
    }

}
