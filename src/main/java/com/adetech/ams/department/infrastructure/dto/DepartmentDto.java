/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.department.infrastructure.dto;

import com.adetech.ams.common.domain.DataCreation;
import com.adetech.ams.common.domain.Status;
import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;
import com.adetech.ams.department.Department;
import java.util.Optional;

/**
 *
 * @author Ade
 */
public class DepartmentDto {
    
    @NotNull
    public String departmentId;
    @NotNull
    public String name;
    @Nullable
    public String description;
    @Nullable
    public String createdBy;
    @Nullable
    public long createdDate;
    
    public DepartmentDto(String departmentId, String name, @Nullable String description, @Nullable String createdBy, @Nullable long createdDate){
        this.departmentId = departmentId;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }
    
    public static DepartmentDto fromDomain(Department department){
        return new DepartmentDto(
                department.departmentId(), 
                department.name(), 
                department.description().orElse(null), 
                department.dataCreation().createdBy(), 
                department.dataCreation().createdDateTime()
        );
    }
    
    public Department toDomain(){
        return Department.create(departmentId, name, Optional.ofNullable(description), DataCreation.create(createdBy, createdDate, Status.DENIED));
    }
    
    
    
}
