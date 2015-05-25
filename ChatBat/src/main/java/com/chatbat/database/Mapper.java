/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatbat.database;

import com.chatbat.domain.DomainObject;
import java.util.Collection;

public interface Mapper<DO extends DomainObject> {

    public long store(DO domainObject) throws MapperException;

    public void update(DO domainObject) throws MapperException;

    public void delete(long id) throws MapperException;

    public DO find(long id) throws MapperException;
    
    public DO findByFieldName(String field, String value) throws MapperException;

    public Collection<DO> findAll();

    public void clear() throws MapperException;
}
