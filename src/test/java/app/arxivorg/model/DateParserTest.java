package app.arxivorg.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class DateParserTest {

    private final String ZONEID = "America/New_York";

    @Test
    void testGetBasicDate() {
        Assertions.assertEquals(DateParser.getBasicDate("Today", ZONEID)[0], LocalDate.now().atStartOfDay(ZoneId.of(ZONEID)).toInstant());
        Assertions.assertEquals(DateParser.getBasicDate("yesterday", ZONEID)[0], LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of(ZONEID)).toInstant());
    }

    @Test
    void testGetDate() {
        Assertions.assertEquals(DateParser.getDate("2020-04-23T04:00:00.00Z"), LocalDate.parse("2020-04-23").atStartOfDay(ZoneId.of(ZONEID)).toInstant());
        Assertions.assertEquals(DateParser.getDate("2000-08-10T04:00:00.00Z"), LocalDate.parse("2000-08-10").atStartOfDay(ZoneId.of(ZONEID)).toInstant());
    }
    @Test
    void isAfter() {
        Assertions.assertTrue(DateParser.isAfter(DateParser.getBasicDate("Today", ZONEID)[0], DateParser.getBasicDate("yesterday", ZONEID)[0]));
        Assertions.assertTrue(DateParser.isAfter(DateParser.getBasicDate("this-month", ZONEID)[0], DateParser.getBasicDate("This Year", ZONEID)[0]));
        Assertions.assertFalse(DateParser.isAfter(DateParser.getBasicDate("this-month", ZONEID)[1], DateParser.getBasicDate("This Year", ZONEID)[1]));
    }

    @Test
    void isBefore() {
        Assertions.assertTrue(DateParser.isBefore(DateParser.getBasicDate("yesterday", ZONEID)[0], DateParser.getBasicDate("today", ZONEID)[0]));
        Assertions.assertTrue(DateParser.isBefore(DateParser.getBasicDate("this-month", ZONEID)[0], DateParser.getBasicDate("This Year", ZONEID)[1]));
        Assertions.assertFalse(DateParser.isBefore(DateParser.getBasicDate("this-month", ZONEID)[1], DateParser.getBasicDate("This Year", ZONEID)[0]));
    }

    @Test
    void isBetween() {
        Assertions.assertTrue(DateParser.isBetween(DateParser.getBasicDate("today", ZONEID)[0], DateParser.getBasicDate("this-month", ZONEID)[0], DateParser.getBasicDate("This Month", ZONEID)[1]));
        Assertions.assertTrue(DateParser.isBetween(DateParser.getBasicDate("yesterday", ZONEID)[0], LocalDate.now().minusDays(2).atStartOfDay(ZoneId.of(ZONEID)).toInstant(), LocalDate.now().atStartOfDay(ZoneId.of(ZONEID)).toInstant()));
        Assertions.assertFalse(DateParser.isBetween(DateParser.getBasicDate("yesterday", ZONEID)[0], LocalDate.now().atStartOfDay(ZoneId.of(ZONEID)).toInstant(), LocalDate.now().plusMonths(4).atStartOfDay(ZoneId.of(ZONEID)).toInstant()));
    }
}