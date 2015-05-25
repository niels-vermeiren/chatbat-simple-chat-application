/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.service;

import com.chatbat.database.MapperException;
import com.chatbat.database.MapperLocator;
import com.chatbat.database.memory.MemoryMapperLocator;
import com.chatbat.database.memory.MemoryUserMapper;
import com.chatbat.domain.DomainException;
import com.chatbat.domain.MapUtil;
import com.chatbat.domain.Message;
import com.chatbat.domain.User;
import com.chatbat.domain.UserState;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author niels
 */
public class ChatService {

    public static ChatService getInstance() {
        return ChatServiceHolder.getInstance();
    }

    private final static class ChatServiceHolder {

        private static final ChatService INSTANCE = new ChatService();

        private static ChatService getInstance() {
            return INSTANCE;
        }
    }

    private ChatService() {
        MapperLocator.loadLocator(new MemoryMapperLocator());

        User niels = null, simon = null, mark = null;
        try {
            niels = new User("Niels", "vermeiren_niels@hotmail.com", "secret");
            simon = new User("Simon", "simoncek@hotmail.com", "secret");
            mark = new User("Mark", "markthoelen@boerinnekes.be", "secret");
           
        } catch (DomainException ex) {
            Logger.getLogger(MemoryUserMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        niels.addFriend(simon);
        niels.setState(UserState.OFFLINE);
        niels.addFriend(mark);
        mark.setState(UserState.AWAY);
        mark.addFriend(niels);
        simon.addFriend(niels);
        simon.setState(UserState.OFFLINE);
        simon.addFriend(mark);
        mark.addFriend(simon);


        Message mes1 = new Message(niels, simon, "Hoi", true);
        Message mes2 = new Message(simon, niels, "Hoi man.", true);
        Message mes3 = new Message(niels, simon, "Alles goed?.", true);
        Message mes4 = new Message(niels, mark, "Ej jow mark!", false);

        try {
            MapperLocator.userMapper().store(niels);
            MapperLocator.userMapper().store(simon);
            MapperLocator.userMapper().store(mark);
    
            MapperLocator.messageMapper().store(mes1);
            MapperLocator.messageMapper().store(mes2);
            MapperLocator.messageMapper().store(mes3);
            MapperLocator.messageMapper().store(mes4);
        } catch (MapperException ex) {
            Logger.getLogger(MemoryUserMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addUser(User user) throws ServiceException {
        try {
            MapperLocator.userMapper().store(user);
        } catch (MapperException ex) {
            throw new ServiceException(ex);
        }
    }

    public void updateUser(User user) throws ServiceException {
        try {
            MapperLocator.userMapper().update(user);
        } catch (MapperException ex) {
            throw new ServiceException(ex);
        }
    }

    public void deleteUser(long id) throws ServiceException {
        try {
            MapperLocator.userMapper().delete(id);
        } catch (MapperException ex) {
            throw new ServiceException(ex);
        }
    }

    public User getUserByMail(String email) {
        User u = null;
        try {
            u = MapperLocator.userMapper().findByFieldName("email", email);
        } catch (MapperException ex) {
            Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    public User getUser(long id) {
        User u = null;
        try {
            u = MapperLocator.userMapper().find(id);
        } catch (MapperException ex) {
            Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    public long sendMessage(String content, User from, User to, boolean read) throws ServiceException {
        Message message = new Message(from, to, content, read);
        try {
            return MapperLocator.messageMapper().store(message);
        } catch (MapperException ex) {
            throw new ServiceException(ex);
        }
    }

    public Message getMessage(long id) throws ServiceException {
        try {
            return MapperLocator.messageMapper().find(id);
        } catch (MapperException ex) {
            throw new ServiceException(ex);
        }
    }

    public void updateMessage(Message message) throws ServiceException {
        try {
            MapperLocator.messageMapper().update(message);
        } catch (MapperException ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * *
     * Get all messages from a user, messages that have been sent and received.
     * Not very efficient when using a relational database. For now, I have not
     * yet programmed a db.
     *
     * @param user
     * @return Map with the messages, each key is a friend, value is messages
     * from and to this friend
     */
    public Map<Long, Collection<Message>> getMessagesUser(User user) {
        Map<Long, Collection<Message>> messages = new HashMap<>();
        Collection<Message> allMessages = MapperLocator.messageMapper().findAll();

        for (Message m : allMessages) {
            User partner = m.getFromUser().equals(user) ? m.getToUser() : m.getFromUser();
            if (m.getFromUser().equals(user) || m.getToUser().equals(user)) {
                Collection<Message> userMessages = messages.get(partner.getId());
                if (userMessages == null) {
                    Collection<Message> newConversation = new ArrayList<>();
                    newConversation.add(m);
                    messages.put(partner.getId(), newConversation);
                } else {
                    userMessages.add(m);
                }
            }
        }
        return messages;
    }

    /**
     * Not very efficient when using a relational db, for now I'm using memory
     * only so it doesn't matter Get the users and all the messages
     *
     * @param user
     * @return map (linked) with key user and value amount of messages that have been, sorted descending
     * received
     */
    public Map<User, Integer> getTopFriends(User user) {
        Map<User, Integer> result = new HashMap<>();
        for (Message m : MapperLocator.messageMapper().findAll()) {
            if (m.getToUser().equals(user)) {
                if (result.get(m.getFromUser()) == null) {
                    result.put(m.getFromUser(), 1);
                } else {
                    result.put(m.getFromUser(), result.get(m.getFromUser()) + 1);
                }
            }
        }
        return MapUtil.sortDescendingByValue(result);
    }

    public void close() throws ServiceException {
        MapperException localException = null;
        try {
            MapperLocator.userMapper().clear();
        } catch (MapperException ex) {
            localException = ex;
        }
        try {
            MapperLocator.messageMapper().clear();
        } catch (MapperException ex) {
            localException = ex;
        }
        if (localException != null) {
            throw new ServiceException(localException);
        }
    }

    public void changeUserState(User u, UserState state) {
        u.setState(state);
    }

    public String getUserState(User u) {
        return u.getState();
    }

    public boolean userHasFriend(User user, User friend) {
        return user.getFriends().contains(friend);
    }

}
