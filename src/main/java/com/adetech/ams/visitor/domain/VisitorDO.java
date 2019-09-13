/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.visitor.domain;

import com.google.auto.value.AutoValue;
import java.util.Optional;

/**
 *
 * @author Ade
 */
@AutoValue
public abstract class VisitorDO {
    
    public abstract String visitorId();
    public abstract Optional<String> companyId();
    public abstract Optional<String> addressId();
    
    public static VisitorDO create(String visitorId, Optional<String> companyId, Optional<String> addressId){
        return builder().companyId(companyId).visitorId(visitorId).addressId(addressId).build();
    }
    
    public static Builder builder(){
        return new AutoValue_VisitorDO.Builder();
    }
    
    @AutoValue.Builder
    public abstract static class Builder{
        public abstract Builder visitorId(String visitorId);
        public abstract Builder companyId(Optional<String> companyId);
        public abstract Builder addressId(Optional<String> addressId);
        public abstract VisitorDO build();
    }
    
}
