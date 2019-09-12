package com.adetech.ams.visitor.common;

import com.adetech.ams.common.domain.DataCreation;
import com.adetech.ams.common.domain.Status;
import com.adetech.ams.visitor.Visitor;
import static com.adetech.ams.db.Tables.ADDRESSES;
import static com.adetech.ams.db.Tables.VISITORS;
import static com.adetech.ams.db.Tables.COMPANY;
import com.google.i18n.phonenumbers.Phonenumber;
import java.util.Optional;
import org.jooq.Record;

/**
 *
 * @author Ade
 */
public interface Deserializer {

    default Visitor deserializeVisitor(Record r) {
        return Visitor.builder()
                .visitorId(r.get(VISITORS.VISITORID))
                .firstName(r.get(VISITORS.FIRSTNAME))
                .lastName(r.get(VISITORS.LASTNAME))
                .otherNames(Optional.ofNullable(r.get(VISITORS.FIRSTNAME)))
                .phoneNumber(new Phonenumber.PhoneNumber().setRawInput(r.get(VISITORS.FIRSTNAME)))
                .email(Optional.ofNullable(r.get(VISITORS.EMAILADDRESS)))
                .address(deserializeVisitorAddress(r))
                .company(deserializeVisitorCompany(r))
                .dataCreation(deserializeDataCreation(r))
                .build();
    }

    default Optional<Visitor.Address> deserializeVisitorAddress(Record r) {
        return r.get(ADDRESSES.ADDRESSID)!= null ? Optional.ofNullable(Visitor.Address.builder()
                .id(r.get(ADDRESSES.ADDRESSID))
                .streetName(r.get(ADDRESSES.STREETNAME))
                .buildingNumber(r.get(ADDRESSES.BUILDINGNUMBER))
                .unitNo(Optional.of(r.get(ADDRESSES.UNITNUMBER)))
                .otherDescriptions(Optional.empty())
                .build()) : Optional.empty();
    }
    
     default Optional<Visitor.Company> deserializeVisitorCompany(Record r) {
        return r.get(COMPANY.COMPANYID) != null ? Optional.ofNullable(Visitor.Company.builder()
                .id(r.get(COMPANY.COMPANYID))
                .name(r.get(COMPANY.NAME))
                .address(deserializeVisitorAddress(r).orElseThrow())
                .build()) : Optional.empty();
    }
     
     default DataCreation deserializeDataCreation(Record r){
         return DataCreation.create(r.get(VISITORS.CREATEDBY), r.get(VISITORS.CREATEDDATE), Status.valueOf(r.get(VISITORS.STATUS)));
     }

}
