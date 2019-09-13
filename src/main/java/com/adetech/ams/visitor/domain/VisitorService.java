package com.adetech.ams.visitor.domain;

import com.adetech.ams.common.domain.DataCreation;
import com.adetech.ams.common.domain.Status;
import com.adetech.ams.exceptions.NotFoundException;
import com.adetech.ams.utils.Cuid;
import com.adetech.ams.visitor.Visitor;
import com.adetech.ams.visitor.infrastructure.JooqVisitorRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ade
 */
@Service
public class VisitorService {
    
    private final JooqVisitorRepository visitorRepository;
    
    @Autowired
    public VisitorService(JooqVisitorRepository visitorRepository){
        this.visitorRepository = visitorRepository;
    }
    
    public Visitor add(Visitor visitor) throws NotFoundException{
        return visitorRepository.saveUpdate(visitor).orElseThrow(() -> new NotFoundException(String.format("Save Failed!!!,  Visitor with ID ( %s ) has it does not exist !!!", visitor.visitorId())));
    }
    
    public Visitor getVisitorById(String id) throws NotFoundException{
        return visitorRepository.getVisitorById(id).orElseThrow(() -> new NotFoundException(String.format("Visitor with ID ( %s ) does not exist", id)));
    }
    
    
    public Visitor update(String visitorId, UpdateVisitor visitorUpdate) throws NotFoundException{
        return visitorRepository.update(visitorId, visitorUpdate).orElseThrow(() -> new NotFoundException(String.format("Update Failed!!!,  Visitor with ID ( %s ) has it does not exist !!!", visitorId)));
    }
    
    public Visitor delete(String id) throws NotFoundException{
        return visitorRepository.delete(id).orElseThrow(() -> new NotFoundException(String.format("Update Failed!!!,  Visitor with ID ( %s ) has it does not exist !!!", id)));
    }
    
    public static Visitor toVisitor(CreateVisitor createVisitor) {
        String visitorId = Cuid.createCuid();
        String companyId = Cuid.createCuid();
        
        Visitor.Company visitorCompany = createVisitor.company().map( createCompany -> Visitor.Company.create(
                        companyId,
                        createCompany.name(),
                        createCompany.address().map( address ->  Visitor.Address.create(
                                Cuid.createCuid(),
                                companyId, address.streetName(),
                                address.buildingNumber(),
                                address.unitNo(),
                                address.otherDescriptions(), 
                                address.type()
                        )
                        )
        )).orElse(null);

        Visitor.Address visitorAddress =   createVisitor.address().map( createAddress -> Visitor.Address.create(
                        Cuid.createCuid(),
                        visitorId,
                        createAddress.streetName(),
                        createAddress.buildingNumber(),
                        createAddress.unitNo(), 
                        createAddress.otherDescriptions(),
                        createAddress.type())
         ).orElse(null);
               
        return Visitor.create(
                visitorId,
                createVisitor.firstName(),
                createVisitor.lastName(),
                createVisitor.otherNames(),
                createVisitor.phoneNumber(),
                createVisitor.email(),
                Optional.ofNullable(visitorCompany),
                Optional.ofNullable(visitorAddress),
                DataCreation.create(createVisitor.createdBy(), LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), Status.APPROVED)
        );
    }
}
