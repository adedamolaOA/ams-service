/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.common.domain;

import com.google.auto.value.AutoValue;

/**
 *
 * @author Ade
 */
@AutoValue
public abstract class DataCreation {
    
    public abstract String createdBy();
    public abstract long createdDateTime();
    public abstract Status status();
    
    public static DataCreation create(String createdBy, long createdDateTime, Status status){
        return builder()
                .createdBy(createdBy)
                .createdDateTime(createdDateTime)
                .status(status)
                .build();
    }
    
    public static Builder builder(){
        return new AutoValue_DataCreation.Builder();
    }
    
    @AutoValue.Builder    
    public abstract static class Builder{
        public abstract Builder createdBy(String createdBy);
        public abstract Builder createdDateTime(long createdDateTime);
        public abstract Builder status(Status status);
        public abstract DataCreation build();
    }
    
}
