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
public enum EndpointCommandEnum {
    CHANGESTATUSCOMMAND("changeStatus", "com.chatbat.application.endpoint.endpointCommand.ChangeStatusCommand"),
    GETFRIENDSCOMMAND("getFriends", "com.chatbat.application.endpoint.endpointCommand.GetFriendsCommand"),
    SENDMESSAGECOMMAND("sendMessage", "com.chatbat.application.endpoint.endpointCommand.SendMessageCommand"), 
    MARKMESSAGESASREADCOMMAND("markMessagesAsRead", "com.chatbat.application.endpoint.endpointCommand.MarkMessagesAsReadCommand"),
    ADDFRIENDCOMMAND("addFriend", "com.chatbat.application.endpoint.endpointCommand.AddFriendCommand");
    
    private final String action;
    private final String path;
    
    EndpointCommandEnum(String action, String path) {
        this.action = action;
        this.path = path;
    }
    
    public String getPath() { return path; }
    public String getAction() { return action; }
    
}
