/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.database.memory;

import com.chatbat.database.Mapper;
import com.chatbat.database.MapperException;
import com.chatbat.domain.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author niels
 */
public class MemoryUserMapper implements Mapper<User> {

    private final Map<Long, User> users;
    private static long maxId;

    public MemoryUserMapper() {
        users = new HashMap<>();

    }

    @Override
    public long store(User user) throws MapperException {
        //TODO: exception als user al bestaat
        long id = maxId++;
   
        
        user.setId(id);
        users.put(id, user);
        return id;
    }

    @Override
    public void update(User user) throws MapperException {
        User toUpdate = users.get(user.getId());
        if (toUpdate == null) {
            throw new MapperException("User with id '" + user.getId() + "' not found");
        }
        users.put(user.getId(), user);
    }

    @Override
    public void delete(long id) throws MapperException {
        User toDelete = users.get(id);
        if (toDelete == null) {
            throw new MapperException("User with id '" + id + "' not found");
        }
        users.remove(id);
    }

    @Override
    public User find(long id) throws MapperException {
        
        User user = users.get(id);
        if (user == null) {
            throw new MapperException("User with id '" + id + "' not found");
        }
        return user;
    }
    
    public User findByEmail(String email) {
        for(User u: users.values()) {
            if(u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public void clear() throws MapperException {
        users.clear();
    }

    @Override
    public User findByFieldName(String field, String value) throws MapperException {
        User u = null;
        switch(field) {
            case "email": 
                u = findByEmail(value);
                break;
            default:
                break;
        }
        return u;
    }

}
