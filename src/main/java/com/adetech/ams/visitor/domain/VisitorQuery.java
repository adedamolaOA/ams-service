/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.visitor.domain;

import com.google.auto.value.AutoValue;
import java.util.List;

/**
 *
 * @author Ade
 */
@AutoValue
public abstract class VisitorQuery {

    public abstract String firstName();

    public abstract String lastName();

    public abstract String phoneNumber();

    public abstract String email();

    public abstract String companyId();

    public abstract String visitorId();

    public abstract List<String> visitorIds();
    
    public static Builder builder(){
        return new AutoValue_VisitorQuery.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder firstName(String firstName);

        public abstract Builder lastName(String lastName);

        public abstract Builder phoneNumber(String phoneNumber);

        public abstract Builder email(String email);

        public abstract Builder companyId(String companyId);

        public abstract Builder visitorId(String visitorId);

        public abstract Builder visitorIds(List<String> visitorIds);
        
        public abstract VisitorQuery build();

    }

}
