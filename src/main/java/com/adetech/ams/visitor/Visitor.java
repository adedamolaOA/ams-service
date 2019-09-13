/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.visitor;

import com.adetech.ams.common.domain.AddressType;
import com.adetech.ams.common.domain.DataCreation;
import com.google.auto.value.AutoValue;
import com.google.i18n.phonenumbers.Phonenumber;
import java.util.Optional;

/**
 *
 * @author Ade
 */
@AutoValue
public abstract class Visitor {

    public abstract String visitorId();

    public abstract String firstName();

    public abstract String lastName();

    public abstract Optional<String> otherNames();

    public abstract Phonenumber.PhoneNumber phoneNumber();

    public abstract Optional<String> email();

    public abstract Optional<Company> company();

    public abstract Optional<Address> address();
    
    public abstract DataCreation dataCreation();

    /**
     *
     * @param visitorId
     * @param firstName
     * @param lastName
     * @param otherNames
     * @param phoneNumber
     * @param email
     * @param company
     * @param address
     * @param dataCreation
     * @return
     */
    public static Visitor create(
            String visitorId,
            String firstName,
            String lastName,
            Optional<String> otherNames,
            Phonenumber.PhoneNumber phoneNumber,
            Optional<String> email,
            Optional<Company> company,
            Optional<Address> address,
            DataCreation dataCreation
    ) {
        return builder()
                .visitorId(visitorId)
                .address(address)
                .firstName(firstName)
                .lastName(lastName)
                .otherNames(otherNames)
                .phoneNumber(phoneNumber)
                .email(email)
                .company(company)
                .address(address)
                .dataCreation(dataCreation)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_Visitor.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder visitorId(String visitorId);

        public abstract Builder firstName(String firstName);

        public abstract Builder lastName(String lastName);

        public abstract Builder otherNames(Optional<String> otherNames);

        public abstract Builder phoneNumber(Phonenumber.PhoneNumber phoneNumber);

        public abstract Builder email(Optional<String> email);

        public abstract Builder company(Optional<Company> company);

        public abstract Builder address(Optional<Address> address);
        
        public abstract Builder dataCreation(DataCreation auditEvent);

        public abstract Visitor build();
    }

    @AutoValue
    public abstract static class Company {
        
        public abstract String id();

        public abstract String name();

        public abstract Optional<Address>  address();
        
        public static Company create(String id, String name, Optional<Address>  address){
            return builder().id(id).name(name).address(address).build();
        }
        
        public static Builder builder(){
            return new AutoValue_Visitor_Company.Builder();
        }
        
        @AutoValue.Builder
        public abstract static class Builder{
            public abstract Builder id(String id);
            public abstract Builder name(String name);
            public abstract Builder address(Optional<Address> address);
            public abstract Company build();
        }
    }

    @AutoValue
    public abstract static class Address {
        
        public abstract String id();
        
        public abstract String distictId();

        public abstract String streetName();

        public abstract String buildingNumber();

        public abstract Optional<String> unitNo();

        public abstract Optional<String> otherDescriptions();
        
        public abstract AddressType type();
        
         
        public static Address create(String id,String distictId, String streetName, String buildingNumber, Optional<String> unitNo, Optional<String> otherDescription, AddressType type){
            return builder().id(id).distictId(distictId).buildingNumber(buildingNumber).streetName(streetName).otherDescriptions(otherDescription).unitNo(unitNo).type(type).build();
        }
        
        public static Builder builder(){
            return new AutoValue_Visitor_Address.Builder();
        }
        @AutoValue.Builder
        public abstract static class Builder{
            
            public abstract Builder id(String id);
            public abstract Builder streetName(String streetName);
            public abstract Builder buildingNumber(String buildingNumber);
            public abstract Builder unitNo(Optional<String> unitNo);
            public abstract Builder otherDescriptions(Optional<String> otherDescriptions);
            public abstract Builder distictId(String distictId);
            public abstract Builder type(AddressType type);
            public abstract Address build();
        }
      
    }
}
