/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.database;

import com.chatbat.domain.Message;
import com.chatbat.domain.User;

/**
 *
 * @author niels
 */
public abstract class MapperLocator {

    private static MapperLocator instance;
    protected Mapper<User> userMapper;
    protected Mapper<Message> messageMapper;

    public static void loadLocator(MapperLocator locator) {
        instance = locator;
    }

    public static Mapper<User> userMapper() {
        return instance.userMapper;
    }
    
    public static Mapper<Message> messageMapper() {
        return instance.messageMapper;
    }
   
}
