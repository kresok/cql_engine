package org.opencds.cqf.cql.execution;

import org.junit.Assert;
import org.opencds.cqf.cql.exception.CqlException;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CqlValueLiteralsAndSelectorsTest extends CqlExecutionTestBase {


    /**
     * {@link org.opencds.cqf.cql.elm.execution.NullEvaluator#evaluate(Context)}
     */
    @Test
    public void testNull() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("Null").getExpression().evaluate(context);
        Assert.assertNull(result);
        assertThat(result, is(nullValue()));

    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.EquivalentEvaluator#evaluate(Context)}
     */
    @Test
    public void testBoolean() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("BooleanFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("BooleanTrue").getExpression().evaluate(context);
        assertThat(result, is(true));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.LiteralEvaluator#evaluate(Context)}
     */
    @Test
    public void testInteger() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("IntegerZero").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef("IntegerPosZero").getExpression().evaluate(context);
        assertThat(result, is(+0));

        result = context.resolveExpressionRef("IntegerNegZero").getExpression().evaluate(context);
        assertThat(result, is(-0));

        result = context.resolveExpressionRef("IntegerOne").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("IntegerPosOne").getExpression().evaluate(context);
        assertThat(result, is(+1));

        result = context.resolveExpressionRef("IntegerNegOne").getExpression().evaluate(context);
        assertThat(result, is(-1));

        result = context.resolveExpressionRef("IntegerTwo").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef("IntegerPosTwo").getExpression().evaluate(context);
        assertThat(result, is(+2));

        result = context.resolveExpressionRef("IntegerNegTwo").getExpression().evaluate(context);
        assertThat(result, is(-2));

        result = context.resolveExpressionRef("Integer10Pow9").getExpression().evaluate(context);
        assertThat(result, is((int)Math.pow(10,9)));

        result = context.resolveExpressionRef("IntegerPos10Pow9").getExpression().evaluate(context);
        assertThat(result, is(+1*(int)Math.pow(10,9)));

        result = context.resolveExpressionRef("IntegerNeg10Pow9").getExpression().evaluate(context);
        assertThat(result, is(-1*(int)Math.pow(10,9)));

        result = context.resolveExpressionRef("Integer2Pow31ToZero1IntegerMaxValue").getExpression().evaluate(context);
        assertThat(result, is(2147483647));
        assertThat(result, is((int)(Math.pow(2,30) -1 + Math.pow(2,30)))); //Power(2,30)-1+Power(2,30)

        result = context.resolveExpressionRef("IntegerPos2Pow31ToZero1IntegerMaxValue").getExpression().evaluate(context);
        assertThat(result, is(+2147483647));
        assertThat(result, is(+1* (int)(Math.pow(2,30) -1 + Math.pow(2,30))));

        result = context.resolveExpressionRef("IntegerNeg2Pow31ToZero1").getExpression().evaluate(context);
        assertThat(result, is(-2147483647));
        assertThat(result, is(-1* (int)(Math.pow(2,30) -1 + Math.pow(2,30))));

        try {
            context.resolveExpressionRef("Integer2Pow31").getExpression().evaluate(context);
        } catch (CqlException ex) {

        }

        try {
            context.resolveExpressionRef("IntegerPos2Pow31").getExpression().evaluate(context);
        } catch (CqlException ex) {

        }

        result = context.resolveExpressionRef("IntegerNeg2Pow31IntegerMinValue").getExpression().evaluate(context);
        assertThat(result, is(-2147483648));
        assertThat(result, is(-1* (int)(Math.pow(2,30) ) -1* (int)(Math.pow(2,30) )));

        try {
            context.resolveExpressionRef("Integer2Pow31ToInf1").getExpression().evaluate(context);
        } catch (CqlException ex) {
        }

        try {
            context.resolveExpressionRef("IntegerPos2Pow31ToInf1").getExpression().evaluate(context);
        } catch (CqlException ex) {
        }

        try {
            context.resolveExpressionRef("IntegerNeg2Pow31ToInf1").getExpression().evaluate(context);
        } catch (CqlException ex) {
            //System.out.println(ex.toString());
        }


    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.LiteralEvaluator#evaluate(Context)}
     */
    @Test
    public void testDecimal() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("DecimalZero").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(0.0)));

        result = context.resolveExpressionRef("DecimalPosZero").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(42.0).subtract(new BigDecimal(42.0))));

        result = context.resolveExpressionRef("DecimalNegZero").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(42.0).subtract(new BigDecimal(42.0))));

        result = context.resolveExpressionRef("DecimalOne").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(BigDecimal.ONE));

        result = context.resolveExpressionRef("DecimalPosOne").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(BigDecimal.ONE));

        result = context.resolveExpressionRef("DecimalNegOne").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal((double)-1)));

        result = context.resolveExpressionRef("DecimalTwo").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(2.0)));

        result = context.resolveExpressionRef("DecimalPosTwo").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(2.0)));

        result = context.resolveExpressionRef("DecimalNegTwo").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal((double)-2)));

        result = context.resolveExpressionRef("Decimal10Pow9").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(Math.pow(10.0, 9.0))));

        result = context.resolveExpressionRef("DecimalNeg10Pow9").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(-Math.pow(10.0, 9.0))));

        result = context.resolveExpressionRef("Decimal2Pow31ToZero1").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(2147483647.0)));
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(Math.pow(2.0,30.0) -1 + Math.pow(2.0,30.0))));

        result = context.resolveExpressionRef("DecimalPos2Pow31ToZero1").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(2147483647.0)));
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(Math.pow(2.0,30.0) -1 + Math.pow(2.0,30.0))));

        result = context.resolveExpressionRef("DecimalNeg2Pow31ToZero1").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(-2147483647.0)));
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(-1*(Math.pow(2.0,30.0)) +1 -1*(Math.pow(2.0,30.0)))));

        result = context.resolveExpressionRef("Decimal2Pow31").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(2147483648.0)));
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal((Math.pow(2.0,30.0)) + (Math.pow(2.0,30.0)))));

        result = context.resolveExpressionRef("DecimalPos2Pow31").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(2147483648.0)));
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal((Math.pow(2.0,30.0)) + (Math.pow(2.0,30.0)))));

        result = context.resolveExpressionRef("DecimalNeg2Pow31").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(-2147483648.0)));
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(-(Math.pow(2.0,30.0)) - (Math.pow(2.0,30.0)))));

        result = context.resolveExpressionRef("Decimal2Pow31ToInf1").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(2147483649.0)));
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal((Math.pow(2.0,30.0)) + 1.0 + (Math.pow(2.0,30.0)))));

        result = context.resolveExpressionRef("DecimalPos2Pow31ToInf1").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(2147483649.0)));
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal((Math.pow(2.0,30.0)) + 1.0 + (Math.pow(2.0,30.0)))));

        result = context.resolveExpressionRef("DecimalNeg2Pow31ToInf1").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(-2147483649.0)));
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(-(Math.pow(2.0,30.0)) -1.0 - (Math.pow(2.0,30.0)))));

        result = context.resolveExpressionRef("DecimalZeroStep").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(0.00000000)));
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(42.0).subtract(new BigDecimal(42.0))));

        result = context.resolveExpressionRef("DecimalPosZeroStep").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(0.00000000)));
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(42.0).subtract(new BigDecimal(42.0))));

        result = context.resolveExpressionRef("DecimalNegZeroStep").getExpression().evaluate(context);
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(-0.00000000)));
        assertThat((BigDecimal)result, comparesEqualTo(new BigDecimal(42.0).subtract(new BigDecimal(42.0))));

        result = context.resolveExpressionRef("DecimalOneStep").getExpression().evaluate(context);
        assertThat(((BigDecimal)result).setScale(8, RoundingMode.HALF_EVEN), comparesEqualTo(new BigDecimal(Math.pow(10.0,-8)).setScale(8,RoundingMode.HALF_EVEN)));

        result = context.resolveExpressionRef("DecimalPosOneStep").getExpression().evaluate(context);
        assertThat(((BigDecimal)result).setScale(8, RoundingMode.HALF_EVEN), comparesEqualTo(new BigDecimal(Math.pow(10.0,-8)).setScale(8,RoundingMode.HALF_EVEN)));

        result = context.resolveExpressionRef("DecimalNegOneStep").getExpression().evaluate(context);
        assertThat(((BigDecimal)result).setScale(8, RoundingMode.HALF_EVEN), comparesEqualTo(new BigDecimal(-1*Math.pow(10.0,-8)).setScale(8,RoundingMode.HALF_EVEN)));

        result = context.resolveExpressionRef("DecimalTwoStep").getExpression().evaluate(context);
        assertThat(((BigDecimal)result).setScale(8, RoundingMode.HALF_EVEN), comparesEqualTo(new BigDecimal(2 * Math.pow(10.0,-8)).setScale(8,RoundingMode.HALF_EVEN)));

        result = context.resolveExpressionRef("DecimalPosTwoStep").getExpression().evaluate(context);
        assertThat(((BigDecimal)result).setScale(8, RoundingMode.HALF_EVEN), comparesEqualTo(new BigDecimal(2 * Math.pow(10.0,-8)).setScale(8,RoundingMode.HALF_EVEN)));

        result = context.resolveExpressionRef("DecimalNegTwoStep").getExpression().evaluate(context);
        assertThat(((BigDecimal)result).setScale(8, RoundingMode.HALF_EVEN), comparesEqualTo(new BigDecimal(-2 * Math.pow(10.0,-8)).setScale(8,RoundingMode.HALF_EVEN)));

        result = context.resolveExpressionRef("DecimalTenStep").getExpression().evaluate(context);
        assertThat(((BigDecimal)result).setScale(7, RoundingMode.HALF_EVEN), comparesEqualTo(new BigDecimal(Math.pow(10.0,-7)).setScale(7,RoundingMode.HALF_EVEN)));

        result = context.resolveExpressionRef("DecimalPosTenStep").getExpression().evaluate(context);
        assertThat(((BigDecimal)result).setScale(7, RoundingMode.HALF_EVEN), comparesEqualTo(new BigDecimal(Math.pow(10.0,-7)).setScale(7,RoundingMode.HALF_EVEN)));

        result = context.resolveExpressionRef("DecimalNegTenStep").getExpression().evaluate(context);
        assertThat(((BigDecimal)result).setScale(7, RoundingMode.HALF_EVEN), comparesEqualTo(new BigDecimal(-1*Math.pow(10.0,-7)).setScale(7,RoundingMode.HALF_EVEN)));

//        try {
//            context.resolveExpressionRef("DecimalTenthStep").getExpression().evaluate(context);
//        } catch (CqlException ex) {
//
//        }

//        try {
//            context.resolveExpressionRef("DecimalPosTenthStep").getExpression().evaluate(context);
//        } catch (CqlException ex) {
//
//        }
//
//        try {
//            context.resolveExpressionRef("DecimalNegTenthStep").getExpression().evaluate(context);
//        } catch (CqlException ex) {
//
//        }





    }

}
