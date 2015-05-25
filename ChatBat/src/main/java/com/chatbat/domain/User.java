package com.chatbat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class User extends DomainObject implements Comparable<User> {

    private String name;
    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private Collection<User> friends;
    private UserState state;
    

    public User() {

    }

    public User(String name, String email, String password) throws DomainException {
        friends = new ArrayList<>();
        this.name = name;
        this.email = email;
        setHashedPassword(password);
    }

    public void setState(UserState u) {
        this.state = u;
    }

    public String getState() {
        return state.getStatus();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws DomainException {
        if (name == null || name.isEmpty()) {
            throw new DomainException("Name should not be empty");
        }
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws DomainException {
        if (email == null || email.isEmpty()) {
            throw new DomainException("Email should not be empty");
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws DomainException {
        if (password == null || password.isEmpty()) {
            throw new DomainException("Password should not be empty");
        }
        this.password = password;
    }

    public final void setHashedPassword(String password) throws DomainException {
        if (password == null || password.isEmpty()) {
            throw new DomainException("Password should not be empty");
        }
        this.password = hashPassword(password);
    }

    public boolean isPasswordCorrect(String password) throws DomainException {
        String hashedPassword = hashPassword(password);
        return getPassword().equals(hashedPassword);
    }

    private String hashPassword(String password) throws DomainException {
        String encrypted;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
        } catch (NoSuchAlgorithmException e) {
            throw new DomainException(e.getMessage(), e);
        }
        //TODO Salt
        digest.update(password.getBytes());
        byte[] encryptedBytes = new byte[40];
        encryptedBytes = digest.digest();
        encrypted = new BigInteger(1, encryptedBytes).toString(16);
        return encrypted;
    }

    public void addFriend(User u) {
        if(!friends.contains(u)) {
            friends.add(u);
        }
    }

    public void removeFriend(User u) {
        if(friends.contains(u)) {
            friends.remove(u);
        }
    }

    public Collection<User> getFriends() {
        return friends;
    }

    @Override
    public int compareTo(User o) {
        if (getState().equals(o.getState())) {
            return this.getName().compareTo(o.getName());
        }
        if (getState().equals("Online") && o.getState().equals("Away")) {
            return -1;
        }
        if (getState().equals("Away") && o.getState().equals("Online")) {
            return 1;
        }
        if (getState().equals("Online") && o.getState().equals("Offline")) {
            return -1;
        }
        if (getState().equals("Offline") && o.getState().equals("Online")) {
            return 1;
        }
        if (getState().equals("Offline") && o.getState().equals("Away")) {
            return 1;
        } else {
            return -1;
        }
    }
    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if(o != null && o instanceof User) {
            User u = (User)o;
            if(email.equals(u.getEmail())) {
                result = true;
            }
        } 
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.email);
        return hash;
    }
    
}
