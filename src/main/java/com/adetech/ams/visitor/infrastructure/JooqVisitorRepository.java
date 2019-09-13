package com.adetech.ams.visitor.infrastructure;

import com.adetech.ams.common.domain.AddressType;
import com.adetech.ams.common.domain.DBContext;
import com.adetech.ams.common.domain.DataCreation;
import com.adetech.ams.common.domain.Status;
import com.adetech.ams.common.infrastructure.JooqRepository;
import static com.adetech.ams.db.Tables.ADDRESSES;
import com.adetech.ams.visitor.Visitor;
import com.adetech.ams.visitor.domain.VisitorQuery;
import com.adetech.ams.visitor.domain.VisitorRepository;
import java.util.List;
import java.util.Optional;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.adetech.ams.db.Tables.VISITORS;
import static com.adetech.ams.db.Tables.COMPANY;
import com.adetech.ams.db.tables.Addresses;
import com.adetech.ams.db.tables.records.AddressesRecord;
import com.adetech.ams.db.tables.records.CompanyRecord;
import com.adetech.ams.db.tables.records.VisitorsRecord;
import com.adetech.ams.exceptions.NotFoundException;
import com.adetech.ams.utils.Cuid;
import com.adetech.ams.visitor.Visitor.Address;
import com.adetech.ams.visitor.common.Deserializer;
import com.adetech.ams.visitor.domain.UpdateVisitor;
import com.adetech.ams.visitor.domain.VisitorDO;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;

/**
 *
 * @author Ade
 */
@Repository
public class JooqVisitorRepository extends JooqRepository implements VisitorRepository, Deserializer {

    public JooqVisitorRepository(DSLContext readContext, DSLContext writeContext) {
        super(readContext, writeContext);
    }

    @Override
    public Optional<Visitor> saveUpdate(Visitor visitor) {
        getDSLContext(DBContext.WRITE).transaction((tx) -> {
            DSLContext context = tx.dsl();
            DataCreation dataCreation = visitor.dataCreation();
            visitor.company().ifPresent(company -> {               
                Optional<Visitor.Address> address = company.address();
                System.out.println(address);
                saveVisitorCompany(context, company, dataCreation);
                saveAddress(context, address, dataCreation);
            });
            visitor.address().ifPresent( address -> saveAddress(context, Optional.ofNullable(address), dataCreation));
            saveVisitor(context, visitor);
        });
        return VisitorRepository.super.getVisitorById(visitor.visitorId());
    }

