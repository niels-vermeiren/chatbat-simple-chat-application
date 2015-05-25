/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.database.memory;

import com.chatbat.database.Mapper;
import com.chatbat.database.MapperException;
import com.chatbat.domain.Message;
import com.chatbat.domain.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author niels
 */
public class MemoryMessageMapper implements Mapper<Message> {

    private final Map<Long, Message> messages;
    private static long maxId = 1;

    public MemoryMessageMapper() {
        messages = new HashMap<>();
    }

    @Override
    public long store(Message message) throws MapperException {
        //TODO: exception als user al bestaat
        long id = maxId++;
        message.setId(id);
        messages.put(id, message);
        return id;
    }

    @Override
    public void update(Message message) throws MapperException {
        Message toUpdate = messages.get(message.getId());
        if (toUpdate == null) {
            throw new MapperException("Message with id '" + message.getId() + "' not found");
        }
        messages.put(message.getId(), message);
    }

    @Override
    public void delete(long id) throws MapperException {
        Message toDelete = messages.get(id);
        if (toDelete == null) {
            throw new MapperException("Message with id '" + id + "' not found");
        }
        messages.remove(id);
    }

    @Override
    public Message find(long id) throws MapperException {
        Message message = messages.get(id);
        if (message == null) {
            throw new MapperException("User with id '" + id + "' not found");
        }
        return message;
    }

    @Override
    public Collection<Message> findAll() {
        return messages.values();
    }

    @Override
    public void clear() throws MapperException {
        messages.clear();
    }

    @Override
    public Message findByFieldName(String field, String value) throws MapperException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
