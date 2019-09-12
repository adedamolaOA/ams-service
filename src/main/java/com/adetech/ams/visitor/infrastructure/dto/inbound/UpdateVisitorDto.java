/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.visitor.infrastructure.dto.inbound;

import com.adetech.ams.common.domain.Status;
import com.adetech.ams.visitor.domain.CreateVisitor;
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

    public UpdateVisitor toDomain() {
        return UpdateVisitor.create(
                Optional.ofNullable(firstName),
                Optional.ofNullable(lastName),
                Optional.ofNullable(otherNames),
                Optional.ofNullable(new Phonenumber.PhoneNumber().setRawInput(phoneNumber)), 
                Optional.ofNullable(email),
                company == null ? Optional.empty() : company.toDomain(),
                address == null ? Optional.empty() : address.toDomain(), 
                Optional.ofNullable(Status.valueOf(status))
        );
    }

    public class CompanyDto {

        @Nullable
        public String name;

        @Nullable
        public AddressDto address;

        public CompanyDto(String name, AddressDto address) {
            this.name = name;
            this.address = address;
        }

        public Optional<UpdateVisitor.Company> toDomain() {
            return Optional.of(UpdateVisitor.Company.create(Optional.ofNullable(name), address.toDomain()));
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

        public AddressDto(String streetName, String buildingNumber, @Nullable String unitNo, @Nullable String otherDescriptions) {
            this.buildingNumber = buildingNumber;
            this.otherDescriptions = otherDescriptions;
            this.streetName = streetName;
            this.unitNo = unitNo;
        }

        public Optional<UpdateVisitor.Address> toDomain() {
            return  Optional.of(UpdateVisitor.Address.create(Optional.ofNullable(streetName), Optional.ofNullable(buildingNumber), Optional.ofNullable(unitNo), Optional.ofNullable(otherDescriptions)));              
          
        }

    }
}
