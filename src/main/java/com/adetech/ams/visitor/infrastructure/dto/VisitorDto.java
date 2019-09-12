/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.visitor.infrastructure.dto;

import com.adetech.ams.visitor.Visitor;
import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

/**
 *
 * @author Ade
 */
public class VisitorDto {
    @NotNull
    public  String visitorId;

    @NotNull
    public  String firstName;

    @NotNull
    public  String lastName;

    @Nullable
    public  String otherNames;

    public  String phoneNumber;

    @Nullable
    public String email;

    @Nullable
    public  CompanyDto company;

    @Nullable
    public  AddressDto address;
    
    @Nullable
    public  String status;


    public VisitorDto(
            String visitorId, 
            String firstName,
            String lastName,
            @Nullable String otherNames, 
            String phoneNumber, 
            @Nullable String email,
            @Nullable CompanyDto company,
            @Nullable AddressDto address,
            @Nullable String status
    ){
        this.visitorId = visitorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.otherNames = otherNames;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.company = company;        
        this.address = address;
        this.status = status;
        
    }
    
    public static VisitorDto fromDomain(Visitor visitor){
        return new VisitorDto(
                visitor.visitorId(), 
                visitor.firstName(),
                visitor.lastName(), 
                visitor.otherNames().orElse(null), 
                visitor.phoneNumber().getRawInput(), 
                visitor.email().orElse(null), 
                visitor.company().stream().map(CompanyDto::fromDomain).findFirst().orElse(null), 
                visitor.address().stream().map(AddressDto::fromDomain).findFirst().orElse(null), 
                visitor.dataCreation().status().name()
        );
    }

   
    public static class CompanyDto {
        
        @NotNull
        public  String name;

        @NotNull
        public  AddressDto address;
        
        
        public CompanyDto(String name, AddressDto address){
            this.name = name;
            this.address = address;
        }
        
        public static CompanyDto fromDomain(Visitor.Company company){
            return new CompanyDto(company.name(), AddressDto.fromDomain(company.address()));
        }
        
       
    }

   
    public  static class AddressDto {

        @NotNull
        public  String streetName;

        @NotNull
        public  String buildingNumber;

        @Nullable
        public  String unitNo;

        @Nullable
        public  String otherDescriptions;
        
        public AddressDto(String streetName,  String buildingNumber, @Nullable String unitNo, @Nullable String otherDescriptions){
            this.buildingNumber = buildingNumber;
            this.otherDescriptions = otherDescriptions;
            this.streetName = streetName;
            this.unitNo = unitNo;
        }
        
        public static AddressDto fromDomain(Visitor.Address address){
            return new AddressDto(
                    address.streetName(),
                    address.buildingNumber(), 
                    address.unitNo().orElse("None"), 
                    address.otherDescriptions().orElse("None")
            );
        }
        
      
    }
    
    
}
