package com.adetech.ams.visitor.infrastructure;

import com.adetech.ams.common.domain.DBContext;
import com.adetech.ams.common.domain.DataCreation;
import com.adetech.ams.common.domain.Status;
import com.adetech.ams.common.infrastructure.JooqRepository;
import com.adetech.ams.db.Tables;
import com.adetech.ams.visitor.Visitor;
import com.adetech.ams.visitor.domain.VisitorQuery;
import com.adetech.ams.visitor.domain.VisitorRepository;
import java.util.List;
import java.util.Optional;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.adetech.ams.db.Tables.VISITORS;
import static com.adetech.ams.db.Tables.ADDRESSES;
import static com.adetech.ams.db.Tables.COMPANY;
import com.adetech.ams.visitor.common.Deserializer;
import java.util.stream.Collectors;

/**
 *
 * @author Ade
 */
@Repository
public class JooqVisitorRepository extends JooqRepository implements VisitorRepository, Deserializer{

    public JooqVisitorRepository(DSLContext readContext, DSLContext writeContext) {
        super(readContext, writeContext);
    }

    @Override
    public Optional<Visitor> saveUpdate(Visitor visitor) {
        getDSLContext(DBContext.WRITE).transaction((tx) -> {
            DSLContext context = tx.dsl();
            if(visitor.company().isPresent()){
                 Visitor.Company company = visitor.company().get();
                 DataCreation dataCreation = visitor.dataCreation();              
                 Visitor.Address address = company.address();
                 saveVisitorCompany(context, company, dataCreation);
                 saveAddress(context, address, dataCreation);
            }
            saveVisitor(context, visitor);
        });
        return VisitorRepository.super.getVisitorById(visitor.visitorId());
    }

    @Override
    public List<Visitor> getVisitors(Optional<VisitorQuery> query) {
          if(!query.isPresent()){
              return getAllVisitors();
          }
        
          VisitorQuery vq = query.get();
          List<Condition> conditions = conditions(
                vq.visitorId().map(VISITORS.VISITORID::eq),
                vq.visitorIds().map(VISITORS.VISITORID::in),
                vq.firstName().map(name -> name + "%").map(VISITORS.FIRSTNAME::like),
                vq.lastName().map(name -> name + "%").map(VISITORS.FIRSTNAME::like),
                vq.email().map(VISITORS.EMAILADDRESS::eq),
                vq.companyId().map(VISITORS.COMPANYID::eq)
        );

        return getDSLContext(DBContext.READ)
                .select()
                .from(VISITORS)
                .leftJoin(COMPANY)
                .on(COMPANY.COMPANYID.eq(VISITORS.COMPANYID))
                .leftJoin(ADDRESSES)
                .on(ADDRESSES.DISTICTID.eq(VISITORS.VISITORID))
                .where(conditions)
                .fetch()
                .stream()
                .map(this::deserializeVisitor)
                .collect(Collectors.toList());
       
    }

    @Override
    public List<Visitor> getVisitors() {
        return VisitorRepository.super.getVisitors();
    }

    @Override
    public Optional<Visitor> delete(String id) {
        getDSLContext(DBContext.WRITE).update(VISITORS).set(VISITORS.STATUS, Status.DELETED.name()).where(VISITORS.VISITORID.eq(id)).execute();
        return getVisitorById(id);
    }
    
    public List<Visitor> getAllVisitors(){
        return getDSLContext(DBContext.READ)
                .select()
                .from(VISITORS)
                .leftJoin(COMPANY)
                .on(COMPANY.COMPANYID.eq(VISITORS.COMPANYID))
                .leftJoin(ADDRESSES)
                .on(ADDRESSES.DISTICTID.eq(VISITORS.VISITORID))
                .fetch()
                .stream()
                .map(this::deserializeVisitor)
                .collect(Collectors.toList());
    }
    
    public void saveVisitorCompany(DSLContext context,Visitor.Company company, DataCreation dataCreation){
        context.insertInto(COMPANY)
                    .columns(
                            COMPANY.COMPANYID, 
                            COMPANY.NAME, 
                            COMPANY.POSITION, 
                            COMPANY.ACTIVE, 
                            COMPANY.CREATEDBY,
                            COMPANY.CREATEDDATE,
                            COMPANY.STATUS
                    ).values(
                            company.id(), 
                            company.name(), 
                            "DEFALUT", 
                            "YES",
                            dataCreation.createdBy(),
                            dataCreation.createdDateTime(),
                            dataCreation.status().name()
                    ).onDuplicateKeyUpdate()
                         .set(COMPANY.NAME, company.name())
                         .execute();
                 
    }
    
    public void saveAddress(DSLContext context,Visitor.Address address, DataCreation dataCreation){
        context.insertInto(ADDRESSES)
                         .columns(
                                 ADDRESSES.ADDRESSID,
                                 ADDRESSES.DISTICTID,
                                 ADDRESSES.STREETNAME, 
                                 ADDRESSES.BUILDINGNUMBER, 
                                 ADDRESSES.UNITNUMBER, 
                                 ADDRESSES.CREATEDBY, 
                                 ADDRESSES.CREATEDDATE, 
                                 ADDRESSES.STATUS
                         ).values(
                                 address.id(), 
                                 address.distictId(),
                                 address.streetName(),
                                 address.buildingNumber(), 
                                 address.unitNo().orElse(null), 
                                 dataCreation.createdBy(),
                                 dataCreation.createdDateTime(), 
                                 dataCreation.status().name()
                         ).onDuplicateKeyUpdate().set(ADDRESSES.STREETNAME, address.streetName()).execute();
    }
    
    public void saveVisitor(DSLContext context, Visitor visitor){
                    context.insertInto(VISITORS)
                    .columns(
                            VISITORS.VISITORID, 
                            VISITORS.FIRSTNAME, 
                            VISITORS.LASTNAME, 
                            VISITORS.OTHERNAME, 
                            VISITORS.PHONENUMBER, 
                            VISITORS.EMAILADDRESS, 
                            VISITORS.COMPANYID,
                            VISITORS.CREATEDBY,
                            VISITORS.CREATEDDATE,
                            VISITORS.STATUS)
                    .values(
                            visitor.visitorId(), 
                            visitor.firstName(), 
                            visitor.lastName(), 
                            visitor.otherNames().orElse(null), 
                            visitor.phoneNumber().getRawInput(), 
                            visitor.email().orElse(null),
                            visitor.company().isPresent() ? visitor.company().get().id() : null,                            
                            visitor.dataCreation().createdBy(),
                            visitor.dataCreation().createdDateTime(),//LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                            visitor.dataCreation().status().name()
                            
                    ).onDuplicateKeyUpdate().set(VISITORS.FIRSTNAME, visitor.firstName())
                                            .set(VISITORS.LASTNAME, visitor.firstName())
                                            .set(VISITORS.OTHERNAME, visitor.firstName())
                                            .set(VISITORS.PHONENUMBER, visitor.firstName())
                                            .set(VISITORS.EMAILADDRESS, visitor.firstName())
                                            .set(VISITORS.STATUS, visitor.dataCreation().status().name()).execute();
    }
    
}
