package com.adetech.ams.visitor.infrastructure;

import com.adetech.ams.common.infrastructure.JooqRepository;
import com.adetech.ams.visitor.Visitor;
import com.adetech.ams.visitor.domain.VisitorQuery;
import com.adetech.ams.visitor.domain.VisitorRepository;
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
    public Visitor saveUpdate(Visitor visitor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
