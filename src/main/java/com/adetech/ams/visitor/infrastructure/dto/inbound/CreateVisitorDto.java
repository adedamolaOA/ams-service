
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.visitor.infrastructure.dto.inbound;

import com.adetech.ams.visitor.domain.CreateVisitor;
import com.google.i18n.phonenumbers.Phonenumber;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

/**
 *
 * @author Ade
 */
public class CreateVisitorDto {

    @NotNull
    public String firstName;

    @NotNull
    public String lastName;

    @Nullable
    public String otherNames;

    @NotNull
    public String phoneNumber;
    
    @Nullable
    public String email;

    @Nullable
    public CompanyDto company;

    @Nullable
    public AddressDto address;
    
    @NotNull
    public String createdBy;

    public CreateVisitor toDomain() {
        return CreateVisitor.create(
                firstName,
                lastName,
                Optional.ofNullable(otherNames),
                new Phonenumber.PhoneNumber().setRawInput(phoneNumber), 
                Optional.ofNullable(email),
                company == null ? Optional.empty() : company.toDomain(),
                address == null ? Optional.empty() : address.toDomain(), 
                createdBy
        );
    }

    public class CompanyDto {

        @NotNull
        public String name;

        @NotNull
        public AddressDto address;

        public CompanyDto(String name, AddressDto address) {
            this.name = name;
            this.address = address;
        }

        public Optional<CreateVisitor.Company> toDomain() {
            return Optional.of(CreateVisitor.Company.create(name, address.toDomain().orElseThrow()));
        }

    }

    public static class AddressDto {

        @NotNull
        public String streetName;

        @NotNull
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

        public Optional<CreateVisitor.Address> toDomain() {
            return  Optional.of(CreateVisitor.Address.create(streetName, buildingNumber, Optional.ofNullable(unitNo), Optional.ofNullable(otherDescriptions)));              
          
        }

    }
}
