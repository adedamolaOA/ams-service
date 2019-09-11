/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.visitor.domain;

import com.google.auto.value.AutoValue;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Ade
 */
@AutoValue
public abstract class VisitorQuery {

    public abstract Optional<String> firstName();

    public abstract Optional<String> lastName();

    public abstract Optional<String> phoneNumber();

    public abstract Optional<String> email();

    public abstract Optional<String> companyId();

    public abstract Optional<String> visitorId();

    public abstract Optional<List<String>> visitorIds();
    
    public static Builder builder(){
        return new AutoValue_VisitorQuery.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder firstName(Optional<String> firstName);

        public abstract Builder lastName(Optional<String> lastName);

        public abstract Builder phoneNumber(Optional<String> phoneNumber);

        public abstract Builder email(Optional<String> email);

        public abstract Builder companyId(Optional<String> companyId);

        public abstract Builder visitorId(Optional<String> visitorId);

        public abstract Builder visitorIds(Optional<List<String>> visitorIds);
        
        public abstract VisitorQuery build();

    }

}