    @Override
    public List<Visitor> getVisitors(Optional<VisitorQuery> query) {
        if (!query.isPresent()) {
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
        Addresses visitorAddress = ADDRESSES.as("visitor_address");
        Addresses companyAddress = ADDRESSES.as("company_address");
        return getDSLContext(DBContext.READ)
                .select(
                        VISITORS.asterisk(),
                        COMPANY.asterisk(),
                        visitorAddress.ADDRESSID.as("visitorAddressId"),
                        visitorAddress.BUILDINGNUMBER.as("visitorBuildingId"),
                        visitorAddress.DISTICTID.as("visitorDisticitId"),
                        visitorAddress.STREETNAME.as("visitorStreetName"),
                        visitorAddress.UNITNUMBER.as("visitorUnitNo"),
                        visitorAddress.DESCRIPTION.as("visitorDescription"),
                        visitorAddress.TYPE.as("visitorType"),
                        companyAddress.ADDRESSID.as("companyAddressId"),
                        companyAddress.BUILDINGNUMBER.as("companyBuildingId"),
                        companyAddress.DISTICTID.as("companyDisticitId"),
                        companyAddress.STREETNAME.as("companyStreetName"),
                        companyAddress.UNITNUMBER.as("companyUnitNo"),
                        companyAddress.DESCRIPTION.as("companyDescription"),
                        companyAddress.TYPE.as("companyType")
                )
                .from(VISITORS)
                .leftJoin(COMPANY)
                .on(COMPANY.COMPANYID.eq(VISITORS.COMPANYID))
                .leftJoin(visitorAddress)
                .on(visitorAddress.DISTICTID.eq(VISITORS.VISITORID))
                .leftJoin(companyAddress)
                .on(companyAddress.DISTICTID.eq(COMPANY.COMPANYID))
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

    @Override
    public Optional<Visitor> update(String visitorId, UpdateVisitor visitorUpdate) {

        try {
            updateVisitor(visitorId, visitorUpdate);
        } catch (NotFoundException ex) {
            Logger.getLogger(JooqVisitorRepository.class.getName()).log(Level.SEVERE, null, ex);
            return Optional.empty();
        }
        return getVisitorById(visitorId);
    }

    private List<Visitor> getAllVisitors() {
        return getDSLContext(DBContext.READ)
                .select()
                .from(VISITORS)
                .leftJoin(ADDRESSES)
                .on(ADDRESSES.DISTICTID.eq(VISITORS.VISITORID))
                .leftJoin(COMPANY)
                .on(COMPANY.COMPANYID.eq(VISITORS.COMPANYID))
                .join(ADDRESSES)
                .on(ADDRESSES.DISTICTID.eq(COMPANY.COMPANYID))
                .fetch()
                .stream()
                .map(this::deserializeVisitor)
                .collect(Collectors.toList());
    }

    private void saveVisitorCompany(DSLContext context, Visitor.Company company, DataCreation dataCreation) {
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

    private void saveAddress(DSLContext context, Optional<Visitor.Address> addr, DataCreation dataCreation) {
        addr.ifPresent(address -> {
            context.insertInto(ADDRESSES)
                    .columns(
                            ADDRESSES.ADDRESSID,
                            ADDRESSES.DISTICTID,
                            ADDRESSES.STREETNAME,
                            ADDRESSES.BUILDINGNUMBER,
                            ADDRESSES.UNITNUMBER,
                            ADDRESSES.TYPE,
                            ADDRESSES.DESCRIPTION,
                            ADDRESSES.CREATEDBY,
                            ADDRESSES.CREATEDDATE,
                            ADDRESSES.STATUS
                    ).values(
                            address.id(),
                            address.distictId(),
                            address.streetName(),
                            address.buildingNumber(),
                            address.unitNo().orElse(null),
                            address.type().name(),
                            address.otherDescriptions().orElse(null),
                            dataCreation.createdBy(),
                            dataCreation.createdDateTime(),
                            dataCreation.status().name()
                    ).execute();
        });
    }

    public void saveVisitor(DSLContext context, Visitor visitor) {
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
                        visitor.dataCreation().createdDateTime(),
                        visitor.dataCreation().status().name()
                ).onDuplicateKeyUpdate().set(VISITORS.FIRSTNAME, visitor.firstName())
                .set(VISITORS.LASTNAME, visitor.firstName())
                .set(VISITORS.OTHERNAME, visitor.firstName())
                .set(VISITORS.PHONENUMBER, visitor.firstName())
                .set(VISITORS.EMAILADDRESS, visitor.firstName())
                .set(VISITORS.STATUS, visitor.dataCreation().status().name()).execute();
    }

    protected Optional<VisitorDO> visitorDataObject(String visitorId) {
        DSLContext context = getDSLContext(DBContext.READ);
        return context.select()
                .from(VISITORS)
                .leftJoin(COMPANY)
                .on(COMPANY.COMPANYID.eq(VISITORS.COMPANYID))
                .leftJoin(ADDRESSES)
                .on(ADDRESSES.DISTICTID.eq(VISITORS.VISITORID))
                .where(VISITORS.VISITORID.eq(visitorId))
                .fetch()
                .stream()
                .map(this::deserializeVisitorDO).findFirst();
    }

    protected void updateVisitor(String visitorId, UpdateVisitor visitorUpdate) throws NotFoundException {

        Optional<VisitorDO> visitorDO = visitorDataObject(visitorId);
        if (!visitorDO.isPresent()) {
            throw new NotFoundException(String.format("Update Failed !!!, Visitor with ID ( %s )", visitorId));
        }
        getDSLContext(DBContext.WRITE).transaction(configuration -> {
            DSLContext transactionContext = DSL.using(configuration);
            UpdateSetMoreStep<VisitorsRecord> updateStep = transactionContext.update(VISITORS).set(VISITORS.VISITORID, visitorId);
            visitorUpdate.firstName().ifPresent(firstName -> updateStep.set(VISITORS.FIRSTNAME, firstName));
            visitorUpdate.lastName().ifPresent(lastName -> updateStep.set(VISITORS.LASTNAME, lastName));
            visitorUpdate.otherNames().ifPresent(otherNames -> updateStep.set(VISITORS.OTHERNAME, otherNames));
            visitorUpdate.email().ifPresent(email -> updateStep.set(VISITORS.EMAILADDRESS, email));
            visitorUpdate.phoneNumber().ifPresent(phoneNumber -> updateStep.set(VISITORS.PHONENUMBER, phoneNumber.getRawInput()));
            visitorUpdate.company().ifPresent(company -> updateCompay(transactionContext, visitorId, visitorDO.map(VisitorDO::companyId).orElse(Optional.empty()), visitorUpdate));
            visitorUpdate.address().ifPresent(address -> updateAddress(transactionContext, visitorDO.map(VisitorDO::addressId).orElse(Optional.empty()), visitorId, visitorUpdate, AddressType.PERSONAL));
            updateStep.where(VISITORS.VISITORID.eq(visitorId)).execute();
        });
    }

    protected void updateCompanyId(DSLContext context, String visitorId, String companyId) {
        context.update(VISITORS).set(VISITORS.COMPANYID, companyId).where(VISITORS.VISITORID.eq(visitorId)).execute();
    }

    protected void updateCompay(DSLContext transactionContext, String visitorId, Optional<String> companyId, UpdateVisitor updateVisitor) {

        updateVisitor.company().ifPresent(company -> {
            companyId.ifPresentOrElse(id -> {
                UpdateSetMoreStep<CompanyRecord> updateStep = transactionContext.update(COMPANY).set(COMPANY.COMPANYID, id);
                company.name().ifPresent(name -> updateStep.set(COMPANY.NAME, name));
                company.address().ifPresent(address -> {
                    getCompanyAddressId(
                            transactionContext,
                            id,
                            address.type()
                    ).ifPresent(
                            companyAddressId
                            -> updateAddress(transactionContext, Optional.of(companyAddressId), visitorId, updateVisitor, address.type()));
                });
                updateStep.where(COMPANY.COMPANYID.eq(id)).execute();
            }, () -> {
                company.name().ifPresent(name -> {
                    String id = Cuid.createCuid();
                    DataCreation dataCreation = DataCreation.create(updateVisitor.updatedBy(), LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), Status.APPROVED);
                    UpdateVisitor.Address address = company.address().isPresent() ? company.address().get() : null;
                    Address companyAddress = address != null && address.streetName().isPresent() ? Address.create(Cuid.createCuid(), id, address.streetName().get(), address.buildingNumber().get(), address.unitNo(), address.otherDescriptions(), address.type()) : null;

                    if (companyAddress != null) {

                        saveAddress(transactionContext, Optional.ofNullable(companyAddress), dataCreation);
                    }

                    Visitor.Company visitorCompany = Visitor.Company.create(
                            id,
                            name,
                            Optional.ofNullable(companyAddress)
                    );
                    saveVisitorCompany(transactionContext, visitorCompany, dataCreation);
                    updateCompanyId(transactionContext, visitorId, id);

                });

            });

        });
    }

    protected void updateAddress(DSLContext transactionContext, Optional<String> addressId, String distictId, UpdateVisitor updateVisitor, AddressType type) {
        System.out.println(addressId);
        switch (type) {
            case COMPANY:
                updateVisitor.company().ifPresent(company -> company.address().ifPresent(address -> {
                    addressId.ifPresentOrElse(id -> {
                        UpdateSetMoreStep<AddressesRecord> updateStep = transactionContext.update(ADDRESSES).set(ADDRESSES.ADDRESSID, id);
                        System.out.println(address);
                        address.streetName().ifPresent(street -> updateStep.set(ADDRESSES.STREETNAME, street));
                        address.buildingNumber().ifPresent(buillding -> updateStep.set(ADDRESSES.BUILDINGNUMBER, buillding));
                        address.unitNo().ifPresent(unit -> updateStep.set(ADDRESSES.UNITNUMBER, unit));
                        address.otherDescriptions().ifPresent(description -> updateStep.set(ADDRESSES.DESCRIPTION, description));
                        updateStep.where(ADDRESSES.ADDRESSID.eq(id)).execute();
                    }, () -> {
                        String id = Cuid.createCuid();
                        Visitor.Address visitorAddress = Visitor.Address.create(id, distictId, address.streetName().get(), address.buildingNumber().get(), address.unitNo(), address.otherDescriptions(), address.type());
                        saveAddress(transactionContext, Optional.ofNullable(visitorAddress), DataCreation.create(updateVisitor.updatedBy(), LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), Status.APPROVED));

                    });
                }));
            default:
                updateVisitor.address().ifPresent(address -> {
                    addressId.ifPresentOrElse(id -> {
                        UpdateSetMoreStep<AddressesRecord> updateStep = transactionContext.update(ADDRESSES).set(ADDRESSES.ADDRESSID, id);
                        System.out.println(address);
                        address.streetName().ifPresent(street -> updateStep.set(ADDRESSES.STREETNAME, street));
                        address.buildingNumber().ifPresent(buillding -> updateStep.set(ADDRESSES.BUILDINGNUMBER, buillding));
                        address.unitNo().ifPresent(unit -> updateStep.set(ADDRESSES.UNITNUMBER, unit));
                        address.otherDescriptions().ifPresent(description -> updateStep.set(ADDRESSES.DESCRIPTION, description));
                        updateStep.where(ADDRESSES.ADDRESSID.eq(id)).execute();
                    }, () -> {
                        String id = Cuid.createCuid();
                        Visitor.Address visitorAddress = Visitor.Address.create(id, distictId, address.streetName().get(), address.buildingNumber().get(), address.unitNo(), address.otherDescriptions(), address.type());
                        saveAddress(transactionContext, Optional.ofNullable(visitorAddress), DataCreation.create(updateVisitor.updatedBy(), LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), Status.APPROVED));

                    });
                });
        }

    }

    protected Optional<String> getCompanyAddressId(DSLContext context, String id, AddressType type) {
        return Optional.ofNullable(context.select(ADDRESSES.ADDRESSID).from(ADDRESSES).where(ADDRESSES.DISTICTID.eq(id)).fetchSingle(ADDRESSES.ADDRESSID));
    }

}
