/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.department.domain;

import com.google.auto.value.AutoValue;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Ade
 */
@AutoValue
public abstract class DepartmentQuery {
    
    public abstract Optional<String> name();
    public abstract Optional<String> departmentId();
    public abstract Optional<List<String>> departmentIds();
    
    public static Builder builder(){
        return AutoValue_DepartmentQuery.builder();
    }
    
    @AutoValue.Builder
    public abstract static class Builder{
        public abstract Builder departmentId(Optional<String> departmentId);
        public abstract Builder departmentIds(Optional<List<String>> departmentIds);
        public abstract Builder name(Optional<String> name);
        public abstract DepartmentQuery builder();
    }
    
}
