/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.visitor.infrastructure;

import com.adetech.ams.exceptions.NotFoundException;
import com.adetech.ams.visitor.Visitor;
import com.adetech.ams.visitor.domain.UpdateVisitor;
import com.adetech.ams.visitor.domain.VisitorService;
import com.adetech.ams.visitor.infrastructure.dto.VisitorDto;
import com.adetech.ams.visitor.infrastructure.dto.inbound.CreateVisitorDto;
import com.adetech.ams.visitor.infrastructure.dto.inbound.UpdateVisitorDto;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Ade
 */
@RestController
@RequestMapping("/ams/visitor")
public class VisitorController {
    
    private final VisitorService service;
    
    public VisitorController( VisitorService service){
        this.service = service;
    }
    
    @PostMapping("/")
    public VisitorDto add(@Valid @RequestBody CreateVisitorDto dto) throws NotFoundException{
        Visitor visitor = VisitorService.toVisitor(dto.toDomain());
        return VisitorDto.fromDomain(service.add(visitor));
    }
    
    @GetMapping("/{id}")
    public VisitorDto getById(@PathVariable String id) throws NotFoundException{
        return VisitorDto.fromDomain(service.getVisitorById(id));
    }
    
    @PutMapping("/{id}")
    public VisitorDto update(@PathVariable String id, @Valid @RequestBody UpdateVisitorDto dto) throws NotFoundException{
        return VisitorDto.fromDomain(service.update(id, dto.toDomain()));
    }
    
    @DeleteMapping("/{id}")
    public VisitorDto delete(@PathVariable String id) throws NotFoundException{
        return VisitorDto.fromDomain(service.delete(id));      
    }
    
}
