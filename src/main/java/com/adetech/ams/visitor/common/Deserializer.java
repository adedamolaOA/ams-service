package com.adetech.ams.visitor.common;

import com.adetech.ams.common.domain.AddressType;
import com.adetech.ams.common.domain.DataCreation;
import com.adetech.ams.common.domain.Status;
import static com.adetech.ams.db.Tables.ADDRESSES;
import com.adetech.ams.visitor.Visitor;
import static com.adetech.ams.db.Tables.VISITORS;
import static com.adetech.ams.db.Tables.COMPANY;
import com.adetech.ams.db.tables.Addresses;
import com.adetech.ams.visitor.domain.VisitorDO;
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
                .otherNames(Optional.ofNullable(r.get(VISITORS.OTHERNAME)))
                .phoneNumber(new Phonenumber.PhoneNumber().setRawInput(r.get(VISITORS.PHONENUMBER)))
                .email(Optional.ofNullable(r.get(VISITORS.EMAILADDRESS)))
                .address(deserializeVisitorAddress(r, AddressType.PERSONAL))
                .company(deserializeVisitorCompany(r))
                .dataCreation(deserializeDataCreation(r))
                .build();
    }

    default Optional<Visitor.Address> deserializeVisitorAddress(Record r, AddressType type) {
        Addresses visitorAddress = ADDRESSES.as("visitor_address");
        Addresses companyAddress = ADDRESSES.as("company_address");
        switch(type){
            case COMPANY:
                return Optional.ofNullable(r.get(companyAddress.ADDRESSID.as("companyAddressId"))).isPresent() ? Optional.ofNullable(Visitor.Address.builder()
                .id(r.get(companyAddress.ADDRESSID.as("companyAddressId")))
                .distictId(r.get(companyAddress.DISTICTID.as("companyDisticitId")))
                .streetName(r.get(companyAddress.STREETNAME.as("companyStreetName")))
                .buildingNumber(r.get(companyAddress.BUILDINGNUMBER.as("companyBuildingId")))
                .unitNo(Optional.ofNullable(r.get(companyAddress.UNITNUMBER.as("companyUnitNo"))))
                .type(r.get( companyAddress.TYPE.as("companyType")) != null ? AddressType.valueOf(r.get( companyAddress.TYPE.as("companyType"))) : AddressType.NONE)
                .otherDescriptions(Optional.ofNullable(r.get(companyAddress.DESCRIPTION.as("companyDescription"))))
                .build()) : Optional.empty();
            default:
                return Optional.ofNullable(r.get(visitorAddress.ADDRESSID.as("visitorAddressId"))).isPresent() ? Optional.ofNullable(Visitor.Address.builder()
                .id(r.get(visitorAddress.ADDRESSID.as("visitorAddressId")))
                .distictId(r.get(visitorAddress.DISTICTID.as("visitorDisticitId")))
                .streetName(r.get(visitorAddress.STREETNAME.as("visitorStreetName")))
                .buildingNumber(r.get(visitorAddress.BUILDINGNUMBER.as("visitorBuildingId")))
                .unitNo(Optional.ofNullable(r.get(visitorAddress.UNITNUMBER.as("visitorUnitNo"))))
                .type(r.get( visitorAddress.TYPE.as("visitorType")) != null ? AddressType.valueOf(r.get( visitorAddress.TYPE.as("visitorType"))) : AddressType.NONE)
                .otherDescriptions(Optional.ofNullable(r.get(visitorAddress.DESCRIPTION.as("visitorDescription"))))
                .build()) : Optional.empty();
        }
         
        
    }
    
     default Optional<Visitor.Company> deserializeVisitorCompany(Record r) {
        return r.get(COMPANY.COMPANYID) != null ? Optional.ofNullable(Visitor.Company.builder()
                .id(r.get(COMPANY.COMPANYID))
                .name(r.get(COMPANY.NAME))
                .address(deserializeVisitorAddress(r, AddressType.COMPANY))
                .build()) : Optional.empty();
    }
     
     default DataCreation deserializeDataCreation(Record r){
         return DataCreation.create(r.get(VISITORS.CREATEDBY), r.get(VISITORS.CREATEDDATE), Status.valueOf(r.get(VISITORS.STATUS)));
     }
     
     default VisitorDO deserializeVisitorDO(Record r){
         return VisitorDO.create(r.get(VISITORS.VISITORID), Optional.ofNullable(r.get(COMPANY.COMPANYID)), Optional.ofNullable(r.get(ADDRESSES.DISTICTID)));
     }

}
