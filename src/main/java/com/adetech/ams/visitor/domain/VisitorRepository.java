/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.visitor.domain;

import com.adetech.ams.visitor.Visitor;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Ade
 */
public interface VisitorRepository {
    
    public Optional<Visitor> saveUpdate(Visitor visitor);
    public List<Visitor> getVisitors(Optional<VisitorQuery> query);
    default List<Visitor> getVisitors(){
        return getVisitors(Optional.empty());
    }
    default Optional<Visitor> getVisitorById(String id){
        return getVisitors(Optional.of(VisitorQuery.builder().visitorId(Optional.of(id)).build())).stream().findFirst();
    }
    public Optional<Visitor> delete(String id);
    public Optional<Visitor> update(String visitorId, UpdateVisitor visitorUpdate);
}
