package org.opencds.cqf.cql.engine.fhir.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.hamcrest.Matchers.instanceOf;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hl7.fhir.dstu3.model.*;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.Concept;
import org.opencds.cqf.cql.engine.runtime.CqlType;
import org.opencds.cqf.cql.engine.runtime.Date;
import org.opencds.cqf.cql.engine.runtime.DateTime;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.opencds.cqf.cql.engine.runtime.Quantity;
import org.opencds.cqf.cql.engine.runtime.Ratio;
import org.opencds.cqf.cql.engine.runtime.Tuple;
import org.apache.commons.lang3.NotImplementedException;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Dstu3TypeConverterTests {

    private Dstu3FhirTypeConverter typeConverter;

    protected Boolean compareIterables(Iterable<Object> left, Iterable<Object> right) {
        Iterator<Object> leftIterator = left.iterator();
        Iterator<Object> rightIterator = right.iterator();

        while (leftIterator.hasNext() && rightIterator.hasNext()) {
            Object currentL = leftIterator.next();
            Object currentR = rightIterator.next();

            Boolean result = compareObjects(currentL, currentR);
            if (!result) {
                return false;
            }
        }

        return !leftIterator.hasNext() && !leftIterator.hasNext();
    }

    @SuppressWarnings("unchecked")
    protected Boolean compareObjects(Object left, Object right) {
        if (left == null ^ right == null) {
            return false;
        }

        if (left == null && right == null) {
            return true;
        }

        if (!left.getClass().equals(right.getClass())) {
            return false;
        }

        if (left instanceof Iterable<?>) {
            return compareIterables((Iterable<Object>) left, (Iterable<Object>) right);
        }

        if (left instanceof CqlType) {
            return ((CqlType) left).equals((CqlType) right);
        }

        if (left instanceof Base) {
            return ((Base) left).equalsDeep((Base) right);
        }

        return left.equals(right);
    }

    @BeforeClass
    public void initialize() {
        this.typeConverter = new Dstu3FhirTypeConverter();
    }

    // CQL-to-FHIR
    @Test
    public void TestIsFhirType() {
        assertTrue(this.typeConverter.isFhirType(new Patient()));
        assertTrue(this.typeConverter.isFhirType(new IdType()));

        assertFalse(this.typeConverter.isFhirType(5));
        assertFalse(this.typeConverter.isFhirType(new BigDecimal(0)));
        assertFalse(this.typeConverter.isFhirType(new Code()));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void TestNullIsFhirType() {
        this.typeConverter.isFhirType(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void TestIterableIsFhirType() {
        this.typeConverter.isFhirType(new ArrayList<>());
    }

    @Test
    public void TestToFhirType() {
        IBase actual = this.typeConverter.toFhirType(new Code());
        assertThat(actual, instanceOf(Coding.class));

        actual = this.typeConverter.toFhirType(5);
        assertThat(actual, instanceOf(IntegerType.class));

        actual = this.typeConverter.toFhirType(new IdType());
        assertThat(actual, instanceOf(IdType.class));

        actual = this.typeConverter.toFhirType(null);
        assertNull(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void TestToFhirTypeIterable() {
        this.typeConverter.toFhirType(new ArrayList<>());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void TestToFhirTypeNotCql() {
        this.typeConverter.toFhirType(ZoneOffset.ofHours(3));
    }

    @Test
    public void TestToFhirTypes() {
        List<Object> innerExpected = new ArrayList<>();
        innerExpected.add(new StringType("123"));
        innerExpected.add(null);
        List<Object> expected = new ArrayList<>();
        expected.add(innerExpected);
        expected.add(null);
        expected.add(new IntegerType(5));

        List<Object> innerTest = new ArrayList<>();
        innerTest.add("123");
        innerTest.add(null);
        List<Object> test = new ArrayList<>();
        test.add(innerTest);
        test.add(null);
        test.add(5);

        Iterable<Object> actual = this.typeConverter.toFhirTypes(test);

        assertTrue(compareIterables(expected, actual));
    }

    @Test
    public void TestStringToFhirId() {
        IIdType expected = new IdType("123");
        IIdType actual = this.typeConverter.toFhirId("123");
        assertEquals(expected.getValue(), actual.getValue());

        actual = this.typeConverter.toFhirId(null);
        assertNull(actual);
    }

    @Test
    public void TestPrimitiveCqlTypeToFhirType() {
        IPrimitiveType<Boolean> expectedBoolean = new BooleanType(false);
        IPrimitiveType<Boolean> actualBoolean = this.typeConverter.toFhirBoolean(false);
        assertEquals(expectedBoolean.getValue(), actualBoolean.getValue());

        expectedBoolean = this.typeConverter.toFhirBoolean(null);
        assertNull(expectedBoolean);

        IPrimitiveType<Integer> expectedInteger = new IntegerType(5);
        IPrimitiveType<Integer> actualInteger = this.typeConverter.toFhirInteger(5);
        assertEquals(expectedInteger.getValue(), actualInteger.getValue());

        expectedInteger = this.typeConverter.toFhirInteger(null);
        assertNull(expectedInteger);

        IPrimitiveType<String> expectedString = new StringType("5");
        IPrimitiveType<String> actualString = this.typeConverter.toFhirString("5");
        assertEquals(expectedString.getValue(), actualString.getValue());

        expectedString = this.typeConverter.toFhirString(null);
        assertNull(expectedString);

        IPrimitiveType<BigDecimal> expectedDecimal = new DecimalType(new BigDecimal(2.0));
        IPrimitiveType<BigDecimal> actualDecimal = this.typeConverter.toFhirDecimal(new BigDecimal(2.0));
        assertEquals(expectedDecimal.getValue(), actualDecimal.getValue());

        expectedDecimal = this.typeConverter.toFhirDecimal(null);
        assertNull(expectedDecimal);
    }

    @Test
    public void TestDateToFhirDate() {
        IPrimitiveType<java.util.Date> expectedDate = new DateType("2019-02-03");
        IPrimitiveType<java.util.Date> actualDate = this.typeConverter.toFhirDate(new Date("2019-02-03"));
        assertEquals(expectedDate.getValue(), actualDate.getValue());

        expectedDate = new DateType("2019");
        actualDate = this.typeConverter.toFhirDate(new Date("2019"));
        assertEquals(expectedDate.getValue(), actualDate.getValue());
    }

    @Test
    public void TestDateTimeToFhirDateTime() {
        IPrimitiveType<java.util.Date> expectedDate = new DateTimeType("2019-02-03");
        IPrimitiveType<java.util.Date> actualDate = this.typeConverter
                .toFhirDateTime(new DateTime("2019-02-03", null));
        assertEquals(expectedDate.getValue(), actualDate.getValue());

        expectedDate = new DateTimeType("2019");
        actualDate = this.typeConverter.toFhirDateTime(new DateTime("2019", null));
        assertEquals(expectedDate.getValue(), actualDate.getValue());

        expectedDate = new DateTimeType("2019");
        actualDate = this.typeConverter.toFhirDateTime(new DateTime("2019", null));
        assertEquals(expectedDate.getValue(), actualDate.getValue());
    }

    @Test
    public void TestQuantityToFhirQuantity() {
        org.hl7.fhir.dstu3.model.Quantity expected = new org.hl7.fhir.dstu3.model.Quantity(2.0).setCode("ml")
                .setSystem("http://unitsofmeasure.org");
        org.hl7.fhir.dstu3.model.Quantity actual = (org.hl7.fhir.dstu3.model.Quantity) this.typeConverter
                .toFhirQuantity(new Quantity().withValue(new BigDecimal("2.0")).withUnit("ml"));
        assertTrue(expected.equalsDeep(actual));
    }

    @Test
    public void TestRatioToFhirRatio() {
        org.hl7.fhir.dstu3.model.Quantity expectedNumerator = new org.hl7.fhir.dstu3.model.Quantity(1.0).setCode("ml")
                .setSystem("http://unitsofmeasure.org");
        org.hl7.fhir.dstu3.model.Quantity expectedDenominator = new org.hl7.fhir.dstu3.model.Quantity(2.0).setCode("ml")
                .setSystem("http://unitsofmeasure.org");

        org.hl7.fhir.dstu3.model.Ratio expected = new org.hl7.fhir.dstu3.model.Ratio().setNumerator(expectedNumerator)
                .setDenominator(expectedDenominator);

        Ratio testData = new Ratio();
        testData.setNumerator(new Quantity().withValue(BigDecimal.valueOf(1.0)).withUnit("ml"));
        testData.setDenominator(new Quantity().withValue(BigDecimal.valueOf(2.0)).withUnit("ml"));

        org.hl7.fhir.dstu3.model.Ratio actual = (org.hl7.fhir.dstu3.model.Ratio) this.typeConverter.toFhirRatio(testData);

        assertTrue(expected.equalsDeep(actual));
    }

    @Test()
    public void TestNullToFhirAny() {
        IBase expected = this.typeConverter.toFhirAny(null);
        assertNull(expected);
    }

    @Test(expectedExceptions = NotImplementedException.class)
    public void TestObjectToFhirAny() {
        this.typeConverter.toFhirAny("Huh");
    }

    @Test
    public void TestCodeToFhirCoding() {
        Coding expected = new Coding("http://the-system.com", "test", "system-test").setVersion("1.5");
        Coding actual = (Coding) this.typeConverter.toFhirCoding(new Code().withSystem("http://the-system.com")
                .withCode("test").withDisplay("system-test").withVersion("1.5"));
        assertTrue(expected.equalsDeep(actual));

        expected = (Coding) this.typeConverter.toFhirCoding(null);
        assertNull(expected);
    }

    @Test
    public void TestConceptToFhirCodeableConcept() {
        CodeableConcept expected = new CodeableConcept(
                new Coding("http://the-system.com", "test", "system-test").setVersion("1.5"))
                        .setText("additional-text");
        CodeableConcept actual = (CodeableConcept) this.typeConverter.toFhirCodeableConcept(
                new Concept().withCode(new Code().withSystem("http://the-system.com").withCode("test")
                        .withDisplay("system-test").withVersion("1.5")).withDisplay("additional-text"));
        assertTrue(expected.equalsDeep(actual));

        expected = (CodeableConcept) this.typeConverter.toFhirCodeableConcept(null);
        assertNull(expected);
    }

    @Test
    public void TestIntervalToFhirPeriod() {
        Period expected = new Period().setStartElement(new DateTimeType("2019-02-03"))
                .setEndElement(new DateTimeType("2019-02-05"));
        Period actual = (Period) this.typeConverter
                .toFhirPeriod(new Interval(new Date("2019-02-03"), true, new Date("2019-02-05"), true));
        assertTrue(expected.equalsDeep(actual));

        expected = new Period().setStartElement(new DateTimeType("2019")).setEndElement(new DateTimeType("2020"));
        actual = (Period) this.typeConverter.toFhirPeriod(
                new Interval(new DateTime("2019", null), true, new DateTime("2020", null), true));
        assertTrue(expected.equalsDeep(actual));

        actual = (Period) this.typeConverter.toFhirPeriod(null);
        assertNull(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void TestInvalidIntervalToFhirPeriod() {
        this.typeConverter.toFhirPeriod(new Interval(5, true, 6, true));
    }

    @Test
    public void TestIntervalToFhirRange() {
        Range expected = new Range()
                .setLow((SimpleQuantity)new org.hl7.fhir.dstu3.model.SimpleQuantity().setValue(2.0).setCode("ml").setSystem("http://unitsofmeasure.org"))
                .setHigh((SimpleQuantity)new org.hl7.fhir.dstu3.model.SimpleQuantity().setValue(5.0).setCode("ml").setSystem("http://unitsofmeasure.org"));
        Range actual = (Range) this.typeConverter
                .toFhirRange(new Interval(new Quantity().withValue(new BigDecimal("2.0")).withUnit("ml"), true,
                        new Quantity().withValue(new BigDecimal("5.0")).withUnit("ml"), true));
        assertTrue(expected.equalsDeep(actual));

        actual = (Range) this.typeConverter.toFhirRange(null);
        assertNull(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void TestInvalidIntervalToFhirRange() {
        this.typeConverter.toFhirRange(new Interval(5, true, 6, true));
    }

    @Test
    public void TestIntervalToFhirInterval() {
        Period expectedPeriod = new Period().setStartElement(new DateTimeType("2019-02-03"))
                .setEndElement(new DateTimeType("2019-02-05"));
        Period actualPeriod = (Period) this.typeConverter
                .toFhirInterval(new Interval(new Date("2019-02-03"), true, new Date("2019-02-05"), true));
        assertTrue(expectedPeriod.equalsDeep(actualPeriod));

        Range expectedRange = new Range()
                .setLow((SimpleQuantity)new org.hl7.fhir.dstu3.model.SimpleQuantity().setValue(2.0).setCode("ml").setSystem("http://unitsofmeasure.org"))
                .setHigh((SimpleQuantity)new org.hl7.fhir.dstu3.model.SimpleQuantity().setValue(5.0).setCode("ml").setSystem("http://unitsofmeasure.org"));
        Range actualRange = (Range) this.typeConverter
                .toFhirInterval(new Interval(new Quantity().withValue(new BigDecimal("2.0")).withUnit("ml"), true,
                        new Quantity().withValue(new BigDecimal("5.0")).withUnit("ml"), true));
        assertTrue(expectedRange.equalsDeep(actualRange));

        ICompositeType expected = this.typeConverter.toFhirInterval(null);
        assertNull(expected);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void TestInvalidIntervalToFhirInterval() {
        this.typeConverter.toFhirInterval(new Interval(5, true, 6, true));
    }

    @Test(expectedExceptions = NotImplementedException.class)
    public void TestTupleToFhirTuple() {
        IBase expected = this.typeConverter.toFhirTuple(null);
        assertNull(expected);

        this.typeConverter.toFhirTuple(new Tuple());
    }

    // FHIR-to-CQL
    @Test
    public void TestIsCqlType() {
        assertTrue(this.typeConverter.isCqlType(5));
        assertTrue(this.typeConverter.isCqlType(new BigDecimal(0)));
        assertTrue(this.typeConverter.isCqlType(new Code()));

        assertFalse(this.typeConverter.isCqlType(new Patient()));
        assertFalse(this.typeConverter.isCqlType(new IdType()));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void TestNullIsCqlType() {
        this.typeConverter.isCqlType(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void TestIterableIsCqlType() {
        this.typeConverter.isCqlType(new ArrayList<>());
    }

    @Test
    public void TestToCqlType() {
        Object actual = this.typeConverter.toCqlType(new Code());
        assertThat(actual, instanceOf(Code.class));

        actual = this.typeConverter.toCqlType(new IntegerType(5));
        assertThat(actual, instanceOf(Integer.class));

        actual = this.typeConverter.toCqlType(new StringType("test"));
        assertThat(actual, instanceOf(String.class));

        actual = this.typeConverter.toCqlType(null);
        assertNull(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void TestToCqlTypeIterable() {
        this.typeConverter.toCqlType(new ArrayList<>());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void TestToCqlTypeNotCql() {
        this.typeConverter.toCqlType(ZoneOffset.ofHours(3));
    }

    @Test
    public void TestToCqlTypes() {

        List<Object> innerExpected = new ArrayList<>();
        innerExpected.add("123");
        innerExpected.add(null);
        List<Object> expected = new ArrayList<>();
        expected.add(innerExpected);
        expected.add(null);
        expected.add(5);

        List<Object> innerTest = new ArrayList<>();
        innerTest.add(new StringType("123"));
        innerTest.add(null);
        List<Object> test = new ArrayList<>();
        test.add(innerTest);
        test.add(null);
        test.add(new IntegerType(5));

        Iterable<Object> actual = this.typeConverter.toCqlTypes(test);

        assertTrue(compareIterables(expected, actual));
    }

    @Test
    public void TestStringToCqlId() {
        String expected = "123";
        String actual = this.typeConverter.toCqlId(new IdType("123"));
        assertEquals(expected, actual);

        actual = this.typeConverter.toCqlId(null);
        assertNull(actual);
    }

    @Test
    public void TestPrimitiveFhirTypeToCqlType() {
        Boolean expectedBoolean = false;
        Boolean actualBoolean = this.typeConverter.toCqlBoolean(new BooleanType(false));
        assertEquals(expectedBoolean, actualBoolean);

        expectedBoolean = this.typeConverter.toCqlBoolean(null);
        assertNull(expectedBoolean);

        Integer expectedInteger = 5;
        Integer actualInteger = this.typeConverter.toCqlInteger(new IntegerType(5));
        assertEquals(expectedInteger, actualInteger);

        expectedInteger = this.typeConverter.toCqlInteger(null);
        assertNull(expectedInteger);

        String expectedString = "5";
        String actualString = this.typeConverter.toCqlString(new StringType("5"));
        assertEquals(expectedString, actualString);

        expectedString = this.typeConverter.toCqlString(null);
        assertNull(expectedString);

        BigDecimal expectedDecimal = new BigDecimal(2.0);
        BigDecimal actualDecimal = this.typeConverter.toCqlDecimal(new DecimalType(new BigDecimal(2.0)));
        assertEquals(expectedDecimal, actualDecimal);

        expectedDecimal = this.typeConverter.toCqlDecimal(null);
        assertNull(expectedDecimal);
    }

    @Test
    public void TestDateToCqlType() {
        Date expectedDate = new Date("2019-02-03");
        Date actualDate = this.typeConverter.toCqlDate(new org.hl7.fhir.dstu3.model.DateType("2019-02-03"));
        assertTrue(expectedDate.equal(actualDate));

        expectedDate = new Date("2019");
        actualDate = this.typeConverter.toCqlDate(new DateType("2019"));
        assertTrue(expectedDate.equal(actualDate));
    }

    @Test
    public void TestDateTimeToCqlType() {
        DateTime expectedDate = new DateTime("2019-02-03", ZoneOffset.UTC);
        DateTime actualDate = this.typeConverter.toCqlDateTime(new DateTimeType("2019-02-03"));
        assertTrue(expectedDate.equal(actualDate));

        expectedDate = new DateTime("2019", ZoneOffset.UTC);
        actualDate = this.typeConverter.toCqlDateTime(new DateTimeType("2019"));
        assertTrue(expectedDate.equal(actualDate));

        expectedDate = new DateTime("2019", ZoneOffset.UTC);
        actualDate = this.typeConverter.toCqlDateTime(new DateTimeType("2019"));
        assertTrue(expectedDate.equal(actualDate));
    }

    @Test
    public void TestQuantityToCqlType() {
        Quantity expected = (new Quantity().withValue(new BigDecimal("2.0")).withUnit("ml"));
        Quantity actual = this.typeConverter
                .toCqlQuantity(new org.hl7.fhir.dstu3.model.Quantity(2.0).setUnit("ml")
                .setSystem("http://unitsofmeasure.org"));
        assertTrue(expected.equal(actual));
    }

    @Test
    public void TestRatioToCqlType() {
        Ratio expected = new Ratio();
        expected.setNumerator(new Quantity().withValue(BigDecimal.valueOf(1.0)).withUnit("ml"));
        expected.setDenominator(new Quantity().withValue(BigDecimal.valueOf(2.0)).withUnit("ml"));

        org.hl7.fhir.dstu3.model.Quantity testNumerator = new org.hl7.fhir.dstu3.model.Quantity(1.0).setUnit("ml")
                .setSystem("http://unitsofmeasure.org");
        org.hl7.fhir.dstu3.model.Quantity testDenominator = new org.hl7.fhir.dstu3.model.Quantity(2.0).setUnit("ml")
                .setSystem("http://unitsofmeasure.org");

        org.hl7.fhir.dstu3.model.Ratio test = new org.hl7.fhir.dstu3.model.Ratio().setNumerator(testNumerator)
                .setDenominator(testDenominator);

        Ratio actual = this.typeConverter.toCqlRatio(test);
        assertTrue(expected.equal(actual));
    }

    @Test()
    public void TestNullToCqlType() {
        Object expected = this.typeConverter.toCqlAny(null);
        assertNull(expected);
    }

    @Test(expectedExceptions = NotImplementedException.class)
    public void TestObjectToCqlType() {
        this.typeConverter.toCqlAny(new IdType());
    }

    @Test
    public void TestCodingToCqlCode() {
        Code expected = new Code().withSystem("http://the-system.com")
        .withCode("test").withDisplay("system-test").withVersion("1.5");
        Code actual = this.typeConverter.toCqlCode(new Coding("http://the-system.com", "test", "system-test").setVersion("1.5"));
        assertTrue(expected.equal(actual));

        expected = this.typeConverter.toCqlCode(null);
        assertNull(expected);
    }

    @Test
    public void TestCodeableConceptToCqlConcept() {
        Concept expected = new Concept().withCode(new Code().withSystem("http://the-system.com").withCode("test")
                .withDisplay("system-test").withVersion("1.5")).withDisplay("additional-text");
        Concept actual = this.typeConverter.toCqlConcept(
                new CodeableConcept(new Coding("http://the-system.com", "test", "system-test").setVersion("1.5"))
                        .setText("additional-text"));

        assertTrue(expected.equal(actual));

        expected = this.typeConverter.toCqlConcept(null);
        assertNull(expected);
    }

    @Test
    public void TestPeriodToCqlInterval() {
        Interval expected = new Interval(new Date("2019-02-03"), true, new Date("2019-02-05"), true);
        Interval actual = this.typeConverter
                .toCqlInterval(new Period().setStartElement(new DateTimeType("2019-02-03"))
                .setEndElement(new DateTimeType("2019-02-05")));
        assertTrue(expected.equal(actual));

        expected = new Interval(new Date("2019"), true, new Date("2020"), true);
        actual = this.typeConverter.toCqlInterval(new Period().setStartElement(new DateTimeType("2019")).setEndElement(new DateTimeType("2020")));
        assertTrue(expected.equal(actual));


        expected = new Interval(new DateTime("2020-09-18T19:35:53", ZoneOffset.UTC), true, new DateTime("2020-09-18T19:37:00", ZoneOffset.UTC), true);
        actual = this.typeConverter.toCqlInterval(new Period().setStartElement(new DateTimeType("2020-09-18T19:35:53+00:00")).setEndElement(new DateTimeType("2020-09-18T19:37:00+00:00")));
        assertTrue(expected.equal(actual));

        actual = this.typeConverter.toCqlInterval(null);
        assertNull(null);
    }

    @Test
    public void TestRangeToCqlInterval() {
        Interval expected = new Interval(new Quantity().withValue(new BigDecimal("2.0")).withUnit("ml"), true,
        new Quantity().withValue(new BigDecimal("5.0")).withUnit("ml"), true); 
        Interval actual = this.typeConverter
                .toCqlInterval(new Range()
                .setLow((SimpleQuantity)new org.hl7.fhir.dstu3.model.SimpleQuantity().setValue(2.0).setUnit("ml").setSystem("http://unitsofmeasure.org"))
                .setHigh((SimpleQuantity) new org.hl7.fhir.dstu3.model.SimpleQuantity().setValue(5.0).setUnit("ml").setSystem("http://unitsofmeasure.org")));
        assertTrue(expected.equal(actual));

        actual = this.typeConverter.toCqlInterval(null);
        assertNull(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void TestInvalidTypeToCqlInterval() {
        this.typeConverter.toCqlInterval(new Attachment());
    }

    @Test(expectedExceptions = NotImplementedException.class)
    public void TestTupleToCqlTuple() {
        Object expected = this.typeConverter.toCqlTuple(null);
        assertNull(expected);

        this.typeConverter.toCqlTuple(new Patient());
    }
}
