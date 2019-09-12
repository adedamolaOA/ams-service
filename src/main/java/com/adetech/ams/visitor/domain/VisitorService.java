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
    
    public Visitor saveUpdate(Visitor visitor) throws NotFoundException{
        return visitorRepository.saveUpdate(visitor).orElseThrow(() -> new NotFoundException(String.format("Failed to save Visitor with ID ( %s ) has it does not exist !!!", visitor.visitorId())));
    }
    
    public Visitor getVisitorById(String id) throws NotFoundException{
        return visitorRepository.getVisitorById(id).orElseThrow(() -> new NotFoundException(String.format("Visitor with ID ( %s ) does not exist", id)));
    }
    
    public static Visitor toVisitor(CreateVisitor createVisitor) {
        String visitorId = Cuid.createCuid();
        String companyId = Cuid.createCuid();
        Visitor.Company visitorCompany = createVisitor.company().isPresent()
                ? Visitor.Company.create(
                        companyId,
                        createVisitor.company().get().name(),
                        Visitor.Address.create(
                                Cuid.createCuid(),
                                companyId, createVisitor.company().get().address().streetName(),
                                createVisitor.company().get().address().buildingNumber(),
                                createVisitor.company().get().address().unitNo(),
                                createVisitor.company().get().address().otherDescriptions())) : null;
        Visitor.Address visitorAddress =  createVisitor.address().isPresent()
                ? Visitor.Address.create(
                        Cuid.createCuid(),
                        visitorId,
                        createVisitor.address().get().streetName(),
                        createVisitor.address().get().buildingNumber(),
                        createVisitor.address().get().unitNo(), createVisitor.address().get().otherDescriptions()) : null;
        
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
