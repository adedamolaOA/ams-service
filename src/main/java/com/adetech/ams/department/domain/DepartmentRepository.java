/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.department.domain;

import com.adetech.ams.department.Department;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Ade
 */
public interface DepartmentRepository {
    public Optional<Department> add(Department department);
    public Optional<Department> update(Department department);
    public List<Department> get(Optional<DepartmentQuery> query);
    default Optional<Department> getByDepartmentId(String id){
        return get(Optional.of(DepartmentQuery.builder().departmentId(Optional.of(id)).builder())).stream().findFirst();
    }
    public Optional<Department> delete(String id);
    
}
