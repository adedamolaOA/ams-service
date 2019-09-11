/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.visitor.domain;

import com.adetech.ams.utils.Cuid;
import com.adetech.ams.visitor.Visitor;
import com.adetech.ams.visitor.infrastructure.JooqVisitorRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ade
 */
@Service
public class VisitorService {
    
    private final JooqVisitorRepository visitorRepository;
    
    @Autowired
    public VisitorService(JooqVisitorRepository visitorRepository){
        this.visitorRepository = visitorRepository;
    }
    
    public Optional<Visitor> saveUpdate(Visitor visitor){
        Visitor guest = Visitor.create(
                Cuid.createCuid(), 
                visitor.firstName(), 
                visitor.lastName(), 
                visitor.otherNames(), 
                visitor.phoneNumber(), 
                visitor.email(), 
                visitor.company(), 
                visitor.address(),
                visitor.dataCreation());
        return visitorRepository.saveUpdate(guest);
    }
    
}
