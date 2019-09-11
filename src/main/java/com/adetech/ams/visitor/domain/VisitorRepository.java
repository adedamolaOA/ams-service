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
    
    public Visitor saveUpdate(Visitor visitor);
    public List<Visitor> getVisitors(Optional<VisitorQuery> query);
    default List<Visitor> getVisitors(){
        return getVisitors(Optional.empty());
    }
    public Visitor delete();
}
