/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.department.infrastructure;

import com.adetech.ams.common.domain.DBContext;
import com.adetech.ams.common.domain.DataCreation;
import com.adetech.ams.common.domain.Status;
import com.adetech.ams.common.infrastructure.JooqRepository;

import com.adetech.ams.department.Department;
import com.adetech.ams.department.domain.DepartmentQuery;
import com.adetech.ams.department.domain.DepartmentRepository;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.adetech.ams.db.Tables.DEPARTMENT;
import com.adetech.ams.department.Deserializer;
import org.jooq.Condition;

/**
 *
 * @author Ade
 */
@Repository
public class JooqDepartmentRepository extends JooqRepository implements DepartmentRepository, Deserializer{
    
    public JooqDepartmentRepository(DSLContext readContext, DSLContext writeContext){
        super(readContext, writeContext);
    }

    @Override
    public Optional<Department> add(Department department) {
        getDSLContext(DBContext.WRITE)
                .insertInto(DEPARTMENT)
                .columns(
                        DEPARTMENT.DEPARTMENTID, 
                        DEPARTMENT.NAME, 
                        DEPARTMENT.DESCRIPTION, 
                        DEPARTMENT.CREATEDBY, 
                        DEPARTMENT.CREATEDDATE, 
                        DEPARTMENT.STATUS
                ).values(
                        department.departmentId(), 
                        department.name(), 
                        department.description().orElse(null), 
                        department.dataCreation().createdBy(),
                        department.dataCreation().createdDateTime(), 
                        department.dataCreation().status().name()
                ).onDuplicateKeyUpdate()
                .set(DEPARTMENT.NAME, department.name())
                .set(DEPARTMENT.DESCRIPTION, department.description().orElse(null))
                .execute();
        return getByDepartmentId(department.departmentId());
        
    }

    @Override
    public Optional<Department> update(Department department) {
        getDSLContext(DBContext.WRITE)
                .update(DEPARTMENT)
                .set(DEPARTMENT.NAME, department.name())
                .set(DEPARTMENT.DESCRIPTION, department.description().orElse(null))
                .where(DEPARTMENT.DEPARTMENTID.eq(department.departmentId()))
                .execute();
        
        return getByDepartmentId(department.departmentId());
    }

    @Override
    public List<Department> get(Optional<DepartmentQuery> query) {
        if(!query.isPresent()){
            return getAll();
        }
        
        DepartmentQuery dq = query.get();
        List<Condition> conditions = conditions(
                dq.departmentId().map(DEPARTMENT.DEPARTMENTID::eq),
                dq.departmentIds().map(DEPARTMENT.DEPARTMENTID::in),
                dq.name().map(name -> name + "%").map(DEPARTMENT.NAME::like)
        );
        
        return getDSLContext(DBContext.WRITE).select().from(DEPARTMENT).where(conditions).fetch().map(this::deserializeDepartment);
    }

    @Override
    public Optional<Department> getByDepartmentId(String id) {
        return DepartmentRepository.super.getByDepartmentId(id); 
    }

    @Override
    public Optional<Department> delete(String id) {
        getDSLContext(DBContext.WRITE).update(DEPARTMENT).set(DEPARTMENT.STATUS, Status.DELETED.name()).where(DEPARTMENT.DEPARTMENTID.eq(id)).execute();
        return getByDepartmentId(id);
    }
    
    protected List<Department> getAll(){
        return getDSLContext(DBContext.READ).select().from(DEPARTMENT).fetch().map(this::deserializeDepartment);
    }
    
}
