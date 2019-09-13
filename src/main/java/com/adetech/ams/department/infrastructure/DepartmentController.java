/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.department.infrastructure;

import com.adetech.ams.department.domain.DepartmentService;
import com.adetech.ams.department.infrastructure.dto.DepartmentDto;
import com.adetech.ams.exceptions.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
@RequestMapping("/ams/department")
public class DepartmentController {
    
    private final DepartmentService service;
    
    public DepartmentController(DepartmentService service){
        this.service = service;
    }
    
    @PostMapping("/")
    public DepartmentDto add(@Valid @RequestBody DepartmentDto dto) throws NotFoundException{
        return DepartmentDto.fromDomain(service.add(dto.toDomain()));
    }
    
    @PutMapping("/{id}")
    public DepartmentDto update(@PathVariable String id, @Valid @RequestBody DepartmentDto departmentDto) throws NotFoundException{
        return DepartmentDto.fromDomain(service.update(departmentDto.toDomain()));
    }
    
    @DeleteMapping("/{id}")
    public DepartmentDto delete(@PathVariable String id) throws NotFoundException{
        return DepartmentDto.fromDomain(service.delete(id));
    }
    
    @GetMapping("/")
    public List<DepartmentDto> get(){
        return service.getDepartment(Optional.empty()).stream().map(DepartmentDto::fromDomain).collect(Collectors.toList());
    }
    
}
