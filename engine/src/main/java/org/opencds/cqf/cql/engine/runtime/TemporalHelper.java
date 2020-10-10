package org.opencds.cqf.cql.engine.runtime;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Objects;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.opencds.cqf.cql.engine.execution.Context;

public class TemporalHelper {

    public static String[] normalizeDateTimeElements(int ... elements) {
        String[] ret = new String[elements.length];
        for (int i = 0; i < elements.length; ++i) {
            switch (i) {
                case 0: ret[i] = addLeadingZeroes(elements[i], 4); break;
                case 6: ret[i] = addLeadingZeroes(elements[i], 3); break;
                default: ret[i] = addLeadingZeroes(elements[i], 2); break;
            }
        }

        return ret;
    }

    public static String[] normalizeTimeElements(int ... elements) {
        String[] ret = new String[elements.length];
        for (int i = 0; i < elements.length; ++i) {
            switch (i) {
                case 3: ret[i] = addLeadingZeroes(elements[i], 3); break;
                default: ret[i] = addLeadingZeroes(elements[i], 2); break;
            }
        }

        return ret;
    }

    public static String addLeadingZeroes(int element, int length) {
        String strElement = Integer.toString(element);
        return StringUtils.repeat("0", length - strElement.length()) + strElement;
    }

    public static String autoCompleteDateTimeString(String dateString, Precision precision) {
        switch (precision) {
            case YEAR: return dateString + "-01-01T00:00:00.000";
            case MONTH: return dateString + "-01T00:00:00.000";
            case DAY: return dateString + "T00:00:00.000";
            case HOUR: return dateString + ":00:00.000";
            case MINUTE: return dateString + ":00.000";
            case SECOND: return dateString + ".000";
            default: return dateString;
        }
    }

    public static String autoCompleteDateString(String dateString, Precision precision) {
        switch (precision) {
            case YEAR: return dateString + "-01-01";
            case MONTH: return dateString + "-01";
            default: return dateString;
        }
    }

    public static String autoCompleteTimeString(String timeString, Precision precision) {
        switch (precision) {
            case HOUR:
            case MINUTE: return timeString + ":00.000";
            case SECOND: return timeString + ".000";
            default: return timeString;
        }
    }

    public static int[] cleanArray(Integer ... elements) {
        return Arrays.stream(elements).filter(Objects::nonNull).mapToInt(x -> x).toArray();
    }

    public static BigDecimal zoneToOffset(ZoneOffset zone) {
        int seconds = zone.get(ChronoField.OFFSET_SECONDS);
        return new BigDecimal(Double.toString(seconds/60f/60f));
    }

    public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime) {
        Context c = Context.getContext();
        if (c != null) {
            return localDateTime.atZone(c.getEvaluationZonedDateTime().getZone());
        }
        else {
            return localDateTime.atZone(TimeZone.getDefault().toZoneId());
        }
    }

    public static OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        return toZonedDateTime(localDateTime).toOffsetDateTime();
    }

    public static int weeksToDays(int weeks) {
        int years = 0;
        if (weeks >= 52) {
            years = (weeks / 52);
            weeks -= years * 52 ;
        }
        return weeks * 7 + (years * 365);
    }
}
