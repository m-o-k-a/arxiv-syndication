package app.arxivorg.model;

import javafx.scene.control.DatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

public class DateParser {

    /**
     * The method getDate convert to Instant the time stored in a datePicker with a specified ZoneId.
     * @param datePicker
     * @param ZONEID
     * @return
     */
    public static Instant getDate(DatePicker datePicker, String ZONEID) {
        LocalDate localDate = datePicker.getValue();
        return Instant.from(localDate.atStartOfDay(ZoneId.of(ZONEID)));
    }

    /**
     * The method getDate convert to Instant the time stored in a string (an Instant format String).
     * @param date
     * @return
     */
    public static Instant getDate(String date) {
        return Instant.parse(date);
    }

    /**
     * The method isAfter check if the specified date is after another date.
     * @param date
     * @param beforeDate
     * @return
     */
    public static boolean isAfter(Instant date, Instant beforeDate) {
        return date.isAfter(beforeDate);
    }

    /**
     * The method isAfter check if the specified date is before another date.
     * @param date
     * @param afterDate
     * @return
     */
    public static boolean isBefore(Instant date, Instant afterDate) {
        return date.isBefore(afterDate);
    }

    /**
     * The method isAfter check if the specified date is between to dates.
     * @param date
     * @param beforeDate
     * @param afterDate
     * @return
     */
    public static boolean isBetween(Instant date, Instant beforeDate, Instant afterDate) {
        return date.isBefore(afterDate) && date.isAfter(beforeDate);
    }

    /**
     * The method getBasicDate return an array of Instant [start, end] according to a specified string and ZoneId:
     * today || Today,
     * yesterday || Yesterday,
     * this-week || This Week,
     * this-month || This Month,
     * this-year || This Year
     * @param date
     * @param ZONEID
     * @return
     */
    public static Instant[] getBasicDate(String date, String ZONEID) {
        Instant[] dates = new Instant[2];
        String[] dateTemp = Instant.now().toString().split("-");
        switch(date) {
            case "today":
            case "Today":
                dates[0] = LocalDate.now().atStartOfDay(ZoneId.of(ZONEID)).toInstant();
                dates[1] = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.of(ZONEID)).toInstant();
                return dates;
            case "yesterday":
            case "Yesterday":
                dates[0] = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of(ZONEID)).toInstant();
                dates[1] = LocalDate.now().atStartOfDay(ZoneId.of(ZONEID)).toInstant();
                return dates;
            case "this-week":
            case "This Week":
                dates[0] = LocalDate.now().minus(Period.ofDays(7)).atStartOfDay(ZoneId.of(ZONEID)).toInstant();
                dates[1] = LocalDate.now().atStartOfDay(ZoneId.of(ZONEID)).toInstant();
                return dates;
            case "this-month":
            case "This Month":
                dates[0] = LocalDate.parse(dateTemp[0]+"-"+dateTemp[1]+"-01").atStartOfDay(ZoneId.of(ZONEID)).toInstant();
                dates[1] = LocalDate.parse(dateTemp[0]+"-"+dateTemp[1]+"-01").plusMonths(1).atStartOfDay(ZoneId.of(ZONEID)).toInstant();
                return dates;
            case "this-year":
            case "This Year":
                dates[0] = LocalDate.parse(dateTemp[0]+"-01-01").atStartOfDay(ZoneId.of(ZONEID)).toInstant();
                dates[1] = LocalDate.parse(dateTemp[0]+"-12-31").atStartOfDay(ZoneId.of(ZONEID)).toInstant();
                return dates;
            default :
                return null;
        }
    }

    /**
     * The method getAdvancedDate is used with the Interface controller, allowing to generate
     * an Instant array [start, end] with the values specified in theses datepickers.
     * @param datePickerFrom
     * @param datePickerTo
     * @param ZONEID
     * @return
     */
    public static Instant[] getAdvancedDate(DatePicker datePickerFrom, DatePicker datePickerTo, String ZONEID) {
        Instant[] dates = new Instant[2];
        if(datePickerFrom.getValue() != null) { dates[0] = datePickerFrom.getValue().atStartOfDay(ZoneId.of(ZONEID)).toInstant(); }
        else { dates[0] = Instant.MIN; }
        if(datePickerTo.getValue() != null) { dates[1] = datePickerTo.getValue().plusDays(1).atStartOfDay(ZoneId.of(ZONEID)).toInstant(); }
        else { dates[1] = Instant.MAX; }
        return dates;
    }

}
