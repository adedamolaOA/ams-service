package com.adetech.ams.visitor.infrastructure;

import com.adetech.ams.common.domain.DBContext;
import com.adetech.ams.common.infrastructure.JooqRepository;
import static com.adetech.ams.db.Tables.VISITORS;
import com.adetech.ams.visitor.Visitor;
import com.adetech.ams.visitor.domain.VisitorQuery;
import com.adetech.ams.visitor.domain.VisitorRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Ade
 */
@Repository
public class JooqVisitorRepository extends JooqRepository implements VisitorRepository{

    public JooqVisitorRepository(DSLContext readContext, DSLContext writeContext) {
        super(readContext, writeContext);
    }

    @Override
    public Optional<Visitor> saveUpdate(Visitor visitor) {
        getDSLContext(DBContext.WRITE).transaction((tx) -> {
            DSLContext context = tx.dsl();
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
        });
        return getVisitorById(visitor.visitorId());
    }

    @Override
    public List<Visitor> getVisitors(Optional<VisitorQuery> query) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Visitor> getVisitors() {
        return VisitorRepository.super.getVisitors(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Visitor delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
