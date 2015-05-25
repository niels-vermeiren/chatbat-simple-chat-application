/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.domain;

/**
 *
 * @author niels
 */
public enum UserState {
    ONLINE("Online"),
    OFFLINE("Offline"),
    AWAY("Away");
    
    private final String status;
    
    UserState(String status) {
        this.status = status;
    }
    
    public String getStatus() {
        return this.status;
    }
    
}
