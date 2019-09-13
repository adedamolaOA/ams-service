/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.visitor.infrastructure.dto.inbound;

import com.adetech.ams.common.domain.AddressType;
import com.adetech.ams.common.domain.Status;
import com.adetech.ams.visitor.domain.UpdateVisitor;
import com.google.i18n.phonenumbers.Phonenumber;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

/**
 *
 * @author Ade
 */
public class UpdateVisitorDto {
    @Nullable
    public String firstName;

    @Nullable
    public String lastName;

    @Nullable
    public String otherNames;

    @Nullable
    public String phoneNumber;
    
    @Nullable
    public String email;

    @Nullable
    public CompanyDto company;

    @Nullable
    public AddressDto address;
    
    @Nullable
    public String status;
    
    @Nullable
    public String updateBy;

    public UpdateVisitor toDomain() {
        Phonenumber.PhoneNumber phone = phoneNumber != null ? new Phonenumber.PhoneNumber().setRawInput(phoneNumber) : null;
        Status stat = status != null ? Status.valueOf(status) : null;
        return UpdateVisitor.create(
                Optional.ofNullable(firstName),
                Optional.ofNullable(lastName),
                Optional.ofNullable(otherNames),
                Optional.ofNullable(phone), 
                Optional.ofNullable(email),
                company == null ? Optional.empty() : company.toDomain(),
                address == null ? Optional.empty() : address.toDomain(), 
                Optional.ofNullable(stat),
                updateBy
        );
    }

    public static class CompanyDto {

        @Nullable
        public String name;

        @Nullable
        public AddressDto address;

        public CompanyDto(@Nullable String name, @Nullable AddressDto address) {
            this.name = name;
            this.address = address;
        }

        public Optional<UpdateVisitor.Company> toDomain() {
            return Optional.of(UpdateVisitor.Company.create(Optional.ofNullable(name), address != null ? address.toDomain(): Optional.empty()));
        }

    }

    public static class AddressDto {

        @Nullable
        public String streetName;

        @Nullable
        public String buildingNumber;

        @Nullable
        public String unitNo;

        @Nullable
        public String otherDescriptions;
        
        @NotNull
        public String type;

        public AddressDto(@Nullable String streetName, @Nullable String buildingNumber, @Nullable String unitNo, @Nullable String otherDescriptions, String type) {
            this.buildingNumber = buildingNumber;
            this.otherDescriptions = otherDescriptions;
            this.streetName = streetName;
            this.unitNo = unitNo;
            this.type = type;
        }

        public Optional<UpdateVisitor.Address> toDomain() {
            return  Optional.of(UpdateVisitor.Address.create(Optional.ofNullable(streetName), Optional.ofNullable(buildingNumber), Optional.ofNullable(unitNo), Optional.ofNullable(otherDescriptions), AddressType.valueOf(type)));              
          
        }

    }
}
