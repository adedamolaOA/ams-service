/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.visitor.domain;

import com.adetech.ams.common.domain.Status;
import com.adetech.ams.visitor.Visitor;
import static com.adetech.ams.visitor.Visitor.Address.builder;
import com.google.auto.value.AutoValue;
import com.google.i18n.phonenumbers.Phonenumber;
import java.util.Optional;

/**
 *
 * @author Ade
 */
@AutoValue
public abstract class UpdateVisitor {

    public abstract Optional<String> firstName();

    public abstract Optional<String> lastName();

    public abstract Optional<String> otherNames();

    public abstract Optional<Phonenumber.PhoneNumber> phoneNumber();

    public abstract Optional<String> email();

    public abstract Optional<Company> company();

    public abstract Optional<Address> address();

    public abstract Optional<Status> status();

    
    public static UpdateVisitor create(
            Optional<String> firstName,
            Optional<String> lastName,
            Optional<String> otherNames,
            Optional<Phonenumber.PhoneNumber> phoneNumber,
            Optional<String> email,
            Optional<Company> company,
            Optional<Address> address,
            Optional<Status> status
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
                .status(status)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_UpdateVisitor.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder firstName(Optional<String> firstName);

        public abstract Builder lastName(Optional<String> lastName);

        public abstract Builder otherNames(Optional<String> otherNames);

        public abstract Builder phoneNumber(Optional<Phonenumber.PhoneNumber> phoneNumber);

        public abstract Builder email(Optional<String> email);

        public abstract Builder company(Optional<Company> company);

        public abstract Builder address(Optional<Address> address);

        public abstract Builder status(Optional<Status> status);

        public abstract UpdateVisitor build();
    }

    @AutoValue
    public abstract static class Company {

        public abstract Optional<String> name();

        public abstract Optional<Address> address();

        public static Company create(Optional<String> name, Optional<Address> address) {
            return builder().name(name).address(address).build();
        }

        public static Builder builder() {
            return new AutoValue_UpdateVisitor_Company.Builder();
        }

        @AutoValue.Builder
        public abstract static class Builder {

            public abstract Builder name(Optional<String> name);

            public abstract Builder address(Optional<Address> address);

            public abstract Company build();
        }
    }

    @AutoValue
    public abstract static class Address {

        public abstract Optional<String> streetName();

        public abstract Optional<String> buildingNumber();

        public abstract Optional<String> unitNo();

        public abstract Optional<String> otherDescriptions();

        public static Address create(Optional<String> streetName, Optional<String> buildingNumber, Optional<String> unitNo, Optional<String> otherDescription){
            return builder().buildingNumber(buildingNumber).streetName(streetName).otherDescriptions(otherDescription).unitNo(unitNo).build();
        }
        public static Builder builder() {
            return new AutoValue_UpdateVisitor_Address.Builder();
        }

        @AutoValue.Builder
        public abstract static class Builder {

            public abstract Builder streetName(Optional<String> streetName);

            public abstract Builder buildingNumber(Optional<String> buildingNumber);

            public abstract Builder unitNo(Optional<String> unitNo);

            public abstract Builder otherDescriptions(Optional<String> otherDescriptions);

            public abstract Address build();
        }

    }
}
