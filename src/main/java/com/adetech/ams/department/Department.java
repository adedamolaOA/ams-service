/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.department;

import com.adetech.ams.common.domain.DataCreation;
import com.google.auto.value.AutoValue;
import java.util.Optional;

/**
 *
 * @author Ade
 */
@AutoValue
public abstract class Department {
    
    public abstract String departmentId();
    public abstract String name();
    public abstract Optional<String> description();
    public abstract DataCreation dataCreation();
    public static Department create(String departmentId, String name, Optional<String> description, DataCreation dataCreation){
        return builder().dataCreation(dataCreation).departmentId(departmentId).name(name).description(description).builder();
    }
    
    public static Builder builder(){
        return AutoValue_Department.builder();
    }
    
    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder departmentId(String departmentId);
        public abstract Builder name(String name);
        public abstract Builder description(Optional<String> description);
        public abstract Builder dataCreation(DataCreation dataCreation);
        public abstract Department builder();
    }
    
}
