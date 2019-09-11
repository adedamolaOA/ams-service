package com.adetech.ams.common.domain;

/**
 *
 * @author Ade
 */
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

@Service
public class TimeService {
    public static final int SERVICE_HOUR = 2;
    public static final String LAGOS="Africa/Lagos";
    public static final ZoneId DEFAULT_TIMEZONE = ZoneId.of(LAGOS);

    public Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.MILLIS);
    }

    public static TemporalAdjuster serviceStart() {
        return (Temporal temporal) -> {
            if (temporal instanceof ZonedDateTime) {
                return ((ZonedDateTime) temporal)
                        .truncatedTo(ChronoUnit.DAYS)
                        .withHour(SERVICE_HOUR);
            } else {
                throw new DateTimeException("Unsupported temporal " + (temporal == null ? "" : temporal.getClass().getName()));
            }
        };
    }
}