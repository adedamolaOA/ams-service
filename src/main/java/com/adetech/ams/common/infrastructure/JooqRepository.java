/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.common.infrastructure;

import com.adetech.ams.common.domain.DBContext;
import com.adetech.ams.common.domain.TimeService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;

/**
 *
 * @author Ade
 */
public class JooqRepository {

    private final DSLContext readContext;
    private final DSLContext writeContext;

    protected JooqRepository(DSLContext readContext, DSLContext writeContext) {
        this.readContext = readContext;
        this.writeContext = writeContext;
    }

    protected DSLContext getDSLContext(DBContext dbContext) {
        return dbContext.equals(DBContext.WRITE) ? writeContext : readContext;
    }

    @SafeVarargs
    protected static List<Condition> conditions(Optional<Condition>... conditions) {
        return Stream.of(conditions)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
    protected Condition dateComparison(Field<Long> date, LocalDate localDate){

           Field<LocalDateTime> fromUnixTime = DSL.function("from_unixtime",LocalDateTime.class, date.divide(1000));
           Field<LocalDateTime> convertTz = DSL.function("convert_tz", LocalDateTime.class, fromUnixTime, DSL.inline("SYSTEM"), DSL.inline(TimeService.DEFAULT_TIMEZONE.getId()));

           return DSL.function("date",LocalDate.class, convertTz).eq(localDate);
    }

}
