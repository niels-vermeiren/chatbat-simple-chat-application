/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.database.memory;

import com.chatbat.database.MapperLocator;

/**
 *
 * @author niels
 */
public class MemoryMapperLocator extends MapperLocator {
    
    public MemoryMapperLocator() {
        userMapper = new MemoryUserMapper();
        messageMapper = new MemoryMessageMapper();
    }
    
}
