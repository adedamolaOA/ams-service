/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.department;

import com.adetech.ams.common.domain.DataCreation;
import com.adetech.ams.common.domain.Status;
import static com.adetech.ams.db.Tables.DEPARTMENT;
import java.util.Optional;
import org.jooq.Record;

/**
 *
 * @author Ade
 */
public interface Deserializer {
    
    default Department deserializeDepartment(Record r){
        return Department.create(r.get(DEPARTMENT.DEPARTMENTID), r.get(DEPARTMENT.NAME), Optional.ofNullable(r.get(DEPARTMENT.DESCRIPTION)), DataCreation.create(r.get(DEPARTMENT.CREATEDBY), r.get(DEPARTMENT.CREATEDDATE), Status.valueOf(r.get(DEPARTMENT.STATUS))));
    }
}
