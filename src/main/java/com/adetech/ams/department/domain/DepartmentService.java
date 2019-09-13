/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.department.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.adetech.ams.department.Department;
import com.adetech.ams.exceptions.NotFoundException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Ade
 */
@Service
public class DepartmentService {
    
    private final DepartmentRepository repository;
    
    @Autowired
    public DepartmentService(DepartmentRepository repository){
        this.repository = repository;
    }
    
    public Department add(Department department) throws NotFoundException{
       return repository.add(department).orElseThrow(() -> new NotFoundException(String.format("Add Failed !!!, Department ID ( %s ) does not exist", department.departmentId())));
    }
    
    public Department update(Department department) throws NotFoundException{
        return repository.update(department).orElseThrow(() -> new NotFoundException(String.format("Update Failed !!!, Department ID ( %s ) does not exist", department.departmentId())));
    }
    
    public Department delete(String id) throws NotFoundException{
        return repository.delete(id).orElseThrow(() -> new NotFoundException(String.format("Delete Failed !!!, Department ID ( %s ) does not exist", id)));
    }
    
    public List<Department> getDepartment(Optional<DepartmentQuery> query){
        return repository.get(query);
    }
    
    public Department getDepartmentById(String id) throws NotFoundException{
        return repository.getByDepartmentId(id).orElseThrow(() -> new NotFoundException(String.format("Department Not Found !!!, Department ID ( %s ) does not exist", id)));
    }
       
    
}
