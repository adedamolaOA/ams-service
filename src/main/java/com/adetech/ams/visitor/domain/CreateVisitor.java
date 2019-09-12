/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.visitor.domain;

import com.adetech.ams.common.domain.DataCreation;
import com.adetech.ams.common.domain.Status;
import com.adetech.ams.utils.Cuid;
import com.adetech.ams.visitor.Visitor;
import com.google.auto.value.AutoValue;
import com.google.i18n.phonenumbers.Phonenumber;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

/**
 *
 * @author Ade
 */
@AutoValue
public abstract class CreateVisitor {

    public abstract String firstName();

    public abstract String lastName();

    public abstract Optional<String> otherNames();

    public abstract Phonenumber.PhoneNumber phoneNumber();

    public abstract Optional<String> email();

    public abstract Optional<Company> company();

    public abstract Optional<Address> address();

    public abstract String createdBy();

    public static CreateVisitor create(
            String firstName,
            String lastName,
            Optional<String> otherNames,
            Phonenumber.PhoneNumber phoneNumber,
            Optional<String> email,
            Optional<Company> company,
            Optional<Address> address,
            String createdBy
    ) {
        return builder()
                .address(address)
                .firstName(firstName)
                .lastName(lastName)
                .otherNames(otherNames)
                .phoneNumber(phoneNumber)
                .email(email)
                .company(company)
                .address(address)
                .createdBy(createdBy)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_CreateVisitor.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder firstName(String firstName);

        public abstract Builder lastName(String lastName);

        public abstract Builder otherNames(Optional<String> otherNames);

        public abstract Builder phoneNumber(Phonenumber.PhoneNumber phoneNumber);

        public abstract Builder email(Optional<String> email);

        public abstract Builder company(Optional<Company> company);

        public abstract Builder address(Optional<Address> address);

        public abstract Builder createdBy(String createdBy);

        public abstract CreateVisitor build();
    }

    @AutoValue
    public abstract static class Company {

        public abstract String name();

        public abstract Address address();

        public static Company create(String name, Address address) {
            return builder().name(name).address(address).build();
        }

        public static Builder builder() {
            return new AutoValue_CreateVisitor_Company.Builder();
        }

        @AutoValue.Builder
        public abstract static class Builder {

            public abstract Builder name(String name);

            public abstract Builder address(Address address);

            public abstract Company build();
        }
    }

    @AutoValue
    public abstract static class Address {

        public abstract String streetName();

        public abstract String buildingNumber();

        public abstract Optional<String> unitNo();

        public abstract Optional<String> otherDescriptions();

        public static Address create(String streetName, String buildingNumber, Optional<String> unitNo, Optional<String> otherDescription) {
            return builder().buildingNumber(buildingNumber).streetName(streetName).otherDescriptions(otherDescription).unitNo(unitNo).build();
        }

        public static Builder builder() {
            return new AutoValue_CreateVisitor_Address.Builder();
        }

        @AutoValue.Builder
        public abstract static class Builder {

            public abstract Builder streetName(String streetName);

            public abstract Builder buildingNumber(String buildingNumber);

            public abstract Builder unitNo(Optional<String> unitNo);

            public abstract Builder otherDescriptions(Optional<String> otherDescriptions);

            public abstract Address build();
        }

    }

   
}
